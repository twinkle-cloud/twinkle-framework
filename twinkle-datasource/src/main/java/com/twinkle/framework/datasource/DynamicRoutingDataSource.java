package com.twinkle.framework.datasource;

import com.p6spy.engine.spy.P6DataSource;
import com.twinkle.framework.datasource.exception.DataSourceException;
import com.twinkle.framework.datasource.provider.DynamicDataSourceProvider;
import com.twinkle.framework.datasource.strategy.RoutingDataSourceStrategy;
import com.twinkle.framework.datasource.utils.DynamicDataSourceContextHolder;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/12/19 6:31 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class DynamicRoutingDataSource extends AbstractRoutingDataSource implements InitializingBean, DisposableBean {
    private static final String UNDERLINE = "_";
    @Setter
    private boolean strict;
    private boolean p6spy;

    @Setter
    private DynamicDataSourceProvider provider;
    @Setter
    private Class<? extends RoutingDataSourceStrategy> strategy;
    @Setter
    private String primary;

    /**
     * ALl of the data sources.
     */
    private Map<String, DataSource> dataSourceMap = new LinkedHashMap<>();
    /**
     * Group data sources.
     */
    private Map<String, DynamicGroupDataSource> groupDataSources = new ConcurrentHashMap<>();

    @Override
    public DataSource determineDataSource() {
        return getDataSource(DynamicDataSourceContextHolder.peek());
    }

    private DataSource determinePrimaryDataSource() {
        log.debug("Switch to the primary datasource");
        return this.groupDataSources.containsKey(this.primary) ? this.groupDataSources.get(primary)
                .determineDataSource() : this.dataSourceMap.get(this.primary);
    }

    /**
     * Get all of the data sources.
     *
     * @return
     */
    public Map<String, DataSource> getCurrentDataSources() {
        return this.dataSourceMap;
    }

    /**
     * Get all of the group data sources.
     *
     * @return
     */
    public Map<String, DynamicGroupDataSource> getCurrentGroupDataSources() {
        return this.groupDataSources;
    }

    /**
     * Get Data source.
     *
     * @param _dataSourceName Data Source Name.
     * @return
     */
    public DataSource getDataSource(String _dataSourceName) {
        if (StringUtils.isEmpty(_dataSourceName)) {
            return determinePrimaryDataSource();
        } else if (!this.groupDataSources.isEmpty() && this.groupDataSources.containsKey(_dataSourceName)) {
            log.debug("Switch to the group datasource named [{}]", _dataSourceName);
            return this.groupDataSources.get(_dataSourceName).determineDataSource();
        } else if (this.dataSourceMap.containsKey(_dataSourceName)) {
            log.debug("Switch to the datasource named [{}]", _dataSourceName);
            return this.dataSourceMap.get(_dataSourceName);
        }
        if (this.strict) {
            throw new DataSourceException(ExceptionCode.DATASOURCE_NOT_INITIALIZED, "Could not find a datasource named" + _dataSourceName);
        }
        return determinePrimaryDataSource();
    }

    /**
     * Add Data Source.
     *
     * @param _dataSourceName
     * @param _dataSource
     */
    public synchronized void addDataSource(String _dataSourceName, DataSource _dataSource) {
        if (this.p6spy) {
            _dataSource = new P6DataSource(_dataSource);
        }
        this.dataSourceMap.put(_dataSourceName, _dataSource);
        if (_dataSourceName.contains(UNDERLINE)) {
            String testGroup = _dataSourceName.split(UNDERLINE)[0];
            if (this.groupDataSources.containsKey(testGroup)) {
                this.groupDataSources.get(testGroup).addDatasource(_dataSource);
            } else {
                try {
                    DynamicGroupDataSource tempGroupDataSource = new DynamicGroupDataSource(testGroup,
                            this.strategy.newInstance());
                    tempGroupDataSource.addDatasource(_dataSource);
                    this.groupDataSources.put(testGroup, tempGroupDataSource);
                } catch (Exception e) {
                    log.error("Add the datasource named [{}] error.", _dataSourceName, e);
                    this.dataSourceMap.remove(_dataSourceName);
                }
            }
        }
        log.info("Load datasource [{}] successfully.", _dataSourceName);
    }

    /**
     * Remove Data Source
     *
     * @param _dataSourceName
     */
    public synchronized void removeDataSource(String _dataSourceName) {
        if (this.dataSourceMap.containsKey(_dataSourceName)) {
            DataSource dataSource = dataSourceMap.get(_dataSourceName);
            this.dataSourceMap.remove(_dataSourceName);
            if (_dataSourceName.contains(UNDERLINE)) {
                String group = _dataSourceName.split(UNDERLINE)[0];
                if (this.groupDataSources.containsKey(group)) {
                    this.groupDataSources.get(group).removeDatasource(dataSource);
                }
            }
            log.info("Remove the database named [{}] successfully.", _dataSourceName);
        } else {
            log.warn("Could not find a database named [{}].", _dataSourceName);
        }
    }

    /**
     * Enable p6spy for JDBC data source.
     *
     * @param _enableFlag
     */
    public void setP6spy(boolean _enableFlag) {
        if (_enableFlag) {
            try {
                Class.forName("com.p6spy.engine.spy.P6DataSource");
                log.info("Detect P6SPY plugin and enabled it");
                this.p6spy = true;
            } catch (Exception e) {
                log.warn("Could not find P6SPY libs. ");
            }
        } else {
            this.p6spy = false;
        }
    }

    @Override
    public void destroy() throws Exception {
        log.info("Going to close the data sources ....");
        for (Map.Entry<String, DataSource> item : dataSourceMap.entrySet()) {
            DataSource dataSource = item.getValue();
            if (p6spy) {
                Field realDataSourceField = P6DataSource.class.getDeclaredField("realDataSource");
                realDataSourceField.setAccessible(true);
                dataSource = (DataSource) realDataSourceField.get(dataSource);
            }
            Class<? extends DataSource> clazz = dataSource.getClass();
            try {
                Method closeMethod = clazz.getDeclaredMethod("close");
                closeMethod.invoke(dataSource);
            } catch (NoSuchMethodException e) {
                log.warn("Close the datasource named [{}] failed,", item.getKey());
            }
        }
        log.info("Complete close datasource successfully, bye");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, DataSource> dataSources = this.provider.loadDataSources();
        //Add Group Data Source.
        for (Map.Entry<String, DataSource> dsItem : dataSources.entrySet()) {
            addDataSource(dsItem.getKey(), dsItem.getValue());
        }
        //Check the default Primary data source.
        if (this.groupDataSources.containsKey(this.primary)) {
            log.info(
                    "Datasource initial loaded [{}],primary group datasource named [{}]",
                    dataSources.size(), this.primary);
        } else if (this.dataSourceMap.containsKey(this.primary)) {
            log.info("Datasource initial loaded [{}], primary datasource named [{}]",
                    dataSources.size(), this.primary);
        } else {
            throw new DataSourceException(ExceptionCode.DATASOURCE_PRIMARY_MISSING, "Please check the setting of primary.");
        }
    }
}
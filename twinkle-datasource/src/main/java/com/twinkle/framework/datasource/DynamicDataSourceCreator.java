package com.twinkle.framework.datasource;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import com.twinkle.framework.datasource.exception.DataSourceException;
import com.twinkle.framework.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.twinkle.framework.datasource.spring.boot.autoconfigure.druid.DruidConfig;
import com.twinkle.framework.datasource.utils.DruidWallConfigUtil;
import com.twinkle.framework.datasource.spring.boot.autoconfigure.hikari.HikariCpConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Function: Data source builder. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/12/19 6:31 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class DynamicDataSourceCreator {
    /**
     * DRUID数据源类
     */
    private static final String DRUID_DATASOURCE = "com.alibaba.druid.pool.DruidDataSource";
    /**
     * HikariCp数据源
     */
    private static final String HIKARI_DATASOURCE = "com.zaxxer.hikari.HikariDataSource";
    /**
     * JNDI数据源查找
     */
    private static final JndiDataSourceLookup JNDI_DATA_SOURCE_LOOKUP = new JndiDataSourceLookup();

    private static Method createMethod;
    private static Method typeMethod;
    private static Method urlMethod;
    private static Method usernameMethod;
    private static Method passwordMethod;
    private static Method driverClassNameMethod;
    private static Method buildMethod;

    /**
     * 是否存在druid
     */
    private static Boolean druidExists = false;
    /**
     * 是否存在hikari
     */
    private static Boolean hikariExists = false;

    static {
        //to support springboot 1.5 and 2.x
        Class<?> builderClass = null;
        try {
            builderClass = Class.forName("org.springframework.boot.jdbc.DataSourceBuilder");
        } catch (Exception ignored) {
        }
        if (builderClass == null) {
            try {
                builderClass = Class
                        .forName("org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder");
            } catch (Exception e) {
            }
        }
        if (builderClass != null) {
            try {
                createMethod = builderClass.getDeclaredMethod("create");
                typeMethod = builderClass.getDeclaredMethod("type", Class.class);
                urlMethod = builderClass.getDeclaredMethod("url", String.class);
                usernameMethod = builderClass.getDeclaredMethod("username", String.class);
                passwordMethod = builderClass.getDeclaredMethod("password", String.class);
                driverClassNameMethod = builderClass.getDeclaredMethod("driverClassName", String.class);
                buildMethod = builderClass.getDeclaredMethod("build");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static {
        try {
            Class.forName(DRUID_DATASOURCE);
            druidExists = true;
        } catch (ClassNotFoundException ignored) {
        }
        try {
            Class.forName(HIKARI_DATASOURCE);
            hikariExists = true;
        } catch (ClassNotFoundException ignored) {
        }
    }

    @Setter
    private DruidConfig druidGlobalConfig;
    @Setter
    private HikariCpConfig hikariGlobalConfig;

    @Autowired(required = false)
    private ApplicationContext applicationContext;
    @Setter
    private String globalPublicKey;

    /**
     * Build the datasource with given properties.
     *
     * @param _properties datasource info.
     * @return
     */
    public DataSource createDataSource(DataSourceProperty _properties) {
        DataSource tempDataSource;
        //If JNDI data source.
        String jndiName = _properties.getJndiName();
        if (jndiName != null && !jndiName.isEmpty()) {
            tempDataSource = createJNDIDataSource(jndiName);
        } else {
            Class<? extends DataSource> tempDataSourceTypeClass = _properties.getType();
            if (tempDataSourceTypeClass == null) {
                if (druidExists) {
                    tempDataSource = createDruidDataSource(_properties);
                } else if (hikariExists) {
                    tempDataSource = createHikariDataSource(_properties);
                } else {
                    tempDataSource = createBasicDataSource(_properties);
                }
            } else if (DRUID_DATASOURCE.equals(tempDataSourceTypeClass.getName())) {
                tempDataSource = createDruidDataSource(_properties);
            } else if (HIKARI_DATASOURCE.equals(tempDataSourceTypeClass.getName())) {
                tempDataSource = createHikariDataSource(_properties);
            } else {
                tempDataSource = createBasicDataSource(_properties);
            }
        }
        String tempSchema = _properties.getSchema();
        if (StringUtils.hasText(tempSchema)) {
            runScript(tempDataSource, tempSchema, _properties);
        }
        String tempInitialData = _properties.getData();
        if (StringUtils.hasText(tempInitialData)) {
            runScript(tempDataSource, tempInitialData, _properties);
        }
        return tempDataSource;
    }

    /**
     * Build the base datasource.
     *
     * @param _properties data source configuration.
     * @return
     */
    public DataSource createBasicDataSource(DataSourceProperty _properties) {
        try {
            if (StringUtils.isEmpty(_properties.getPublicKey())) {
                _properties.setPublicKey(globalPublicKey);
            }
            Object tempObj1 = createMethod.invoke(null);
            Object tempObj2 = typeMethod.invoke(tempObj1, _properties.getType());
            Object tempObj3 = urlMethod.invoke(tempObj2, _properties.getUrl());
            Object tempObj4 = usernameMethod.invoke(tempObj3, _properties.getUsername());
            Object tempObj5 = passwordMethod.invoke(tempObj4, _properties.getPassword());
            Object tempObj6 = driverClassNameMethod.invoke(tempObj5, _properties.getDriverClassName());
            return (DataSource) buildMethod.invoke(tempObj6);
        } catch (Exception e) {
            throw new DataSourceException(ExceptionCode.DATASOURCE_BASE_INITIALIZE_FAILED,
                    "Build basic database named " + _properties.getPollName()
                            + " error.", e);
        }
    }

    /**
     * Build JNDI datasource.
     *
     * @param jndiName jndi datasource name.
     * @return
     */
    public DataSource createJNDIDataSource(String jndiName) {
        return JNDI_DATA_SOURCE_LOOKUP.getDataSource(jndiName);
    }

    /**
     * Build Druid data source.
     *
     * @param _properties data source configuration.
     * @return
     */
    public DataSource createDruidDataSource(DataSourceProperty _properties) {
        if (StringUtils.isEmpty(_properties.getPublicKey())) {
            _properties.setPublicKey(globalPublicKey);
        }
        DruidDataSource tempDataSource = new DruidDataSource();
        tempDataSource.setUsername(_properties.getUsername());
        tempDataSource.setPassword(_properties.getPassword());
        tempDataSource.setUrl(_properties.getUrl());
        tempDataSource.setDriverClassName(_properties.getDriverClassName());
        tempDataSource.setName(_properties.getPollName());
        DruidConfig tempConfig = _properties.getDruid();
        Properties tempProperties = tempConfig.toProperties(this.druidGlobalConfig);
        String tempFilterName = tempProperties.getProperty("druid.filters");
        List<Filter> tempProxyFilterList = new ArrayList<>(2);
        if (!StringUtils.isEmpty(tempFilterName) && tempFilterName.contains("stat")) {
            StatFilter tempStatFilter = new StatFilter();
            tempStatFilter.configFromProperties(tempProperties);
            tempProxyFilterList.add(tempStatFilter);
        }
        if (!StringUtils.isEmpty(tempFilterName) && tempFilterName.contains("wall")) {
            WallConfig tempWallConfig = DruidWallConfigUtil
                    .toWallConfig(_properties.getDruid().getWall(), this.druidGlobalConfig.getWall());
            WallFilter tempWallFilter = new WallFilter();
            tempWallFilter.setConfig(tempWallConfig);
            tempProxyFilterList.add(tempWallFilter);
        }

        if (this.applicationContext != null) {
            for (String tempFilterId : this.druidGlobalConfig.getProxyFilters()) {
                tempProxyFilterList.add(this.applicationContext.getBean(tempFilterId, Filter.class));
            }
        }
        tempDataSource.setProxyFilters(tempProxyFilterList);
        tempDataSource.configFromPropety(tempProperties);
        //Connection Related parameters.
        tempDataSource.setConnectProperties(tempConfig.getConnectionProperties());
        //Set druid's parameters which not in built-in properties.
        Boolean testOnReturn = tempConfig.getTestOnReturn() == null ? druidGlobalConfig.getTestOnReturn()
                : tempConfig.getTestOnReturn();
        if (testOnReturn != null && testOnReturn.equals(true)) {
            tempDataSource.setTestOnReturn(true);
        }
        Integer validationQueryTimeout =
                tempConfig.getValidationQueryTimeout() == null ? druidGlobalConfig.getValidationQueryTimeout()
                        : tempConfig.getValidationQueryTimeout();
        if (validationQueryTimeout != null && !validationQueryTimeout.equals(-1)) {
            tempDataSource.setValidationQueryTimeout(validationQueryTimeout);
        }

        Boolean sharePreparedStatements =
                tempConfig.getSharePreparedStatements() == null ? druidGlobalConfig.getSharePreparedStatements()
                        : tempConfig.getSharePreparedStatements();
        if (sharePreparedStatements != null && sharePreparedStatements.equals(true)) {
            tempDataSource.setSharePreparedStatements(true);
        }
        Integer connectionErrorRetryAttempts =
                tempConfig.getConnectionErrorRetryAttempts() == null ? druidGlobalConfig
                        .getConnectionErrorRetryAttempts() : tempConfig.getConnectionErrorRetryAttempts();
        if (connectionErrorRetryAttempts != null && !connectionErrorRetryAttempts.equals(1)) {
            tempDataSource.setConnectionErrorRetryAttempts(connectionErrorRetryAttempts);
        }
        Boolean breakAfterAcquireFailure =
                tempConfig.getBreakAfterAcquireFailure() == null ? druidGlobalConfig
                        .getBreakAfterAcquireFailure() : tempConfig.getBreakAfterAcquireFailure();
        if (breakAfterAcquireFailure != null && breakAfterAcquireFailure.equals(true)) {
            tempDataSource.setBreakAfterAcquireFailure(true);
        }

        Integer timeout = tempConfig.getRemoveAbandonedTimeoutMillis() == null ?
                druidGlobalConfig.getRemoveAbandonedTimeoutMillis()
                : tempConfig.getRemoveAbandonedTimeoutMillis();
        if (timeout != null) {
            tempDataSource.setRemoveAbandonedTimeout(timeout);
        }

        Boolean abandoned = tempConfig.getRemoveAbandoned() == null ?
                druidGlobalConfig.getRemoveAbandoned() : tempConfig.getRemoveAbandoned();
        if (abandoned != null) {
            tempDataSource.setRemoveAbandoned(abandoned);
        }

        Boolean logAbandoned = tempConfig.getLogAbandoned() == null ?
                druidGlobalConfig.getLogAbandoned() : tempConfig.getLogAbandoned();
        if (logAbandoned != null) {
            tempDataSource.setLogAbandoned(logAbandoned);
        }

        try {
            tempDataSource.init();
        } catch (SQLException e) {
            throw new DataSourceException(ExceptionCode.DATASOURCE_DRUID_INITIALIZE_FAILED, "Build druid data source error.", e);
        }
        return tempDataSource;
    }

    /**
     * Build Hikari Data Source.
     *
     * @param _properties Data Source Configuration.
     * @return
     */
    public DataSource createHikariDataSource(DataSourceProperty _properties) {
        if (StringUtils.isEmpty(_properties.getPublicKey())) {
            _properties.setPublicKey(globalPublicKey);
        }
        HikariCpConfig tempCpConfig = _properties.getHikari();
        HikariConfig tempConfig = tempCpConfig.toHikariConfig(hikariGlobalConfig);
        tempConfig.setUsername(_properties.getUsername());
        tempConfig.setPassword(_properties.getPassword());
        tempConfig.setJdbcUrl(_properties.getUrl());
        tempConfig.setDriverClassName(_properties.getDriverClassName());
        tempConfig.setPoolName(_properties.getPollName());
        return new HikariDataSource(tempConfig);
    }

    /**
     * Run initialize Scripts.
     *
     * @param _dataSource
     * @param _location   the path for initial scripts.
     * @param _properties
     */
    private void runScript(DataSource _dataSource, String _location,
                           DataSourceProperty _properties) {
        if (StringUtils.hasText(_location)) {
            ResourceDatabasePopulator tempPopultor = new ResourceDatabasePopulator();
            tempPopultor.setContinueOnError(_properties.isContinueOnError());
            tempPopultor.setSeparator(_properties.getSeparator());
            ClassPathResource tempResource = new ClassPathResource(_location);
            if (tempResource.exists()) {
                tempPopultor.addScript(tempResource);
                try {
                    DatabasePopulatorUtils.execute(tempPopultor, _dataSource);
                } catch (Exception e) {
                    log.warn("Execute sql error. Exception: {}", e);
                }
            } else {
                log.warn("Could not find schema or data file {}.", _location);
            }
        }
    }
}

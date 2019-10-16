package com.twinkle.framework.datasource.plugin;

import com.twinkle.framework.datasource.DynamicRoutingDataSource;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.IncorrectResultSetColumnCountException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Function: Database health indicator. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/12/19 6:31 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class DataSourceHealthIndicator extends AbstractHealthIndicator {

    /**
     * Healthy info map.
     */
    private static Map<String, Boolean> DB_HEALTH = new ConcurrentHashMap<>();
    /**
     * The current data source.
     */
    private DataSource dataSource;

    public DataSourceHealthIndicator(DataSource _dataSource) {
        this.dataSource = _dataSource;
    }

    /**
     * The connection heatlth satus.
     *
     * @param _dataSourceName
     */
    public static boolean getDbHealth(String _dataSourceName) {
        return DB_HEALTH.get(_dataSourceName);
    }

    /**
     * Update the data source connection status.
     *
     * @param _dataSourceName
     * @param _healthy
     */
    public static Boolean setDbHealth(String _dataSourceName, boolean _healthy) {
        return DB_HEALTH.put(_dataSourceName, _healthy);
    }

    @Override
    protected void doHealthCheck(Health.Builder _builder) throws Exception {
        if (this.dataSource instanceof DynamicRoutingDataSource) {
            Map<String, DataSource> dataSourceMap = ((DynamicRoutingDataSource) this.dataSource).getCurrentDataSources();
            // Check the data source is ready or not?
            for (Map.Entry<String, DataSource> dataSource : dataSourceMap.entrySet()) {
                Integer result = 0;
                try {
                    result = query(dataSource.getValue());
                } finally {
                    DB_HEALTH.put(dataSource.getKey(), 1 == result);
                    _builder.withDetail(dataSource.getKey(), result);
                }
            }
        }
    }


    private Integer query(DataSource _dataSource) {
        //todo 这里应该可以配置或者可重写？
        List<Integer> results = new JdbcTemplate(_dataSource).query("SELECT 1", new RowMapper<Integer>() {

            @Override
            public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columns = metaData.getColumnCount();
                if (columns != 1) {
                    throw new IncorrectResultSetColumnCountException(1, columns);
                }
                return (Integer) JdbcUtils.getResultSetValue(resultSet, 1, Integer.class);
            }
        });
        return DataAccessUtils.requiredSingleResult(results);
    }
}
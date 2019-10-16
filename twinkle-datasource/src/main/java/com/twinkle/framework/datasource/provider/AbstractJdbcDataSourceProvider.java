package com.twinkle.framework.datasource.provider;

import com.twinkle.framework.datasource.spring.boot.autoconfigure.DataSourceProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

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
public abstract class AbstractJdbcDataSourceProvider extends AbstractDataSourceProvider implements DynamicDataSourceProvider {
    /**
     * JDBC driver
     */
    private String driverClassName;
    /**
     * JDBC url
     */
    private String url;
    /**
     * JDBC username
     */
    private String username;
    /**
     * JDBC password
     */
    private String password;

    public AbstractJdbcDataSourceProvider(String _driverClassName, String _url, String _username,
                                          String _password) {
        this.driverClassName = _driverClassName;
        this.url = _url;
        this.username = _username;
        this.password = _password;
    }

    @Override
    public Map<String, DataSource> loadDataSources() {
        Connection tempConnection = null;
        Statement tempStatement = null;
        try {
            Class.forName(driverClassName);
            log.info("Load the data source's driver successfully.");
            tempConnection = DriverManager.getConnection(url, username, password);
            log.info("Get the data source's connection successfully.");
            tempStatement = tempConnection.createStatement();
            Map<String, DataSourceProperty> tempPropertyMap = executeStmt(tempStatement);
            return createDataSourceMap(tempPropertyMap);
        } catch (Exception e) {
            log.error("Load data source failed, Exception: {}", e);
        } finally {
            JdbcUtils.closeConnection(tempConnection);
            JdbcUtils.closeStatement(tempStatement);
        }
        return null;
    }

    /**
     * Execute Statement to get the DataSource Properties.
     *
     * @param statement
     * @return
     * @throws SQLException
     */
    protected abstract Map<String, DataSourceProperty> executeStmt(Statement statement) throws SQLException;
}

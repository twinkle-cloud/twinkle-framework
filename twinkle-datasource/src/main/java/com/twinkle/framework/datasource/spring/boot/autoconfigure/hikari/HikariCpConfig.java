package com.twinkle.framework.datasource.spring.boot.autoconfigure.hikari;

import com.zaxxer.hikari.HikariConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Properties;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Function: HikariCp global configuration. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/12/19 6:31 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Data
@Slf4j
public class HikariCpConfig {
    private static final long CONNECTION_TIMEOUT = SECONDS.toMillis(30);
    private static final long VALIDATION_TIMEOUT = SECONDS.toMillis(5);
    private static final long IDLE_TIMEOUT = MINUTES.toMillis(10);
    private static final long MAX_LIFETIME = MINUTES.toMillis(30);
    private static final int DEFAULT_POOL_SIZE = 10;

    private String username;
    private String password;
    private String driverClassName;
    private String jdbcUrl;
    private String poolName;

    private String catalog;
    private Long connectionTimeout;
    private Long validationTimeout;
    private Long idleTimeout;
    private Long leakDetectionThreshold;
    private Long maxLifetime;
    private Integer maxPoolSize;
    private Integer minIdle;

    private Long initializationFailTimeout;
    private String connectionInitSql;
    private String connectionTestQuery;
    private String dataSourceClassName;
    private String dataSourceJndiName;
    private String schema;
    private String transactionIsolationName;
    private Boolean isAutoCommit;
    private Boolean isReadOnly;
    private Boolean isIsolateInternalQueries;
    private Boolean isRegisterMbeans;
    private Boolean isAllowPoolSuspension;
    private Properties dataSourceProperties;
    private Properties healthCheckProperties;

    public HikariConfig toHikariConfig(HikariCpConfig _globalConfig) {
        HikariConfig tempConfig = new com.zaxxer.hikari.HikariConfig();

        String tempSchema = schema == null ? _globalConfig.getSchema() : schema;
        if (tempSchema != null) {
            try {
                Field schemaField = com.zaxxer.hikari.HikariConfig.class.getDeclaredField("schema");
                schemaField.setAccessible(true);
                schemaField.set(tempConfig, tempSchema);
            } catch (NoSuchFieldException e) {
                log.warn("DataSource has schema property, but the current Hikari does not support it currently.");
            } catch (IllegalAccessException e) {
                log.error("Load the Hikari Schema Field failed. Exception: {}", e);
            }
        }

        String tempCatalog = catalog == null ? _globalConfig.getCatalog() : catalog;
        if (tempCatalog != null) {
            tempConfig.setCatalog(tempCatalog);
        }

        Long tempConnectionTimeout =
                connectionTimeout == null ? _globalConfig.getConnectionTimeout() : connectionTimeout;
        if (tempConnectionTimeout != null && !tempConnectionTimeout.equals(CONNECTION_TIMEOUT)) {
            tempConfig.setConnectionTimeout(tempConnectionTimeout);
        }

        Long tempValidationTimeout =
                validationTimeout == null ? _globalConfig.getValidationTimeout() : validationTimeout;
        if (tempValidationTimeout != null && !tempValidationTimeout.equals(VALIDATION_TIMEOUT)) {
            tempConfig.setValidationTimeout(tempValidationTimeout);
        }

        Long tempIdleTimeout = idleTimeout == null ? _globalConfig.getIdleTimeout() : idleTimeout;
        if (tempIdleTimeout != null && !tempIdleTimeout.equals(IDLE_TIMEOUT)) {
            tempConfig.setIdleTimeout(tempIdleTimeout);
        }

        Long tempLeakDetectionThreshold =
                leakDetectionThreshold == null ? _globalConfig.getLeakDetectionThreshold()
                        : leakDetectionThreshold;
        if (tempLeakDetectionThreshold != null) {
            tempConfig.setLeakDetectionThreshold(tempLeakDetectionThreshold);
        }

        Long tempMaxLifetime = maxLifetime == null ? _globalConfig.getMaxLifetime() : maxLifetime;
        if (tempMaxLifetime != null && !tempMaxLifetime.equals(MAX_LIFETIME)) {
            tempConfig.setMaxLifetime(tempMaxLifetime);
        }

        Integer tempMaxPoolSize = maxPoolSize == null ? _globalConfig.getMaxPoolSize() : maxPoolSize;
        if (tempMaxPoolSize != null && !tempMaxPoolSize.equals(-1)) {
            tempConfig.setMaximumPoolSize(tempMaxPoolSize);
        }

        Integer tempMinIdle = minIdle == null ? _globalConfig.getMinIdle() : getMinIdle();
        if (tempMinIdle != null && !tempMinIdle.equals(-1)) {
            tempConfig.setMinimumIdle(tempMinIdle);
        }

        Long tempInitializationFailTimeout =
                initializationFailTimeout == null ? _globalConfig.getInitializationFailTimeout()
                        : initializationFailTimeout;
        if (tempInitializationFailTimeout != null && !tempInitializationFailTimeout.equals(1L)) {
            tempConfig.setInitializationFailTimeout(tempInitializationFailTimeout);
        }

        String tempConnectionInitSql =
                connectionInitSql == null ? _globalConfig.getConnectionInitSql() : connectionInitSql;
        if (tempConnectionInitSql != null) {
            tempConfig.setConnectionInitSql(tempConnectionInitSql);
        }

        String tempConnectionTestQuery =
                connectionTestQuery == null ? _globalConfig.getConnectionTestQuery() : connectionTestQuery;
        if (tempConnectionTestQuery != null) {
            tempConfig.setConnectionTestQuery(tempConnectionTestQuery);
        }

        String tempDataSourceClassName =
                dataSourceClassName == null ? _globalConfig.getDataSourceClassName() : dataSourceClassName;
        if (tempDataSourceClassName != null) {
            tempConfig.setDataSourceClassName(tempDataSourceClassName);
        }

        String tempDataSourceJndiName =
                dataSourceJndiName == null ? _globalConfig.getDataSourceJndiName() : dataSourceJndiName;
        if (tempDataSourceJndiName != null) {
            tempConfig.setDataSourceJNDI(tempDataSourceJndiName);
        }

        String tempTransactionIsolationName =
                transactionIsolationName == null ? _globalConfig.getTransactionIsolationName()
                        : transactionIsolationName;
        if (tempTransactionIsolationName != null) {
            tempConfig.setTransactionIsolation(tempTransactionIsolationName);
        }

        Boolean tempAutoCommit = isAutoCommit == null ? _globalConfig.getIsAutoCommit() : isAutoCommit;
        if (tempAutoCommit != null && tempAutoCommit.equals(Boolean.FALSE)) {
            tempConfig.setAutoCommit(false);
        }

        Boolean tempReadOnly = isReadOnly == null ? _globalConfig.getIsReadOnly() : isReadOnly;
        if (tempReadOnly != null) {
            tempConfig.setReadOnly(tempReadOnly);
        }

        Boolean tempIsolateInternalQueries =
                isIsolateInternalQueries == null ? _globalConfig.getIsIsolateInternalQueries()
                        : isIsolateInternalQueries;
        if (tempIsolateInternalQueries != null) {
            tempConfig.setIsolateInternalQueries(tempIsolateInternalQueries);
        }

        Boolean tempRegisterMbeans =
                isRegisterMbeans == null ? _globalConfig.getIsRegisterMbeans() : isRegisterMbeans;
        if (tempRegisterMbeans != null) {
            tempConfig.setRegisterMbeans(tempRegisterMbeans);
        }

        Boolean tempAllowPoolSuspension =
                isAllowPoolSuspension == null ? _globalConfig.getIsAllowPoolSuspension()
                        : isAllowPoolSuspension;
        if (tempAllowPoolSuspension != null) {
            tempConfig.setAllowPoolSuspension(tempAllowPoolSuspension);
        }

        Properties tempDataSourceProperties =
                dataSourceProperties == null ? _globalConfig.getDataSourceProperties()
                        : dataSourceProperties;
        if (tempDataSourceProperties != null) {
            tempConfig.setDataSourceProperties(tempDataSourceProperties);
        }

        Properties tempHealthCheckProperties =
                healthCheckProperties == null ? _globalConfig.getHealthCheckProperties()
                        : healthCheckProperties;
        if (tempHealthCheckProperties != null) {
            tempConfig.setHealthCheckProperties(tempHealthCheckProperties);
        }
        return tempConfig;
    }
}

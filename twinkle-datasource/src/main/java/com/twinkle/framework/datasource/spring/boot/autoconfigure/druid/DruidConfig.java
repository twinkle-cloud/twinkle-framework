package com.twinkle.framework.datasource.spring.boot.autoconfigure.druid;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.alibaba.druid.pool.DruidAbstractDataSource.*;

/**
 * Function: Druid DataSource Configurations. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/12/19 6:31 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Slf4j
public class DruidConfig {
    private Integer initialSize;
    private Integer maxActive;
    private Integer minIdle;
    private Integer maxWait;
    private Long timeBetweenEvictionRunsMillis;
    private Long timeBetweenLogStatsMillis;
    private Integer statSqlMaxSize;
    private Long minEvictableIdleTimeMillis;
    private Long maxEvictableIdleTimeMillis;
    private Boolean testWhileIdle;
    private Boolean testOnBorrow;
    private Boolean testOnReturn;
    private String validationQuery;
    private Integer validationQueryTimeout;
    private Boolean useGlobalDataSourceStat;
    private Boolean asyncInit;
    private String filters;
    private Boolean clearFiltersEnable;
    private Boolean resetStatEnable;
    private Integer notFullTimeoutRetryCount;
    private Integer maxWaitThreadCount;
    private Boolean failFast;
    private Long phyTimeoutMillis;
    private Boolean keepAlive;
    private Boolean poolPreparedStatements;
    private Boolean initVariants;
    private Boolean initGlobalVariants;
    private Boolean useUnfairLock;
    private Boolean killWhenSocketReadTimeout;
    private Properties connectionProperties;
    private Integer maxPoolPreparedStatementPerConnectionSize;
    private String initConnectionSqls;
    private Boolean sharePreparedStatements;
    private Integer connectionErrorRetryAttempts;
    private Boolean breakAfterAcquireFailure;
    private Boolean removeAbandoned;
    private Integer removeAbandonedTimeoutMillis;
    private Boolean logAbandoned;
    private String publicKey;
    @NestedConfigurationProperty
    private DruidWallConfig wall = new DruidWallConfig();

    @NestedConfigurationProperty
    private DruidStatConfig stat = new DruidStatConfig();

    private List<String> proxyFilters = new ArrayList<>(2);

    public Properties toProperties(DruidConfig _globalConfig) {
        Properties properties = new Properties();
        Integer tempInitialSize = initialSize == null ? _globalConfig.getInitialSize() : initialSize;
        if (tempInitialSize != null && !tempInitialSize.equals(DEFAULT_INITIAL_SIZE)) {
            properties.setProperty("druid.initialSize", String.valueOf(tempInitialSize));
        }

        Integer tempMaxActive = maxActive == null ? _globalConfig.getMaxActive() : maxActive;
        if (tempMaxActive != null && !tempMaxActive.equals(DEFAULT_MAX_WAIT)) {
            properties.setProperty("druid.maxActive", String.valueOf(tempMaxActive));
        }

        Integer tempMinIdle = minIdle == null ? _globalConfig.getMinIdle() : minIdle;
        if (tempMinIdle != null && !tempMinIdle.equals(DEFAULT_MIN_IDLE)) {
            properties.setProperty("druid.minIdle", String.valueOf(tempMinIdle));
        }

        Integer tempMaxWait = maxWait == null ? _globalConfig.getMaxWait() : maxWait;
        if (tempMaxWait != null && !tempMaxWait.equals(DEFAULT_MAX_WAIT)) {
            properties.setProperty("druid.maxWait", String.valueOf(tempMaxWait));
        }

        Long tempTimeBetweenEvictionRunsMillis =
                timeBetweenEvictionRunsMillis == null ? _globalConfig.getTimeBetweenEvictionRunsMillis()
                        : timeBetweenEvictionRunsMillis;
        if (tempTimeBetweenEvictionRunsMillis != null && !tempTimeBetweenEvictionRunsMillis
                .equals(DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS)) {
            properties.setProperty("druid.timeBetweenEvictionRunsMillis",
                    String.valueOf(tempTimeBetweenEvictionRunsMillis));
        }

        Long tempTimeBetweenLogStatsMillis =
                timeBetweenLogStatsMillis == null ? _globalConfig.getTimeBetweenLogStatsMillis()
                        : timeBetweenLogStatsMillis;
        if (tempTimeBetweenLogStatsMillis != null && tempTimeBetweenLogStatsMillis > 0) {
            properties.setProperty("druid.timeBetweenLogStatsMillis",
                    String.valueOf(tempTimeBetweenLogStatsMillis));
        }

        Integer tempStatSqlMaxSize =
                statSqlMaxSize == null ? _globalConfig.getStatSqlMaxSize() : statSqlMaxSize;
        if (tempStatSqlMaxSize != null) {
            properties.setProperty("druid.stat.sql.MaxSize", String.valueOf(tempStatSqlMaxSize));
        }

        Long tempMinEvictableIdleTimeMillis =
                minEvictableIdleTimeMillis == null ? _globalConfig.getMinEvictableIdleTimeMillis()
                        : minEvictableIdleTimeMillis;
        if (tempMinEvictableIdleTimeMillis != null && !tempMinEvictableIdleTimeMillis
                .equals(DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS)) {
            properties.setProperty("druid.minEvictableIdleTimeMillis",
                    String.valueOf(tempMinEvictableIdleTimeMillis));
        }

        Long tempMaxEvictableIdleTimeMillis =
                maxEvictableIdleTimeMillis == null ? _globalConfig.getMaxEvictableIdleTimeMillis()
                        : maxEvictableIdleTimeMillis;
        if (tempMaxEvictableIdleTimeMillis != null && !tempMaxEvictableIdleTimeMillis
                .equals(DEFAULT_MAX_EVICTABLE_IDLE_TIME_MILLIS)) {
            properties.setProperty("druid.maxEvictableIdleTimeMillis",
                    String.valueOf(tempMaxEvictableIdleTimeMillis));
        }

        Boolean tempTestWhileIdle =
                testWhileIdle == null ? _globalConfig.getTestWhileIdle() : testWhileIdle;
        if (tempTestWhileIdle != null && !tempTestWhileIdle.equals(DEFAULT_WHILE_IDLE)) {
            properties.setProperty("druid.testWhileIdle", "false");
        }

        Boolean tempTestOnBorrow = testOnBorrow == null ? _globalConfig.getTestOnBorrow() : testOnBorrow;
        if (tempTestOnBorrow != null && !tempTestOnBorrow.equals(DEFAULT_TEST_ON_BORROW)) {
            properties.setProperty("druid.testOnBorrow", "true");
        }

        String tempValidationQuery =
                validationQuery == null ? _globalConfig.getValidationQuery() : validationQuery;
        if (tempValidationQuery != null && tempValidationQuery.length() > 0) {
            properties.setProperty("druid.validationQuery", tempValidationQuery);
        }

        Boolean tempUseGlobalDataSourceStat =
                useGlobalDataSourceStat == null ? _globalConfig.getUseGlobalDataSourceStat()
                        : useGlobalDataSourceStat;
        if (tempUseGlobalDataSourceStat != null && tempUseGlobalDataSourceStat.equals(Boolean.TRUE)) {
            properties.setProperty("druid.useGlobalDataSourceStat", "true");
        }

        Boolean tempAsyncInit = asyncInit == null ? _globalConfig.getAsyncInit() : asyncInit;
        if (tempAsyncInit != null && tempAsyncInit.equals(Boolean.TRUE)) {
            properties.setProperty("druid.asyncInit", "true");
        }

        //filters单独处理，默认了stat,wall
        String tempFilters = filters == null ? _globalConfig.getFilters() : filters;
        if (tempFilters == null) {
            tempFilters = "stat,wall";
        }
        if (publicKey != null && publicKey.length() > 0 && !tempFilters.contains("config")) {
            tempFilters += ",config";
        }
        properties.setProperty("druid.filters", tempFilters);

        Boolean tempClearFiltersEnable =
                clearFiltersEnable == null ? _globalConfig.getClearFiltersEnable() : clearFiltersEnable;
        if (tempClearFiltersEnable != null && tempClearFiltersEnable.equals(Boolean.FALSE)) {
            properties.setProperty("druid.clearFiltersEnable", "false");
        }

        Boolean tempResetStatEnable =
                resetStatEnable == null ? _globalConfig.getResetStatEnable() : resetStatEnable;
        if (tempResetStatEnable != null && tempResetStatEnable.equals(Boolean.FALSE)) {
            properties.setProperty("druid.resetStatEnable", "false");
        }

        Integer tempNotFullTimeoutRetryCount =
                notFullTimeoutRetryCount == null ? _globalConfig.getNotFullTimeoutRetryCount()
                        : notFullTimeoutRetryCount;
        if (tempNotFullTimeoutRetryCount != null && !tempNotFullTimeoutRetryCount.equals(0)) {
            properties.setProperty("druid.notFullTimeoutRetryCount",
                    String.valueOf(tempNotFullTimeoutRetryCount));
        }

        Integer tempMaxWaitThreadCount =
                maxWaitThreadCount == null ? _globalConfig.getMaxWaitThreadCount() : maxWaitThreadCount;
        if (tempMaxWaitThreadCount != null && !tempMaxWaitThreadCount.equals(-1)) {
            properties.setProperty("druid.maxWaitThreadCount", String.valueOf(tempMaxWaitThreadCount));
        }

        Boolean tempFailFast = failFast == null ? _globalConfig.getFailFast() : failFast;
        if (tempFailFast != null && tempFailFast.equals(Boolean.TRUE)) {
            properties.setProperty("druid.failFast", "true");
        }

        Long tempPhyTimeoutMillis =
                phyTimeoutMillis == null ? _globalConfig.getPhyTimeoutMillis() : phyTimeoutMillis;
        if (tempPhyTimeoutMillis != null && !tempPhyTimeoutMillis.equals(DEFAULT_PHY_TIMEOUT_MILLIS)) {
            properties.setProperty("druid.phyTimeoutMillis", String.valueOf(tempPhyTimeoutMillis));
        }

        Boolean tempKeepAlive = keepAlive == null ? _globalConfig.getKeepAlive() : keepAlive;
        if (tempKeepAlive != null && tempKeepAlive.equals(Boolean.TRUE)) {
            properties.setProperty("druid.keepAlive", "true");
        }

        Boolean tempPoolPreparedStatements =
                poolPreparedStatements == null ? _globalConfig.getPoolPreparedStatements()
                        : poolPreparedStatements;
        if (tempPoolPreparedStatements != null && tempPoolPreparedStatements.equals(Boolean.TRUE)) {
            properties.setProperty("druid.poolPreparedStatements", "true");
        }

        Boolean tempInitVariants = initVariants == null ? _globalConfig.getInitVariants() : initVariants;
        if (tempInitVariants != null && tempInitVariants.equals(Boolean.TRUE)) {
            properties.setProperty("druid.initVariants", "true");
        }

        Boolean tempInitGlobalVariants =
                initGlobalVariants == null ? _globalConfig.getInitGlobalVariants() : initGlobalVariants;
        if (tempInitGlobalVariants != null && tempInitGlobalVariants.equals(Boolean.TRUE)) {
            properties.setProperty("druid.initGlobalVariants", "true");
        }

        Boolean tempUseUnfairLock =
                useUnfairLock == null ? _globalConfig.getUseUnfairLock() : useUnfairLock;
        if (tempUseUnfairLock != null) {
            properties.setProperty("druid.useUnfairLock", String.valueOf(tempUseUnfairLock));
        }

        Boolean tempKillWhenSocketReadTimeout =
                killWhenSocketReadTimeout == null ? _globalConfig.getKillWhenSocketReadTimeout()
                        : killWhenSocketReadTimeout;
        if (tempKillWhenSocketReadTimeout != null && tempKillWhenSocketReadTimeout
                .equals(Boolean.TRUE)) {
            properties.setProperty("druid.killWhenSocketReadTimeout", "true");
        }

        Properties tempConnectProperties =
                connectionProperties == null ? _globalConfig.getConnectionProperties()
                        : connectionProperties;

        if (publicKey != null && publicKey.length() > 0) {
            if (tempConnectProperties == null) {
                tempConnectProperties = new Properties();
            }
            log.info(
                    "Detect druid publicKey,It is highly recommended that you use the built-in encryption method");
            tempConnectProperties.setProperty("config.decrypt", "true");
            tempConnectProperties.setProperty("config.decrypt.key", this.publicKey);
        }
        connectionProperties = tempConnectProperties;

        Integer tempMaxPoolPreparedStatementPerConnectionSize =
                maxPoolPreparedStatementPerConnectionSize == null ? _globalConfig
                        .getMaxPoolPreparedStatementPerConnectionSize()
                        : maxPoolPreparedStatementPerConnectionSize;
        if (tempMaxPoolPreparedStatementPerConnectionSize != null
                && !tempMaxPoolPreparedStatementPerConnectionSize.equals(10)) {
            properties.setProperty("druid.maxPoolPreparedStatementPerConnectionSize",
                    String.valueOf(tempMaxPoolPreparedStatementPerConnectionSize));
        }

        String tempInitConnectionSqls =
                initConnectionSqls == null ? _globalConfig.getInitConnectionSqls() : initConnectionSqls;
        if (tempInitConnectionSqls != null && tempInitConnectionSqls.length() > 0) {
            properties.setProperty("druid.initConnectionSqls", tempInitConnectionSqls);
        }
        //stat配置参数
        Boolean tempLogSlowSql =
                stat.getLogSlowSql() == null ? _globalConfig.stat.getLogSlowSql() : stat.getLogSlowSql();
        if (tempLogSlowSql != null && tempLogSlowSql) {
            properties.setProperty("druid.stat.logSlowSql", "true");
        }
        Long tempSlowSqlMillis = stat.getSlowSqlMillis() == null ? _globalConfig.stat.getSlowSqlMillis()
                : stat.getSlowSqlMillis();
        if (tempSlowSqlMillis != null) {
            properties.setProperty("druid.stat.slowSqlMillis", tempSlowSqlMillis.toString());
        }

        Boolean tempMergeSql =
                stat.getMergeSql() == null ? _globalConfig.stat.getMergeSql() : stat.getMergeSql();
        if (tempMergeSql != null && tempMergeSql) {
            properties.setProperty("druid.stat.mergeSql", "true");
        }
        return properties;
    }

    public List<String> getProxyFilters() {
        return proxyFilters;
    }

    public void setProxyFilters(List<String> proxyFilters) {
        this.proxyFilters = proxyFilters;
    }
}
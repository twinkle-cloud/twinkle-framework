package com.twinkle.framework.datasource.spring.boot.autoconfigure;

import com.twinkle.framework.datasource.spring.boot.autoconfigure.druid.DruidConfig;
import com.twinkle.framework.datasource.spring.boot.autoconfigure.hikari.HikariCpConfig;
import com.twinkle.framework.datasource.strategy.LoadBalanceRoutingDataSourceStrategy;
import com.twinkle.framework.datasource.strategy.RoutingDataSourceStrategy;
import com.twinkle.framework.datasource.utils.CryptoUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.core.Ordered;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/12/19 6:31 PM<br/>
 *
 * @author chenxj
 * @see DataSourceProperties
 * @since JDK 1.8
 */
@Slf4j
@Getter
@Setter
@ConfigurationProperties(prefix = DynamicDataSourceProperties.PREFIX)
public class DynamicDataSourceProperties {
    public static final String PREFIX = "spring.datasource.dynamic";
    public static final String HEALTH = PREFIX + ".health";

    /**
     * Required DataSource. Default: master.
     */
    private String primary = "master";
    /**
     * If Ture,
     * Did not find the datasource, will throw exception.
     * Otherwise
     * will use the primary datasource.
     */
    private Boolean strict = false;
    /**
     * Enable p6spy or not?
     */
    private Boolean p6spy = false;
    /**
     * Enable spring actuator to do the check health?
     */
    private boolean health = false;
    /**
     * All of the data sources.
     */
    private Map<String, DataSourceProperty> datasource = new LinkedHashMap<>();
    /**
     * The routing algorithm for multi read(slave) data sources,
     * Use LB strategy by default.
     */
    private Class<? extends RoutingDataSourceStrategy> strategy = LoadBalanceRoutingDataSourceStrategy.class;
    /**
     * aop's order
     * Highest by default.
     */
    private Integer order = Ordered.HIGHEST_PRECEDENCE;
    /**
     * Druid global parameters.
     */
    @NestedConfigurationProperty
    private DruidConfig druid = new DruidConfig();
    /**
     * HikariCp global parameters.
     */
    @NestedConfigurationProperty
    private HikariCpConfig hikari = new HikariCpConfig();

    /**
     * Global publicKey
     */
    private String publicKey = CryptoUtil.DEFAULT_PUBLIC_KEY_STRING;
}

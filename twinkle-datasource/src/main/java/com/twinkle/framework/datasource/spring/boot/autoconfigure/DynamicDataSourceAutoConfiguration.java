package com.twinkle.framework.datasource.spring.boot.autoconfigure;

import com.twinkle.framework.datasource.DynamicDataSourceConfigure;
import com.twinkle.framework.datasource.DynamicDataSourceCreator;
import com.twinkle.framework.datasource.DynamicRoutingDataSource;
import com.twinkle.framework.datasource.aop.DynamicDataSourceAdvisor;
import com.twinkle.framework.datasource.aop.DynamicDataSourceAnnotationAdvisor;
import com.twinkle.framework.datasource.aop.DynamicDataSourceAnnotationInterceptor;
import com.twinkle.framework.datasource.plugin.DataSourceHealthIndicator;
import com.twinkle.framework.datasource.processor.HeaderDataSourceProcessor;
import com.twinkle.framework.datasource.processor.DataSourceProcessor;
import com.twinkle.framework.datasource.processor.SessionDataSourceProcessor;
import com.twinkle.framework.datasource.processor.SpelExpressionDataSourceProcessor;
import com.twinkle.framework.datasource.provider.DynamicDataSourceProvider;
import com.twinkle.framework.datasource.provider.YmlDynamicDataSourceProvider;
import com.twinkle.framework.datasource.spring.boot.autoconfigure.druid.DruidDynamicDataSourceConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import javax.sql.DataSource;

/**
 * Function: The Core Auto configure center. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/12/19 6:31 PM<br/>
 *
 * @author chenxj
 * @see DynamicDataSourceProvider
 * @see DynamicRoutingDataSource
 * @since JDK 1.8
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(DynamicDataSourceProperties.class)
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@Import(DruidDynamicDataSourceConfiguration.class)
@ConditionalOnProperty(prefix = DynamicDataSourceProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class DynamicDataSourceAutoConfiguration {

    @Autowired
    private DynamicDataSourceProperties properties;

    /**
     * Health Checker, will be used in MasterSlaveAutoRoutingPlugin
     *
     * @param _dataSource
     */
    @Bean
    @ConditionalOnProperty(DynamicDataSourceProperties.HEALTH)
    @ConditionalOnMissingBean
    public DataSourceHealthIndicator dataSourceHealthIndicator(DataSource _dataSource) {
        return new DataSourceHealthIndicator(_dataSource);
    }

    @Bean
    @ConditionalOnMissingBean
    public DynamicDataSourceProvider dynamicDataSourceProvider() {
        return new YmlDynamicDataSourceProvider(this.properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public DynamicDataSourceCreator dynamicDataSourceCreator() {
        DynamicDataSourceCreator tempDataSourceCreator = new DynamicDataSourceCreator();
        tempDataSourceCreator.setDruidGlobalConfig(this.properties.getDruid());
        tempDataSourceCreator.setHikariGlobalConfig(this.properties.getHikari());
        tempDataSourceCreator.setGlobalPublicKey(this.properties.getPublicKey());
        return tempDataSourceCreator;
    }

    @Bean
    @ConditionalOnMissingBean
    public DataSource dataSource(DynamicDataSourceProvider _provider) {
        DynamicRoutingDataSource tempDataSource = new DynamicRoutingDataSource();
        tempDataSource.setPrimary(this.properties.getPrimary());
        tempDataSource.setStrategy(this.properties.getStrategy());
        tempDataSource.setProvider(_provider);
        tempDataSource.setP6spy(this.properties.getP6spy());
        tempDataSource.setStrict(this.properties.getStrict());
        return tempDataSource;
    }

    @Bean
    @ConditionalOnMissingBean
    public DynamicDataSourceAnnotationAdvisor dynamicDatasourceAnnotationAdvisor(DataSourceProcessor _processor) {
        DynamicDataSourceAnnotationInterceptor tempInterceptor = new DynamicDataSourceAnnotationInterceptor();
        tempInterceptor.setDataSourceProcessor(_processor);
        DynamicDataSourceAnnotationAdvisor tempAdvisor = new DynamicDataSourceAnnotationAdvisor(
                tempInterceptor);
        tempAdvisor.setOrder(this.properties.getOrder());
        return tempAdvisor;
    }

    @Bean
    @ConditionalOnMissingBean
    public DataSourceProcessor dsProcessor() {
        HeaderDataSourceProcessor tempHeaderProcessor = new HeaderDataSourceProcessor();
        SessionDataSourceProcessor tempSessionProcessor = new SessionDataSourceProcessor();
        SpelExpressionDataSourceProcessor tempSpelExpressionProcessor = new SpelExpressionDataSourceProcessor();
        tempHeaderProcessor.setNextProcessor(tempSessionProcessor);
        tempSessionProcessor.setNextProcessor(tempSpelExpressionProcessor);
        return tempHeaderProcessor;
    }

    @Bean
    @ConditionalOnBean(DynamicDataSourceConfigure.class)
    public DynamicDataSourceAdvisor dynamicAdvisor(
            DynamicDataSourceConfigure _configure, DataSourceProcessor _processor) {
        DynamicDataSourceAdvisor tempAdvisor = new DynamicDataSourceAdvisor(
                _configure.getMatchers());
        tempAdvisor.setDataSourceProcessor(_processor);
        tempAdvisor.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return tempAdvisor;
    }
}

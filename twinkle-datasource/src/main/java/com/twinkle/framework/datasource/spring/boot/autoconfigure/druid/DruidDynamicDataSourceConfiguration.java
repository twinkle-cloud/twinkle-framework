package com.twinkle.framework.datasource.spring.boot.autoconfigure.druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.properties.DruidStatProperties;
import com.alibaba.druid.spring.boot.autoconfigure.stat.DruidFilterConfiguration;
import com.alibaba.druid.spring.boot.autoconfigure.stat.DruidSpringAopConfiguration;
import com.alibaba.druid.spring.boot.autoconfigure.stat.DruidStatViewServletConfiguration;
import com.alibaba.druid.spring.boot.autoconfigure.stat.DruidWebStatFilterConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/12/19 6:31 PM<br/>
 *
 * @author chenxj
 * @see DruidDataSourceAutoConfigure
 * @since JDK 1.8
 */
@Configuration
@ConditionalOnClass(DruidDataSource.class)
@EnableConfigurationProperties({DruidStatProperties.class})
@Import({
        DruidSpringAopConfiguration.class,
        DruidStatViewServletConfiguration.class,
        DruidWebStatFilterConfiguration.class,
        DruidFilterConfiguration.class})
public class DruidDynamicDataSourceConfiguration {

}

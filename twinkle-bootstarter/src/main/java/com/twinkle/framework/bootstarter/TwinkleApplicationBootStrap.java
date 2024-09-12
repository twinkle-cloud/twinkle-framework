package com.twinkle.framework.bootstarter;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-19 22:00<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@SpringBootApplication(scanBasePackages={"com.twinkle.framework"}, exclude= {DataSourceAutoConfiguration.class, DruidDataSourceAutoConfigure.class})
public class TwinkleApplicationBootStrap {
    public static void main(String[] args) {
        SpringApplication.run(TwinkleApplicationBootStrap.class, args);
//        new TwinkleLauncher().loadTwinkleService();
//        SpringUtil.setRootApplicationContext(tempContext);
    }
}

package com.twinkle.framework.bootstarter.config;

import com.twinkle.framework.api.constant.CommonConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(TwinkleLaunchProperties.class)
@ConfigurationProperties(prefix = CommonConstant.TWINKLE_PROPERTY_LAUNCH)
public class TwinkleLaunchProperties {
    /**
     * The initial Low-Code Template file path.
     */
    private String programLoadPath;
}

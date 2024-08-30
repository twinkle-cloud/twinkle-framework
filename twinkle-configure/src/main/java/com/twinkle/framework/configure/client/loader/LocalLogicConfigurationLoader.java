package com.twinkle.framework.configure.client.loader;

import com.alibaba.fastjson2.JSON;
import com.twinkle.framework.configure.client.data.ConfigClientProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-16 21:38<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
@Order(1)
public class LocalLogicConfigurationLoader implements PropertySourceLocator {
    private final String CHARACTER_ENCODE_UTF8 = "UTF-8";
    private ConfigClientProperties defaultProperties;
    public LocalLogicConfigurationLoader(ConfigClientProperties _properties){
        this.defaultProperties = _properties;
    }
    @Override
    public PropertySource<?> locate(Environment environment) {
        ConfigClientProperties properties = this.defaultProperties.override(environment);
        CompositePropertySource composite = new CompositePropertySource("configLogic");
        if(!StringUtils.hasText(properties.getLocalFile())){
            log.warn("The local file is not set in the property file.");
            return composite;
        }
        try {
            File tempFile = new File(properties.getLocalFile());
            if(!tempFile.isFile()) {
                log.warn("The local file [{}] is not a valid file.", properties.getLocalFile());
                return composite;
            }
            String tempConfiguration = FileUtils.readFileToString(tempFile, CHARACTER_ENCODE_UTF8);
            if(StringUtils.hasText(tempConfiguration)) {
                composite.addPropertySource(
                        new MapPropertySource(IConfigurationLoader.LOGIC_CONFIGURATION_KEY, JSON.parseObject(tempConfiguration)));
            }
        } catch (IOException ex) {
            log.warn("Encountered error while reading Logic configuration file [{}] from local.", properties.getLocalFile());
        }
        return composite;
    }
}

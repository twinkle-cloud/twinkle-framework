package com.twinkle.framework.configure.client.loader;

import com.twinkle.framework.configure.client.data.ConfigClientProperties;
import com.twinkle.framework.configure.client.data.ConfigurationPath;
import com.twinkle.framework.configure.client.data.IRemotePath;
import com.twinkle.framework.configure.client.parser.IConfigurationParser;
import com.twinkle.framework.configure.client.parser.LogicConfigurationParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-17 10:23<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
@Order(1)
public class SpringRemoteLogicConfigurationLoader extends AbstractSpringRemoteLoader {
    private final static String LOGIC_CONF_PROFILE = "LOGIC";
    public SpringRemoteLogicConfigurationLoader(ConfigClientProperties _properties){
        super(_properties);
    }
    @Override
    IConfigurationParser getConfigurationParser() {
        return new LogicConfigurationParser();
    }

    @Override
    IRemotePath getRemotePath(ConfigClientProperties properties, String label) {
        ConfigurationPath tempPath = ConfigurationPath.builder()
                .path("/{name}/" + LOGIC_CONF_PROFILE)
                .build();
        tempPath.addArg(properties.getName());
        tempPath.buildHttpHeader(properties);
        return tempPath;
    }
}

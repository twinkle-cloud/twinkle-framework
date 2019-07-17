package com.twinkle.framework.configure.client.loader;

import com.twinkle.framework.configure.client.data.ConfigClientProperties;
import com.twinkle.framework.configure.client.data.ConfigurationPath;
import com.twinkle.framework.configure.client.data.IRemotePath;
import com.twinkle.framework.configure.client.parser.IConfigurationParser;
import com.twinkle.framework.configure.client.parser.SysConfigurationParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-15 22:35<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
@Order(0)
public class SpringRemoteSysConfigurationLoader extends AbstractSpringRemoteLoader {
    public SpringRemoteSysConfigurationLoader(ConfigClientProperties defaultProperties){
        super(defaultProperties);
    }
    @Override
    IConfigurationParser getConfigurationParser() {
        return new SysConfigurationParser();
    }

    @Override
    IRemotePath getRemotePath(ConfigClientProperties properties, String label) {
        ConfigurationPath tempPath = ConfigurationPath.builder()
                .path("/{name}/{profile}")
                .build();
        tempPath.addArg(properties.getName());
        tempPath.addArg(properties.getProfile());

        if (StringUtils.hasText(label)) {
            if (label.contains("/")) {
                label = label.replace("/", "(_)");
            }
            tempPath.addArg(label);
            tempPath.setPath(tempPath.getPath() + "/{label}");
        }
        tempPath.buildHttpHeader(properties);
        return tempPath;
    }
}

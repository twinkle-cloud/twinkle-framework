package com.twinkle.framework.configure.client.loader;

import org.springframework.core.env.PropertySource;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-15 22:35<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface IConfigurationLoader {
    String LOGIC_CONFIGURATION_KEY = "Twinkle-Logic";
    PropertySource<?> loadPropertySource();
}

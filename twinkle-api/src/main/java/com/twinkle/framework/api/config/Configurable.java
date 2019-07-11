package com.twinkle.framework.api.config;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-10 17:00<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface Configurable {
    void configure(ConfigNode _conf) throws com.twinkle.framework.api.exception.ConfigurationException;
}

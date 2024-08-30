package com.twinkle.framework.api.config;

import com.alibaba.fastjson2.JSONObject;

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
    /**
     * The default no available value.
     */
    String NO_AVAILABLE = "NA";
    /**
     * Parse the configuration of the component.
     *
     * @param _conf
     * @throws com.twinkle.framework.api.exception.ConfigurationException
     */
    void configure(JSONObject _conf) throws com.twinkle.framework.api.exception.ConfigurationException;
}

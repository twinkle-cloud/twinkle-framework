package com.twinkle.framework.api.component.rule;

import com.alibaba.fastjson.JSONObject;
import com.twinkle.framework.api.exception.ConfigurationException;

/**
 * IRuleChain
 *
 * @author chenxj
 */
public interface IRuleChain extends IRule {
    /**
     * Configure the rule chain.
     *
     * @param _conf
     * @param _keyName
     * @throws ConfigurationException
     */
    void configureChain(JSONObject _conf, String _keyName) throws ConfigurationException;

    /**
     * Get the Rule chain's size.
     *
     * @return
     */
    int size();
}

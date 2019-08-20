package com.twinkle.framework.ruleengine;

import com.alibaba.fastjson.JSONObject;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.api.rule.IRuleChain;
import com.twinkle.framework.api.rule.IRuleChainManager;

/**
 * @author chenxj
 */
public class RuleChainManager implements IRuleChainManager {

    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {

    }

    @Override
    public IRuleChain getRuleChain(String _ruleChainName) {
        return null;
    }

    @Override
    public void addRuleChain(String _ruleChainName, IRuleChain _ruleChain) {

    }
}

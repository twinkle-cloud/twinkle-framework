package com.twinkle.framework.ruleengine;

import com.alibaba.fastjson.JSONObject;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.api.rule.IRuleChain;
import com.twinkle.framework.api.rule.RuleChainManager;

public class DefaultRuleChainManager implements RuleChainManager{

    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {

    }

    @Override
    public void initialize() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void registerRuleChain(IRuleChain IRuleChain) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public IRuleChain getRuleChain(String ruleKey) {
        // TODO Auto-generated method stub
        return null;
    }

}

package com.twinkle.framework.ruleengine;

import com.alibaba.fastjson.JSONObject;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.api.rule.Rule;
import com.twinkle.framework.api.rule.IRuleManager;

public class DefaultRuleManager implements IRuleManager {

    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {

    }

    @Override
    public void initialize() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void registerRule(Rule rule) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Rule getRule(String ruleKey) {
        // TODO Auto-generated method stub
        return null;
    }

}

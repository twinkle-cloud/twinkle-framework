package com.twinkle.framework.ruleengine;

import com.alibaba.fastjson.JSONObject;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.api.rule.IRuleChain;
import com.twinkle.framework.api.rule.IRule;
import com.twinkle.framework.core.context.model.NormalizedContext;

public class RuleChain implements IRuleChain {

    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {

    }

    @Override
    public void addRule(IRule IRule) {

    }

    @Override
    public void applyRule(NormalizedContext _context) {

    }
}

package com.twinkle.framework.api.rule;

import com.twinkle.framework.api.config.Configurable;

/**
 * RuleChainManager
 * */
public interface RuleChainManager extends Configurable {

    /**
     * RuleChainManager 初始化
     * */
    public void initialize();
    
    /**
     * 注册ruleChain
     * */
    public void registerRuleChain(IRuleChain IRuleChain);
    
    /**
     * 获取ruleChain
     * */
    public IRuleChain getRuleChain(String ruleKey);
}

package com.twinkle.framework.api.rule;

/**
 * RuleChainManager
 * */
public interface RuleChainManager {

    /**
     * RuleChainManager 初始化
     * */
    public void initialize();
    
    /**
     * 注册ruleChain
     * */
    public void registerRuleChain(RuleChain ruleChain);
    
    /**
     * 获取ruleChain
     * */
    public RuleChain getRuleChain(String ruleKey);
}

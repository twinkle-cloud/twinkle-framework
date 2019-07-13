package com.twinkle.framework.api.rule;

/**
 * RuleChain
 * */
public interface RuleChain {

    /**
     * RuleChain 初始化
     * */
    public void initialize();
    
    /**
     * 执行ruleChain
     * */
    public void applyRuleChain();
    
    /**
     * 添加rule
     * */
    public void addRule(Rule rule);
}

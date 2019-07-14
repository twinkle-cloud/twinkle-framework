package com.twinkle.framework.api.rule;

/**
 * RuleManager
 * */
public interface RuleManager {

    /**
     * RuleManager 初始化
     * */
    public void initialize();
    
    /**
     * 注册registerRule
     * */
    public void registerRule(Rule rule);
    
    /**
     * 获取rule
     * */
    public Rule getRule(String ruleKey);
}

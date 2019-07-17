package com.twinkle.framework.api.rule;

import com.twinkle.framework.api.config.Configurable;

/**
 * IRuleManager
 * */
public interface IRuleManager extends Configurable {

    /**
     * IRuleManager 初始化
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

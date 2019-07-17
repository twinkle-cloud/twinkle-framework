package com.twinkle.framework.api.rule;

import com.twinkle.framework.api.config.Configurable;

/**
 * IRuleChain
 * */
public interface IRuleChain extends Configurable {

    /**
     * IRuleChain 初始化
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

package com.twinkle.framework.api.rule;

/**
 * IRuleChain
 * */
public interface IRuleChain extends IRule {
    
    /**
     * 添加rule
     * */
    public void addRule(IRule IRule);
}

package com.twinkle.framework.api.rule;

/**
 * Rule
 * */
public interface Rule {

    /**
     * rule 初始化
     * */
    public void initialize();
    
    /**
     * 执行rule
     * */
    public void applyRule();
    
    /**
     * 判断nextRule
     * @return ruleKey
     * */
    public String deduceNextRule();
}

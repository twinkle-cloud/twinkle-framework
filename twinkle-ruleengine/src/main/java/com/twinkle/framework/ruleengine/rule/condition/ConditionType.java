package com.twinkle.framework.ruleengine.rule.condition;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     11/5/19 5:15 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public enum ConditionType {
    AND("com.twinkle.framework.ruleengine.rule.condition.AndCondition"),
    OR("com.twinkle.framework.ruleengine.rule.condition.OrCondition"),
    UNKNOWN("com.twinkle.framework.ruleengine.rule.condition.ConditionCheck");

    private String clazz;
    private ConditionType(String _clazz) {
        this.clazz = _clazz;
    }

    public String getClazz(){
        return this.clazz;
    }
}

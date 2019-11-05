package com.twinkle.framework.ruleengine.rule.condition;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     11/4/19 10:24 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public enum ConditionOperator {
    AND("and"), OR("or"),
    EQUALS("="), NE("!="),
    LT("<"), GT(">"),
    LE("<="), GE(">="),
    STARTS("starts"), NOT_STARTS("!starts"),
    ENDS("ends"),NOT_ENDS("!ends"),
    CONTAINS("contains"), NOT_CONTAINS("!contains"),
    IS("is"),IS_NOT("!is"),
    NULL("null"), IS_NULL("isnull"), IS_NOT_NULL("!isnull"),
    CONTAINS_ALL("containsAll"), NOT_CONTAINS_ALL("!containsAll"),
    WITHIN("within"), NOT_WITHIN("!within"),
    UNKNOWN("unknown");
    private String operator;
    private ConditionOperator (String _operator) {
        this.operator = _operator;
    }
    public static ConditionOperator valueOfOperator(String _operator){
        if(_operator.equals("and")) {
            return AND;
        }
        if(_operator.equals("or")) {
            return OR;
        }
        if(_operator.equals("=")) {
            return EQUALS;
        }
        if(_operator.equals("!=")) {
            return NE;
        }
        if(_operator.equals("<")) {
            return LT;
        }
        if(_operator.equals(">")) {
            return GT;
        }
        if(_operator.equals("<=")) {
            return LE;
        }
        if(_operator.equals(">=")) {
            return GE;
        }
        if(_operator.equals("within")) {
            return WITHIN;
        }
        if(_operator.equals("!within")) {
            return NOT_WITHIN;
        }
        if(_operator.equals("starts")) {
            return STARTS;
        }
        if(_operator.equals("!starts")) {
            return NOT_STARTS;
        }
        if(_operator.equals("ends")) {
            return ENDS;
        }
        if(_operator.equals("!ends")) {
            return NOT_ENDS;
        }
        if(_operator.equals("contains")) {
            return CONTAINS;
        }
        if(_operator.equals("!contains")) {
            return NOT_CONTAINS;
        }
        if(_operator.equals("is")) {
            return IS;
        }
        if(_operator.equals("!is")) {
            return IS_NOT;
        }
        if(_operator.equals("null")) {
            return NULL;
        }
        if(_operator.equals("isnull")) {
            return IS_NULL;
        }
        if(_operator.equals("!isnull")) {
            return IS_NOT_NULL;
        }
        if(_operator.equals("containsAll")) {
            return CONTAINS_ALL;
        }
        if(_operator.equals("!containsAll")) {
            return NOT_CONTAINS_ALL;
        }
        return UNKNOWN;
    }
}

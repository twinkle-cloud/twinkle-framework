package com.twinkle.framework.ruleengine.rule.support;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     11/4/19 5:30 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface IRange {
    char OPERATOR_INTERVAL = '-';
    char OPERATOR_LESS = '<';
    char OPERATOR_GREATER = '>';
    char[] OPERATORS = new char[]{OPERATOR_INTERVAL, OPERATOR_LESS, OPERATOR_GREATER};

    /**
     * To check the value is in the range or not?
     *
     * @param _val
     * @param <T>
     * @return
     */
    default <T> boolean testValue(T _val) {
        return false;
    };

    /**
     * To check the value is in the range or not?
     *
     * @param _val
     * @return
     */
    default boolean testValue(int _val){
        return false;
    };

    /**
     * To check the value is in the range or not?
     *
     * @param _val
     * @return
     */
    default boolean testValue(long _val){
        return false;
    };
}

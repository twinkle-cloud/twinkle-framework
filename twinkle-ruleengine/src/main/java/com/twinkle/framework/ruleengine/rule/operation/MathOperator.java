package com.twinkle.framework.ruleengine.rule.operation;

import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.exception.ConfigurationException;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/7/19 4:01 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public enum MathOperator {
    ADD("+"), SUBTRACT("-"), MULTIPLY("*"), DIVIDE("/"),
    MOD("%"), AND("&"), OR("|"), XOR("^"), SHIFTL("<<"), SHIFTR(">>"),
    UNKOWN("unknown");
    private String operator;

    private MathOperator(String _operator) {
        this.operator = _operator;
    }

    public static MathOperator valueOfOperator(String _operator) {
        if (_operator.equals("+")) {
            return ADD;
        } else if (_operator.equals("-")) {
            return SUBTRACT;
        } else if (_operator.equals("*")) {
            return MULTIPLY;
        } else if (_operator.equals("/")) {
            return DIVIDE;
        } else if (_operator.equals("%")) {
            return MOD;
        } else if (_operator.equals("&")) {
            return AND;
        } else if (_operator.equals("|")) {
            return OR;
        } else if (_operator.equals("^")) {
            return XOR;
        } else if (_operator.equals("<<")) {
            return SHIFTL;
        } else if (_operator.equals(">>")) {
            return SHIFTR;
        }
        throw new ConfigurationException(ExceptionCode.RULE_ADN_MATH_OPERATION_INVALID, "In MathOperation.loadOperations():  operator '+', '-', '*', '/', '&', '|', '%', '<<', or '>>' (" + _operator + ")");
    }
}

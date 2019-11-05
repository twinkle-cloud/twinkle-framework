package com.twinkle.framework.ruleengine.rule.support;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     11/4/19 5:12 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class IntegerRange implements IRange{
    private int operatorPos = -1;
    private int operatorType = -1;
    /**
     * The start Value of this range.
     */
    private int startValue = -1;
    /**
     * The end value of this range.
     */
    private int endValue = -1;

    static {
        Arrays.sort(OPERATORS);
    }

    /**
     * Initialize an integer range with start value, end value.
     *
     * @param _startValue
     * @param _endValue
     */
    public IntegerRange(int _startValue, int _endValue) {
        this.operatorType = Arrays.binarySearch(OPERATORS, OPERATOR_INTERVAL);
        this.startValue = _startValue;
        this.endValue = _endValue;
    }

    /**
     * Initialize an integer range with start value, end value and range key.
     *
     * @param _startValue
     * @param _endValue
     * @param _rangeKey
     */
    public IntegerRange(int _startValue, int _endValue, char _rangeKey) {
        this.operatorType = Arrays.binarySearch(OPERATORS, _rangeKey);
        this.startValue = _startValue;
        this.endValue = _endValue;
    }

    /**
     * Initialize an integer range with expression.
     *
     * @param _expression
     */
    public IntegerRange(String _expression) {
        String tempStartValue = null;
        String tempEndValue = null;
        if (this.findOperator(_expression)) {
            if (this.operatorType == Arrays.binarySearch(OPERATORS, OPERATOR_INTERVAL)) {
                tempStartValue = _expression.substring(0, this.operatorPos);
                tempEndValue = _expression.substring(this.operatorPos + 1);
            } else if (this.operatorType == Arrays.binarySearch(OPERATORS, OPERATOR_LESS)) {
                tempStartValue = "-2147483648";
                tempEndValue = _expression.substring(this.operatorPos + 1);
            } else if (this.operatorType == Arrays.binarySearch(OPERATORS, OPERATOR_GREATER)) {
                tempStartValue = _expression.substring(this.operatorPos + 1);
                tempEndValue = "2147483647";
            }

            this.startValue = Integer.parseInt(tempStartValue.trim());
            this.endValue = Integer.parseInt(tempEndValue.trim());
            log.debug("IntegerRange.IntegerRange({}) startValue = {} endValue = {}.", _expression, this.startValue, this.endValue);
        }
    }

    /**
     * Parse the expression to get the Operator.
     *
     * @param _expression
     * @return
     */
    private boolean findOperator(String _expression) {
        boolean tempFindFlag = false;
        String tempExpression = _expression.trim();

        for (int i = 0; i < OPERATORS.length; ++i) {
            int tempPos;
            if ((tempPos = tempExpression.indexOf(OPERATORS[i])) != -1) {
                this.operatorPos = tempPos;
                this.operatorType = i;
                tempFindFlag = true;
            }
        }

        if (!tempFindFlag) {
            log.warn("Could not find the integer range operator in the expression [{}].", _expression);
        }

        return tempFindFlag;
    }

    @Override
    public String toString() {
        return Integer.toString(this.startValue) + OPERATOR_INTERVAL + Integer.toString(this.endValue);
    }

    /**
     * To verify the given value is in this range or not?
     *
     * @param _val
     * @return
     */
    @Override
    public boolean testValue(int _val) {
        return _val >= this.startValue && _val <= this.endValue;
    }

    public static void main(String[] _args) {
        IntegerRange tempRange = new IntegerRange(_args[0]);
        System.out.println("produced the following: " + tempRange.toString());
    }
}

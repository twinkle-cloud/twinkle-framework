package com.twinkle.framework.ruleengine.rule.support;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     11/4/19 6:35 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class LongRange implements IRange {
    private int operatorPos_ = -1;
    private int operatorType_ = -1;
    private long startValue = -1L;
    private long endValue = -1L;

    static {
        Arrays.sort(OPERATORS);
    }

    public LongRange(long _startValue, long _endValue) {
        this.operatorType_ = Arrays.binarySearch(OPERATORS, OPERATOR_INTERVAL);
        this.startValue = _startValue;
        this.endValue = _endValue;
    }

    public LongRange(long _startValue, long _endValue, char _rangeKey) {
        this.operatorType_ = Arrays.binarySearch(OPERATORS, _rangeKey);
        this.startValue = _startValue;
        this.endValue = _endValue;
    }

    public LongRange(String _expression) {
        String tempStartValue = null;
        String tempEndValue = null;
        if (this.findOperator(_expression)) {
            if (this.operatorType_ == Arrays.binarySearch(OPERATORS, '-')) {
                tempStartValue = _expression.substring(0, this.operatorPos_);
                tempEndValue = _expression.substring(this.operatorPos_ + 1);
            } else if (this.operatorType_ == Arrays.binarySearch(OPERATORS, '<')) {
                // "-9223372036854775808";
                tempStartValue = Long.toString(Long.MIN_VALUE);
                tempEndValue = _expression.substring(this.operatorPos_ + 1);
            } else if (this.operatorType_ == Arrays.binarySearch(OPERATORS, '>')) {
                tempStartValue = _expression.substring(this.operatorPos_ + 1);
                //tempEndValue = "9223372036854775807";
                tempEndValue = Long.toString(Long.MAX_VALUE);
            }

            this.startValue = Long.parseLong(tempStartValue.trim());
            this.endValue = Long.parseLong(tempEndValue.trim());
            log.debug("LongRange.LongRange({}) start_val_ = {} end_val_ = {}", _expression, this.startValue, this.endValue);

        }
    }

    /**
     * Parse the expression to get the operator in the expression.
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
                this.operatorPos_ = tempPos;
                this.operatorType_ = i;
                tempFindFlag = true;
            }
        }

        if (!tempFindFlag) {
            log.warn("Could not find the lang range operator in the expression [{}].", _expression);
        }

        return tempFindFlag;
    }

    @Override
    public String toString() {
        return Long.toString(this.startValue) + OPERATOR_INTERVAL + Long.toString(this.endValue);
    }

    @Override
    public boolean testValue(long _val) {
        return _val >= this.startValue && _val <= this.endValue;
    }

    public static void main(String[] _args) {
        LongRange tempRange = new LongRange(_args[0]);
        System.out.println("produced the following: " + tempRange.toString());
    }
}

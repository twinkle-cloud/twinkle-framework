package com.twinkle.framework.ruleengine.rule.operation;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/11/19 5:52 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class CeiOperation extends AbstractRoundingOperation {
    @Override
    protected double roundingMethod(double _srcValue, double _precision) {
        return Math.ceil(_srcValue * _precision) / _precision;
    }
}

package com.twinkle.framework.ruleengine.rule.operation;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/11/19 5:53 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class RoundOperation extends AbstractRoundingOperation {
    @Override
    protected double roundingMethod(double _srcValue, double _precision) {
        return Math.round(_srcValue * _precision) / _precision;
    }
}

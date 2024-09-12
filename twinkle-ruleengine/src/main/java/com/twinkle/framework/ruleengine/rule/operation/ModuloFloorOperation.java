package com.twinkle.framework.ruleengine.rule.operation;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/11/19 6:26 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class ModuloFloorOperation extends AbstractConfigurableModuloRoundingOperation {

    @Override
    protected long moduloRoundingMethod(long _srcValue, long _modulo) {
        return _srcValue - _srcValue % _modulo;
    }

    @Override
    protected int moduloRoundingMethod(int _srcValue, int _modulo) {
        return _srcValue - _srcValue % _modulo;
    }
}

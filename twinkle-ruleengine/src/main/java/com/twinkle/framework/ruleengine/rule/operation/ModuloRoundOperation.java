package com.twinkle.framework.ruleengine.rule.operation;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/11/19 6:28 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class ModuloRoundOperation extends AbstractModuloRoundingOperation {
    @Override
    protected long moduloRoundingMethod(long _srcValue, long _modulo) {
        return _srcValue + _modulo / 2L - (_srcValue + _modulo / 2L) % _modulo;
    }

    @Override
    protected int moduloRoundingMethod(int _srcValue, int _modulo) {
        return _srcValue + _modulo / 2 - (_srcValue + _modulo / 2) % _modulo;
    }
}

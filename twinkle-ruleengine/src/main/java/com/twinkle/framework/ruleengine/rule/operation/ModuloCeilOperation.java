package com.twinkle.framework.ruleengine.rule.operation;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/11/19 6:24 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class ModuloCeilOperation extends AbstractModuloRoundingOperation {
    @Override
    protected long moduloRoundingMethod(long _srcValue, long _modulo) {
        return _srcValue + _modulo - 1L - (_srcValue + _modulo - 1L) % _modulo;
    }

    @Override
    protected int moduloRoundingMethod(int _srcValue, int _modulo) {
        return _srcValue + _modulo - 1 - (_srcValue + _modulo - 1) % _modulo;
    }
}

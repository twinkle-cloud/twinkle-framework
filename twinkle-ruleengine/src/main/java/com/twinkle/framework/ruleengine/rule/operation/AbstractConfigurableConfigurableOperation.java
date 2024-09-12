package com.twinkle.framework.ruleengine.rule.operation;

import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.api.exception.RuleException;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/7/19 10:14 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public abstract class AbstractConfigurableConfigurableOperation extends AbstractConfigurableAttributeOperation {
    protected boolean initialized = false;

    @Override
    public void applyRule(NormalizedContext _context) throws RuleException {
        if (!this.initialized) {
            throw new RuleException(ExceptionCode.RULE_APPLY_OPERATION_NOT_INITIALIZED, this.getClass().getSimpleName() + " has not been properly initialized");
        }
        this._applyRule(_context);
        if (this.nextRule != null) {
            this.nextRule.applyRule(_context);
        }
    }

    /**
     * Do the real apply.
     * @param _context
     * @throws RuleException
     */
    protected abstract void _applyRule(NormalizedContext _context) throws RuleException;

}

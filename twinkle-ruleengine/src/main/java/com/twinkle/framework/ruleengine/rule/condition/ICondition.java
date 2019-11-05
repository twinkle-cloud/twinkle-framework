package com.twinkle.framework.ruleengine.rule.condition;

import com.twinkle.framework.api.config.Configurable;
import com.twinkle.framework.api.exception.RuleException;
import com.twinkle.framework.api.context.NormalizedContext;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-19 10:07<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface ICondition extends Configurable {
    /**
     * Condition Operation Name in the expression line in the configuration file.
     */
    String S_O_NULL = "null";

    /**
     * Do the check.
     *
     * @param _context
     * @return
     * @throws RuleException
     */
    boolean check(NormalizedContext _context) throws RuleException;

    /**
     * Do the condition check.
     *
     * @param _context
     * @param _validateFlag : Do the attr validation or not while doing the check.
     * @return
     * @throws RuleException
     */
    boolean check(NormalizedContext _context, boolean _validateFlag) throws RuleException;
}

package com.twinkle.framework.ruleengine.rule.condition;

import com.twinkle.framework.api.exception.RuleException;
import com.twinkle.framework.api.context.NormalizedContext;
import lombok.extern.slf4j.Slf4j;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-19 11:36<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class OrCondition extends AbstractBinaryCondition {
    @Override
    public boolean check(NormalizedContext _context, boolean _validateFlag) throws RuleException {
        return this.leftCondition.check(_context, _validateFlag) || this.rightCondition.check(_context, _validateFlag);
    }
}

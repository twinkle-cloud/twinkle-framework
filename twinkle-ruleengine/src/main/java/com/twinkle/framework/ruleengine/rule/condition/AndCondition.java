package com.twinkle.framework.ruleengine.rule.condition;

import com.twinkle.framework.api.exception.RuleException;
import com.twinkle.framework.core.context.model.NormalizedContext;
import lombok.extern.slf4j.Slf4j;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-19 11:29<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class AndCondition extends AbstractBinaryCondition {
    @Override
    public boolean check(NormalizedContext _context) throws RuleException {
        return this.leftCondition.check(_context) && this.rightCondition.check(_context);
    }
}

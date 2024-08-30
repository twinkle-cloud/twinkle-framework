package com.twinkle.framework.ruleengine.rule;

import com.alibaba.fastjson2.JSONObject;
import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.api.exception.RuleException;
import com.twinkle.framework.api.context.NormalizedContext;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-22 18:31<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class LastRule extends AbstractRule {
    private boolean executed;
    private boolean shouldCheck;

    public LastRule() {
        this.executed = false;
        this.shouldCheck = false;
    }

    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {

    }

    @Override
    public void applyRule(NormalizedContext _context) throws RuleException {
        this.executed = true;
        if (this.nextRule != null) {
            try {
                this.nextRule.applyRule(_context);
            } catch (RuleException e) {
                throw new RuleException(ExceptionCode.RULE_APPLY_OUTSIDE_RULECHAIN, "Exception from outside this chain", e);
            }
        }
    }

    public boolean wasInvoked() {
        return this.shouldCheck ? this.executed : true;
    }

    public void reset() {
        this.executed = false;
    }

    public void setShouldCheck(boolean _shouldCheck) {
        this.shouldCheck = _shouldCheck;
    }

}

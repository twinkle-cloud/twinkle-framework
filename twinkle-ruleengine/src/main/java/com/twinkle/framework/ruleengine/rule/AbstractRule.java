package com.twinkle.framework.ruleengine.rule;

import com.twinkle.framework.api.rule.IRule;
import com.twinkle.framework.core.context.ContextSchema;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-18 14:12<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public abstract class AbstractRule implements IRule {
    /**
     * The Next Rule.
     */
    protected transient IRule nextRule = null;
    /**
     * The Context Schema.
     */
    protected ContextSchema contextSchema;

    public AbstractRule() {
        contextSchema = ContextSchema.getInstance();
    }

    public void addNextRule(IRule _rule) {
        this.nextRule = _rule;
    }

    public IRule getNextRule() {
        return this.nextRule;
    }
}

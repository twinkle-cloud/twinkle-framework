package com.twinkle.framework.ruleengine.rule;

import com.twinkle.framework.api.component.AbstractComponent;
import com.twinkle.framework.api.component.rule.IRule;
import com.twinkle.framework.core.context.ContextSchema;
import lombok.Data;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-18 14:12<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Data
public abstract class AbstractRule extends AbstractComponent implements IRule {
    /**
     * The Next Rule.
     */
    protected transient IRule nextRule = null;
    /**
     * The Context Schema.
     */
    protected transient ContextSchema contextSchema;

    public AbstractRule() {
        contextSchema = ContextSchema.getInstance();
    }

    @Override
    public void addNextRule(IRule _rule) {
        this.nextRule = _rule;
    }

    public IRule getNextRule() {
        return this.nextRule;
    }
}

package com.twinkle.framework.ruleengine.rule;

import com.twinkle.framework.api.component.AbstractConfigurableComponent;
import com.twinkle.framework.api.component.rule.IRule;
import com.twinkle.framework.context.PrimitiveAttributeSchema;
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
public abstract class AbstractConfigurableRule extends AbstractConfigurableComponent implements IRule {
    /**
     * The Next Rule.
     */
    protected transient IRule nextRule = null;
    /**
     * The Context Schema.
     */
    protected transient PrimitiveAttributeSchema primitiveAttributeSchema;

    public AbstractConfigurableRule() {
        primitiveAttributeSchema = PrimitiveAttributeSchema.getInstance();
    }

    @Override
    public void addNextRule(IRule _rule) {
        this.nextRule = _rule;
    }

    public IRule getNextRule() {
        return this.nextRule;
    }
}

package com.twinkle.framework.ruleengine.rule.condition;

import com.twinkle.framework.context.PrimitiveAttributeSchema;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-19 11:46<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public abstract class AbstractCondition implements ICondition {
    /**
     * The Context Schema.
     */
    protected PrimitiveAttributeSchema primitiveAttributeSchema;

    public AbstractCondition() {
        this.primitiveAttributeSchema = PrimitiveAttributeSchema.getInstance();
    }
}

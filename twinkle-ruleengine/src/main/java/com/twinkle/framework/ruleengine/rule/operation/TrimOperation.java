package com.twinkle.framework.ruleengine.rule.operation;

import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.api.exception.RuleException;
import com.twinkle.framework.core.lang.Attribute;
import lombok.extern.slf4j.Slf4j;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/11/19 8:05 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class TrimOperation extends AbstractConfigurableBinaryOperation {

    @Override
    public void applyRule(NormalizedContext _context) throws RuleException {
        log.debug("Going to apply TrimOperation.applyRule()");
        Attribute tempSrcAttr = _context.getAttribute(this.srcIndex);
        Attribute tempDestAttr = this.primitiveAttributeSchema.newAttributeInstance(this.destIndex);
        tempDestAttr.setValue(tempSrcAttr.toString().trim());
        if (this.setDstAttribute(_context, tempDestAttr) && this.nextRule != null) {
            this.nextRule.applyRule(_context);
        }
    }
}

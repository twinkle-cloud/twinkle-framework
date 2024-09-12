package com.twinkle.framework.ruleengine.rule.operation;

import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.api.exception.RuleException;
import com.twinkle.framework.core.lang.Attribute;
import lombok.extern.slf4j.Slf4j;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/11/19 6:51 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class LowerOperation extends AbstractConfigurableBinaryOperation {
    @Override
    public void applyRule(NormalizedContext _context) throws RuleException {
        log.debug("Going to apply LowerOperation.applyRule()");

        Attribute tempSrcAttr = _context.getAttribute(this.srcIndex);
        Attribute tempDestAttr = this.primitiveAttributeSchema.newAttributeInstance(this.destIndex);
        tempDestAttr.setValue(tempSrcAttr.toString().toLowerCase());
        if (this.setDstAttribute(_context, tempDestAttr) && this.nextRule != null) {
            this.nextRule.applyRule(_context);
        }
    }
}

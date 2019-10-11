package com.twinkle.framework.ruleengine.rule.operation;

import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.api.exception.RuleException;
import com.twinkle.framework.core.lang.Attribute;
import com.twinkle.framework.core.lang.IIntegerAttribute;
import lombok.extern.slf4j.Slf4j;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/11/19 6:41 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class LastindexofOperation extends IndexofOperation {
    @Override
    public void applyRule(NormalizedContext _context) throws RuleException {
        log.debug("Going to apply LastindexofOperation.applyRule()");

        Attribute tempSrcAttr = _context.getAttribute(this.srcIndex);
        IIntegerAttribute tempDestAttr = (IIntegerAttribute)this.primitiveAttributeSchema.newAttributeInstance(this.destIndex);
        tempDestAttr.setValue(tempSrcAttr.toString().lastIndexOf(this.getPatternString(_context)));
        this.setDstAttribute(_context, tempDestAttr);
        if (this.nextRule != null) {
            this.nextRule.applyRule(_context);
        }
    }
}

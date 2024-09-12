package com.twinkle.framework.ruleengine.rule.operation;

import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.api.exception.RuleException;
import com.twinkle.framework.core.lang.Attribute;
import com.twinkle.framework.core.lang.IIntegerAttribute;
import lombok.extern.slf4j.Slf4j;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/11/19 6:47 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class LengthOperation extends AbstractConfigurableBinaryOperation {
    @Override
    public void loadOperation(String _operation) throws ConfigurationException {
        super.loadOperation(_operation);
        if (!(this.primitiveAttributeSchema.newAttributeInstance(this.destIndex) instanceof IIntegerAttribute)) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION, "LengthOperation destination in " + _operation + " should be of type IntegerAttribute or LongAttribute");
        }
    }

    @Override
    public void applyRule(NormalizedContext _context) throws RuleException {
        log.debug("Going to apply LengthOperation.applyRule()");

        Attribute tempSrcAttr = _context.getAttribute(this.srcIndex);
        IIntegerAttribute tempDestAttr = (IIntegerAttribute)this.primitiveAttributeSchema.newAttributeInstance(this.destIndex);
        tempDestAttr.setValue(tempSrcAttr.toString().length());
        this.setDstAttribute(_context, tempDestAttr);
        if (this.nextRule != null) {
            this.nextRule.applyRule(_context);
        }
    }
}

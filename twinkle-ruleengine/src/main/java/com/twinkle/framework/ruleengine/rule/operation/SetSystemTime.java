package com.twinkle.framework.ruleengine.rule.operation;

import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.api.exception.RuleException;
import com.twinkle.framework.core.lang.*;
import lombok.extern.slf4j.Slf4j;

import java.util.StringTokenizer;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/7/19 5:27 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class SetSystemTime extends AttributeOperation {
    private int destIndex;
    private Attribute destAttribute;
    private int attrType = -1;

    public SetSystemTime() {
        this.destIndex = -1;
        this.destAttribute = null;
        this.attrType = -1;
    }

    @Override
    public void loadOperation(String _operation) throws ConfigurationException {
        StringTokenizer tempTokenizer = new StringTokenizer(_operation);
        if (tempTokenizer.countTokens() < 2) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION, "In SetSystemTime.loadOperation(): operation missing fields (" + _operation + ")");
        }
        String tempOperator = tempTokenizer.nextToken();
        if (!tempOperator.equals("setsystime")) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION, "In SetSystemTime.loadOperation(): only set operation supported, not (" + _operation + ")");
        }
        String tempDestAttrName = tempTokenizer.nextToken();
        this.destIndex = this.primitiveAttributeSchema.getAttributeIndex(tempDestAttrName, _operation);
        this.destAttribute = this.primitiveAttributeSchema.newAttributeInstance(this.destIndex);
        if (this.destAttribute instanceof ILongAttribute) {
            this.attrType = Attribute.LONG_TYPE;
        } else {
            if (!(this.destAttribute instanceof IIntegerAttribute)) {
                throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ATTR_NOT_ALLOWED, "Attribute type is not supported " + tempDestAttrName);
            }
            this.attrType = Attribute.INTEGER_TYPE;
        }
    }

    @Override
    public void applyRule(NormalizedContext _context) throws RuleException {
        log.debug("Going to apply SetSystemTime.applyRule()");
        long tempCurrentTime = System.currentTimeMillis();
        switch (this.attrType) {
            case Attribute.INTEGER_TYPE:
                ((IIntegerAttribute) this.destAttribute).setValue((int) (tempCurrentTime / 1000L));
                break;
            case Attribute.LONG_TYPE:
                ((ILongAttribute) this.destAttribute).setValue(tempCurrentTime);
                break;
            default:
                throw new RuleException(ExceptionCode.RULE_APPLY_OPERATION_NOT_SUPPORT, "Internal error, unsupported operation.");
        }

        this.modifyContextAttribute(_context, this.destAttribute, this.destIndex);
        if (this.nextRule != null) {
            this.nextRule.applyRule(_context);
        }
    }
}

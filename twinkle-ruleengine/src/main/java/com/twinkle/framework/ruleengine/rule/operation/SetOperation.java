package com.twinkle.framework.ruleengine.rule.operation;

import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.api.exception.RuleException;
import com.twinkle.framework.core.lang.Attribute;
import lombok.extern.slf4j.Slf4j;

import java.util.StringTokenizer;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/7/19 5:40 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class SetOperation extends AttributeOperation {
    private int destIndex;
    private Attribute destAttribute;
    private boolean isSetFlag;
    private boolean isDstTree_ = false;

    public SetOperation() {
        this.destIndex = -1;
        this.destAttribute = null;
        this.isSetFlag = false;
    }

    @Override
    public void loadOperation(String _operation) throws ConfigurationException {
        StringTokenizer tempTokenizer = new StringTokenizer(_operation);
        if (tempTokenizer.countTokens() < 3) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION, "In SetOperation.loadOperation(): operation missing fields (" + _operation + ")");
        }
        String tempOperation = tempTokenizer.nextToken();
        if (!tempOperation.equals("set")) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION, "In SetOperation.loadOperation(): only set operation supported, not (" + _operation + ")");
        }
        String tempDestAttrName = tempTokenizer.nextToken();

        this.destIndex = this.primitiveAttributeSchema.getAttributeIndex(tempDestAttrName, _operation);
        this.destAttribute = this.primitiveAttributeSchema.newAttributeInstance(this.destIndex);
        String tempValue = tempTokenizer.nextToken("\"");
        if (tempTokenizer.hasMoreTokens()) {
            tempValue = tempTokenizer.nextToken();
        } else {
            tempValue = tempValue.trim();
        }
        this.destAttribute.setValue(tempValue);
    }

    @Override
    public void applyRule(NormalizedContext _context) throws RuleException {
        log.debug("Going to apply SetOperation.applyRule()");

        if (this.modifyContextAttribute(_context, this.destAttribute, this.destIndex)) {
            this.isSetFlag = true;
        }
        if (this.nextRule != null) {
            this.nextRule.applyRule(_context);
        }
    }

    @Override
    public void reset(NormalizedContext _context) {
        log.debug("Going to apply SetOperation.reset()");
        if (this.destIndex != -1) {
            Attribute tempDestAttribute = _context.getAttribute(this.destIndex);
            if (tempDestAttribute != null) {
                if (this.isSetFlag) {
                    tempDestAttribute.setEmptyValue();
                }
            } else {
                _context.setAttribute(this.primitiveAttributeSchema.newAttributeInstance(this.destIndex), this.destIndex);
                this.isSetFlag = true;
            }

            if (this.nextRule != null) {
                ((AttributeOperation) this.nextRule).reset(_context);
            }

        }
    }
}

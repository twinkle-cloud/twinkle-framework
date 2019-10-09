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
 * Date:     10/7/19 4:54 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class SetIfNullOperation extends AbstractAttributeOperation {
    private int destIndex;
    private Attribute destAttr;
    private boolean isSetFlag;

    public SetIfNullOperation() {
        this.destIndex = -1;
        this.destAttr = null;
        this.isSetFlag = false;
    }

    public SetIfNullOperation(String _operation) {
        this();
        try {
            this.loadOperation("setifnull " + _operation);
        } catch (Exception e) {
            log.info("Load operation failed [{}].", e);
        }
    }

    @Override
    public void loadOperation(String _operation) throws ConfigurationException {
        StringTokenizer tempTokenizer = new StringTokenizer(_operation);
        if (tempTokenizer.countTokens() < 3) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION, "In SetIfNullOperation.loadOperation(): operation missing fields (" + _operation + ")");
        }
        String tempOperation = tempTokenizer.nextToken();
        if (!tempOperation.equals("setifnull")) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION, "In SetIfNullOperation.loadOperation(): only setifnull operation supported, not (" + _operation + ")");
        }
        String tempAttrName = tempTokenizer.nextToken();
        this.destIndex = this.primitiveAttributeSchema.getAttributeIndex(tempAttrName, _operation);
        this.destAttr = this.primitiveAttributeSchema.newAttributeInstance(this.destIndex);
        String tempAttrValue = tempTokenizer.nextToken("\"");
        if (tempTokenizer.hasMoreTokens()) {
            tempAttrValue = tempTokenizer.nextToken();
        } else {
            tempAttrValue = tempAttrValue.trim();
        }

        this.destAttr.setValue(tempAttrValue);
    }

    @Override
    public void applyRule(NormalizedContext _context) throws RuleException {
        Attribute tempAttribute = _context.getAttribute(this.destIndex);
        if (tempAttribute == null) {
            this.modifyContextAttribute(_context, this.destAttr, this.destIndex);
            this.isSetFlag = true;
        }

        if (this.nextRule != null) {
            this.nextRule.applyRule(_context);
        }
    }

    @Override
    public void reset(NormalizedContext _context) {
        log.debug("Going to apply SetIfNullOperation.reset()");
        if (this.destIndex != -1) {
            Attribute tempAttr = _context.getAttribute(this.destIndex);
            if (tempAttr != null) {
                if (this.isSetFlag) {
                    tempAttr.setEmptyValue();
                }
            } else {
                _context.setAttribute(this.primitiveAttributeSchema.newAttributeInstance(this.destIndex), this.destIndex);
                this.isSetFlag = true;
            }

            if (this.nextRule != null) {
                ((AbstractAttributeOperation) this.nextRule).reset(_context);
            }
        }
    }
}

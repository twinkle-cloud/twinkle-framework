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
 * Date:     10/7/19 5:15 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class CopyIfNullOperation extends AttributeOperation {
    private int srcIndex;
    private int destIndex;
    private Attribute destAttribute;
    private boolean isSetFlag;
    private boolean isSrcTree_;
    private boolean isDstTree_;

    public CopyIfNullOperation() {
        this.isSrcTree_ = false;
        this.isDstTree_ = false;
        this.srcIndex = -1;
        this.destIndex = -1;
        this.destAttribute = null;
        this.isSetFlag = false;
    }

    public CopyIfNullOperation(String _operation) {
        this();
        try {
            this.loadOperation("copyifnull " + _operation);
        } catch (Exception e) {
            log.info("exception {0}", e);
        }
    }

    @Override
    public void loadOperation(String _operation) throws ConfigurationException {
        StringTokenizer tempTokenizer = new StringTokenizer(_operation);
        if (tempTokenizer.countTokens() < 3) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION, "In CopyIfNullOperation.loadOperation(): operation missing fields (" + _operation + ")");
        }
        String tempOperation = tempTokenizer.nextToken();
        if (!tempOperation.equals("copyifnull")) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION, "In CopyIfNullOperation.loadOperation(): only copyifnull operation supported, not (" + _operation + ")");
        }
        String tempSrcAttrName = tempTokenizer.nextToken();
        this.srcIndex = this.primitiveAttributeSchema.getAttributeIndex(tempSrcAttrName, _operation);
        String tempDestAttrName = tempTokenizer.nextToken();
        this.destIndex = this.primitiveAttributeSchema.getAttributeIndex(tempDestAttrName, _operation);
        this.destAttribute = this.primitiveAttributeSchema.newAttributeInstance(this.destIndex);
    }

    @Override
    public void applyRule(NormalizedContext _context) throws RuleException {
        log.debug("Going to apply CopyIfNullOperation.applyRule()");

        Attribute tempDestAttribute = _context.getAttribute(this.destIndex);
        if (tempDestAttribute == null) {
            Attribute tempSrcAttribute = _context.getAttribute(this.srcIndex);
            if (tempSrcAttribute == null) {
                log.warn("Source attribute for copyifnull is not set in NME");
                return;
            }

            this.destAttribute.setValue(tempSrcAttribute);
            try {
                this.modifyContextAttribute(_context, this.destAttribute, this.destIndex);
            } catch (NumberFormatException e) {
                log.warn("CopyIfNull operation failed with NumberFormatException.", e);
                return;
            }
            this.isSetFlag = true;
        }

        if (this.nextRule != null) {
            this.nextRule.applyRule(_context);
        }
    }
    @Override
    public void reset(NormalizedContext _context) {
        log.debug("Going to apply CopyIfNullOperation.reset()");
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
                ((AttributeOperation)this.nextRule).reset(_context);
            }

        }
    }
}

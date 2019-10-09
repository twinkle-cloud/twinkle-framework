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
 * Date:     10/7/19 5:49 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class MoveOperation extends AbstractAttributeOperation {
    private int srcIndex;
    private int destIndex;
    private boolean isSrcTree_ = false;
    private boolean isDstTree_ = false;

    public MoveOperation() {
        this.srcIndex = -1;
        this.destIndex = -1;
    }

    @Override
    public void loadOperation(String _operation) throws ConfigurationException {
        StringTokenizer tempTokenizer = new StringTokenizer(_operation);
        if (tempTokenizer.countTokens() < 3) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION, "In MoveOperation.loadOperation(): operation missing fields (" + _operation + ")");
        }
        String tempOperator = tempTokenizer.nextToken();
        if (!tempOperator.equals("move")) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION, "In MoveOperation.loadOperation(): only move operation supported, not (" + _operation + ")");
        }
        String tempAttrName = tempTokenizer.nextToken();
        this.srcIndex = this.primitiveAttributeSchema.getAttributeIndex(tempAttrName, _operation);
        tempAttrName = tempTokenizer.nextToken();
        this.destIndex = this.primitiveAttributeSchema.getAttributeIndex(tempAttrName, _operation);

    }

    @Override
    public void applyRule(NormalizedContext _context) throws RuleException {
        log.debug("Going to apply MoveOperation.applyRule()");
        Attribute tempSrcAttribute = _context.getAttribute(this.srcIndex);
        this.modifyContextAttribute(_context, tempSrcAttribute, this.destIndex);
        tempSrcAttribute.setEmptyValue();
        if (this.nextRule != null) {
            this.nextRule.applyRule(_context);
        }
    }

    @Override
    public void reset(NormalizedContext _context) {
        log.debug("Going to apply MoveOperation.reset()");
        if (this.destIndex != -1) {
            Attribute tempDestAttribute = _context.getAttribute(this.destIndex);
            if (tempDestAttribute != null) {
                tempDestAttribute.setEmptyValue();
            } else {
                _context.setAttribute(this.primitiveAttributeSchema.newAttributeInstance(this.destIndex), this.destIndex);
            }
            if (this.nextRule != null) {
                ((AbstractAttributeOperation) this.nextRule).reset(_context);
            }
        }
    }
}

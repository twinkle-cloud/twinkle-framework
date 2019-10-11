package com.twinkle.framework.ruleengine.rule.operation;

import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.core.lang.Attribute;
import lombok.extern.slf4j.Slf4j;

import java.util.StringTokenizer;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/11/19 5:14 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public abstract class AbstractBinaryOperation extends AbstractAttributeOperation {
    protected int srcIndex;
    protected int destIndex;
    protected String operation;

    public AbstractBinaryOperation() {
        this.srcIndex = -1;
        this.destIndex = -1;
    }

    @Override
    public void loadOperation(String _operation) throws ConfigurationException {
        StringTokenizer tempTokenizer = new StringTokenizer(_operation);
        if (tempTokenizer.countTokens() < 3) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION, "Operation missing fields (" + _operation + ") - Requires three tokens <op> <src> <dst>");
        }
        this.operation = tempTokenizer.nextToken();
        String tempAttrName = tempTokenizer.nextToken();
        this.srcIndex = this.primitiveAttributeSchema.getAttributeIndex(tempAttrName, _operation);
        tempAttrName = tempTokenizer.nextToken();
        this.destIndex = this.primitiveAttributeSchema.getAttributeIndex(tempAttrName, _operation);
    }

    /**
     * Update the Dest Attribute.
     *
     * @param _context
     * @param _srcAttr
     * @return
     */
    protected boolean setDstAttribute(NormalizedContext _context, Attribute _srcAttr) {
        try {
            this.modifyContextAttribute(_context, _srcAttr, this.destIndex);
            return true;
        } catch (NumberFormatException e) {
            log.warn("Update Dest Attribute failed, Exception: {}", e);
            return false;
        }
    }
}

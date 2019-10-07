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
 * Date:     10/7/19 6:07 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class SwapOperation extends AttributeOperation {
    private int srcIndex;
    private int destIndex;

    public SwapOperation() {
        this.srcIndex = -1;
        this.destIndex = -1;
    }

    @Override
    public void loadOperation(String _operation) throws ConfigurationException {
        StringTokenizer tempTokenizer = new StringTokenizer(_operation);
        if (tempTokenizer.countTokens() < 3) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION, "In SwapOperation.loadOperation(): operation missing fields (" + _operation + ")");
        }
        String tempOperator = tempTokenizer.nextToken();
        if (!tempOperator.equals("swap")) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION, "In SwapOperation.loadOperation(): only swap operation supported, not (" + _operation + ")");
        }
        String tempAttrName = tempTokenizer.nextToken();
        this.srcIndex = this.primitiveAttributeSchema.getAttributeIndex(tempAttrName, _operation);
        tempAttrName = tempTokenizer.nextToken();
        this.destIndex = this.primitiveAttributeSchema.getAttributeIndex(tempAttrName, _operation);
    }

    @Override
    public void applyRule(NormalizedContext _context) throws RuleException {
        log.debug("Going to apply SwapOperation.applyRule()");

        Attribute tempSrcAttribute = (Attribute) _context.getAttribute(this.srcIndex).clone();
        this.modifyContextAttribute(_context, _context.getAttribute(this.destIndex), this.srcIndex);
        this.modifyContextAttribute(_context, tempSrcAttribute, this.destIndex);
        if (this.nextRule != null) {
            this.nextRule.applyRule(_context);
        }
    }
}

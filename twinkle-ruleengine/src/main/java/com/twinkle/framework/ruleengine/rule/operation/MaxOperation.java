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
 * Date:     10/7/19 4:51 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class MaxOperation extends AbstractConfigurableAttributeOperation {
    private int leftAttrIndex;
    private int rightAttrIndex;
    private int destAttrIndex;

    @Override
    public void loadOperation(String _operation) throws ConfigurationException {
        StringTokenizer tempTokenizer = new StringTokenizer(_operation);
        if (tempTokenizer.countTokens() < 4) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION, "In MaxOperation.loadOperation(): operation missing fields (" + _operation + ")");
        }
        String tempOperator = tempTokenizer.nextToken();
        if (!tempOperator.equals("max")) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION, "In MaxOperation.loadOperation(): only max operation supported, not (" + _operation + ")");
        }
        String tempAttrName = tempTokenizer.nextToken();
        this.leftAttrIndex = this.primitiveAttributeSchema.getAttributeIndex(tempAttrName, _operation);
        tempAttrName = tempTokenizer.nextToken();
        this.rightAttrIndex = this.primitiveAttributeSchema.getAttributeIndex(tempAttrName, _operation);
        tempAttrName = tempTokenizer.nextToken();
        this.destAttrIndex = this.primitiveAttributeSchema.getAttributeIndex(tempAttrName, _operation);

    }

    @Override
    public void applyRule(NormalizedContext _context) throws RuleException {
        log.debug("Going to apply MaxOperation.applyRule().");
        Attribute tempLeftAttr = _context.getAttribute(this.leftAttrIndex);
        Attribute tempRightAttr = _context.getAttribute(this.rightAttrIndex);

        Attribute tempDestAttr = null;
        if (tempLeftAttr.compareTo(tempRightAttr) > 0) {
            tempDestAttr = tempLeftAttr;
        } else {
            tempDestAttr = tempRightAttr;
        }
        //Update the min value into the dest attribute in the context.
        this.modifyContextAttribute(_context, tempDestAttr, this.destAttrIndex);
        if (this.nextRule != null) {
            this.nextRule.applyRule(_context);
        }
    }
}

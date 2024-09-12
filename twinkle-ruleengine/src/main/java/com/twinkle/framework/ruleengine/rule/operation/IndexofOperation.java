package com.twinkle.framework.ruleengine.rule.operation;

import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.api.exception.RuleException;
import com.twinkle.framework.core.lang.Attribute;
import com.twinkle.framework.core.lang.IIntegerAttribute;
import lombok.extern.slf4j.Slf4j;

import java.util.StringTokenizer;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/11/19 6:32 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class IndexofOperation extends AbstractConfigurableBinaryOperation {
    protected String pattern;
    protected int patternAttrIndex = -1;

    @Override
    public void loadOperation(String _operation) throws ConfigurationException {
        StringBuffer tempBuffer = new StringBuffer();
        StringTokenizer tempTokenizer = new StringTokenizer(_operation);
        if (tempTokenizer.countTokens() < 4) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION, "Operation missing fields (" + _operation + ") - Requires four tokens <op> <string> <src> <dst>");
        }
        tempBuffer.append(tempTokenizer.nextToken());
        tempBuffer.append(" ");
        this.pattern = tempTokenizer.nextToken();
        this.pattern = this.pattern.trim();
        if (this.pattern.startsWith("\"")) {
            if (this.pattern.length() != 1 && this.pattern.endsWith("\"")) {
                this.pattern = this.pattern.substring(1, this.pattern.length() - 1);
            } else {
                this.pattern = this.pattern + tempTokenizer.nextToken("\"");
                this.pattern = this.pattern.substring(1);
                tempTokenizer.nextToken(" ");
            }
        } else {
            this.patternAttrIndex = this.primitiveAttributeSchema.getAttributeIndex(this.pattern, _operation);
        }

        if (tempTokenizer.countTokens() < 2) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION, "Operation missing fields (" + _operation + ") - Requires four tokens <op> <string or attr> <src> <dst>");
        } else {
            tempBuffer.append(tempTokenizer.nextToken());
            tempBuffer.append(" ");
            tempBuffer.append(tempTokenizer.nextToken());
            super.loadOperation(tempBuffer.toString());
            if (!(this.primitiveAttributeSchema.newAttributeInstance(this.destIndex) instanceof IIntegerAttribute)) {
                throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION, "IndexofOperation destination in " + _operation + " should be of type IntegerAttribute or LongAttribute");
            }
        }
    }

    /**
     * Get pattern String.
     *
     * @param _context
     * @return
     */
    protected String getPatternString(NormalizedContext _context) {
        if (this.patternAttrIndex != -1) {
            return _context.getAttribute(this.patternAttrIndex).toString();
        } else {
            return this.pattern;
        }
    }
    @Override
    public void applyRule(NormalizedContext _context) throws RuleException {
        log.debug("Going to apply IndexofOperation.applyRule()");

        Attribute tempSrcAttr = _context.getAttribute(this.srcIndex);
        IIntegerAttribute tempDestAttr = (IIntegerAttribute) this.primitiveAttributeSchema.newAttributeInstance(this.destIndex);
        tempDestAttr.setValue(tempSrcAttr.toString().indexOf(this.getPatternString(_context)));
        this.setDstAttribute(_context, tempDestAttr);
        if (this.nextRule != null) {
            this.nextRule.applyRule(_context);
        }
    }
}

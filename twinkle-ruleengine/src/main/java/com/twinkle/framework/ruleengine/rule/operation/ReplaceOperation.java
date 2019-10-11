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
 * Date:     10/11/19 6:53 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class ReplaceOperation extends AbstractBinaryOperation {
    protected String pattern;
    protected String replace;
    protected int patternAttrIndex = -1;
    protected int replaceAttrIndex = -1;

    @Override
    public void loadOperation(String _operation) throws ConfigurationException {
        StringBuffer tempBuffer = new StringBuffer();
        StringTokenizer tempTokenizer = new StringTokenizer(_operation);
        if (tempTokenizer.countTokens() < 5) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION, "Operation missing fields (" + _operation + ") - Requires five tokens: replace <pattern> <src> <replace> <dst>");
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

        this.replace = tempTokenizer.nextToken();
        this.replace = this.replace.trim();
        if (this.replace.startsWith("\"")) {
            if (this.replace.length() != 1 && this.replace.endsWith("\"")) {
                this.replace = this.replace.substring(1, this.replace.length() - 1);
            } else {
                this.replace = this.replace + tempTokenizer.nextToken("\"");
                this.replace = this.replace.substring(1);
                tempTokenizer.nextToken(" ");
            }
        } else {
            this.replaceAttrIndex = this.primitiveAttributeSchema.getAttributeIndex(this.replace, _operation);
        }

        if (tempTokenizer.countTokens() < 2) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION, "Operation missing fields (" + _operation + ") - Requires five tokens: replace <pattern> <src> <replace> <dst>");
        }
        tempBuffer.append(tempTokenizer.nextToken());
        tempBuffer.append(" ");
        tempBuffer.append(tempTokenizer.nextToken());
        super.loadOperation(tempBuffer.toString());
    }

    @Override
    public void applyRule(NormalizedContext _context) throws RuleException {
        log.debug("Going to apply ReplaceOperation.applyRule()");

        String tempSrcStr = _context.getAttribute(this.srcIndex).toString();
        Attribute tempDestAttr = this.primitiveAttributeSchema.newAttributeInstance(this.destIndex);
        int tempIndex = 0;
        String tempPattern = this.getPatternStr(_context);
        String tempReplace = this.getReplaceStr(_context);
        int tempPatternIndex = tempSrcStr.indexOf(tempPattern);

        StringBuffer tempBuffer = new StringBuffer();
        for(; tempPatternIndex != -1; tempPatternIndex = tempSrcStr.indexOf(tempPattern, tempIndex)) {
            tempBuffer.append(tempSrcStr.substring(tempIndex, tempPatternIndex));
            tempBuffer.append(tempReplace);
            tempIndex = tempPatternIndex + tempPattern.length();
        }

        tempBuffer.append(tempSrcStr.substring(tempIndex));
        tempDestAttr.setValue(tempBuffer.toString());
        this.setDstAttribute(_context, tempDestAttr);
        if (this.nextRule != null) {
            this.nextRule.applyRule(_context);
        }
    }


    protected String getPatternStr(NormalizedContext _context) {
        if (this.patternAttrIndex != -1) {
            return _context.getAttribute(this.patternAttrIndex).toString();
        } else {
            return this.pattern;
        }
    }

    protected String getReplaceStr(NormalizedContext _context) {
        if (this.replaceAttrIndex != -1) {
            return _context.getAttribute(this.replaceAttrIndex).toString();
        } else {
            return this.replace;
        }
    }

}

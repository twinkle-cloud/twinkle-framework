package com.twinkle.framework.ruleengine.rule.operation;

import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.api.exception.RuleException;
import com.twinkle.framework.core.lang.Attribute;
import com.twinkle.framework.core.lang.INumericAttribute;
import lombok.extern.slf4j.Slf4j;

import java.util.StringTokenizer;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/11/19 7:40 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class SubstrOperation extends AbstractConfigurableBinaryOperation {
    protected int beginIndex = -2147483648;
    protected int endIndex = -2147483648;
    protected int beginAttrIndex = -1;
    protected int endAttrIndex = -1;
    protected String beginPattern = null;
    protected String endPattern = null;
    protected String srcAttrName = null;
    protected String dstAttrName = null;

    @Override
    public void loadOperation(String _operation) throws ConfigurationException {
        StringBuffer tempBuffer = new StringBuffer();
        StringTokenizer tempTokenizer = new StringTokenizer(_operation);
        if (tempTokenizer.countTokens() < 5) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION, "Operation missing fields (" + _operation + ") - Requires five tokens: substr <begin> <end> <src> <dst>");
        }
        tempBuffer.append(tempTokenizer.nextToken());
        tempBuffer.append(" ");
        this.beginPattern = tempTokenizer.nextToken();
        this.beginPattern = this.beginPattern.trim();
        int tempKeyIndex;
        if (this.beginPattern.startsWith("\"")) {
            if (this.beginPattern.length() != 1 && this.beginPattern.endsWith("\"")) {
                this.beginPattern = this.beginPattern.substring(1, this.beginPattern.length() - 1);
            } else {
                this.beginPattern = this.beginPattern + tempTokenizer.nextToken("\"");
                this.beginPattern = this.beginPattern.substring(1);
                tempTokenizer.nextToken(" ");
            }
        } else if (Character.isDigit(this.beginPattern.charAt(0))) {
            this.beginIndex = Integer.parseInt(this.beginPattern);
        } else if (this.beginPattern.indexOf("length") != -1) {
            tempKeyIndex = this.beginPattern.indexOf("-");
            this.beginPattern = this.beginPattern.substring(tempKeyIndex);
            this.beginIndex = Integer.parseInt(this.beginPattern);
        } else {
            this.beginAttrIndex = this.primitiveAttributeSchema.getAttributeIndex(this.beginPattern, _operation);
        }

        this.endPattern = tempTokenizer.nextToken();
        this.endPattern = this.endPattern.trim();
        if (this.endPattern.startsWith("\"")) {
            if (this.endPattern.length() != 1 && this.endPattern.endsWith("\"")) {
                this.endPattern = this.endPattern.substring(1, this.endPattern.length() - 1);
            } else {
                this.endPattern = this.endPattern + tempTokenizer.nextToken("\"");
                this.endPattern = this.endPattern.substring(1);
                tempTokenizer.nextToken(" ");
            }
        } else if (Character.isDigit(this.endPattern.charAt(0))) {
            this.endIndex = Integer.parseInt(this.endPattern);
        } else if (this.endPattern.indexOf("length") != -1) {
            tempKeyIndex = this.endPattern.indexOf("-");
            if (tempKeyIndex != -1) {
                this.endPattern = this.endPattern.substring(tempKeyIndex);
                this.endIndex = Integer.parseInt(this.endPattern);
            } else {
                this.endIndex = 0;
            }
        } else {
            this.endAttrIndex = this.primitiveAttributeSchema.getAttributeIndex(this.endPattern, _operation);
        }

        if (tempTokenizer.countTokens() < 2) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION, "Operation missing fields (" + _operation + ") - Requires four tokens: substr <begin> <end> <src> <dst>");
        }
        this.srcAttrName = tempTokenizer.nextToken();
        tempBuffer.append(this.srcAttrName);
        tempBuffer.append(" ");
        this.dstAttrName = tempTokenizer.nextToken();
        tempBuffer.append(this.dstAttrName);
        super.loadOperation(tempBuffer.toString());
    }

    @Override
    public void applyRule(NormalizedContext _context) throws RuleException {
        log.debug("SubstrOperation:applyRule()");

        String tempSrcStr = _context.getAttribute(this.srcIndex).toString();
        int tempSrcLength = tempSrcStr.length();
        int tempBeginIndex;
        if (this.beginIndex != -2147483648) {
            if (this.beginIndex < 0) {
                tempBeginIndex = tempSrcLength + this.beginIndex;
            } else {
                tempBeginIndex = this.beginIndex;
            }
        } else {
            tempBeginIndex = this.getBeginIndex(_context, tempSrcStr);
        }

        int tempEndIndex;
        if (this.endIndex != -2147483648) {
            if (this.endIndex <= 0) {
                tempEndIndex = tempSrcLength + this.endIndex;
            } else {
                tempEndIndex = this.endIndex;
            }
        } else {
            tempEndIndex = this.getEndIndex(_context, tempSrcStr);
        }

        if (tempEndIndex > tempSrcLength) {
            log.debug("Configured end position {} is beyond the string {}'s length. Changing the end position value to the length of the string", tempEndIndex, tempSrcStr);
            if (tempBeginIndex <= tempSrcLength) {
                log.warn("The operation \"substr {} {} {} {}\" cannot be performed due to the length of [{}] is {}. The operation is changed to \"substr {} {} {} {}\"",
                        tempBeginIndex, this.endIndex, this.srcAttrName, this.dstAttrName, tempSrcStr, tempSrcLength, tempBeginIndex, tempSrcLength, this.srcAttrName, this.dstAttrName);
            }
            tempEndIndex = tempSrcLength;
        }

        Attribute tempAttr = this.primitiveAttributeSchema.newAttributeInstance(this.destIndex);
        if (tempBeginIndex >= 0 && tempEndIndex >= 0 && tempBeginIndex <= tempEndIndex) {
            tempAttr.setValue(tempSrcStr.substring(tempBeginIndex, tempEndIndex));
        } else {
            tempAttr.setValue("");
            if (tempBeginIndex > tempSrcLength) {
                log.warn("The operation substr \"{} {} {} {}\" cannot be performed as begin index {} exceeds end index {}. Finally, an empty string would be assigned to the {}.",
                        tempBeginIndex, tempEndIndex, this.srcAttrName, this.dstAttrName, tempBeginIndex, tempEndIndex, this.dstAttrName);
            }
        }

        if (this.setDstAttribute(_context, tempAttr) && this.nextRule != null) {
            this.nextRule.applyRule(_context);
        }
    }

    /**
     * Get begin index of substr operation.
     *
     * @param _context
     * @param _srcStr
     * @return
     */
    protected int getBeginIndex(NormalizedContext _context, String _srcStr) {
        if (this.beginAttrIndex != -1) {
            Attribute tempAttr = _context.getAttribute(this.beginAttrIndex);
            return tempAttr instanceof INumericAttribute ? ((INumericAttribute) tempAttr).getInt() : _srcStr.indexOf(tempAttr.toString());
        }
        int tempIndex = _srcStr.indexOf(this.beginPattern);
        return tempIndex >= 0 ? tempIndex + this.beginPattern.length() : tempIndex;
    }

    /**
     * Get end index of the substr operation.
     *
     * @param _context
     * @param _srcStr
     * @return
     */
    protected int getEndIndex(NormalizedContext _context, String _srcStr) {
        if (this.endAttrIndex != -1) {
            Attribute tempAttr = _context.getAttribute(this.endAttrIndex);
            return tempAttr instanceof INumericAttribute ? ((INumericAttribute) tempAttr).getInt() : _srcStr.lastIndexOf(tempAttr.toString());
        }
        return _srcStr.lastIndexOf(this.endPattern);
    }
}

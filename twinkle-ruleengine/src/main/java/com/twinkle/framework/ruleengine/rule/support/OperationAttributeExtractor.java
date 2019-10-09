package com.twinkle.framework.ruleengine.rule.support;

import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.core.lang.Attribute;
import com.twinkle.framework.ruleengine.rule.operation.DigestOperation;
import com.twinkle.framework.ruleengine.utils.OperationUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/7/19 10:46 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class OperationAttributeExtractor extends OperationAttributeInfo {
    public final AbstractOperationAttributeHelper attributeHelper;

    public OperationAttributeExtractor(String _attrName) throws ConfigurationException {
        super(_attrName);
        this.attributeHelper = OperationUtil.OPERATION_HELPERS.get(this.attrClass);
        if (this.attributeHelper == null) {
            throw new ConfigurationException(ExceptionCode.OPERATION_DIGEST_CAL_NOT_IMPLEMENTED, "Digest calculation is not implemented for the attribute type " + this.attrClass.getName() + " (attribute: " + _attrName + ")");
        } else {
            log.debug("OperationAttributeExtractor created for " + _attrName);
        }
    }

    public void extractData(DataOutputStream _outputStream, NormalizedContext _context) throws IOException {
        Attribute tempAttribute = _context.getAttribute(this.attrIndex);
        if (tempAttribute == null) {
            throw new IOException("Attribute " + this.attrName + " is not found in the context");
        }
        this.attributeHelper.writeAttributeValue(tempAttribute, _outputStream);
        log.debug("Extracted attribute: " + this.attrName + "; value " + tempAttribute.toString());
    }
}

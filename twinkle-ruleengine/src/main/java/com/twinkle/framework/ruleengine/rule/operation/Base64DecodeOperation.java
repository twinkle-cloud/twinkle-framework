package com.twinkle.framework.ruleengine.rule.operation;

import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.api.exception.RuleException;
import com.twinkle.framework.core.lang.BinaryAttribute;
import com.twinkle.framework.core.lang.StringAttribute;
import lombok.extern.slf4j.Slf4j;

import java.util.Base64;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/9/19 7:01 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class Base64DecodeOperation extends AbstractConfigurableBase64Operation {
    public static final String OP_CODE = "decBase64";
    @Override
    public void loadOperation(String _operation) throws ConfigurationException {
        this.loadOperation(_operation, OP_CODE);
        this.sourceAttrInfo.checkIfCompatibleWith(StringAttribute.class);
        this.targetAttrInfo.checkIfCompatibleWith(BinaryAttribute.class);
        this.initialized = true;
    }
    @Override
    protected void _applyRule(NormalizedContext _context) throws RuleException {
        StringAttribute tempAttr = (StringAttribute)this.sourceAttrInfo.getAttributeIfPresent(_context);

        try {
            ((BinaryAttribute)this.targetAttrInfo.getAttribute(_context)).setValue(Base64.getDecoder().decode(tempAttr.getValue()));
        } catch (IllegalArgumentException e) {
            log.warn("The given value couldn't be decoded as base64. Exception:", e);
        }
    }
}

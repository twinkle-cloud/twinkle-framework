package com.twinkle.framework.ruleengine.rule.support;

import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.context.AttributeInfo;
import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.api.exception.RuleException;
import com.twinkle.framework.context.PrimitiveAttributeSchema;
import com.twinkle.framework.core.lang.Attribute;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/7/19 10:29 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class OperationAttributeInfo {
    public final String attrName;
    public final int attrIndex;
    public final Class<?> attrClass;

    public OperationAttributeInfo(String _attrName) throws ConfigurationException {
        this.attrName = _attrName;
        AttributeInfo tempInfo = PrimitiveAttributeSchema.getInstance().getAttribute(_attrName);
        if (tempInfo == null) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ATTR_NOT_IN_SCHEMA, "Unknown attribute: " + _attrName);
        }
        this.attrIndex = tempInfo.getIndex();
        this.attrClass = tempInfo.newAttributeInstance().getClass();
    }

    public OperationAttributeInfo checkIfCompatibleWith(Class<?> _class) throws ConfigurationException {
        if (!_class.isAssignableFrom(this.attrClass)) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ATTR_CLASS_INCOMPATIBLE, String.format("%s attribyte's type %s is not compatible with %s", this.attrName, this.attrClass.toString(), _class.toString()));
        }
        return this;
    }

    public Attribute getAttribute(NormalizedContext _context) {
        Attribute tempAttribute = _context.getAttribute(this.attrIndex);
        if (tempAttribute == null) {
            if (!_context.getType().isMember(this.attrIndex)) {
                AttributeInfo tempInfo = PrimitiveAttributeSchema.getInstance().getAttribute(this.attrIndex);
                _context.getType().addAttribute(tempInfo);
            }

            tempAttribute = PrimitiveAttributeSchema.getInstance().newAttributeInstance(this.attrIndex);
            _context.setAttribute(tempAttribute, this.attrIndex);
        }

        return tempAttribute;
    }

    public Attribute getAttributeIfPresent(NormalizedContext _context) throws RuleException {
        Attribute tempAttribute = _context.getAttribute(this.attrIndex);
        if (tempAttribute == null) {
            throw new RuleException(ExceptionCode.RULE_ATTR_NOT_INITIALIZED, String.format("The %s attribute is absent", this.attrName));
        } else {
            return tempAttribute;
        }
    }
}

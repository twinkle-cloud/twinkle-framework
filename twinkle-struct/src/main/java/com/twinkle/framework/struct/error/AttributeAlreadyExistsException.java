package com.twinkle.framework.struct.error;

import com.twinkle.framework.struct.type.StructAttributeType;
import lombok.Getter;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/29/19 10:39 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Getter
public final class AttributeAlreadyExistsException extends StructAttributeException {
    /**
     * Attribute name.
     */
    private String attributeName = null;
    /**
     * Struct NC Attribute type name.
     */
    private String structAttributeTypeName = null;

    public AttributeAlreadyExistsException(StructAttributeType _ncAttributeType, String _attrName) {
        super(_attrName + " attribute already exists in " + _ncAttributeType);
        this.attributeName = _attrName;
        this.structAttributeTypeName = _ncAttributeType != null ? _ncAttributeType.getName() : null;
    }

    public AttributeAlreadyExistsException(StructAttributeType _ncAttributeType, String _attrName, Throwable _exception) {
        super(_attrName + " attribute already exists in " + _ncAttributeType, _exception);
        this.attributeName = _attrName;
        this.structAttributeTypeName = _ncAttributeType != null ? _ncAttributeType.getName() : null;
    }

    public AttributeAlreadyExistsException(String _ncAttributeName, String _attrName) {
        super(_attrName + " attribute already exists in " + _ncAttributeName);
        this.attributeName = _attrName;
        this.structAttributeTypeName = _ncAttributeName;
    }

    public AttributeAlreadyExistsException(String _ncAttributeName, String _attrName, Throwable _exception) {
        super(_attrName + " attribute already exists in " + _ncAttributeName, _exception);
        this.attributeName = _attrName;
        this.structAttributeTypeName = _ncAttributeName;
    }
}

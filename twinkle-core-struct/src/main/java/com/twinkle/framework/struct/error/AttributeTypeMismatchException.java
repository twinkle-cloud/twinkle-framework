package com.twinkle.framework.struct.error;

import com.twinkle.framework.struct.ref.AttributeRef;
import com.twinkle.framework.core.type.AttributeType;
import lombok.Getter;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/29/19 10:47 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Getter
public final class AttributeTypeMismatchException extends StructAttributeException {
    private String attrName = null;
    private String actualTypeName = null;
    private String requestedTypeName = null;

    public AttributeTypeMismatchException(AttributeRef _attributeRef, AttributeType _actualType, AttributeType _requiredType) {
        super("The actual type of attribute " + _attributeRef + " is " + _actualType + " and is used as " + _requiredType);
        this.attrName = _attributeRef != null ? _attributeRef.getName() : null;
        this.actualTypeName = _actualType != null ? _actualType.getName() : null;
        this.requestedTypeName = _requiredType != null ? _requiredType.getName() : null;
    }

    public AttributeTypeMismatchException(AttributeRef _attributeRef, AttributeType _actualType) {
        super("The actual type of attribute " + _attributeRef + " is " + _actualType + " and is used as an array type.");
        this.attrName = _attributeRef != null ? _attributeRef.getName() : null;
        this.actualTypeName = _actualType != null ? _actualType.getName() : null;
    }

    public AttributeTypeMismatchException(AttributeRef _attributeRef, AttributeType _actualType, AttributeType _requiredType, Throwable var4) {
        super("The actual type of attribute " + _attributeRef + " is " + _actualType + " and is used as " + _requiredType, var4);
        this.attrName = _attributeRef != null ? _attributeRef.getName() : null;
        this.actualTypeName = _actualType != null ? _actualType.getName() : null;
        this.requestedTypeName = _requiredType != null ? _requiredType.getName() : null;
    }

    public AttributeTypeMismatchException(String _attributeName, String _actualTypeName, String _requiredTypeName) {
        super("The actual type of attribute " + _attributeName + " is " + _actualTypeName + " and is used as " + _requiredTypeName);
        this.attrName = _attributeName;
        this.actualTypeName = _actualTypeName;
        this.requestedTypeName = _requiredTypeName;
    }

    public AttributeTypeMismatchException(String _attributeName, String _actualTypeName) {
        super("The actual type of attribute " + _attributeName + " is " + _actualTypeName + " and is used as an array type.");
        this.attrName = _attributeName;
        this.actualTypeName = _actualTypeName;
    }

    public AttributeTypeMismatchException(String _attributeName, String _actualTypeName, String _requiredTypeName, Throwable _exception) {
        super("The actual type of attribute " + _attributeName + " is " + _actualTypeName + " and is used as " + _requiredTypeName, _exception);
        this.attrName = _attributeName;
        this.actualTypeName = _actualTypeName;
        this.requestedTypeName = _requiredTypeName;
    }
}

package com.twinkle.framework.core.error;

import com.twinkle.framework.core.lang.struct.AttributeRef;
import lombok.Getter;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/29/19 10:45 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Getter
public final class AttributeNotSetException extends StructAttributeException {
    private static final boolean FILL_STACKTRACE = System.getProperty(AttributeNotSetException.class.getName() + ".NoFillStackTrace") == null;
    private String attributeName = null;

    public AttributeNotSetException(AttributeRef _attributeRef) {
        super("A value for attribute " + _attributeRef + " is not set.");
        this.attributeName = _attributeRef != null ? _attributeRef.getName() : null;
    }

    public AttributeNotSetException(AttributeRef _attributeRef, Throwable _exception) {
        super("A value for attribute " + _attributeRef + " is not set.", _exception);
        this.attributeName = _attributeRef != null ? _attributeRef.getName() : null;
    }

    public AttributeNotSetException(String _attributeName) {
        super("A value for attribute " + _attributeName + " is not set.");
        this.attributeName = _attributeName;
    }

    public AttributeNotSetException(String _attributeName, Throwable _exception) {
        super("A value for attribute " + _attributeName + " is not set.", _exception);
        this.attributeName = _attributeName;
    }

    public Throwable fillInStackTrace() {
        return (Throwable)(FILL_STACKTRACE ? super.fillInStackTrace() : this);
    }
}

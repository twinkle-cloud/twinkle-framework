package com.twinkle.framework.struct.error;

import lombok.Getter;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/29/19 10:37 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Getter
public final class AttributeNotFoundException extends StructAttributeException {
    private static final boolean FILL_STACKTRACE = System.getProperty(AttributeNotFoundException.class.getName() + ".NoFillStackTrace") == null;
    private String attributeName = null;

    public AttributeNotFoundException(String _attrName) {
        super("Attribute " + _attrName + " not found.");
        this.attributeName = _attrName;
    }

    public AttributeNotFoundException(String _attrName, Throwable _exception) {
        super("Attribute " + _attrName + " not found.", _exception);
        this.attributeName = _attrName;
    }

    public Throwable fillInStackTrace() {
        return (Throwable)(FILL_STACKTRACE ? super.fillInStackTrace() : this);
    }
}

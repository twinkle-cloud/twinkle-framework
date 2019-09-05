package com.twinkle.framework.core.error;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/29/19 10:53 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public final class BadAttributeNameException extends StructAttributeException {
    public BadAttributeNameException(String _attributeName) {
        super(_attributeName);
    }

    public BadAttributeNameException(String _attributeName, Throwable _exception) {
        super(_attributeName, _exception);
    }
}

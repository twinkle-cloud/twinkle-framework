package com.twinkle.framework.core.error;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/4/19 10:55 AM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class StructAttributeInstantiationException extends StructAttributeException {
    public StructAttributeInstantiationException(String _msg) {
        super(_msg);
    }

    public StructAttributeInstantiationException(String _msg, Throwable _exception) {
        super(_msg, _exception);
    }

    public StructAttributeInstantiationException(Throwable _exception) {
        super(_exception);
    }
}

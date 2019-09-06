package com.twinkle.framework.struct.error;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/29/19 10:30 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class StructAttributeException extends RuntimeException {
    private static final boolean FILL_STACKTRACE = System.getProperty(StructAttributeException.class.getName() + ".NoFillStackTrace") == null;

    public StructAttributeException(String _msg) {
        super(_msg);
    }

    public StructAttributeException(String _msg, Throwable _exception) {
        super(_msg, _exception);
    }

    public StructAttributeException(Throwable _exception) {
        super(_exception);
    }

    public Throwable fillInStackTrace() {
        return (Throwable)(FILL_STACKTRACE ? super.fillInStackTrace() : this);
    }
}

package com.twinkle.framework.core.error;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/4/19 6:23 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class SerializationException extends RuntimeException {
    public SerializationException() {
    }

    public SerializationException(String _msg) {
        super(_msg);
    }

    public SerializationException(String _msg, Throwable _e) {
        super(_msg, _e);
    }

    public SerializationException(Throwable _e) {
        super(_e);
    }

    public SerializationException(String _msg, Throwable _e, boolean _enableSuppression, boolean _writableStackTrace) {
        super(_msg, _e, _enableSuppression, _writableStackTrace);
    }
}

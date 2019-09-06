package com.twinkle.framework.core.error;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/30/19 10:18 AM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class NoSpaceException extends RuntimeException {
    public NoSpaceException(String _msg) {
        super(_msg);
    }

    public NoSpaceException(String _msg, Throwable _exception) {
        super(_msg, _exception);
    }

    public NoSpaceException(Throwable _exception) {
        super(_exception);
    }
}

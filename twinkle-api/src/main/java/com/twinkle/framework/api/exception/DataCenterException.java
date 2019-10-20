package com.twinkle.framework.api.exception;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/16/19 6:35 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class DataCenterException extends RuntimeException {

    private int code;
    private String message;

    public DataCenterException() {
    }

    public DataCenterException(int _code, String _msg) {
        super(_msg);
        this.code = _code;
    }

    public DataCenterException(int _code, String _msg, Throwable _cause) {
        super(_msg, _cause);
        this.code = _code;
        this.message = _msg;
    }
}

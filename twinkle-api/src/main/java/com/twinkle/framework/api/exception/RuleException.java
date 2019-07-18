package com.twinkle.framework.api.exception;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-18 15:57<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class RuleException extends Exception {
    private int code;
    private String message;

    public RuleException() {
    }

    public RuleException(int _code, String _msg) {
        super(_msg);
        this.code = _code;
    }

    public RuleException(int _code, String _msg, Throwable _cause) {
        super(_msg, _cause);
        this.code = _code;
        this.message = _msg;
    }
}

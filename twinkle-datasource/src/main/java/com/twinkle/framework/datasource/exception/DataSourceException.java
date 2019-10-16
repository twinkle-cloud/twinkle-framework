package com.twinkle.framework.datasource.exception;

/**
 * Function: Data source Related Exception. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/12/19 6:31 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class DataSourceException extends RuntimeException {
    /**
     * The exception code, refer to ExceptionCode.
     */
    private int code;

    public DataSourceException(int _code, String _message) {
        super(_message);
        this.code = _code;
    }

    public DataSourceException(int _code, String _message, Throwable _cause) {
        super(_message, _cause);
        this.code = _code;
    }
}

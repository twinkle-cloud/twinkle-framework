package com.twinkle.framework.api.exception;

import lombok.Getter;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-10 11:05<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Getter
public class ConfigurationException extends RuntimeException {
    /**
     * The exception code, refer to ExceptionCode.
     */
    private int code;

    public ConfigurationException(){
        super();
    }

    public ConfigurationException(int _code, String _msg) {
        super(_msg);
    }
    public ConfigurationException(int _code, String _msg, Throwable _e) {
        super(_msg, _e);
    }
}

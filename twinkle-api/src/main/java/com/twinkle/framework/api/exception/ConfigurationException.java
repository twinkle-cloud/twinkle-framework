package com.twinkle.framework.api.exception;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-10 11:05<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class ConfigurationException extends RuntimeException {
    private int code;

    public ConfigurationException(){
        super();
    }

    public ConfigurationException(int _code, String _msg) {
        super(_msg);
    }
}

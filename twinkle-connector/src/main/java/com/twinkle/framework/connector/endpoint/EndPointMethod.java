package com.twinkle.framework.connector.endpoint;

import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.exception.ConfigurationException;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-20 17:39<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public enum EndPointMethod {

    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    /**
     * Get the Enum value with given Enum Index.
     *
     * @param _value
     * @return
     */
    public static EndPointMethod valueOf(int _value) {
        switch (_value) {
            case 1:
                return GET;
            case 2:
                return HEAD;
            case 3:
                return POST;
            case 4:
                return PUT;
            case 5:
                return PATCH;
            case 6:
                return DELETE;
            case 7:
                return OPTIONS;
            case 8:
                return TRACE;
            default:
                throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ATTR_VALUE_INVALID, "HTTP method [" + _value + "] is not supported.");
        }
    }
}

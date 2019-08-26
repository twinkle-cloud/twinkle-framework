package com.twinkle.framework.connector.http.endpoint;

import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.exception.ConfigurationException;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 Refer Spring.METHOD <br/>
 * Date:     2019-08-20 17:39<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public enum HttpEndPointMethod {

    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    /**
     * Get the Enum value with given Enum Index.
     *
     * @param _value
     * @return
     */
    public static HttpEndPointMethod valueOf(int _value) {
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

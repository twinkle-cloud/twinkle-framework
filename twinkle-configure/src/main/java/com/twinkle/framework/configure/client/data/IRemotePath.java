package com.twinkle.framework.configure.client.data;

import org.springframework.http.HttpHeaders;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-16 20:51<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface IRemotePath {
    /**
     * Token header name.
     */
    String TOKEN_HEADER = "X-Config-Token";

    /**
     * State header name.
     */
    String STATE_HEADER = "X-Config-State";
    /**
     * Authorization header name.
     */
    String AUTHORIZATION = "authorization";

    String getFullPath();
    String[] getArgs();
    HttpHeaders getHttpHeaders(ConfigClientProperties _properties, int _uriIndex);
}

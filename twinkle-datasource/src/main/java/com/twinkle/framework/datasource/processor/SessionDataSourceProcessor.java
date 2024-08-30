package com.twinkle.framework.datasource.processor;

import jakarta.servlet.http.HttpServletRequest;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/12/19 6:31 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class SessionDataSourceProcessor extends DataSourceProcessor {
    /**
     * session
     */
    private static final String SESSION_PREFIX = "#session";

    @Override
    public boolean matches(String _key) {
        return _key.startsWith(SESSION_PREFIX);
    }

    @Override
    public String doDetermineDatasource(MethodInvocation _invocation, String _key) {
        HttpServletRequest tempRequest = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        return tempRequest.getSession().getAttribute(_key.substring(9)).toString();
    }
}

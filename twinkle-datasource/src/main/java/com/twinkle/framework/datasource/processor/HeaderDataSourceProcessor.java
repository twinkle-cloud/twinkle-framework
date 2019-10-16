package com.twinkle.framework.datasource.processor;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Function: Header processor. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/12/19 6:31 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class HeaderDataSourceProcessor extends DataSourceProcessor {
    /**
     * header prefix
     */
    private static final String HEADER_PREFIX = "#header";

    @Override
    public boolean matches(String _key) {
        return _key.startsWith(HEADER_PREFIX);
    }

    @Override
    public String doDetermineDatasource(MethodInvocation _invocation, String _key) {
        HttpServletRequest tempRequest = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        return tempRequest.getHeader(_key.substring(8));
    }
}

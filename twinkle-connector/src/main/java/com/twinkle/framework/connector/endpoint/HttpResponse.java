package com.twinkle.framework.connector.endpoint;

import com.twinkle.framework.api.config.Configurable;
import com.twinkle.framework.core.datastruct.descriptor.TypeDescriptor;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-20 14:22<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface HttpResponse extends Configurable {
    /**
     * Get the return type of the endpoint's method.
     *
     * @return
     */
    TypeDescriptor getMethodReturnType();
}

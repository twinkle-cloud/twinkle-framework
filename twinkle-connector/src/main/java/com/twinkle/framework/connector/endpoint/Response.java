package com.twinkle.framework.connector.endpoint;

import com.twinkle.framework.api.config.Configurable;
import com.twinkle.framework.core.datastruct.descriptor.TypeDescriptor;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/24/19 5:12 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface Response extends Configurable {
    /**
     * Get the return type of the endpoint's method.
     *
     * @return
     */
    TypeDescriptor getMethodReturnType();
}

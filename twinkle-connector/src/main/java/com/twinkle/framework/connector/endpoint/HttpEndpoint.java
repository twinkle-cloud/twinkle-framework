package com.twinkle.framework.connector.endpoint;

import com.twinkle.framework.api.config.Configurable;
import com.twinkle.framework.core.datastruct.descriptor.MethodTypeDescriptor;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-31 11:18<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface HttpEndpoint extends Configurable {
    /**
     * Get Method's descriptor.
     *
     * @return
     */
    MethodTypeDescriptor getMethodDescriptor();
}

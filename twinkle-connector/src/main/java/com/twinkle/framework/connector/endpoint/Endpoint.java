package com.twinkle.framework.connector.endpoint;

import com.twinkle.framework.api.component.IComponent;
import com.twinkle.framework.api.config.Configurable;
import com.twinkle.framework.core.datastruct.descriptor.MethodTypeDescriptor;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/24/19 5:10 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface Endpoint extends Configurable, IComponent {

    /**
     * Get Method's descriptor.
     *
     * @return
     */
    MethodTypeDescriptor getMethodDescriptor();
}

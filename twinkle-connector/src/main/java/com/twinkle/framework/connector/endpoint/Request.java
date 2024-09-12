package com.twinkle.framework.connector.endpoint;

import com.twinkle.framework.api.component.IConfigurableComponent;
import com.twinkle.framework.asm.descriptor.AttributeDescriptor;

import java.util.List;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/24/19 5:12 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface Request extends IConfigurableComponent {
    /**
     * Get the method's parameters by given method type.
     *
     * @Param _requestType
     * @return
     */
    List<AttributeDescriptor> getMethodParameters();
}

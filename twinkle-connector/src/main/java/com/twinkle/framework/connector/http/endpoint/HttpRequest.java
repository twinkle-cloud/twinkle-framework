package com.twinkle.framework.connector.http.endpoint;

import com.twinkle.framework.asm.descriptor.AttributeDescriptor;
import com.twinkle.framework.connector.endpoint.Request;

import java.util.List;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-20 14:20<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface HttpRequest extends Request {
    /**
     * Get the method's parameters by given method type.
     *
     * @Param _requestType
     * @return
     */
    List<AttributeDescriptor> getMethodParameters(HttpEndPointMethod _requestType);
}

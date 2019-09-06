package com.twinkle.framework.connector.http.server.endpoint;

import com.twinkle.framework.asm.handler.MethodInstructionHandler;
import com.twinkle.framework.connector.http.endpoint.AbstractHttpEndpoint;
import com.twinkle.framework.connector.http.server.handler.DefaultHttpMethodInstructionHandler;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-21 17:08<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class RestHttpEndpoint extends AbstractHttpEndpoint {
    protected MethodInstructionHandler getInstructionHandler(){
        return new DefaultHttpMethodInstructionHandler();
    }
}

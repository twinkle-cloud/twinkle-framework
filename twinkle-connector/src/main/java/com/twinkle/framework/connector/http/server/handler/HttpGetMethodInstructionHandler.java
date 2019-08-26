package com.twinkle.framework.connector.http.server.handler;

import com.twinkle.framework.core.datastruct.handler.AbstractMethodInstructionHandler;
import com.twinkle.framework.core.datastruct.schema.MethodDef;
import org.objectweb.asm.MethodVisitor;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-21 17:03<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class HttpGetMethodInstructionHandler extends AbstractMethodInstructionHandler {
    @Override
    public MethodVisitor addInstructions(MethodVisitor _visitor, String _className, MethodDef _methodDef) {
        return _visitor;
    }
}

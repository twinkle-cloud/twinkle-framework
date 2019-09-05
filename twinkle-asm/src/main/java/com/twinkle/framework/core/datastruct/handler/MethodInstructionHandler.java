package com.twinkle.framework.core.datastruct.handler;

import com.twinkle.framework.core.datastruct.define.MethodDef;
import org.objectweb.asm.MethodVisitor;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-21 16:55<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface MethodInstructionHandler {
    /**
     * Add instructions for some method.
     *
     * @param _visitor
     * @param _className
     * @param _methodDef
     * @return
     */
    MethodVisitor addInstructions(MethodVisitor _visitor, String _className, MethodDef _methodDef);
}

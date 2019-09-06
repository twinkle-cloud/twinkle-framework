package com.twinkle.framework.asm.handler;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/24/19 9:33 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public abstract class AbstractMethodInstructionHandler implements MethodInstructionHandler {
    /**
     * Visit int value.
     *
     * @param _visitor
     * @param _value
     */
    protected void visitConstantValue(MethodVisitor _visitor, int _value) {
        switch (_value) {
            case 0:
                _visitor.visitInsn(Opcodes.ICONST_0);
                break;
            case 1:
                _visitor.visitInsn(Opcodes.ICONST_1);
                break;
            case 2:
                _visitor.visitInsn(Opcodes.ICONST_2);
                break;
            case 3:
                _visitor.visitInsn(Opcodes.ICONST_3);
                break;
            case 4:
                _visitor.visitInsn(Opcodes.ICONST_4);
                break;
            case 5:
                _visitor.visitInsn(Opcodes.ICONST_5);
                break;
            default:
                if (_value < 128) {
                    _visitor.visitIntInsn(Opcodes.BIPUSH, _value);
                } else if (_value < 32768) {
                    _visitor.visitIntInsn(Opcodes.SIPUSH, _value);
                } else {
                    throw new RuntimeException("The NC index " + _value + " exceeds the limitation.");
                }
        }
    }
}

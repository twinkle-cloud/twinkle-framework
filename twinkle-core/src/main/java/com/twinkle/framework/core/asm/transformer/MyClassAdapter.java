package com.twinkle.framework.core.asm.transformer;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-06-26 18:25<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class MyClassAdapter extends ClassNode {
    public MyClassAdapter(ClassVisitor cv) {
        super(Opcodes.ASM7);
        this.cv = cv;
    }

    @Override
    public void visitEnd() {
        // put your transformation code here
        accept(cv);
    }
}

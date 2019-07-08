package com.twinkle.framework.core.asm.transformer;

import com.twinkle.framework.core.asm.data.MethodDefine;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-03 15:29<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class AddZinitMethodTransformer extends AddMethodTransformer {
    public AddZinitMethodTransformer(ClassTransformer _transformer, MethodDefine _methodDefine) {
        super(_transformer, _methodDefine);
    }

    @Override
    public void packMethodNode() {
        InsnList il = this.methodNode.instructions;
        LabelNode labelA = new LabelNode();
        //Need add into the labelNodeList firstly.
        this.labelNodeList.add(labelA);
        il.add(labelA);

        il.add(new LdcInsnNode(
            Type.getObjectType(this.methodDefine.getClassDefine().getInternalName())
        ));

        il.add(new MethodInsnNode(
                Opcodes.INVOKESTATIC,
                Type.getInternalName(LoggerFactory.class),
                "getLogger",
                Type.getMethodDescriptor(Type.getType(Logger.class), Type.getType(Class.class))
        ));

        il.add(new FieldInsnNode(
                Opcodes.PUTSTATIC,
                this.methodDefine.getClassDefine().getInternalName(),
                "log",
                Type.getDescriptor(Logger.class)
        ));
        il.add(new InsnNode(Opcodes.RETURN));
    }
}

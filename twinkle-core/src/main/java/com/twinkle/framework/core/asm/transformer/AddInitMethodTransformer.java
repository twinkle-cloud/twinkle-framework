package com.twinkle.framework.core.asm.transformer;

import com.twinkle.framework.core.asm.data.MethodDefine;
import lombok.extern.slf4j.Slf4j;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-03 15:10<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class AddInitMethodTransformer extends AddMethodTransformer {
    public AddInitMethodTransformer(ClassTransformer _transformer, MethodDefine _methodDefine) {
        super(_transformer, _methodDefine);
    }
    @Override
    public void packMethodNode() {
        InsnList il = this.methodNode.instructions;
        LabelNode labelA = new LabelNode();
        //Need add into the labelNodeList firstly.
        this.labelNodeList.add(labelA);
        il.add(labelA);
        il.add(new VarInsnNode(Opcodes.ALOAD, 0));
        il.add(new MethodInsnNode(
                Opcodes.INVOKESPECIAL,
                Type.getInternalName(Object.class),
                "<init>",
                Type.getMethodDescriptor(Type.getType(Void.TYPE))
        ));

        il.add(new InsnNode(Opcodes.RETURN));
        LabelNode labelB = new LabelNode();
        //Need add into the labelNodeList firstly.
        this.labelNodeList.add(labelB);
        il.add(labelB);
    }
}

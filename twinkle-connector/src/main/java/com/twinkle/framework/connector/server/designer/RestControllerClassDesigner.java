package com.twinkle.framework.connector.server.designer;

import com.twinkle.framework.api.constant.ResultCode;
import com.twinkle.framework.api.data.GeneralResult;
import com.twinkle.framework.connector.demo.service.HelloWorldService;
import com.twinkle.framework.connector.demo.data.HelloRequest;
import com.twinkle.framework.core.asm.designer.AbstractGeneralClassDesigner;
import com.twinkle.framework.core.asm.designer.LocalAttributeIndexInfo;
import com.twinkle.framework.core.datastruct.builder.LogAttributeDefBuilder;
import com.twinkle.framework.core.datastruct.schema.AttributeDef;
import com.twinkle.framework.core.datastruct.schema.BeanTypeDef;
import com.twinkle.framework.core.datastruct.schema.GeneralClassTypeDef;
import com.twinkle.framework.core.datastruct.schema.MethodDef;
import lombok.extern.slf4j.Slf4j;
import org.objectweb.asm.*;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-15 15:28<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class RestControllerClassDesigner extends AbstractGeneralClassDesigner {
    public RestControllerClassDesigner(String _className, GeneralClassTypeDef _beanTypeDef) {
        super(_className, _beanTypeDef);
    }

    @Override
    protected void addClassDefinition(ClassVisitor _visitor, String _className, String _superName, List<AttributeDef> _attrDefList, BeanTypeDef _beanTypeDef) {
        List<AttributeDef> tempAttrList = _attrDefList;
        tempAttrList.add(LogAttributeDefBuilder.getAttributeDef());
        super.addClassDefinition(_visitor, _className, _superName, tempAttrList, _beanTypeDef);
        this.addMethodsDefinition(_visitor, _className, ((GeneralClassTypeDef)_beanTypeDef).getMethods());
    }

    public MethodVisitor addHelloWorldRuleChain(MethodVisitor _visitor, String _className, MethodDef _methodDef) {
        Label tempLabelA = new Label();
        _visitor.visitLabel(tempLabelA);
        _visitor.visitFieldInsn(Opcodes.GETSTATIC, _className, "log", Type.getDescriptor(Logger.class));
        _visitor.visitLdcInsn("The request body is: {}");
        _visitor.visitVarInsn(Opcodes.ALOAD, 1);
        _visitor.visitMethodInsn(Opcodes.INVOKEINTERFACE, Type.getInternalName(Logger.class), "info", Type.getMethodDescriptor(Type.getType(Void.TYPE), Type.getType(String.class), Type.getType(Object.class)));

        Label tempLabelB = new Label();
        _visitor.visitLabel(tempLabelB);
        _visitor.visitVarInsn(Opcodes.ALOAD, 0);
        _visitor.visitFieldInsn(Opcodes.GETFIELD, _className, "helloWorldService", Type.getDescriptor(HelloWorldService.class));

        _visitor.visitVarInsn(Opcodes.ALOAD, 1);
        _visitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, Type.getInternalName(HelloRequest.class), "getUserName", Type.getMethodDescriptor(Type.getType(String.class)));
        _visitor.visitMethodInsn(Opcodes.INVOKEINTERFACE, Type.getInternalName(HelloWorldService.class), "sayHello", Type.getMethodDescriptor(Type.getType(String.class), Type.getType(String.class)));
        _visitor.visitVarInsn(Opcodes.ASTORE, 3);

        Label tempLabelC = new Label();
        _visitor.visitLabel(tempLabelC);
        _visitor.visitTypeInsn(Opcodes.NEW, Type.getInternalName(GeneralResult.class));
        _visitor.visitInsn(Opcodes.DUP);
        _visitor.visitMethodInsn(Opcodes.INVOKESPECIAL,
                Type.getInternalName(GeneralResult.class),
                "<init>",
                Type.getMethodDescriptor(Type.getType(Void.TYPE)));
        _visitor.visitVarInsn(Opcodes.ASTORE, 4);

        Label tempLabelD = new Label();
        _visitor.visitLabel(tempLabelD);
        _visitor.visitVarInsn(Opcodes.ALOAD, 4);
        _visitor.visitLdcInsn(ResultCode.OPERATION_SUCCESS);
        _visitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                Type.getInternalName(GeneralResult.class),
                "setCode",
                Type.getMethodDescriptor(Type.getType(Void.TYPE), Type.getType(String.class)));

        Label tempLabelE = new Label();
        _visitor.visitLabel(tempLabelE);
        _visitor.visitVarInsn(Opcodes.ALOAD, 4);
        _visitor.visitVarInsn(Opcodes.ALOAD, 3);
        _visitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                Type.getInternalName(GeneralResult.class),
                "setData",
                Type.getMethodDescriptor(Type.getType(Void.TYPE), Type.getType(Object.class)));

        Label tempLabelF = new Label();
        _visitor.visitLabel(tempLabelF);
        _visitor.visitVarInsn(Opcodes.ALOAD, 4);
        _visitor.visitInsn(Opcodes.ARETURN);
        Label tempLabelG = new Label();
        _visitor.visitLabel(tempLabelG);

        this.addMethodAttributeIndex(_methodDef.getName(), "this", new LocalAttributeIndexInfo());

        return _visitor;
    }
}

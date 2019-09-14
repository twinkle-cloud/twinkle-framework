package com.twinkle.framework.struct.asm.designer;

import com.alibaba.fastjson.JSONWriter;
import com.twinkle.framework.asm.classloader.SerializerClassLoader;
import com.twinkle.framework.asm.descriptor.ArrayTypeDescriptor;
import com.twinkle.framework.asm.descriptor.AttributeDescriptor;
import com.twinkle.framework.asm.descriptor.BeanTypeDescriptor;
import com.twinkle.framework.asm.descriptor.TypeDescriptor;
import com.twinkle.framework.struct.serialize.AbstractSchemaBasedSerializer;
import com.twinkle.framework.struct.type.StructAttribute;
import com.twinkle.framework.core.lang.util.Array;
import com.twinkle.framework.asm.utils.ClassDesignerUtil;
import org.objectweb.asm.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/4/19 10:30 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class SerializerGeneratorClassDesigner extends AbstractGeneratorClassDesigner {
    public SerializerGeneratorClassDesigner(BeanTypeDescriptor _descriptor, SerializerClassLoader _classLoader) {
        super(_descriptor, _classLoader, AbstractSchemaBasedSerializer.class);
    }
    @Override
    protected String getSuffix() {
        return "Serializer";
    }
    @Override
    protected void generateInit(ClassVisitor _visitor) {
        MethodVisitor tempVisitor = _visitor.visitMethod(Opcodes.ACC_PROTECTED, "initSerializers", ClassDesignerUtil.getMethodSignature(Void.TYPE, new Class[0]), (String)null, (String[])null);
        tempVisitor.visitCode();
        Iterator<AttributeDescriptor> tempItr = this.typeDescriptor.getAttributes().iterator();

        while(tempItr.hasNext()) {
            AttributeDescriptor tempAttrDescriptor = tempItr.next();
            TypeDescriptor tempDescriptor = tempAttrDescriptor.getType();
            if (tempDescriptor.isBean()) {
                this.addTypeSerializer(tempVisitor, tempDescriptor);
            }
            if (tempDescriptor instanceof ArrayTypeDescriptor) {
                ArrayTypeDescriptor tempArrayDescriptor = (ArrayTypeDescriptor)tempDescriptor;
                this.addTypeSerializer(tempVisitor, tempArrayDescriptor.getElementType());
            }
        }

        tempVisitor.visitInsn(Opcodes.RETURN);
        tempVisitor.visitMaxs(AUTO_STACK_SIZE, AUTO_LOCAL_VARS);
        tempVisitor.visitEnd();
    }

    private void addTypeSerializer(MethodVisitor _visitor, TypeDescriptor _descriptor) {
        _visitor.visitVarInsn(Opcodes.ALOAD, 0);
        _visitor.visitFieldInsn(Opcodes.GETFIELD, this.targetInternalClassName, "serializers", Type.getDescriptor(Map.class));
        _visitor.visitLdcInsn(getQualifiedName(_descriptor));
        String tempInternalName = ClassDesignerUtil.getInternalName(this.getClassName(_descriptor));
        _visitor.visitTypeInsn(Opcodes.NEW, tempInternalName);
        _visitor.visitInsn(Opcodes.DUP);
        _visitor.visitMethodInsn(Opcodes.INVOKESPECIAL, tempInternalName, "<init>", ClassDesignerUtil.getMethodSignature(Void.TYPE, new Class[0]));
        _visitor.visitMethodInsn(Opcodes.INVOKEINTERFACE, ClassDesignerUtil.getInternalName(Map.class), "put", ClassDesignerUtil.getMethodSignature(Object.class, new Class[]{Object.class, Object.class}));
        _visitor.visitInsn(Opcodes.POP);
    }
    @Override
    protected void generateProcess(ClassVisitor _visitor) {
        MethodVisitor tempVisitor = _visitor.visitMethod(4, "writeFields", ClassDesignerUtil.getMethodSignature(Void.TYPE, new Class[]{JSONWriter.class, StructAttribute.class}), null, new String[]{Type.getInternalName(IOException.class)});
        tempVisitor.visitCode();
        String tempInternalClassName = this.typeDescriptor.getClassName().replace('.', '/');
        tempVisitor.visitVarInsn(Opcodes.ALOAD, 2);
        tempVisitor.visitTypeInsn(Opcodes.CHECKCAST, tempInternalClassName);
        tempVisitor.visitVarInsn(Opcodes.ASTORE, 3);
        Iterator<AttributeDescriptor> tempItr = this.typeDescriptor.getAttributes().iterator();

        while(tempItr.hasNext()) {
            AttributeDescriptor tempItemDescriptor = tempItr.next();
            this.transformAttribute(tempVisitor, tempInternalClassName, tempItemDescriptor);
        }

        tempVisitor.visitInsn(Opcodes.RETURN);
        tempVisitor.visitMaxs(AUTO_STACK_SIZE, AUTO_LOCAL_VARS);
        tempVisitor.visitEnd();
    }

    protected void transformAttribute(MethodVisitor _visitor, String _className, AttributeDescriptor _descriptor) {
        TypeDescriptor tempDescriptor = _descriptor.getType();
        if (tempDescriptor.isPrimitive()) {
            this.transformPrimitiveAttribute(_visitor, _className, _descriptor);
        } else {
            this.transformObjectAttribute(_visitor, _className, _descriptor);
        }

    }

    protected void transformObjectAttribute(MethodVisitor _visitor, String _className, AttributeDescriptor _descriptor) {
        try {
            String tempName = _descriptor.getName();
            Class tempClass = this.classLoader.loadClass(_className.replace('/', '.'));
            Method tempMethod = tempClass.getDeclaredMethod(ClassDesignerUtil.getGetterName(tempName));
            Label tempLabel = new Label();
            _visitor.visitVarInsn(Opcodes.ALOAD, 3);
            _visitor.visitMethodInsn(Opcodes.INVOKEINTERFACE, _className, ClassDesignerUtil.getFlagGetterName(tempName), ClassDesignerUtil.getMethodSignature(Boolean.TYPE, new Class[0]));
            _visitor.visitJumpInsn(Opcodes.IFEQ, tempLabel);
            _visitor.visitVarInsn(Opcodes.ALOAD, 0);
            _visitor.visitVarInsn(Opcodes.ALOAD, 1);
            _visitor.visitLdcInsn(tempName);
            _visitor.visitVarInsn(Opcodes.ALOAD, 3);
            _visitor.visitMethodInsn(Opcodes.INVOKEINTERFACE, _className, tempMethod.getName(), Type.getMethodDescriptor(tempMethod));
            _visitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, this.targetInternalClassName, "writeProperty", ClassDesignerUtil.getMethodSignature(Void.TYPE, new Class[]{JSONWriter.class, String.class, Object.class}));
            _visitor.visitLabel(tempLabel);
        } catch (Exception var8) {
            throw new RuntimeException(var8);
        }
    }

    protected void transformPrimitiveAttribute(MethodVisitor _visitor, String _className, AttributeDescriptor _descriptor) {
        TypeDescriptor tempDescriptor = _descriptor.getType();
        Class<?> tempClass = ClassDesignerUtil.classForName(tempDescriptor.getClassName());
        if (Array.class.isAssignableFrom(tempClass)) {
            this.transformObjectAttribute(_visitor, _className, _descriptor);
        } else {
            Label tempLabel = new Label();
            _visitor.visitVarInsn(Opcodes.ALOAD, 3);
            String var7 = _descriptor.getName();
            _visitor.visitMethodInsn(Opcodes.INVOKEINTERFACE, _className, ClassDesignerUtil.getFlagGetterName(var7), ClassDesignerUtil.getMethodSignature(Boolean.TYPE, new Class[0]));
            _visitor.visitJumpInsn(Opcodes.IFEQ, tempLabel);
            _visitor.visitVarInsn(Opcodes.ALOAD, 0);
            _visitor.visitVarInsn(Opcodes.ALOAD, 1);
            _visitor.visitLdcInsn(var7);
            _visitor.visitVarInsn(Opcodes.ALOAD, 3);
            _visitor.visitMethodInsn(Opcodes.INVOKEINTERFACE, _className, ClassDesignerUtil.getGetterName(var7), ClassDesignerUtil.getMethodSignature(tempClass, new Class[0]));
            _visitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, this.targetInternalClassName, "writeProperty", ClassDesignerUtil.getMethodSignature(Void.TYPE, new Class[]{JSONWriter.class, String.class, tempClass}));
            _visitor.visitLabel(tempLabel);
        }
    }
}

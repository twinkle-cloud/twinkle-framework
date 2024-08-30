package com.twinkle.framework.struct.asm.designer;

import com.alibaba.fastjson2.JSONReader;
import com.twinkle.framework.asm.classloader.SerializerClassLoader;
import com.twinkle.framework.asm.descriptor.ArrayTypeDescriptor;
import com.twinkle.framework.asm.descriptor.AttributeDescriptor;
import com.twinkle.framework.asm.descriptor.BeanTypeDescriptor;
import com.twinkle.framework.asm.descriptor.TypeDescriptor;
import com.twinkle.framework.struct.serialize.AbstractSchemaBasedDeSerializer;
import com.twinkle.framework.struct.serialize.AbstractSchemaBasedSerializer;
import com.twinkle.framework.struct.lang.StructAttribute;
import com.twinkle.framework.struct.util.StructAttributeArray;
import com.twinkle.framework.asm.utils.ClassDesignerUtil;
import org.objectweb.asm.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/4/19 10:50 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class DeserializerGeneratorClassDesigner extends AbstractGeneratorClassDesigner {
    public DeserializerGeneratorClassDesigner(BeanTypeDescriptor var1, SerializerClassLoader var2) {
        super(var1, var2, AbstractSchemaBasedSerializer.class);
    }
    @Override
    protected String getSuffix() {
        return "Deserializer";
    }
    @Override
    protected void generateInit(ClassVisitor _visitor) {
        MethodVisitor tempVisitor = _visitor.visitMethod(4, "initDeserializers", ClassDesignerUtil.getMethodSignature(Void.TYPE, new Class[0]), (String)null, (String[])null);
        tempVisitor.visitCode();
        Iterator<AttributeDescriptor> tempItr = this.typeDescriptor.getAttributes().iterator();

        while(tempItr.hasNext()) {
            AttributeDescriptor tempAttrDescriptor = tempItr.next();
            TypeDescriptor tempTypeDescriptor = tempAttrDescriptor.getType();
            if (tempTypeDescriptor.isBean()) {
                this.addTypeDeserializer(tempVisitor, tempTypeDescriptor);
            }

            if (tempTypeDescriptor instanceof ArrayTypeDescriptor) {
                ArrayTypeDescriptor tempArrayDescriptor = (ArrayTypeDescriptor)tempTypeDescriptor;
                this.addTypeDeserializer(tempVisitor, tempArrayDescriptor.getElementType());
            }
        }

        tempVisitor.visitInsn(Opcodes.RETURN);
        tempVisitor.visitMaxs(AUTO_STACK_SIZE, AUTO_LOCAL_VARS);
        tempVisitor.visitEnd();
    }

    private void addTypeDeserializer(MethodVisitor _visitor, TypeDescriptor _descriptor) {
        _visitor.visitVarInsn(Opcodes.ALOAD, 0);
        _visitor.visitFieldInsn(Opcodes.GETFIELD, this.targetInternalClassName, "deserializers", Type.getDescriptor(Map.class));
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
        this.generateOnProperty(_visitor);
    }

    protected void generateOnProperty(ClassVisitor _visitor) {
        Class<?> tempClass;
        try {
            tempClass = this.classLoader.loadClass(this.typeDescriptor.getClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        MethodVisitor tempVisitor = _visitor.visitMethod(4, "onProperty", ClassDesignerUtil.getMethodSignature(Void.TYPE, new Class[]{String.class, JSONReader.class, StructAttribute.class}), null, new String[]{Type.getInternalName(IOException.class)});
        tempVisitor.visitCode();
        tempVisitor.visitVarInsn(Opcodes.ALOAD, 3);
        tempVisitor.visitTypeInsn(Opcodes.CHECKCAST, ClassDesignerUtil.getInternalName(this.typeDescriptor.getClassName()));
        tempVisitor.visitVarInsn(Opcodes.ASTORE, 4);
        Label tempLabelA = new Label();
        Iterator<AttributeDescriptor> tempAttrItr = this.typeDescriptor.getAttributes().iterator();

        while(tempAttrItr.hasNext()) {
            AttributeDescriptor tempDescriptor = tempAttrItr.next();
            Label tempLabelB = new Label();
            TypeDescriptor tempTypeDescriptor = tempDescriptor.getType();
            String tempAttrName = tempDescriptor.getName();
            tempVisitor.visitLdcInsn(tempAttrName);
            tempVisitor.visitVarInsn(Opcodes.ALOAD, 1);
            tempVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, ClassDesignerUtil.getInternalName(String.class), "equals", ClassDesignerUtil.getMethodSignature(Boolean.TYPE, new Class[]{Object.class}));
            tempVisitor.visitJumpInsn(Opcodes.IFEQ, tempLabelB);
            if (tempTypeDescriptor.isBean()) {
                this.generateOnBeanProperty(tempVisitor, tempDescriptor);
            } else if (tempTypeDescriptor instanceof ArrayTypeDescriptor) {
                this.generateOnArrayProperty(tempVisitor, tempDescriptor, tempClass);
            } else {
                this.generateOnPrimitiveProperty(tempVisitor, tempDescriptor, tempClass);
            }
            tempVisitor.visitJumpInsn(Opcodes.GOTO, tempLabelA);
            tempVisitor.visitLabel(tempLabelB);
        }

        tempVisitor.visitVarInsn(Opcodes.ALOAD, 0);
        tempVisitor.visitVarInsn(Opcodes.ALOAD, 1);
        tempVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, ClassDesignerUtil.getInternalName(AbstractSchemaBasedDeSerializer.class), "unknownProperty", ClassDesignerUtil.getMethodSignature(Void.TYPE, new Class[]{String.class}));
        tempVisitor.visitLabel(tempLabelA);
        tempVisitor.visitInsn(Opcodes.RETURN);
        tempVisitor.visitMaxs(AUTO_STACK_SIZE, AUTO_LOCAL_VARS);
        tempVisitor.visitEnd();
    }

    protected void generateOnBeanProperty(MethodVisitor _visitor, AttributeDescriptor _descriptor) {
        BeanTypeDescriptor tempDescriptor = (BeanTypeDescriptor)_descriptor.getType();
        Class tempBeanClass = this.getBeanClass(tempDescriptor);
        _visitor.visitVarInsn(Opcodes.ALOAD, 4);
        _visitor.visitVarInsn(Opcodes.ALOAD, 0);
        _visitor.visitVarInsn(Opcodes.ALOAD, 2);
        _visitor.visitLdcInsn(getQualifiedName(tempDescriptor));
        _visitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, ClassDesignerUtil.getInternalName(AbstractSchemaBasedDeSerializer.class), "readStructAttribute", ClassDesignerUtil.getMethodSignature(StructAttribute.class, new Class[]{JSONReader.class, String.class}));
        _visitor.visitTypeInsn(Opcodes.CHECKCAST, ClassDesignerUtil.getInternalName(tempBeanClass));
        _visitor.visitMethodInsn(Opcodes.INVOKEINTERFACE, ClassDesignerUtil.getInternalName(this.typeDescriptor.getClassName()), ClassDesignerUtil.getSetterName(_descriptor.getName()), ClassDesignerUtil.getMethodSignature(Void.TYPE, new Class[]{tempBeanClass}));
    }

    protected void generateOnArrayProperty(MethodVisitor _visitor, AttributeDescriptor _attrDescriptor, Class<?> _class) {
        ArrayTypeDescriptor tempTypeDescriptor = (ArrayTypeDescriptor)_attrDescriptor.getType();
        TypeDescriptor tempElementTypeDescriptor = tempTypeDescriptor.getElementType();
        if (tempElementTypeDescriptor.isPrimitive()) {
            throw new RuntimeException("Not implemented");
        } else {
            _visitor.visitVarInsn(Opcodes.ALOAD, 4);
            _visitor.visitVarInsn(Opcodes.ALOAD, 0);
            _visitor.visitVarInsn(Opcodes.ALOAD, 2);
            _visitor.visitLdcInsn(getQualifiedName(tempElementTypeDescriptor));
            _visitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, ClassDesignerUtil.getInternalName(AbstractSchemaBasedDeSerializer.class), "readStructAttributeArray", ClassDesignerUtil.getMethodSignature(StructAttributeArray.class, new Class[]{JSONReader.class, String.class}));
            _visitor.visitMethodInsn(Opcodes.INVOKEINTERFACE, ClassDesignerUtil.getInternalName(_class), ClassDesignerUtil.getSetterName(_attrDescriptor.getName()), ClassDesignerUtil.getMethodSignature(Void.TYPE, new Class[]{StructAttributeArray.class}));
        }
    }

    protected void generateOnPrimitiveProperty(MethodVisitor _visitor, AttributeDescriptor _attrDescriptor, Class<?> _class) {
        try {
            String tempAttrName = _attrDescriptor.getName();
            String tempSetterName = ClassDesignerUtil.getSetterName(tempAttrName);
            String tempGetterName = ClassDesignerUtil.getGetterName(tempAttrName);
            Method tempGetterMethod = _class.getDeclaredMethod(tempGetterName);
            Class<?> tempReturnTypeClass = tempGetterMethod.getReturnType();
            String tempReturnTypeClassName = tempReturnTypeClass.getCanonicalName();
            Method tempSetterMethod = _class.getDeclaredMethod(tempSetterName, tempReturnTypeClass);
            String tempPrefix = "read";
            if (tempReturnTypeClass.isPrimitive()) {
                tempPrefix = tempPrefix + Character.toUpperCase(tempReturnTypeClassName.charAt(0)) + tempReturnTypeClassName.substring(1);
            } else {
                tempPrefix = tempPrefix + tempReturnTypeClass.getSimpleName();
            }

            _visitor.visitVarInsn(Opcodes.ALOAD, 4);
            _visitor.visitVarInsn(Opcodes.ALOAD, 0);
            _visitor.visitVarInsn(Opcodes.ALOAD, 2);
            _visitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, ClassDesignerUtil.getInternalName(AbstractSchemaBasedDeSerializer.class), tempPrefix, ClassDesignerUtil.getMethodSignature(tempReturnTypeClass, new Class[]{JSONReader.class}));
            _visitor.visitMethodInsn(Opcodes.INVOKEINTERFACE, ClassDesignerUtil.getInternalName(this.typeDescriptor.getClassName()), tempSetterName, Type.getMethodDescriptor(tempSetterMethod));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}

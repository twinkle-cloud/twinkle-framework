package com.twinkle.framework.asm.designer;

import com.twinkle.framework.asm.define.AnnotationDef;
import com.twinkle.framework.asm.define.AttributeDef;
import com.twinkle.framework.asm.define.BeanTypeDef;
import com.twinkle.framework.asm.define.StaticAttributeValueDef;
import com.twinkle.framework.asm.utils.TypeUtil;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.Modifier;
import java.util.List;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-14 23:03<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public abstract class AbstractGeneralBeanClassDesigner extends AbstractBeanClassDesigner {
    public AbstractGeneralBeanClassDesigner(String _className, BeanTypeDef _beanTypeDef) {
        super(_className, _beanTypeDef);
    }

    /**
     * Add fields to this class.
     *
     * @param _visitor
     * @param _attrDefList
     */
    protected void addFields(ClassVisitor _visitor, List<AttributeDef> _attrDefList) {
        _attrDefList.stream().filter(item -> !Modifier.isStatic(item.getAccess())).forEach(item -> this.addField(_visitor, item));
    }

    /**
     * Add methods to this class.
     *
     * @param _visitor
     * @param _className
     * @param _attrDefList
     */
    protected void addGetterSetterMethodsDefinition(ClassVisitor _visitor, String _className, List<AttributeDef> _attrDefList) {
        for (AttributeDef tempDef : _attrDefList) {
            this.addGetterDefinition(_visitor, _className, tempDef);
            this.addSetterDefinition(_visitor, _className, tempDef);
            if (tempDef.getDefaultValue() != null) {
                this.addDefaultGetterDefinition(_visitor, _className, tempDef);
            }
        }
    }

    /**
     * Add some Field of this classes.
     *
     * @param _visitor
     * @param _attrDef
     * @return
     */
    protected FieldVisitor addField(ClassVisitor _visitor, AttributeDef _attrDef) {
        String tempFieldSignature;
        if (_attrDef.getType().isGeneric()) {
            tempFieldSignature = TypeUtil.getGenericFieldSignature(_attrDef.getType());
        } else {
            tempFieldSignature = null;
        }
        FieldVisitor tempVisitor = _visitor.visitField(_attrDef.getAccess(), _attrDef.getFieldName(), TypeUtil.getFieldDescriptor(_attrDef.getType().getType()), tempFieldSignature, null);
        _attrDef.getAnnotations().stream().forEach(item -> this.addFieldAnnotation(tempVisitor, item));

        tempVisitor.visitEnd();
        return tempVisitor;
    }

    /**
     * Add default constructor definition for this class.
     *
     * @param _visitor
     * @param _className
     * @param _superName
     * @param _attrDefList
     * @return
     */
    protected MethodVisitor addDefaultConstructorDefinition(ClassVisitor _visitor, String _className, String _superName, List<AttributeDef> _attrDefList) {
        String tempMethodSignature = TypeUtil.getMethodDescriptor(new Class[0], Void.TYPE);
        MethodVisitor tempVisitor = _visitor.visitMethod(Opcodes.ACC_PUBLIC, "<init>", tempMethodSignature, null, null);
        tempVisitor.visitCode();
        tempVisitor.visitVarInsn(Opcodes.ALOAD, 0);
        tempVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, _superName, "<init>", tempMethodSignature);
        _attrDefList.stream().forEach(item -> this.handleAttributeInit(tempVisitor, _className, item));

        tempVisitor.visitInsn(Opcodes.RETURN);
        tempVisitor.visitMaxs(AUTO_STACK_SIZE, AUTO_LOCAL_VARS);
        tempVisitor.visitEnd();
        return tempVisitor;
    }

    /**
     * Init the parameters
     *
     * @param _visitor
     * @param _className
     * @param _attrDef
     */
    protected void handleAttributeInit(MethodVisitor _visitor, String _className, AttributeDef _attrDef) {
        Object tempValueObj = _attrDef.getDefaultValue();
        if (tempValueObj != null && !(tempValueObj instanceof StaticAttributeValueDef)) {
            _visitor.visitVarInsn(Opcodes.ALOAD, 0);
            _visitor.visitFieldInsn(Opcodes.GETSTATIC, _className, this.getDefaultConstantName(_attrDef), TypeUtil.getFieldDescriptor(_attrDef.getType().getType()));
            if (this.isMutableDefaultInterface(_attrDef)) {
                _visitor.visitMethodInsn(Opcodes.INVOKEINTERFACE, _attrDef.getType().getType().getInternalName(), "clone", TypeUtil.getMethodDescriptor(new Class[0], Object.class));
                _visitor.visitTypeInsn(Opcodes.CHECKCAST, _attrDef.getType().getType().getInternalName());
            } else if (this.isMutableDefault(_attrDef)) {
                _visitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, _attrDef.getType().getType().getInternalName(), "clone", TypeUtil.getMethodDescriptor(new Class[0], Object.class));
                _visitor.visitTypeInsn(Opcodes.CHECKCAST, _attrDef.getType().getType().getInternalName());
            }

            _visitor.visitFieldInsn(Opcodes.PUTFIELD, _className, _attrDef.getFieldName(), TypeUtil.getFieldDescriptor(_attrDef.getType().getType()));
        }
    }

    /**
     * Add get method to this class.
     *
     * @param _visitor
     * @param _className
     * @param _attrDef
     * @return
     */
    protected MethodVisitor addGetterDefinition(ClassVisitor _visitor, String _className, AttributeDef _attrDef) {
        String tempSignature;
        if (_attrDef.getType().isGeneric()) {
            tempSignature = TypeUtil.getGenericGetterSignature(_attrDef.getType());
        } else {
            tempSignature = null;
        }
        MethodVisitor tempVisitor = _visitor.visitMethod(Opcodes.ACC_PUBLIC, TypeUtil.getGetterName(_attrDef), TypeUtil.getGetterSignature(_attrDef.getType().getType()), tempSignature, null);
        _attrDef.getAnnotations().stream().forEach(item -> this.addMethodAnnotation(tempVisitor, item, AnnotationDef.Kind.GETTER));

        tempVisitor.visitCode();
        tempVisitor.visitVarInsn(Opcodes.ALOAD, 0);
        tempVisitor.visitFieldInsn(Opcodes.GETFIELD, _className, _attrDef.getFieldName(), TypeUtil.getFieldDescriptor(_attrDef.getType().getType()));
        tempVisitor.visitInsn(TypeUtil.getOpcode(_attrDef.getType().getType(), Opcodes.IRETURN));
        tempVisitor.visitMaxs(AUTO_STACK_SIZE, AUTO_LOCAL_VARS);
        tempVisitor.visitEnd();
        return tempVisitor;
    }

    /**
     * Add method to this class.
     *
     * @param _visitor
     * @param _className
     * @param _attrDef
     * @return
     */
    protected MethodVisitor addSetterDefinition(ClassVisitor _visitor, String _className, AttributeDef _attrDef) {
        String tempSignature;
        if (_attrDef.getType().isGeneric()) {
            tempSignature = TypeUtil.getGenericSetterSignature(_attrDef.getType());
        } else {
            tempSignature = null;
        }

        MethodVisitor tempVisitor = _visitor.visitMethod(Opcodes.ACC_PUBLIC, TypeUtil.getSetterName(_attrDef), TypeUtil.getSetterSignature(_attrDef.getType().getType()), tempSignature, null);
        _attrDef.getAnnotations().stream().forEach(item -> this.addMethodAnnotation(tempVisitor, item, AnnotationDef.Kind.SETTER));

        tempVisitor.visitCode();
        if (_attrDef.isReadOnly()) {
            tempVisitor.visitTypeInsn(Opcodes.NEW, "java/lang/UnsupportedOperationException");
            tempVisitor.visitInsn(Opcodes.DUP);
            tempVisitor.visitLdcInsn(TypeUtil.getSetterName(_attrDef) + ": readonly attribute [" + _attrDef.getName() + "]");
            tempVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/UnsupportedOperationException", "<init>", "(Ljava/lang/String;)V");
            tempVisitor.visitInsn(Opcodes.ATHROW);
        } else {
            tempVisitor.visitVarInsn(Opcodes.ALOAD, 0);
            tempVisitor.visitVarInsn(TypeUtil.getOpcode(_attrDef.getType().getType(), Opcodes.ILOAD), 1);
            tempVisitor.visitFieldInsn(Opcodes.PUTFIELD, _className, _attrDef.getFieldName(), TypeUtil.getFieldDescriptor(_attrDef.getType().getType()));
            tempVisitor.visitInsn(Opcodes.RETURN);
        }

        tempVisitor.visitMaxs(AUTO_STACK_SIZE, AUTO_LOCAL_VARS);
        tempVisitor.visitEnd();
        return tempVisitor;
    }

    /**
     * Add default geeter definition.
     *
     * @param _visitor
     * @param _className
     * @param _attrDef
     * @return
     */
    protected MethodVisitor addDefaultGetterDefinition(ClassVisitor _visitor, String _className, AttributeDef _attrDef) {
        String tempSignature;
        if (_attrDef.getType().isGeneric()) {
            tempSignature = TypeUtil.getGenericGetterSignature(_attrDef.getType());
        } else {
            tempSignature = null;
        }

        MethodVisitor tempVisitor = _visitor.visitMethod(Opcodes.ACC_PUBLIC, TypeUtil.getDefaultGetterName(_attrDef), TypeUtil.getGetterSignature(_attrDef.getType().getType()), tempSignature, null);
        tempVisitor.visitCode();
        Object tempDefaultValueObj = _attrDef.getDefaultValue();
        tempVisitor.visitVarInsn(Opcodes.ALOAD, 0);
        if (tempDefaultValueObj != null) {
            tempVisitor.visitFieldInsn(Opcodes.GETSTATIC, _className, this.getDefaultConstantName(_attrDef), TypeUtil.getFieldDescriptor(_attrDef.getType().getType()));
        } else {
            tempVisitor.visitInsn(TypeUtil.getNullOpcode(_attrDef.getType().getType()));
        }
        tempVisitor.visitInsn(TypeUtil.getOpcode(_attrDef.getType().getType(), Opcodes.IRETURN));
        tempVisitor.visitMaxs(AUTO_STACK_SIZE, AUTO_LOCAL_VARS);
        tempVisitor.visitEnd();
        return tempVisitor;
    }
}

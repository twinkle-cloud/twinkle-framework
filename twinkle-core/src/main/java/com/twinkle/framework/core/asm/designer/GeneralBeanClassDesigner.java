package com.twinkle.framework.core.asm.designer;

import com.twinkle.framework.core.asm.helper.CloneHelper;
import com.twinkle.framework.core.asm.helper.EqualsHelper;
import com.twinkle.framework.core.datastruct.Bean;
import com.twinkle.framework.core.datastruct.define.*;
import com.twinkle.framework.core.utils.TypeUtil;
import lombok.Getter;
import org.objectweb.asm.*;

import java.util.*;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-04 12:55<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class GeneralBeanClassDesigner extends AbstractGeneralBeanClassDesigner {
    public static final String IMPL_SUFFIX = "Impl";
    @Getter
    private String beanInterfaceName = this.getClassName();
    protected static final Collection<Type> CLONE_EXCLUSIONS = new HashSet();

    protected static final String STRING_BUILDER_CLASS_NAME;
    protected static final String STRING_BUILDER_APPEND_STRING;
    protected static final String STRING_BUILDER_APPEND_METHOD = "append";
    protected static final String ARRAYS_CLASS_NAME;
    protected static final String ARRAYS_TOSTRING_METHOD = "toString";

    static {
        CLONE_EXCLUSIONS.add(TypeUtil.STRING_TYPE);
        STRING_BUILDER_CLASS_NAME = TypeUtil.STRING_BUILDER_TYPE.getInternalName();
        STRING_BUILDER_APPEND_STRING = TypeUtil.getStringBuilderAppendSignature(TypeUtil.STRING_TYPE);
        ARRAYS_CLASS_NAME = TypeUtil.ARRAYS_TYPE.getInternalName();
    }

    /**
     * General Bean Class Designer.
     *
     * @param _className
     * @param _beanTypeDef
     */
    public GeneralBeanClassDesigner(String _className, BeanTypeDef _beanTypeDef) {
        super(_className, _beanTypeDef);
        if (this.beanInterfaceName.endsWith(IMPL_SUFFIX)) {
            String tempBeanInterface = this.beanInterfaceName.substring(0, _className.length() - IMPL_SUFFIX.length());
            if (this.getBeanInterfaces().contains(tempBeanInterface)) {
                this.beanInterfaceName = tempBeanInterface;
            }
        }
    }

    @Override
    protected String[] getDefaultInterfaces() {
        return new String[]{Bean.class.getName(), Cloneable.class.getName()};
    }

    @Override
    protected int initAccessFlags() {
        return this.getClassAccessFlags();
    }

    @Override
    protected ClassVisitor addClassDeclaration(ClassVisitor _visitor, String _className, String _superName, List<String> _interfaceList, BeanTypeDef _beanTypeDef) {
        super.addClassDeclaration(_visitor, _className, _superName, _interfaceList, _beanTypeDef);
        _visitor.visitInnerClass(BeanImplBuilderDesigner.getImplBuilderInnerClassName(_className), _className, "ImplBuilder", Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC);
        _beanTypeDef.getAnnotations().stream().forEach(item -> this.addClassAnnotation(_visitor, item));

        return _visitor;
    }

    @Override
    protected void addClassDefinition(ClassVisitor _visitor, String _className, String _superName, List<AttributeDef> _attrDefList, BeanTypeDef _beanTypeDef) {
        this.addDefaultConstants(_visitor, _className, _attrDefList);
        this.addFields(_visitor, _attrDefList);
        this.addDefaultConstructorDefinition(_visitor, _className, _superName, _attrDefList);
        this.addGetterSetterMethodsDefinition(_visitor, _className, _attrDefList);
        this.addToStringDefinition(_visitor, _className, _attrDefList);
        this.addCloneDefinition(_visitor, _className, _attrDefList);
        this.addEqualsDefinition(_visitor, _className, _attrDefList);
    }

    /**
     * public xxx super Object.
     *
     * @return
     */
    protected int getClassAccessFlags() {
        return Opcodes.ACC_PUBLIC + Opcodes.ACC_SUPER;
    }

    /**
     * Add toString() method to this class.
     *
     * @param _visitor
     * @param _className
     * @param _attrDefList
     * @return
     */
    protected MethodVisitor addToStringDefinition(ClassVisitor _visitor, String _className, List<AttributeDef> _attrDefList) {
        String tempSignature = TypeUtil.getMethodDescriptor(new Class[0], String.class);
        MethodVisitor tempVisitor = _visitor.visitMethod(Opcodes.ACC_PUBLIC, ARRAYS_TOSTRING_METHOD, tempSignature, null, null);
        tempVisitor.visitCode();
        tempVisitor.visitTypeInsn(Opcodes.NEW, STRING_BUILDER_CLASS_NAME);
        tempVisitor.visitInsn(Opcodes.DUP);
        tempVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, STRING_BUILDER_CLASS_NAME, "<init>", TypeUtil.getMethodDescriptor(new Class[0], Void.TYPE));
        tempVisitor.visitVarInsn(Opcodes.ASTORE, 1);
        this.addAppendStringStatement(tempVisitor, "{");

        for(int i = 0; i< _attrDefList.size(); i++) {
            if (i > 0) {
                this.addAppendStringStatement(tempVisitor, ",");
            }
            this.addAppendStatement(tempVisitor, _className, _attrDefList.get(i));
        }

        this.addAdditionalAttributesToString(tempVisitor, _className);
        this.addAppendStringStatement(tempVisitor, "}");
        tempVisitor.visitVarInsn(Opcodes.ALOAD, 1);
        tempVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, STRING_BUILDER_CLASS_NAME, ARRAYS_TOSTRING_METHOD, tempSignature);
        tempVisitor.visitInsn(Opcodes.ARETURN);
        tempVisitor.visitMaxs(AUTO_STACK_SIZE, AUTO_LOCAL_VARS);
        tempVisitor.visitEnd();
        return tempVisitor;
    }

    protected void addAdditionalAttributesToString(MethodVisitor _visitor, String _className) {
    }

    /**
     * Add public clone() to this class.
     *
     * @param _visitor
     * @param _className
     * @param _attrDefList
     * @return
     */
    protected MethodVisitor addCloneDefinition(ClassVisitor _visitor, String _className, List<AttributeDef> _attrDefList) {
        String tempMethodSignature = TypeUtil.getMethodDescriptor(new Class[0], Object.class);
        MethodVisitor tempVisitor = _visitor.visitMethod(Opcodes.ACC_PUBLIC, "clone", tempMethodSignature, null, new String[]{TypeUtil.CLONE_NOT_SUPPORTED_EX_TYPE.getInternalName()});
        tempVisitor.visitCode();
        tempVisitor.visitVarInsn(Opcodes.ALOAD, 0);
        tempVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, TypeUtil.OBJECT_TYPE.getInternalName(), "clone", tempMethodSignature);
        tempVisitor.visitTypeInsn(Opcodes.CHECKCAST, _className);
        tempVisitor.visitVarInsn(Opcodes.ASTORE, 1);
        _attrDefList.stream().forEach(item -> this.handleAttributeClone(tempVisitor, _className, item));

        tempVisitor.visitVarInsn(Opcodes.ALOAD, 1);
        tempVisitor.visitInsn(Opcodes.ARETURN);
        tempVisitor.visitMaxs(AUTO_STACK_SIZE, AUTO_LOCAL_VARS);
        tempVisitor.visitEnd();
        return tempVisitor;
    }

    /**
     * Add attribute clone process seg to the Clone method.
     *
     * @param _visitor
     * @param _className
     * @param _attrDef
     */
    protected void handleAttributeClone(MethodVisitor _visitor, String _className, AttributeDef _attrDef) {
        if (!_attrDef.isReadOnly()) {
            TypeDef tempAttrDefType = _attrDef.getType();
            if (!tempAttrDefType.isPrimitive() && !tempAttrDefType.isEnum() && !CLONE_EXCLUSIONS.contains(tempAttrDefType.getType())) {
                Label tempLabel = new Label();
                _visitor.visitVarInsn(Opcodes.ALOAD, 0);
                _visitor.visitFieldInsn(Opcodes.GETFIELD, _className, _attrDef.getFieldName(), TypeUtil.getFieldDescriptor(tempAttrDefType.getType()));
                _visitor.visitJumpInsn(Opcodes.IFNULL, tempLabel);
                if (tempAttrDefType.isBean()) {
                    this.handleCloneableClone(_visitor, _className, _attrDef);
                } else if (tempAttrDefType.isArray()) {
                    if (((ArrayTypeDef) tempAttrDefType).getElementType().isBean()) {
                        this.handleCloneableClone(_visitor, _className, _attrDef);
                    } else {
                        this.handleCloneableClone(_visitor, _className, _attrDef);
                    }
                } else if (Cloneable.class.isAssignableFrom(((ClassTypeDef) tempAttrDefType).getTypeClass())) {
                    this.handleCloneableClone(_visitor, _className, _attrDef);
                } else {
                    this.handleNonCloneableClone(_visitor, _className, _attrDef);
                }

                _visitor.visitLabel(tempLabel);
            } else {
                this.handlePrimitiveClone(_visitor, _className, _attrDef);
            }
        }
    }

    /**
     * Add primitive attribute clone process seg to the Clone method.
     *
     * @param _visitor
     * @param _className
     * @param _attrDef
     */
    protected void handlePrimitiveClone(MethodVisitor _visitor, String _className, AttributeDef _attrDef) {
    }

    /**
     * Add clonable attribute clone process seg to the Clone method.
     *
     * @param _visitor
     * @param _className
     * @param _attrDef
     */
    protected void handleCloneableClone(MethodVisitor _visitor, String _className, AttributeDef _attrDef) {
        TypeDef tempTypeDef = _attrDef.getType();
        Type tempAttrType = tempTypeDef.getType();
        String tempMethodSignature = TypeUtil.getMethodDescriptor(new Class[0], Object.class);
        _visitor.visitVarInsn(Opcodes.ALOAD, 1);
        _visitor.visitVarInsn(Opcodes.ALOAD, 0);
        _visitor.visitFieldInsn(Opcodes.GETFIELD, _className, _attrDef.getFieldName(), TypeUtil.getFieldDescriptor(tempAttrType));
        if (tempTypeDef.isArray()) {
            _visitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, tempAttrType.getInternalName(), "clone", tempMethodSignature);
        } else if (tempTypeDef.isBean()) {
            _visitor.visitMethodInsn(Opcodes.INVOKEINTERFACE, tempAttrType.getInternalName(), "clone", tempMethodSignature);
        } else if (((ClassTypeDef) tempTypeDef).getTypeClass().isInterface()) {
            _visitor.visitMethodInsn(Opcodes.INVOKEINTERFACE, tempAttrType.getInternalName(), "clone", tempMethodSignature);
        } else {
            _visitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, tempAttrType.getInternalName(), "clone", tempMethodSignature);
        }

        _visitor.visitTypeInsn(Opcodes.CHECKCAST, tempAttrType.getInternalName());
        _visitor.visitFieldInsn(Opcodes.PUTFIELD, _className, _attrDef.getFieldName(), TypeUtil.getFieldDescriptor(tempAttrType));
    }

    /**
     * Add non-cloneable attribute clone process seg to the Clone method.
     *
     * @param var1
     * @param var2
     * @param var3
     */
    protected void handleNonCloneableClone(MethodVisitor var1, String var2, AttributeDef var3) {
        TypeDef var4 = var3.getType();
        var1.visitVarInsn(Opcodes.ALOAD, 1);
        var1.visitVarInsn(Opcodes.ALOAD, 0);
        var1.visitFieldInsn(Opcodes.GETFIELD, var2, var3.getFieldName(), TypeUtil.getFieldDescriptor(var4.getType()));
        var1.visitLdcInsn(var3.getName());
        var1.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getType(CloneHelper.class).getInternalName(), "dynamicClone", TypeUtil.getMethodDescriptor(new Class[]{Object.class, String.class}, Object.class));
        var1.visitTypeInsn(Opcodes.CHECKCAST, var4.getType().getInternalName());
        var1.visitFieldInsn(Opcodes.PUTFIELD, var2, var3.getFieldName(), TypeUtil.getFieldDescriptor(var4.getType()));
    }

    /**
     * Add public boolean equals() method to this class.
     *
     * @param _visitor
     * @param _className
     * @param _attrDefList
     * @return
     */
    protected MethodVisitor addEqualsDefinition(ClassVisitor _visitor, String _className, List<AttributeDef> _attrDefList) {
        String tempMethodSignature = TypeUtil.getMethodDescriptor(new Class[]{Object.class}, Boolean.TYPE);
        MethodVisitor tempVisitor = _visitor.visitMethod(Opcodes.ACC_PUBLIC, "equals", tempMethodSignature, null, null);
        tempVisitor.visitCode();
        tempVisitor.visitVarInsn(Opcodes.ALOAD, 0);
        tempVisitor.visitVarInsn(Opcodes.ALOAD, 1);
        Label tempLabel1 = new Label();
        tempVisitor.visitJumpInsn(Opcodes.IF_ACMPNE, tempLabel1);
        tempVisitor.visitInsn(Opcodes.ICONST_1);
        tempVisitor.visitInsn(Opcodes.IRETURN);
        tempVisitor.visitLabel(tempLabel1);
        tempVisitor.visitVarInsn(Opcodes.ALOAD, 1);
        tempVisitor.visitTypeInsn(Opcodes.INSTANCEOF, _className);
        Label tempLabel2 = new Label();
        tempVisitor.visitJumpInsn(Opcodes.IFNE, tempLabel2);
        tempVisitor.visitInsn(Opcodes.ICONST_0);
        tempVisitor.visitInsn(Opcodes.IRETURN);
        tempVisitor.visitLabel(tempLabel2);
        tempVisitor.visitVarInsn(Opcodes.ALOAD, 1);
        tempVisitor.visitTypeInsn(Opcodes.CHECKCAST, _className);
        tempVisitor.visitVarInsn(Opcodes.ASTORE, 2);
        _attrDefList.stream().forEach(item -> this.handleAttributeEquals(tempVisitor, _className, item));

        tempVisitor.visitInsn(Opcodes.ICONST_1);
        tempVisitor.visitInsn(Opcodes.IRETURN);
        tempVisitor.visitMaxs(AUTO_STACK_SIZE, AUTO_LOCAL_VARS);
        tempVisitor.visitEnd();
        return tempVisitor;
    }

    /**
     * Add attribute's equals seg to the equals() methods.
     *
     * @param _visitor
     * @param _className
     * @param _attrDef
     */
    protected void handleAttributeEquals(MethodVisitor _visitor, String _className, AttributeDef _attrDef) {
        TypeDef tempAttrType = _attrDef.getType();
        if (tempAttrType.isPrimitive()) {
            this.handlePrimitiveEquals(_visitor, _className, _attrDef);
        } else if (tempAttrType.isArray()) {
            this.handleArrayEquals(_visitor, _className, _attrDef);
        } else {
            this.handleObjectEquals(_visitor, _className, _attrDef);
        }
    }

    /**
     * Add primitive attribute's equals seg to the equals() methods.
     *
     * @param _visitor
     * @param _className
     * @param _attrDef
     */
    protected void handlePrimitiveEquals(MethodVisitor _visitor, String _className, AttributeDef _attrDef) {
        Type tempAttrType = _attrDef.getType().getType();
        _visitor.visitVarInsn(Opcodes.ALOAD, 0);
        _visitor.visitFieldInsn(Opcodes.GETFIELD, _className, _attrDef.getFieldName(), TypeUtil.getFieldDescriptor(tempAttrType));
        _visitor.visitVarInsn(Opcodes.ALOAD, 2);
        _visitor.visitFieldInsn(Opcodes.GETFIELD, _className, _attrDef.getFieldName(), TypeUtil.getFieldDescriptor(tempAttrType));
        Label tempLabel = new Label();
        switch (tempAttrType.getSort()) {
            case Type.FLOAT:
                _visitor.visitInsn(Opcodes.FCMPL);
                _visitor.visitJumpInsn(Opcodes.IFEQ, tempLabel);
                break;
            case Type.LONG:
                _visitor.visitInsn(Opcodes.LCMP);
                _visitor.visitJumpInsn(Opcodes.IFEQ, tempLabel);
                break;
            case Type.DOUBLE:
                _visitor.visitInsn(Opcodes.DCMPL);
                _visitor.visitJumpInsn(Opcodes.IFEQ, tempLabel);
                break;
            default:
                _visitor.visitJumpInsn(Opcodes.IF_ICMPEQ, tempLabel);
        }

        _visitor.visitInsn(Opcodes.ICONST_0);
        _visitor.visitInsn(Opcodes.IRETURN);
        _visitor.visitLabel(tempLabel);
    }

    /**
     * Add array Equals equals seg to the equals() methods.
     *
     * @param _visitor
     * @param _className
     * @param _atrrDef
     */
    protected void handleArrayEquals(MethodVisitor _visitor, String _className, AttributeDef _atrrDef) {
        Type tempAttrType = _atrrDef.getType().getType();
        _visitor.visitVarInsn(Opcodes.ALOAD, 0);
        _visitor.visitFieldInsn(Opcodes.GETFIELD, _className, _atrrDef.getFieldName(), TypeUtil.getFieldDescriptor(tempAttrType));
        _visitor.visitVarInsn(Opcodes.ALOAD, 2);
        _visitor.visitFieldInsn(Opcodes.GETFIELD, _className, _atrrDef.getFieldName(), TypeUtil.getFieldDescriptor(tempAttrType));
        _visitor.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getType(Arrays.class).getInternalName(), "equals", TypeUtil.getArraysEqualsSignature(tempAttrType));
        Label tempLabel = new Label();
        _visitor.visitJumpInsn(Opcodes.IFNE, tempLabel);
        _visitor.visitInsn(Opcodes.ICONST_0);
        _visitor.visitInsn(Opcodes.IRETURN);
        _visitor.visitLabel(tempLabel);
    }

    /**
     * Add object Equals equals seg to the equals() methods.
     *
     * @param _visitor
     * @param _className
     * @param _attrDef
     */
    protected void handleObjectEquals(MethodVisitor _visitor, String _className, AttributeDef _attrDef) {
        Type tempAttrType = _attrDef.getType().getType();
        _visitor.visitVarInsn(Opcodes.ALOAD, 0);
        _visitor.visitFieldInsn(Opcodes.GETFIELD, _className, _attrDef.getFieldName(), TypeUtil.getFieldDescriptor(tempAttrType));
        _visitor.visitVarInsn(Opcodes.ALOAD, 2);
        _visitor.visitFieldInsn(Opcodes.GETFIELD, _className, _attrDef.getFieldName(), TypeUtil.getFieldDescriptor(tempAttrType));
        _visitor.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getType(EqualsHelper.class).getInternalName(), "equals", TypeUtil.getMethodDescriptor(new Class[]{Object.class, Object.class}, Boolean.TYPE));
        Label tempLabel = new Label();
        _visitor.visitJumpInsn(Opcodes.IFNE, tempLabel);
        _visitor.visitInsn(Opcodes.ICONST_0);
        _visitor.visitInsn(Opcodes.IRETURN);
        _visitor.visitLabel(tempLabel);
    }

    /**
     * Add append() statement to the toString() method.
     *
     * @param _visitor
     * @param _className
     * @param _attrDef
     */
    protected void addAppendStatement(MethodVisitor _visitor, String _className, AttributeDef _attrDef) {
        _visitor.visitVarInsn(Opcodes.ALOAD, 1);
        _visitor.visitLdcInsn(_attrDef.getName());
        _visitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, STRING_BUILDER_CLASS_NAME, STRING_BUILDER_APPEND_METHOD, STRING_BUILDER_APPEND_STRING);
        _visitor.visitLdcInsn("=");
        this.addRHSAppendStatement(_visitor, _className, _attrDef);
    }

    /**
     * Add rhsAppend() statement to the toString() method.
     *
     * @param _visitor
     * @param _className
     * @param _attrDef
     */
    protected void addRHSAppendStatement(MethodVisitor _visitor, String _className, AttributeDef _attrDef) {
        Type tempAttrType = _attrDef.getType().getType();
        _visitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, STRING_BUILDER_CLASS_NAME, STRING_BUILDER_APPEND_METHOD, STRING_BUILDER_APPEND_STRING);
        _visitor.visitVarInsn(Opcodes.ALOAD, 0);
        _visitor.visitFieldInsn(Opcodes.GETFIELD, _className, _attrDef.getFieldName(), TypeUtil.getFieldDescriptor(tempAttrType));
        if (!_attrDef.getType().isArray() && _attrDef.getType().getType().getSort() != 9) {
            _visitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, STRING_BUILDER_CLASS_NAME, STRING_BUILDER_APPEND_METHOD, TypeUtil.getStringBuilderAppendSignature(tempAttrType));
        } else {
            _visitor.visitMethodInsn(Opcodes.INVOKESTATIC, ARRAYS_CLASS_NAME, ARRAYS_TOSTRING_METHOD, TypeUtil.getArraysToStringSignature(tempAttrType));
            _visitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, STRING_BUILDER_CLASS_NAME, STRING_BUILDER_APPEND_METHOD, STRING_BUILDER_APPEND_STRING);
        }

        _visitor.visitInsn(Opcodes.POP);
    }

    /**
     * Add append String statement, add given str.
     *
     * @param _visitor
     * @param _str
     */
    protected void addAppendStringStatement(MethodVisitor _visitor, String _str) {
        _visitor.visitVarInsn(Opcodes.ALOAD, 1);
        _visitor.visitLdcInsn(_str);
        _visitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, STRING_BUILDER_CLASS_NAME, STRING_BUILDER_APPEND_METHOD, STRING_BUILDER_APPEND_STRING);
        _visitor.visitInsn(Opcodes.POP);
    }

    @Override
    protected String getDefaultConstantName(AttributeDef _attrDef) {
        return _attrDef.getFieldName() + DEFAULT_SUFFIX;
    }

}

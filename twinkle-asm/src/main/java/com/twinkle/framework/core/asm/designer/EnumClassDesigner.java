package com.twinkle.framework.core.asm.designer;

import com.twinkle.framework.core.datastruct.define.EnumTypeDef;
import com.twinkle.framework.core.utils.TypeUtil;
import lombok.Getter;
import org.objectweb.asm.*;

import java.util.List;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-02 23:40<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Getter
public class EnumClassDesigner extends AbstractClassDesigner {
    private final EnumTypeDef enumTypeDef;
    private final Type enumArrayType;

    public EnumClassDesigner(EnumTypeDef _enumTypeDef) {
        this.enumTypeDef = _enumTypeDef;
        this.enumArrayType = Type.getType("[" + _enumTypeDef.getType().getDescriptor());
    }

    @Override
    public String getCanonicalClassName() {
        return this.enumTypeDef.getType().getClassName();
    }
    @Override
    protected ClassVisitor addClassDeclaration(ClassVisitor _visitor) {
        EnumTypeDef tempEnumType = this.enumTypeDef;
        _visitor.visit(TARGET_JVM, this.getClassAccessFlags(), tempEnumType.getType().getInternalName(), getGenericDescriptor(TypeUtil.ENUM_TYPE, new Type[]{tempEnumType.getType()}), TypeUtil.ENUM_TYPE.getInternalName(), (String[])null);
        return _visitor;
    }
    @Override
    protected void addClassDefinition(ClassVisitor _visitor) {
        this.addFields(_visitor);
        this.addConstructorDefinition(_visitor);
        this.addGetFieldValueMethodDefinition(_visitor);
        this.addEnumConstants(_visitor);
        this.addClassConstructorDefinition(_visitor);
        this.addEnumConstantsMethodDefinition(_visitor);
        this.addValueOfMethodDefinition(_visitor);
    }

    protected int getClassAccessFlags() {
        return Opcodes.ACC_ENUM + Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_SUPER;
    }

    protected void addEnumConstants(ClassVisitor _visitor) {
        EnumTypeDef tempEnumTypeDefine = this.enumTypeDef;
        tempEnumTypeDefine.getEnumNames().stream().forEach(item -> {
            FieldVisitor tempFieldVisitor = _visitor.visitField(Opcodes.ACC_ENUM + Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC + Opcodes.ACC_FINAL
                    , item, tempEnumTypeDefine.getType().getDescriptor(), (String)null, (Object)null);
            tempFieldVisitor.visitEnd();
        });

        Type tempEnumArrayType = this.getEnumArrayType();
        FieldVisitor tempFieldVisitor = _visitor.visitField(Opcodes.ACC_SYNTHETIC +
                Opcodes.ACC_PRIVATE + Opcodes.ACC_STATIC + Opcodes.ACC_FINAL, "$VALUES", tempEnumArrayType.getDescriptor(), (String)null, (Object)null);
        tempFieldVisitor.visitEnd();
    }

    protected MethodVisitor addClassConstructorDefinition(ClassVisitor _visitor) {
        EnumTypeDef tempEnumTypeDefine = this.enumTypeDef;
        List<String> tempEnumNames = tempEnumTypeDefine.getEnumNames();
        List<Object> tempEnumConstraints = tempEnumTypeDefine.getEnumConstraints();
        int tempSize = tempEnumNames.size();
        MethodVisitor tempVisitor = _visitor.visitMethod(8, "<clinit>", "()V", (String)null, (String[])null);
        tempVisitor.visitCode();

        int i;
        for(i = 0; i < tempSize; i++) {
            tempVisitor.visitTypeInsn(Opcodes.NEW, tempEnumTypeDefine.getType().getInternalName());
            tempVisitor.visitInsn(Opcodes.DUP);
            tempVisitor.visitLdcInsn(tempEnumNames.get(i));
            tempVisitor.visitLdcInsn(i);
            tempVisitor.visitLdcInsn(tempEnumConstraints.get(i));
            tempVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, tempEnumTypeDefine.getType().getInternalName(), "<init>", this.getConstructorSignature(tempEnumTypeDefine.getValueType()));
            tempVisitor.visitFieldInsn(Opcodes.PUTSTATIC, tempEnumTypeDefine.getType().getInternalName(), (String)tempEnumNames.get(i), tempEnumTypeDefine.getType().getDescriptor());
        }

        tempVisitor.visitLdcInsn(tempSize);
        tempVisitor.visitTypeInsn(Opcodes.ANEWARRAY, tempEnumTypeDefine.getType().getInternalName());

        for(i = 0; i < tempSize; i++) {
            tempVisitor.visitInsn(Opcodes.DUP);
            tempVisitor.visitLdcInsn(i);
            tempVisitor.visitFieldInsn(Opcodes.GETSTATIC, tempEnumTypeDefine.getType().getInternalName(), (String)tempEnumNames.get(i), tempEnumTypeDefine.getType().getDescriptor());
            tempVisitor.visitInsn(Opcodes.AASTORE);
        }

        Type tempArrayType = this.getEnumArrayType();
        tempVisitor.visitFieldInsn(Opcodes.PUTSTATIC, tempEnumTypeDefine.getType().getInternalName(), "$VALUES", tempArrayType.getDescriptor());
        tempVisitor.visitInsn(Opcodes.RETURN);
        tempVisitor.visitMaxs(AUTO_STACK_SIZE, AUTO_LOCAL_VARS);
        tempVisitor.visitEnd();
        return tempVisitor;
    }

    protected MethodVisitor addEnumConstantsMethodDefinition(ClassVisitor _visitor) {
        EnumTypeDef tempEnumTypeDefine = this.enumTypeDef;
        Type tempArrayType = this.getEnumArrayType();
        MethodVisitor tempVisitor = _visitor.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, "values", getMethodSignature(new Type[0], tempArrayType), null, null);
        tempVisitor.visitCode();
        tempVisitor.visitFieldInsn(Opcodes.GETSTATIC, tempEnumTypeDefine.getType().getInternalName(), "$VALUES", tempArrayType.getDescriptor());
        tempVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, tempArrayType.getDescriptor(), "clone", getMethodSignature(new Type[0], TypeUtil.OBJECT_TYPE));
        tempVisitor.visitTypeInsn(Opcodes.CHECKCAST, tempArrayType.getDescriptor());
        tempVisitor.visitInsn(Opcodes.ARETURN);
        tempVisitor.visitMaxs(AUTO_STACK_SIZE, AUTO_LOCAL_VARS);
        tempVisitor.visitEnd();
        return tempVisitor;
    }

    protected MethodVisitor addValueOfMethodDefinition(ClassVisitor _visitor) {
        EnumTypeDef tempEnumTypeDefine = this.enumTypeDef;
        MethodVisitor tempVisitor = _visitor.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, "valueOf", getMethodSignature(new Type[]{TypeUtil.STRING_TYPE}, tempEnumTypeDefine.getType()), null, null);
        tempVisitor.visitCode();
        tempVisitor.visitLdcInsn(tempEnumTypeDefine.getType());
        tempVisitor.visitVarInsn(Opcodes.ALOAD, 0);
        tempVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, TypeUtil.ENUM_TYPE.getInternalName(), "valueOf", getMethodSignature(new Type[]{TypeUtil.CLASS_TYPE, TypeUtil.STRING_TYPE}, TypeUtil.ENUM_TYPE));
        tempVisitor.visitTypeInsn(Opcodes.CHECKCAST, tempEnumTypeDefine.getType().getInternalName());
        tempVisitor.visitInsn(Opcodes.ARETURN);
        tempVisitor.visitMaxs(AUTO_STACK_SIZE, AUTO_LOCAL_VARS);
        tempVisitor.visitEnd();
        return tempVisitor;
    }

    protected void addFields(ClassVisitor _visitor) {
        FieldVisitor tempVisitor = _visitor.visitField(Opcodes.ACC_PRIVATE + Opcodes.ACC_FINAL, "_value", this.enumTypeDef.getValueType().getDescriptor(), null, null);
        tempVisitor.visitEnd();
    }

    protected MethodVisitor addConstructorDefinition(ClassVisitor _visitor) {
        EnumTypeDef tempEnumTypeDefine = this.enumTypeDef;
        Type tempValueType = tempEnumTypeDefine.getValueType();
        MethodVisitor tempVisitor = _visitor.visitMethod(Opcodes.ACC_PRIVATE, "<init>", this.getConstructorSignature(tempValueType), getMethodSignature(new Type[]{tempValueType}, Type.VOID_TYPE), null);
        tempVisitor.visitCode();
        tempVisitor.visitVarInsn(Opcodes.ALOAD, 0);
        tempVisitor.visitVarInsn(Opcodes.ALOAD, 1);
        tempVisitor.visitVarInsn(Opcodes.ILOAD, 2);
        tempVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, TypeUtil.ENUM_TYPE.getInternalName(), "<init>", getMethodSignature(new Type[]{TypeUtil.STRING_TYPE, Type.INT_TYPE}, Type.VOID_TYPE));
        tempVisitor.visitVarInsn(Opcodes.ALOAD, 0);
        tempVisitor.visitVarInsn(getOpcode(tempValueType, Opcodes.ILOAD), 3);
        tempVisitor.visitFieldInsn(Opcodes.PUTFIELD, tempEnumTypeDefine.getType().getInternalName(), "_value", tempValueType.getDescriptor());
        tempVisitor.visitInsn(Opcodes.RETURN);
        tempVisitor.visitMaxs(AUTO_STACK_SIZE, AUTO_LOCAL_VARS);
        tempVisitor.visitEnd();
        return tempVisitor;
    }

    protected MethodVisitor addGetFieldValueMethodDefinition(ClassVisitor _visitor) {
        EnumTypeDef tempEnumTypeDefine = this.enumTypeDef;
        Type tempValueType = tempEnumTypeDefine.getValueType();
        MethodVisitor tempVisitor = _visitor.visitMethod(Opcodes.ACC_PUBLIC, "value", getMethodSignature(new Type[0], tempValueType), null, null);
        tempVisitor.visitCode();
        tempVisitor.visitVarInsn(Opcodes.ALOAD, 0);
        tempVisitor.visitFieldInsn(Opcodes.GETFIELD, tempEnumTypeDefine.getType().getInternalName(), "_value", tempValueType.getDescriptor());
        tempVisitor.visitInsn(getOpcode(tempValueType, Opcodes.IRETURN));
        tempVisitor.visitMaxs(AUTO_STACK_SIZE, AUTO_LOCAL_VARS);
        tempVisitor.visitEnd();
        return tempVisitor;
    }

    protected String getConstructorSignature(Type _type) {
        return getMethodSignature(new Type[]{TypeUtil.STRING_TYPE, Type.INT_TYPE, _type}, Type.VOID_TYPE);
    }

    protected static String getGenericDescriptor(Type _type, Type[] _genericTypes) {
        StringBuilder tempBuilder = new StringBuilder();
        tempBuilder.append('L').append(_type.getInternalName());
        if (_genericTypes != null && _genericTypes.length > 0) {
            tempBuilder.append('<');
            for(int i = 0; i < _genericTypes.length; ++i) {
                tempBuilder.append(_genericTypes[i].getDescriptor());
            }
            tempBuilder.append('>');
        }

        tempBuilder.append(';');
        return tempBuilder.toString();
    }

    protected static String getMethodSignature(Type[] _parameterTypes, Type _returnType) {
        StringBuilder tempBuilder = new StringBuilder();
        tempBuilder.append('(');
        for(int i = 0; i < _parameterTypes.length; i++) {
            tempBuilder.append(_parameterTypes[i].getDescriptor());
        }
        tempBuilder.append(')');
        tempBuilder.append(_returnType.getDescriptor());
        return tempBuilder.toString();
    }

    /**
     * Returns a JVM instruction opcode adapted to this {@link Type}. This method must not be used for
     * method types.
     *
     * @param _opCode a JVM instruction opcode. This opcode must be one of ILOAD, ISTORE, IALOAD,
     *     IASTORE, IADD, ISUB, IMUL, IDIV, IREM, INEG, ISHL, ISHR, IUSHR, IAND, IOR, IXOR and
     *     IRETURN.
     * @return an opcode that is similar to the given opcode, but adapted to this {@link Type}. For
     *     example, if this type is {@code float} and {@code opcode} is IRETURN, this method returns
     *     FRETURN.
     */
    protected static int getOpcode(Type _type, int _opCode) {
        return _type.getOpcode(_opCode);
    }

}

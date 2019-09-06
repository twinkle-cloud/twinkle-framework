package com.twinkle.framework.asm.designer;

import com.twinkle.framework.asm.builder.InstanceBuilder;
import com.twinkle.framework.asm.utils.TypeUtil;
import lombok.Getter;
import org.objectweb.asm.*;

import java.util.Arrays;
import java.util.List;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-02 21:25<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Getter
public class InstanceBuilderDesigner extends AbstractClassDesigner{
    private final String builderClassName;
    private final Type builderType;
    private final Type instanceType;
    private final Type interfaceType;

    protected InstanceBuilderDesigner(String _className, Type _builderType, Type _interfaceType, Type _instanceType) {
        this.builderClassName = toInternalName(_className);
        this.builderType = _builderType;
        this.instanceType = _instanceType;
        this.interfaceType = _interfaceType;
    }

    public InstanceBuilderDesigner(String _className, Class<? extends InstanceBuilder> _builderClass, Class _interfaceClass, String _instanceClassName) {
        this.builderClassName = toInternalName(_className);
        if (!_builderClass.isInterface()) {
            throw new IllegalArgumentException(_builderClass.getName() + "is not an interface");
        } else {
            this.builderType = Type.getType(_builderClass);
            this.instanceType = Type.getObjectType(toInternalName(_instanceClassName));
            this.interfaceType = Type.getType(_interfaceClass);
        }
    }

    @Override
    public String getCanonicalClassName() {
        return this.toCanonicalName(this.builderClassName);
    }

    protected Type getInterfaceBaseType() {
        return TypeUtil.OBJECT_TYPE;
    }

    protected Type getSuperType() {
        return TypeUtil.OBJECT_TYPE;
    }

    /**
     * Add Class Declaration.
     *
     * @param _visitor
     * @return
     */
    @Override
    protected ClassVisitor addClassDeclaration(ClassVisitor _visitor) {
        _visitor.visit(TARGET_JVM, Opcodes.ACC_PUBLIC + Opcodes.ACC_SUPER,
                this.getBuilderClassName(), getClassSignature(this.getSuperType(),
                        this.getBuilderType(), Arrays.asList(this.getInterfaceType())),
                this.getSuperType().getInternalName(), new String[]{this.getBuilderType().getInternalName()});
        return _visitor;
    }
    @Override
    protected void addClassDefinition(ClassVisitor _visitor) {
        this.addDefaultConstructorDefinition(_visitor);
        this.addNewInstanceDefinition(_visitor);
        this.addNewArrayDefinition(_visitor);
        this.addSyntheticNewInstanceDefinition(_visitor, this.getInterfaceBaseType());
        this.addSyntheticNewArrayDefinition(_visitor, this.getInterfaceBaseType());
    }

    protected MethodVisitor addDefaultConstructorDefinition(ClassVisitor _visitor) {
        String tempDescriptor = "()V";

        MethodVisitor tempVisitor = _visitor.visitMethod(Opcodes.ACC_PUBLIC, "<init>", tempDescriptor, (String)null, (String[])null);
        tempVisitor.visitCode();
        tempVisitor.visitVarInsn(Opcodes.ALOAD, 0);
        tempVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, this.getSuperType().getInternalName(), "<init>", tempDescriptor);
        tempVisitor.visitInsn(Opcodes.RETURN);
        tempVisitor.visitMaxs(AUTO_STACK_SIZE, AUTO_LOCAL_VARS);
        tempVisitor.visitEnd();
        return tempVisitor;
    }

    protected MethodVisitor addNewInstanceDefinition(ClassVisitor _visitor) {
        MethodVisitor tempVisitor = _visitor.visitMethod(Opcodes.ACC_PUBLIC, "newInstance", "()L" + this.getInterfaceType().getInternalName() + ";", (String)null, (String[])null);
        tempVisitor.visitCode();
        tempVisitor.visitTypeInsn(Opcodes.NEW, this.getInstanceType().getInternalName());
        tempVisitor.visitInsn(Opcodes.DUP);
        tempVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, this.getInstanceType().getInternalName(), "<init>", "()V");
        tempVisitor.visitInsn(Opcodes.ARETURN);
        tempVisitor.visitMaxs(AUTO_STACK_SIZE, AUTO_LOCAL_VARS);
        tempVisitor.visitEnd();
        return tempVisitor;
    }

    protected MethodVisitor addNewArrayDefinition(ClassVisitor _visitor) {
        MethodVisitor tempVisitor = _visitor.visitMethod(Opcodes.ACC_PUBLIC, "newArray", "(I)[L" + this.getInterfaceType().getInternalName() + ";", (String)null, (String[])null);
        tempVisitor.visitCode();
        tempVisitor.visitVarInsn(Opcodes.ILOAD, 1);
        tempVisitor.visitTypeInsn(Opcodes.ANEWARRAY, this.getInterfaceType().getInternalName());
        tempVisitor.visitInsn(Opcodes.ARETURN);
        tempVisitor.visitMaxs(AUTO_STACK_SIZE, AUTO_LOCAL_VARS);
        tempVisitor.visitEnd();
        return tempVisitor;
    }

    protected MethodVisitor addSyntheticNewInstanceDefinition(ClassVisitor _visitor, Type _type) {
        MethodVisitor tempVisitor = _visitor.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_VOLATILE + Opcodes.ACC_SYNTHETIC,
                "newInstance", "()" + _type.getDescriptor(), (String)null, (String[])null);
        tempVisitor.visitCode();
        tempVisitor.visitVarInsn(Opcodes.ALOAD, 0);
        tempVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, this.getBuilderClassName(), "newInstance", "()L" + this.getInterfaceType().getInternalName() + ";");
        tempVisitor.visitInsn(Opcodes.ARETURN);
        tempVisitor.visitMaxs(AUTO_STACK_SIZE, AUTO_LOCAL_VARS);
        tempVisitor.visitEnd();
        return tempVisitor;
    }

    protected MethodVisitor addSyntheticNewArrayDefinition(ClassVisitor _visitor, Type _type) {
        MethodVisitor tempVisitor = _visitor.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_VOLATILE + Opcodes.ACC_SYNTHETIC,
                "newArray", "(I)[" + _type.getDescriptor(), null, null);
        tempVisitor.visitCode();
        tempVisitor.visitVarInsn(Opcodes.ALOAD, 0);
        tempVisitor.visitVarInsn(Opcodes.ILOAD, 1);
        tempVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, this.getBuilderClassName(), "newArray", "(I)[L" + this.getInterfaceType().getInternalName() + ";");
        tempVisitor.visitInsn(Opcodes.ARETURN);
        tempVisitor.visitMaxs(AUTO_STACK_SIZE, AUTO_LOCAL_VARS);
        tempVisitor.visitEnd();
        return tempVisitor;
    }

    protected static String getClassSignature(Type _mainType, Type _interfaceType, List<Type> _genericTypes) {
        StringBuilder tempBuilder = new StringBuilder();
        tempBuilder.append(_mainType.getDescriptor());
        tempBuilder.append("L").append(_interfaceType.getInternalName());
        if (_genericTypes != null && _genericTypes.size() > 0) {
            tempBuilder.append("<");
            _genericTypes.stream().forEach(item -> tempBuilder.append(item.getDescriptor()));
            tempBuilder.append(">");
        }

        tempBuilder.append(";");
        return tempBuilder.toString();
    }

    protected static String toInternalName(String _className) {
        return _className.replace('.', '/');
    }

    protected String toCanonicalName(String _className) {
        return _className.replace('/', '.');
    }

}

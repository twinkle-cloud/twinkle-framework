package com.twinkle.framework.struct.asm.designer;

import com.twinkle.framework.asm.classloader.SerializerClassLoader;
import com.twinkle.framework.asm.descriptor.BeanTypeDescriptor;
import com.twinkle.framework.asm.descriptor.TypeDescriptor;
import com.twinkle.framework.asm.designer.ClassDesigner;
import com.twinkle.framework.asm.utils.ClassDesignerUtil;
import org.objectweb.asm.*;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.PrintWriter;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/4/19 7:08 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public abstract class AbstractGeneratorClassDesigner implements ClassDesigner {
    public static final String BASE_SERIALIZERS_PACKAGE = "com.twinkle.framework.struct.datastruct.transform.json.";
    protected BeanTypeDescriptor typeDescriptor;
    protected SerializerClassLoader classLoader;
    protected Class<?> targetSuperclass;
    protected String targetClassName;
    protected String targetInternalClassName;

    public AbstractGeneratorClassDesigner(BeanTypeDescriptor _descriptor, SerializerClassLoader _classLoader, Class<?> _superClass) {
        this.typeDescriptor = _descriptor;
        this.classLoader = _classLoader;
        this.targetSuperclass = _superClass;
        this.targetClassName = this.getClassName(_descriptor);
        this.targetInternalClassName = ClassDesignerUtil.getInternalName(this.targetClassName);
    }

    protected abstract String getSuffix();
    @Override
    public String getCanonicalClassName() {
        return this.targetClassName;
    }
    @Override
    public byte[] toByteArray() {
        return this.toByteArray(false, (PrintWriter)null);
    }
    @Override
    public byte[] toByteArray(boolean _checkFlag, PrintWriter _printWriter) {
        ClassWriter tempWriter = new ClassWriter(3);
        ClassVisitor tempVisitor = tempWriter;
        if (_checkFlag) {
            tempVisitor = new CheckClassAdapter(tempWriter);
        }

        if (_printWriter != null) {
            tempVisitor = new TraceClassVisitor(tempWriter, _printWriter);
        }

        this.declareClass((ClassVisitor)tempVisitor);
        this.declareFields((ClassVisitor)tempVisitor);
        this.generateConstructor((ClassVisitor)tempVisitor);
        this.generateInit((ClassVisitor)tempVisitor);
        this.generateProcess((ClassVisitor)tempVisitor);
        ((ClassVisitor)tempVisitor).visitEnd();
        return tempWriter.toByteArray();
    }

    protected void declareClass(ClassVisitor _visitor) {
        _visitor.visit(TARGET_JVM, Opcodes.ACC_PUBLIC + Opcodes.ACC_SUPER, this.targetInternalClassName, null, Type.getInternalName(this.targetSuperclass), null);
    }

    protected void declareFields(ClassVisitor _visitor) {
    }

    protected void generateConstructor(ClassVisitor _visitor) {
        MethodVisitor var2 = _visitor.visitMethod(Opcodes.ACC_PUBLIC, "<init>", ClassDesignerUtil.getMethodSignature(Void.TYPE, new Class[0]), null, null);
        var2.visitCode();
        var2.visitVarInsn(Opcodes.ALOAD, 0);
        var2.visitInsn(Opcodes.ICONST_0);
        var2.visitLdcInsn(getQualifiedName(this.typeDescriptor));
        var2.visitMethodInsn(Opcodes.INVOKESPECIAL, Type.getInternalName(this.targetSuperclass), "<init>", ClassDesignerUtil.getMethodSignature(Void.TYPE, new Class[]{Boolean.TYPE, String.class}));
        var2.visitInsn(Opcodes.RETURN);
        var2.visitMaxs(AUTO_STACK_SIZE, AUTO_LOCAL_VARS);
        var2.visitEnd();
    }

    protected abstract void generateInit(ClassVisitor _visitor);

    protected abstract void generateProcess(ClassVisitor _visitor);

    protected String getClassName(TypeDescriptor _descriptor) {
            return BASE_SERIALIZERS_PACKAGE + '.' + _descriptor.getName() + this.getSuffix();
    }

    protected Class getBeanClass(TypeDescriptor _descriptor) {
        try {
            return this.classLoader.loadClass(_descriptor.getClassName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected static String getQualifiedName(TypeDescriptor _descriptor) {
        return _descriptor.getName();
    }
}

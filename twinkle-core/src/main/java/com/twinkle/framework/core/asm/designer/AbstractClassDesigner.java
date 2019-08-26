package com.twinkle.framework.core.asm.designer;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.PrintWriter;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-03 14:57<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public abstract class AbstractClassDesigner implements ClassDesigner {

    protected ClassWriter newClass(boolean _checkFlag, PrintWriter _writer) {
        ClassWriter tempClassWriter = new ClassWriter(AUTO_CALCULATE_STACK_SIZE_AND_LOCAL_VARS_NUMBER);
        ClassVisitor tempVistor = tempClassWriter;
        if (_checkFlag) {
            tempVistor = new CheckClassAdapter(tempClassWriter);
        }

        if (_writer != null) {
            tempVistor = new TraceClassVisitor(tempClassWriter, _writer);
        }

        this.addClassDeclaration(tempVistor);
        this.addClassDefinition(tempVistor);
        tempVistor.visitEnd();
        return tempClassWriter;
    }

    abstract ClassVisitor addClassDeclaration(ClassVisitor _visitor);

    abstract void addClassDefinition(ClassVisitor _visitor);

    @Override
    public byte[] toByteArray(boolean _checkFlag, PrintWriter _printWriter) {
        return this.newClass(_checkFlag, _printWriter).toByteArray();
    }

    @Override
    public byte[] toByteArray() {
        return this.newClass(false, null).toByteArray();
    }

}

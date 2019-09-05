package com.twinkle.framework.core.asm.designer;

import com.twinkle.framework.core.datastruct.Bean;
import com.twinkle.framework.core.datastruct.define.AttributeDef;
import com.twinkle.framework.core.datastruct.define.BeanTypeDef;
import com.twinkle.framework.core.utils.TypeUtil;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.List;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-07 10:31<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class BeanInterfaceDesigner extends AbstractBeanClassDesigner {
    public BeanInterfaceDesigner(String _className, BeanTypeDef _beanTypeDef) {
        super(_className, _beanTypeDef);
    }
    @Override
    protected String[] getDefaultInterfaces() {
        return new String[]{Bean.class.getName(), Cloneable.class.getName()};
    }
    @Override
    protected int initAccessFlags() {
        return this.getInterfaceAccessFlags();
    }
    @Override
    protected void addClassDefinition(ClassVisitor _visitor, String _className, String _superName, List<AttributeDef> _attrDefList, BeanTypeDef _beanTypeDef) {
        this.addMethodsDeclaration(_visitor, _attrDefList);
    }

    /**
     * Interface: public interface XXXX{}.
     *
     * JVM by default: The access should to be: public abstract interface.
     *
     * @return
     */
    protected int getInterfaceAccessFlags() {
        return Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT + Opcodes.ACC_INTERFACE;
    }

    /**
     * Add methodes based on the attributes' List.
     *
     * @param _visitor
     * @param _attrDefList
     */
    protected void addMethodsDeclaration(ClassVisitor _visitor, List<AttributeDef> _attrDefList) {
        _attrDefList.stream().parallel().forEach(item -> {
            this.addGetterDeclaration(_visitor, item);
            this.addSetterDeclaration(_visitor, item);
            if (item.getDefaultValue() != null) {
                this.addDefaultGetterDeclaration(_visitor, item);
            }
        });
        this.addCloneDeclaration(_visitor);
    }

    /**
     * Add default getter method.
     *
     * public xxx getAAADefault();
     *
     * @param _visitor
     * @param _attrDef
     * @return
     */
    protected MethodVisitor addDefaultGetterDeclaration(ClassVisitor _visitor, AttributeDef _attrDef) {
        String tempSignature;
        if (_attrDef.getType().isGeneric()) {
            tempSignature = TypeUtil.getGenericGetterSignature(_attrDef.getType());
        } else {
            tempSignature = null;
        }

        MethodVisitor tempVisistor = _visitor.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT, TypeUtil.getDefaultGetterName(_attrDef), TypeUtil.getGetterSignature(_attrDef.getType().getType()), tempSignature, null);
        tempVisistor.visitEnd();
        return tempVisistor;
    }

    /**
     * public xxx getAAA();
     * @param _visitor
     * @param _attrDef
     * @return
     */
    protected MethodVisitor addGetterDeclaration(ClassVisitor _visitor, AttributeDef _attrDef) {
        String tempSignature;
        if (_attrDef.getType().isGeneric()) {
            tempSignature = TypeUtil.getGenericGetterSignature(_attrDef.getType());
        } else {
            tempSignature = null;
        }

        MethodVisitor tempVisitor = _visitor.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT, TypeUtil.getGetterName(_attrDef), TypeUtil.getGetterSignature(_attrDef.getType().getType()), tempSignature, null);
        tempVisitor.visitEnd();
        return tempVisitor;
    }

    /**
     * Add public void setXXX(AAA _value); to the interface.
     *
     * @param _visitor
     * @param _attrDef
     * @return
     */
    protected MethodVisitor addSetterDeclaration(ClassVisitor _visitor, AttributeDef _attrDef) {
        String tempSignature;
        if (_attrDef.getType().isGeneric()) {
            tempSignature = TypeUtil.getGenericSetterSignature(_attrDef.getType());
        } else {
            tempSignature = null;
        }

        MethodVisitor tempVisitor = _visitor.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT, TypeUtil.getSetterName(_attrDef), TypeUtil.getSetterSignature(_attrDef.getType().getType()), tempSignature, null);
        tempVisitor.visitEnd();
        return tempVisitor;
    }

    /**
     * Add public clone(); method to the interface.
     *
     * @param _visitor
     * @return
     */
    protected MethodVisitor addCloneDeclaration(ClassVisitor _visitor) {
        MethodVisitor tempVisitor = _visitor.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT, "clone", TypeUtil.getMethodDescriptor(new Type[0], TypeUtil.OBJECT_TYPE), null, new String[]{Type.getType(CloneNotSupportedException.class).getInternalName()});
        tempVisitor.visitEnd();
        return tempVisitor;
    }
}

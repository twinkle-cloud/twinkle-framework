package com.twinkle.framework.asm.designer;

import com.twinkle.framework.asm.Bean;
import com.twinkle.framework.asm.RecyclableBean;
import com.twinkle.framework.asm.define.AttributeDef;
import com.twinkle.framework.asm.define.BeanTypeDef;
import com.twinkle.framework.asm.utils.TypeUtil;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.List;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-07 22:52<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class RecyclableBeanInterfaceDesigner extends BeanInterfaceDesigner {
    public RecyclableBeanInterfaceDesigner(String _className, BeanTypeDef _beanTypeDef) {
        super(_className, _beanTypeDef);
    }
    @Override
    protected String[] getDefaultInterfaces() {
        return new String[]{Bean.class.getName(), RecyclableBean.class.getName(), Cloneable.class.getName()};
    }
    @Override
    protected void addMethodsDeclaration(ClassVisitor _visitor, List<AttributeDef> _attrDefList) {
        super.addMethodsDeclaration(_visitor, _attrDefList);
        _attrDefList.stream().parallel().forEach(item ->{
            this.addFlagGetterDeclaration(_visitor, item);
            this.addFlagSetterDeclaration(_visitor, item);
        });
    }

    /**
     * Add Flag getter method.
     * public getXXXFlag();
     *
     * @param _visitor
     * @param _attrDef
     * @return
     */
    protected MethodVisitor addFlagGetterDeclaration(ClassVisitor _visitor, AttributeDef _attrDef) {
        MethodVisitor tempVisitor = _visitor.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT, TypeUtil.getFlagGetterName(_attrDef), TypeUtil.getFlagGetterDescriptor(), null, null);
        tempVisitor.visitEnd();
        return tempVisitor;
    }

    /**
     * Add Flag Setter method for the interface
     * public void setXXXFlag(boolean _flag);
     *
     * @param _visitor
     * @param _attrDef
     * @return
     */
    protected MethodVisitor addFlagSetterDeclaration(ClassVisitor _visitor, AttributeDef _attrDef) {
        MethodVisitor tempVisitor = _visitor.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT, TypeUtil.getFlagSetterName(_attrDef), TypeUtil.getFlagSetterDescriptor(), null, null);
        tempVisitor.visitEnd();
        return tempVisitor;
    }
}

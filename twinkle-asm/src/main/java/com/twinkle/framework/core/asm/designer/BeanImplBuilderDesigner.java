package com.twinkle.framework.core.asm.designer;

import com.twinkle.framework.core.datastruct.Bean;
import com.twinkle.framework.core.datastruct.builder.BeanImplBuilder;
import com.twinkle.framework.core.utils.TypeUtil;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-02 21:50<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class BeanImplBuilderDesigner extends InstanceBuilderDesigner {
    protected static final String IMPL_BUILDER_INNER_NAME = "ImplBuilder";
    protected static final String INNER_SEPARATOR = "$";

    public BeanImplBuilderDesigner(String _className, String _interfaceClassName) {
        super(getImplBuilderInnerClassName(_className), Type.getType(BeanImplBuilder.class), Type.getObjectType(toInternalName(_interfaceClassName)), Type.getObjectType(toInternalName(_className)));
    }

    @Override
    protected Type getInterfaceBaseType() {
        return TypeUtil.BEAN_TYPE;
    }

    @Override
    protected ClassVisitor addClassDeclaration(ClassVisitor _visitor) {
        super.addClassDeclaration(_visitor);
        _visitor.visitInnerClass(this.getBuilderClassName(), this.getInstanceType().getInternalName(), IMPL_BUILDER_INNER_NAME, Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC);
        return _visitor;
    }
    @Override
    protected void addClassDefinition(ClassVisitor _visitor) {
        super.addClassDefinition(_visitor);
        this.addSyntheticNewInstanceDefinition(_visitor, TypeUtil.OBJECT_TYPE);
        this.addSyntheticNewArrayDefinition(_visitor, TypeUtil.OBJECT_TYPE);
    }

    protected static String getImplBuilderInnerClassName(String _className) {
        return _className + INNER_SEPARATOR + IMPL_BUILDER_INNER_NAME;
    }

}

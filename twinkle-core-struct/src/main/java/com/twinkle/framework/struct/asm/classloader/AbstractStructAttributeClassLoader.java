package com.twinkle.framework.struct.asm.classloader;

import com.twinkle.framework.asm.classloader.AbstractBeanClassLoader;
import com.twinkle.framework.asm.classloader.EnhancedClassLoader;
import com.twinkle.framework.asm.designer.*;
import com.twinkle.framework.asm.Bean;
import com.twinkle.framework.asm.SimpleReflectiveBean;
import com.twinkle.framework.struct.asm.define.StructAttributeBeanTypeDef;
import com.twinkle.framework.struct.asm.define.StructAttributeBeanTypeDefImpl;
import com.twinkle.framework.struct.error.StructAttributeException;
import com.twinkle.framework.struct.type.AbstractStructAttribute;
import com.twinkle.framework.struct.type.StructType;
import com.twinkle.framework.struct.asm.designer.StructAttributeClassDesigner;
import com.twinkle.framework.struct.asm.designer.StructAttributeImplBuilderDesigner;
import com.twinkle.framework.struct.asm.designer.StructAttributeInterfaceDesigner;
import com.twinkle.framework.struct.utils.StructTypeUtil;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/30/19 8:12 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public abstract class AbstractStructAttributeClassLoader extends EnhancedClassLoader {
    public static final String PACKAGE_PREFIX = "com.twinkle.framework.struct.beans.";
    public static final String IMPL_SUFFIX = "Impl";
    public static final String IMPL_BUILDER_SUFFIX = "$ImplBuilder";
    private final BeanClassDesignerBuilder designerBuilder = new BeanClassDesignerBuilder(SimpleReflectiveBean.class);

    public AbstractStructAttributeClassLoader(ClassLoader _classLoader) {
        super(_classLoader);
    }

    public AbstractStructAttributeClassLoader() {
    }
    @Override
    protected Class<?> findClass(String _className) throws ClassNotFoundException {
        if (_className != null && _className.startsWith(PACKAGE_PREFIX)) {
            String tempInterfaceName = this.getInterfaceName(_className);
            StructType tempStructAttrType = null;

            try {
                tempStructAttrType = this.getStructAttributeType(StructAttributeBeanTypeDefImpl.getStructAttributeQualifiedTypeName(tempInterfaceName));
            } catch (StructAttributeException e) {
            }

            if (tempStructAttrType != null) {
                StructAttributeBeanTypeDef tempStructAttrBeanTypeDef;
                try {
                    tempStructAttrBeanTypeDef = this.createStructAttributeBeanTypeDef(tempStructAttrType);
                } catch (ClassNotFoundException e) {
                    throw new ClassNotFoundException("Failed to load class " + _className + " due to the nested class loading failure: " + e.getMessage(), e);
                }

                ClassDesigner tempDesigner;
                if (!_className.equals(tempInterfaceName)) {
                    Class tempInterfaceClass = this.loadClass(tempInterfaceName);
                    String tempClassName;
                    Class tempClass;
                    if (_className.endsWith(IMPL_BUILDER_SUFFIX)) {
                        tempClassName = this.getClassName(_className);
                        tempClass = this.loadClass(tempClassName);
                        tempDesigner = this.getImplBuilderDesigner(tempClass, tempInterfaceClass);
                        return this.defineClass(_className, tempDesigner);
                    }

                    tempClassName = tempInterfaceClass.getName();
                    tempStructAttrBeanTypeDef.addInterfaceTypeDef(StructAttributeBeanTypeDefImpl.getType(tempClassName));
                    tempDesigner = this.getClassDesigner(_className, tempStructAttrBeanTypeDef);
                    tempClass = this.defineClass(_className, tempDesigner);
                    ((ClassInitializer)tempDesigner).initClass(tempClass);
                    return tempClass;
                }

                tempStructAttrBeanTypeDef.addInterfaceTypeDef(StructTypeUtil.STRUCT_ATTRIBUTE_TYPE);
                tempDesigner = this.getInterfaceDesigner(_className, tempStructAttrBeanTypeDef);
                return this.defineClass(_className, tempDesigner);
            }
        }

        return super.findClass(_className);
    }

    protected abstract StructType getStructAttributeType(String _attrName);

    protected StructAttributeBeanTypeDef createStructAttributeBeanTypeDef(StructType _attrType) throws ClassNotFoundException {
        return new StructAttributeBeanTypeDefImpl(_attrType, this);
    }

    public String getInterfaceName(String _className) {
        return AbstractBeanClassLoader.getInterfaceName(_className);
    }

    protected String getClassName(String _className) {
        return AbstractBeanClassLoader.getClassName(_className);
    }

    public String getStructAttributeBuilderClassName(String _className) {
        return StructAttributeBeanTypeDefImpl.getStructAttributeBuilderClassName(_className);
    }

    public String getStructAttributeQualifiedTypeName(String _className) {
        return StructAttributeBeanTypeDefImpl.getStructAttributeQualifiedTypeName(_className);
    }

    protected ClassDesigner getClassDesigner(String _className, StructAttributeBeanTypeDef _typeDef) {
        return this.designerBuilder.createClassDesigner(_className, _typeDef);
    }

    protected ClassDesigner getInterfaceDesigner(String _className, StructAttributeBeanTypeDef _beanTypeDef) {
        return this.designerBuilder.createInterfaceDesigner(_className, _beanTypeDef);
    }

    protected ClassDesigner getImplBuilderDesigner(Class<?> _attrClassName, Class<?> _interfaceName) {
        return new StructAttributeImplBuilderDesigner(_attrClassName.getName(), _interfaceName.getName());
    }

    private static class BeanClassDesignerBuilder {
        BeanClassDesignerBuilder(Class<? extends Bean> _class) {
        }

        ClassDesigner createClassDesigner(String _className, StructAttributeBeanTypeDef _beanTypeDef) {
            return new StructAttributeClassDesigner(_className, AbstractStructAttribute.class.getName(), _beanTypeDef);
        }

        ClassDesigner createInterfaceDesigner(String _className, StructAttributeBeanTypeDef _beanTypeDef) {
            return new StructAttributeInterfaceDesigner(_className, _beanTypeDef);
        }
    }
}

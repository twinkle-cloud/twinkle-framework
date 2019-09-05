package com.twinkle.framework.core.asm.classloader;

import com.twinkle.framework.core.asm.designer.*;
import com.twinkle.framework.core.datastruct.Bean;
import com.twinkle.framework.core.datastruct.SimpleReflectiveBean;
import com.twinkle.framework.core.datastruct.define.StructAttributeBeanTypeDef;
import com.twinkle.framework.core.datastruct.define.StructAttributeBeanTypeDefImpl;
import com.twinkle.framework.core.error.StructAttributeException;
import com.twinkle.framework.core.lang.struct.AbstractStructAttribute;
import com.twinkle.framework.core.lang.struct.StructAttributeType;
import com.twinkle.framework.core.utils.TypeUtil;

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
    public static final String PACKAGE_PREFIX = "com.twinkle.framework.datastruct.beans.";
    public static final String IMPL_SUFFIX = "Impl";
    public static final String IMPL_BUILDER_SUFFIX = "$ImplBuilder";
    private final BeanClassDesignerBuilder _designerBuilder = new BeanClassDesignerBuilder(SimpleReflectiveBean.class);

    public AbstractStructAttributeClassLoader(ClassLoader _classLoader) {
        super(_classLoader);
    }

    public AbstractStructAttributeClassLoader() {
    }

    protected Class<?> findClass(String _className) throws ClassNotFoundException {
        if (_className != null && _className.startsWith(PACKAGE_PREFIX)) {
            String tempInterfaceName = this.getInterfaceName(_className);
            StructAttributeType tempStructAttrType = null;

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

                tempStructAttrBeanTypeDef.addInterfaceTypeDef(TypeUtil.STRUCT_ATTRIBUTE_TYPE);
                tempDesigner = this.getInterfaceDesigner(_className, tempStructAttrBeanTypeDef);
                return this.defineClass(_className, tempDesigner);
            }
        }

        return super.findClass(_className);
    }

    protected abstract StructAttributeType getStructAttributeType(String _attrName);

    protected StructAttributeBeanTypeDef createStructAttributeBeanTypeDef(StructAttributeType _attrType) throws ClassNotFoundException {
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

    protected ClassDesigner getClassDesigner(String var1, StructAttributeBeanTypeDef var2) {
        return this._designerBuilder.createClassDesigner(var1, var2);
    }

    protected ClassDesigner getInterfaceDesigner(String var1, StructAttributeBeanTypeDef var2) {
        return this._designerBuilder.createInterfaceDesigner(var1, var2);
    }

    protected ClassDesigner getImplBuilderDesigner(Class var1, Class var2) {
        return new StructAttributeImplBuilderDesigner(var1.getName(), var2.getName());
    }

    private static class BeanClassDesignerBuilder {
        BeanClassDesignerBuilder(Class<? extends Bean> var1) {
        }

        ClassDesigner createClassDesigner(String var1, StructAttributeBeanTypeDef var2) {
            return new StructAttributeClassDesigner(var1, AbstractStructAttribute.class.getName(), var2);
        }

        ClassDesigner createInterfaceDesigner(String var1, StructAttributeBeanTypeDef var2) {
            return new StructAttributeInterfaceDesigner(var1, var2);
        }
    }
}

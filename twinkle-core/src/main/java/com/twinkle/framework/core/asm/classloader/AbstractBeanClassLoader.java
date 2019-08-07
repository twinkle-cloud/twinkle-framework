package com.twinkle.framework.core.asm.classloader;

import com.twinkle.framework.core.asm.designer.BeanImplBuilderDesigner;
import com.twinkle.framework.core.asm.designer.ClassDesigner;
import com.twinkle.framework.core.asm.designer.EnumClassDesigner;
import com.twinkle.framework.core.datastruct.Bean;
import com.twinkle.framework.core.datastruct.RecyclableBean;
import com.twinkle.framework.core.datastruct.ReflectiveBean;
import com.twinkle.framework.core.datastruct.builder.TypeDefBuilder;
import com.twinkle.framework.core.datastruct.descriptor.BeanTypeDescriptor;
import com.twinkle.framework.core.datastruct.descriptor.EnumTypeDescriptor;
import com.twinkle.framework.core.datastruct.descriptor.TypeDescriptor;
import com.twinkle.framework.core.datastruct.schema.*;

import java.util.List;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-02 21:18<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public abstract class AbstractBeanClassLoader extends EnhancedClassLoader {
    public static final String PACKAGE_PREFIX = "com.twinkle.framework.core.datastruct.beans.";
    public static final String IMPL_SUFFIX = "Impl";
    public static final String IMPL_BUILDER_SUFFIX = "$ImplBuilder";
    public static final String IMPL_IMPL_BUILDER_SUFFIX = "Impl$ImplBuilder";
    private final AbstractBeanClassLoader.BeanClassDesignerBuilder _designerBuilder;

    protected AbstractBeanClassLoader(ClassLoader _classLoader, Class<? extends Bean> _class) {
        super(_classLoader);
        this._designerBuilder = new AbstractBeanClassLoader.BeanClassDesignerBuilder(_class);
    }

    protected AbstractBeanClassLoader(ClassLoader _classLoader) {
        super(_classLoader);
        this._designerBuilder = new AbstractBeanClassLoader.BeanClassDesignerBuilder(Bean.class);
    }

    protected AbstractBeanClassLoader() {
        this._designerBuilder = new AbstractBeanClassLoader.BeanClassDesignerBuilder(Bean.class);
    }
    @Override
    protected Class<?> findClass(String _className) throws ClassNotFoundException {
        if (_className != null && _className.startsWith(PACKAGE_PREFIX) && !_className.endsWith("[]")) {
            String tempInferfaceName = getInterfaceName(_className);
            TypeDescriptor tempInterfaceDescriptor = this.getTypeDescriptor(tempInferfaceName);
            if (tempInterfaceDescriptor != null) {
                TypeDef tempInterfaceTypeDef;
                try {
                    tempInterfaceTypeDef = this.createTypeDef(tempInterfaceDescriptor);
                } catch (ClassNotFoundException e) {
                    throw new ClassNotFoundException("Failed to load class " + _className + " due to the nested class loading failure: " + e.getMessage(), e);
                }

                ClassDesigner tempInterfaceClassDesigner;
                if (tempInterfaceTypeDef.isEnum()) {
                    tempInterfaceClassDesigner = this.getEnumDesigner(tempInferfaceName, (EnumTypeDef)tempInterfaceTypeDef);
                } else {
                    if (!tempInterfaceTypeDef.isBean()) {
                        throw new ClassNotFoundException("Cannot generate class for descriptor: " + tempInterfaceDescriptor.getName() + " of type " + tempInterfaceDescriptor.getClass().getName());
                    }

                    BeanTypeDef tempInterfaceBeanTypeDef = (BeanTypeDef)tempInterfaceTypeDef;
                    if (!_className.equals(tempInferfaceName)) {
                        Class tempInterfaceClass = this.loadClass(tempInferfaceName);
                        String tempClassName;
                        if (_className.endsWith(IMPL_BUILDER_SUFFIX)) {
                            tempClassName = getClassName(_className);
                            Class tempClass = this.loadClass(tempClassName);
                            tempInterfaceClassDesigner = this.getImplBuilderDesigner(tempClass, tempInterfaceClass);
                        } else {
                            tempClassName = tempInterfaceClass.getName();
                            tempInterfaceBeanTypeDef.addParent(TypeDefBuilder.getObjectType(tempClassName));
                            tempInterfaceClassDesigner = this.getClassDesigner(_className, tempInterfaceBeanTypeDef);
                        }
                    } else {
                        tempInterfaceClassDesigner = this.getInterfaceDesigner(_className, tempInterfaceBeanTypeDef);
                    }
                }

                return this.defineClass(_className, tempInterfaceClassDesigner);
            }
        }

        return super.findClass(_className);
    }

    protected abstract TypeDescriptor getTypeDescriptor(String var1);

    protected TypeDef createTypeDef(TypeDescriptor _descriptor) throws ClassNotFoundException {
        if (_descriptor instanceof BeanTypeDescriptor) {
            return new BeanTypeDefImpl((BeanTypeDescriptor)_descriptor, this);
        } else if (_descriptor instanceof EnumTypeDescriptor) {
            return new EnumTypeDefImpl((EnumTypeDescriptor)_descriptor, this);
        } else {
            throw new IllegalArgumentException(_descriptor.getClassName() + " is not a proper type to generate Java class");
        }
    }

    public static String getClassName(String _className) {
        if (_className.endsWith(IMPL_SUFFIX)) {
            return _className;
        } else {
            return _className.endsWith(IMPL_BUILDER_SUFFIX) ? _className.substring(0, _className.length() - IMPL_BUILDER_SUFFIX.length()) : _className + IMPL_SUFFIX;
        }
    }

    public static String getInterfaceName(String _className) {
        if (_className.endsWith(IMPL_SUFFIX)) {
            return _className.substring(0, _className.length() - IMPL_SUFFIX.length());
        } else {
            return _className.endsWith(IMPL_IMPL_BUILDER_SUFFIX) ? _className.substring(0, _className.length() - IMPL_IMPL_BUILDER_SUFFIX.length()) : _className;
        }
    }

    public static String getImplBuilderName(String _className) {
        if (_className.endsWith(IMPL_BUILDER_SUFFIX)) {
            return _className;
        } else {
            return _className.endsWith(IMPL_SUFFIX) ? _className + IMPL_BUILDER_SUFFIX : _className + IMPL_IMPL_BUILDER_SUFFIX;
        }
    }

    protected ClassDesigner getClassDesigner(String _className, BeanTypeDef _typeDef) {
        return this._designerBuilder.createClassDesigner(_className, _typeDef);
    }

    protected ClassDesigner getInterfaceDesigner(String _className, BeanTypeDef _typeDef) {
        return this._designerBuilder.createInterfaceDesigner(_className, _typeDef);
    }

    protected ClassDesigner getImplBuilderDesigner(Class var1, Class _class) {
        return new BeanImplBuilderDesigner(var1.getName(), _class.getName());
    }

    protected ClassDesigner getEnumDesigner(String _className, EnumTypeDef _typeDef) {
        return new EnumClassDesigner(_typeDef);
    }

    private static class BeanClassDesignerBuilder {
        private static final String RECYCLABLE_BEAN_CLASS_NAME = RecyclableBean.class.getName();
        private static final String REFLECTIVE_BEAN_CLASS_NAME = ReflectiveBean.class.getName();
        private final AbstractBeanClassLoader.BeanClassDesignerBuilder.BeanInterfaceType defaultBeanInterfaceType;

        BeanClassDesignerBuilder(Class<? extends Bean> _class) {
            if (ReflectiveBean.class.isAssignableFrom(_class)) {
                this.defaultBeanInterfaceType = AbstractBeanClassLoader.BeanClassDesignerBuilder.BeanInterfaceType.REFLECTIVE;
            } else if (RecyclableBean.class.isAssignableFrom(_class)) {
                this.defaultBeanInterfaceType = AbstractBeanClassLoader.BeanClassDesignerBuilder.BeanInterfaceType.RECYCLABLE;
            } else {
                this.defaultBeanInterfaceType = AbstractBeanClassLoader.BeanClassDesignerBuilder.BeanInterfaceType.GENERIC;
            }

        }

        ClassDesigner createClassDesigner(String _className, BeanTypeDef _typeDef) {
            List<String> var4 = _typeDef.getInterfaces();
            if (var4.contains(REFLECTIVE_BEAN_CLASS_NAME)) {
                return new RecyclableBeanClassDesigner(_className, _typeDef);
            } else if (var4.contains(RECYCLABLE_BEAN_CLASS_NAME)) {
                return new RecyclableBeanClassDesigner(_className, _typeDef);
            } else {
                switch(this.defaultBeanInterfaceType) {
                    case REFLECTIVE:
                        return new RecyclableBeanClassDesigner(_className, _typeDef);
                    case RECYCLABLE:
                        return new RecyclableBeanClassDesigner(_className, _typeDef);
                    case GENERIC:
                    default:
                        return new BeanClassDesigner(_className, _typeDef);
                }
            }
        }

        ClassDesigner createInterfaceDesigner(String _className, BeanTypeDef _typeDef) {
            List<String> tempInterfaceNameList = _typeDef.getInterfaces();
            if (tempInterfaceNameList.contains(RECYCLABLE_BEAN_CLASS_NAME)) {
                return new RecyclableBeanInterfaceDesigner(_className, _typeDef);
            } else {
                switch(this.defaultBeanInterfaceType) {
                    case REFLECTIVE:
                    case RECYCLABLE:
                        return new RecyclableBeanInterfaceDesigner(_className, _typeDef);
                    case GENERIC:
                    default:
                        return new BeanInterfaceDesigner(_className, _typeDef);
                }
            }
        }

        private static enum BeanInterfaceType {
            GENERIC,
            RECYCLABLE,
            REFLECTIVE;
            private BeanInterfaceType() {
            }
        }
    }
}

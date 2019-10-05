package com.twinkle.framework.asm.classloader;

import com.twinkle.framework.asm.designer.*;
import com.twinkle.framework.asm.Bean;
import com.twinkle.framework.asm.RecyclableBean;
import com.twinkle.framework.asm.ReflectiveBean;
import com.twinkle.framework.asm.builder.TypeDefBuilder;
import com.twinkle.framework.asm.define.*;
import com.twinkle.framework.asm.descriptor.BeanTypeDescriptor;
import com.twinkle.framework.asm.descriptor.EnumTypeDescriptor;
import com.twinkle.framework.asm.descriptor.TypeDescriptor;
import com.twinkle.framework.asm.utils.BeanUtil;

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
    private final BeanClassDesignerBuilder designerBuilder;

    protected AbstractBeanClassLoader(ClassLoader _classLoader, Class<? extends Bean> _class) {
        super(_classLoader);
        this.designerBuilder = new BeanClassDesignerBuilder(_class);
    }

    protected AbstractBeanClassLoader(ClassLoader _classLoader) {
        super(_classLoader);
        this.designerBuilder = new BeanClassDesignerBuilder(Bean.class);
    }

    protected AbstractBeanClassLoader() {
        this.designerBuilder = new BeanClassDesignerBuilder(Bean.class);
    }
    @Override
    protected Class<?> findClass(String _className) throws ClassNotFoundException {
        if (_className != null && _className.startsWith(Bean.DEFAULT_PACKAGE) && !_className.endsWith("[]")) {
            String tempInterfaceName = BeanUtil.getInterfaceName(_className);
            TypeDescriptor tempInterfaceDescriptor = this.getTypeDescriptor(tempInterfaceName);
            if (tempInterfaceDescriptor != null) {
                TypeDef tempInterfaceTypeDef;
                try {
                    tempInterfaceTypeDef = this.createTypeDef(tempInterfaceDescriptor);
                } catch (ClassNotFoundException e) {
                    throw new ClassNotFoundException("Failed to load class " + _className + " due to the nested class loading failure: " + e.getMessage(), e);
                }

                ClassDesigner tempInterfaceClassDesigner;
                if (tempInterfaceTypeDef.isEnum()) {
                    tempInterfaceClassDesigner = this.getEnumDesigner(tempInterfaceName, (EnumTypeDef)tempInterfaceTypeDef);
                } else {
                    if (!tempInterfaceTypeDef.isBean()) {
                        throw new ClassNotFoundException("Cannot generate class for descriptor: " + tempInterfaceDescriptor.getName() + " of type " + tempInterfaceDescriptor.getClass().getName());
                    }

                    BeanTypeDef tempInterfaceBeanTypeDef = (BeanTypeDef)tempInterfaceTypeDef;
                    if (!_className.equals(tempInterfaceName)) {
                        Class tempInterfaceClass = this.loadClass(tempInterfaceName);
                        String tempClassName;
                        if (_className.endsWith(Bean.IMPL_BUILDER_SUFFIX)) {
                            tempClassName = BeanUtil.getClassName(_className);
                            Class tempClass = this.loadClass(tempClassName);
                            tempInterfaceClassDesigner = this.getImplBuilderDesigner(tempClass, tempInterfaceClass);
                        } else {
                            tempClassName = tempInterfaceClass.getName();
                            tempInterfaceBeanTypeDef.addInterfaceTypeDef(TypeDefBuilder.getObjectType(tempClassName));
                            tempInterfaceClassDesigner = this.getClassDesigner(_className, tempInterfaceBeanTypeDef);
                        }
                    } else {
                        tempInterfaceClassDesigner = this.getInterfaceDesigner(_className, tempInterfaceBeanTypeDef);
                    }
                }
                Class<?> tempClass = this.defineClass(_className, tempInterfaceClassDesigner);
                return tempClass;
            }
        }
        Class<?> tempClass = super.findClass(_className);
        return tempClass;
    }

    /**
     * Get the Type descriptor with the given interface name.
     *
     * @param _className
     * @return
     */
    protected abstract TypeDescriptor getTypeDescriptor(String _className);

    /**
     * Create Type Definition with the given TypeDescriptor.
     *
     * @param _descriptor
     * @return
     * @throws ClassNotFoundException
     */
    protected TypeDef createTypeDef(TypeDescriptor _descriptor) throws ClassNotFoundException {
        if (_descriptor instanceof BeanTypeDescriptor) {
            return new BeanTypeDefImpl((BeanTypeDescriptor)_descriptor, this);
        } else if (_descriptor instanceof EnumTypeDescriptor) {
            return new EnumTypeDefImpl((EnumTypeDescriptor)_descriptor, this);
        } else {
            throw new IllegalArgumentException(_descriptor.getClassName() + " is not a proper type to generate Java class");
        }
    }

    /**
     * Get bean's class designer.
     *
     * @param _className
     * @param _typeDef
     * @return
     */
    protected ClassDesigner getClassDesigner(String _className, BeanTypeDef _typeDef) {
        return this.designerBuilder.createClassDesigner(_className, _typeDef);
    }

    /**
     * Get Bean's Interfaces designer.
     *
     * @param _className
     * @param _typeDef
     * @return
     */
    protected ClassDesigner getInterfaceDesigner(String _className, BeanTypeDef _typeDef) {
        return this.designerBuilder.createInterfaceDesigner(_className, _typeDef);
    }

    /**
     * Get Bean's implementation builder class designer with given class name and interface name.
     *
     * @param _class
     * @param _interfaceClass
     * @return
     */
    protected ClassDesigner getImplBuilderDesigner(Class _class, Class _interfaceClass) {
        return new BeanImplBuilderDesigner(_class.getName(), _interfaceClass.getName());
    }

    /**
     * Build an enum class designer.
     *
     * @param _className
     * @param _typeDef
     * @return
     */
    protected ClassDesigner getEnumDesigner(String _className, EnumTypeDef _typeDef) {
        return new EnumClassDesigner(_typeDef);
    }

    private static class BeanClassDesignerBuilder {
        private static final String RECYCLABLE_BEAN_CLASS_NAME = RecyclableBean.class.getName();
        private static final String REFLECTIVE_BEAN_CLASS_NAME = ReflectiveBean.class.getName();
        private final BeanInterfaceType defaultBeanInterfaceType;

        BeanClassDesignerBuilder(Class<? extends Bean> _class) {
            if (ReflectiveBean.class.isAssignableFrom(_class)) {
                this.defaultBeanInterfaceType = BeanInterfaceType.REFLECTIVE;
            } else if (RecyclableBean.class.isAssignableFrom(_class)) {
                this.defaultBeanInterfaceType = BeanInterfaceType.RECYCLABLE;
            } else {
                this.defaultBeanInterfaceType = BeanInterfaceType.GENERIC;
            }
        }

        /**
         * Build an class designer with given class name and class definition.
         *
         * @param _className
         * @param _typeDef
         * @return
         */
        ClassDesigner createClassDesigner(String _className, BeanTypeDef _typeDef) {
            List<String> tempInterfaceList = _typeDef.getInterfaces();
            if (tempInterfaceList.contains(REFLECTIVE_BEAN_CLASS_NAME)) {
                return new ReflectiveBeanClassDesigner(_className, _typeDef);
            } else if (tempInterfaceList.contains(RECYCLABLE_BEAN_CLASS_NAME)) {
                return new RecyclableBeanClassDesigner(_className, _typeDef);
            } else {
                switch(this.defaultBeanInterfaceType) {
                    case REFLECTIVE:
                        return new ReflectiveBeanClassDesigner(_className, _typeDef);
                    case RECYCLABLE:
                        return new RecyclableBeanClassDesigner(_className, _typeDef);
                    case GENERIC:
                    default:
                        return new GeneralBeanClassDesigner(_className, _typeDef);
                }
            }
        }

        /**
         * Build an interface designer with given interface name and interface definition.
         *
         * @param _className
         * @param _typeDef
         * @return
         */
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

        /**
         * Bean Interface Type.
         *
         */
        private static enum BeanInterfaceType {
            GENERIC,
            RECYCLABLE,
            REFLECTIVE;
            private BeanInterfaceType() {
            }
        }
    }
}

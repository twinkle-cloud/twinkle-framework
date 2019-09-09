package com.twinkle.framework.asm.factory;

import com.twinkle.framework.asm.classloader.BeanClassLoader;
import com.twinkle.framework.asm.Bean;
import com.twinkle.framework.asm.descriptor.TypeDescriptors;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-11 14:38<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public abstract class AbstractBeanFactory implements BeanFactory {
    public static final String LEGACY_TYPE_SEPARATOR = ":";
    public static final String TYPE_SEPARATOR = ".";
    private BeanClassLoader beanClassLoader;

    public AbstractBeanFactory(BeanClassLoader _classLoader) {
        this.beanClassLoader = _classLoader;
    }

    public AbstractBeanFactory(ClassLoader _classLoader, TypeDescriptors _typeDescriptors, Class<? extends Bean> _class) {
        this.beanClassLoader = this.initBeanClassLoader(_classLoader, _typeDescriptors, _class);
    }

    public AbstractBeanFactory(ClassLoader _classLoader, TypeDescriptors _typeDescriptors) {
        this.beanClassLoader = this.initBeanClassLoader(_classLoader, _typeDescriptors);
    }

    public AbstractBeanFactory(TypeDescriptors _typeDescriptors) {
        this.beanClassLoader = this.initBeanClassLoader(_typeDescriptors);
    }

    protected void setBeanClassLoader(BeanClassLoader _classLoader) {
        this.beanClassLoader = _classLoader;
    }

    protected BeanClassLoader initBeanClassLoader(ClassLoader _classLoader, TypeDescriptors _typeDescriptors, Class<? extends Bean> _class) {
        return new BeanClassLoader(_classLoader, _typeDescriptors, _class);
    }

    protected BeanClassLoader initBeanClassLoader(ClassLoader _classLoader, TypeDescriptors _typeDescriptors) {
        return new BeanClassLoader(_classLoader, _typeDescriptors);
    }

    protected BeanClassLoader initBeanClassLoader(TypeDescriptors _typeDescriptors) {
        return new BeanClassLoader(_typeDescriptors);
    }
    @Override
    public abstract <T extends Bean> T newInstance(String _className);
    @Override
    public abstract <T extends Bean> T newInstance(Class<T> _class);
    @Override
    public abstract <T extends Bean> T[] newArray(String _className, int _length);
    @Override
    public abstract <T extends Bean> T[] newArray(Class<T> _class, int _length);
    @Override
    public <T extends Bean> String getBeanType(Class<T> _class) {
        return classNameToTypeName(_class.getName());
    }
    @Override
    public <T extends Bean> Class<T> getBeanInterface(String _interfaceName) {
        return this.loadBeanInterfaceByBeanType(_interfaceName);
    }
    @Override
    public <T extends Bean> Class<T> getBeanClass(String _className) {
        return this.loadBeanClassByBeanType(_className);
    }

    public <E extends Enum> Class<E> getEnumClass(String _enumTypeName) {
        return this.loadEnumClassByEnumType(_enumTypeName);
    }
    @Override
    public BeanClassLoader getBeanClassLoader() {
        return this.beanClassLoader;
    }
    @Override
    public TypeDescriptors getTypeDescriptors() {
        return this.beanClassLoader.getTypeDescriptors();
    }

    protected <T extends Bean> Class<T> loadBeanInterfaceByBeanType(String _typeName) {
        String tempInterfaceName = typeNameToInterfaceName(_typeName);

        try {
            return (Class<T>) this.getBeanClassLoader().loadClass(tempInterfaceName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    protected <T extends Bean> Class<T> loadBeanClassByBeanType(String _typeName) {
        String tempClassName = typeNameToClassName(_typeName);

        try {
            return (Class<T>) this.getBeanClassLoader().loadClass(tempClassName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    protected <E extends Enum> Class<E> loadEnumClassByEnumType(String _enumTypeName) {
        String tempInterfaceName = typeNameToInterfaceName(_enumTypeName);

        try {
            return (Class<E>) this.getBeanClassLoader().loadClass(tempInterfaceName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    protected static String classNameToTypeName(String _className) {
        String tempInterfaceName = BeanClassLoader.getInterfaceName(_className);
        if (!tempInterfaceName.startsWith(Bean.DEFAULT_PACKAGE)) {
            throw new IllegalArgumentException("Bean class '" + _className + "' does not belong to the package " + Bean.DEFAULT_PACKAGE);
        } else {
            int tempPackageLength = Bean.DEFAULT_PACKAGE.length();
            return tempInterfaceName.substring(tempPackageLength, tempInterfaceName.length() - tempPackageLength);
        }
    }

    public static String typeNameToInterfaceName(String _typeName) {
        return _typeName.startsWith(Bean.DEFAULT_PACKAGE) ? BeanClassLoader.getInterfaceName(_typeName) : Bean.DEFAULT_PACKAGE + _typeName.replaceAll(LEGACY_TYPE_SEPARATOR, TYPE_SEPARATOR);
    }

    public static String typeNameToClassName(String _typeName) {
        return _typeName.startsWith(Bean.DEFAULT_PACKAGE) ? BeanClassLoader.getClassName(_typeName) : Bean.DEFAULT_PACKAGE + _typeName.replaceAll(LEGACY_TYPE_SEPARATOR, TYPE_SEPARATOR) + "Impl";
    }
}

package com.twinkle.framework.core.asm.factory;

import com.twinkle.framework.core.asm.classloader.BeanClassLoader;
import com.twinkle.framework.core.datastruct.Bean;
import com.twinkle.framework.core.datastruct.descriptor.TypeDescriptors;
import com.twinkle.framework.core.datastruct.serialize.SerializerFactory;

import java.lang.reflect.Array;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-11 14:52<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class BeanFactoryReflectionImpl extends AbstractBeanFactory {
    public BeanFactoryReflectionImpl(BeanClassLoader _classLoader) {
        super(_classLoader);
    }

    public BeanFactoryReflectionImpl(ClassLoader _classLoader, TypeDescriptors _typeDescriptors) {
        super(_classLoader, _typeDescriptors);
    }

    public BeanFactoryReflectionImpl(TypeDescriptors _typeDescriptors) {
        super(_typeDescriptors);
    }

    @Override
    public <T extends Bean> T newInstance(String _className) {
        Class tempClass = this.loadBeanClassByBeanType(_className);
        return (T) this.createNewInstance(tempClass);
    }

    @Override
    public <T extends Bean> T newInstance(Class<T> _class) {
        String tempClassName = _class.getName();
        return tempClassName.endsWith("Impl") ? this.createNewInstance(_class) : this.newInstance(tempClassName);
    }

    @Override
    public <T extends Bean> T[] newArray(String _className, int _length) {
        Class tempClass = this.loadBeanClassByBeanType(_className);
        return (T[]) this.createNewArray(tempClass, _length);
    }

    @Override
    public <T extends Bean> T[] newArray(Class<T> _class, int _length) {
        String tempClassName = _class.getName();
        return tempClassName.endsWith("Impl") ? this.createNewArray(_class, _length) : this.newArray(tempClassName, _length);
    }

    protected <T extends Bean> T createNewInstance(Class<T> _class) {
        try {
            return _class.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    protected <T extends Bean> T[] createNewArray(Class<T> _class, int _length) {
        return (T[])Array.newInstance(_class, _length);
    }

    @Override
    public SerializerFactory getSerializerFactory(String _factoryName) {
        throw new UnsupportedOperationException("getSerializerFactory");
    }
    @Override
    public SerializerFactory getDefaultSerializerFactory() {
        throw new UnsupportedOperationException("getDefaultSerializerFactory");
    }
}

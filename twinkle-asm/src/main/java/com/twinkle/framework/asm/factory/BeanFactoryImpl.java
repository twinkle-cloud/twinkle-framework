package com.twinkle.framework.asm.factory;

import com.twinkle.framework.asm.classloader.BeanClassLoader;
import com.twinkle.framework.asm.Bean;
import com.twinkle.framework.asm.builder.BeanImplBuilder;
import com.twinkle.framework.asm.descriptor.TypeDescriptors;
import com.twinkle.framework.asm.serialize.SerializerFactory;
import com.twinkle.framework.asm.utils.BeanUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-11 14:48<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class BeanFactoryImpl extends AbstractBeanFactory {
    private final Map<String, BeanImplBuilder> builders = new ConcurrentHashMap();

    public BeanFactoryImpl(BeanClassLoader _classLoader) {
        super(_classLoader);
    }

    public BeanFactoryImpl(ClassLoader _classLoader, TypeDescriptors _typeDescriptors, Class<? extends Bean> _class) {
        super(_classLoader, _typeDescriptors, _class);
    }

    public BeanFactoryImpl(ClassLoader _classLoader, TypeDescriptors _typeDescriptors) {
        super(_classLoader, _typeDescriptors);
    }

    public BeanFactoryImpl(TypeDescriptors _typeDescriptors) {
        super(_typeDescriptors);
    }

    @Override
    public <T extends Bean> T newInstance(String _className) {
        BeanImplBuilder<T> tempBuilder = (BeanImplBuilder)this.builders.get(_className);
        if (tempBuilder == null) {
            tempBuilder = this.loadBeanImplBuilderByBeanType(_className);
            this.builders.put(_className, tempBuilder);
        }

        return tempBuilder.newInstance();
    }

    @Override
    public <T extends Bean> T newInstance(Class<T> _class) {
        String tempClassName = _class.getName();
        BeanImplBuilder<T> tempBuilder = this.builders.get(tempClassName);
        if (tempBuilder == null) {
            tempBuilder = this.loadBeanImplBuilderByBeanClass(tempClassName);
            this.builders.put(tempClassName, tempBuilder);
        }

        return tempBuilder.newInstance();
    }

    @Override
    public <T extends Bean> T[] newArray(String _className, int _length) {
        BeanImplBuilder<T> tempBuilder = this.builders.get(_className);
        if (tempBuilder == null) {
            tempBuilder = this.loadBeanImplBuilderByBeanType(_className);
            this.builders.put(_className, tempBuilder);
        }

        return tempBuilder.newArray(_length);
    }

    @Override
    public <T extends Bean> T[] newArray(Class<T> _class, int _length) {
        String tempClassName = _class.getName();
        BeanImplBuilder<T> tempBuilder = this.builders.get(tempClassName);
        if (tempBuilder == null) {
            tempBuilder = this.loadBeanImplBuilderByBeanClass(tempClassName);
            this.builders.put(tempClassName, tempBuilder);
        }

        return tempBuilder.newArray(_length);
    }

    protected <T extends Bean> BeanImplBuilder<T> loadBeanImplBuilderByBeanType(String _beanType) {
        Class tempBeanClass = this.loadBeanImplBuilderClassByBeanType(_beanType);

        try {
            return (BeanImplBuilder)tempBeanClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    protected <T extends Bean> BeanImplBuilder<T> loadBeanImplBuilderByBeanClass(String _beanName) {
        Class tempBeanClass = this.loadBeanImplBuilderClassByBeanClass(_beanName);

        try {
            return (BeanImplBuilder)tempBeanClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    protected <T extends Bean> Class<? extends BeanImplBuilder<T>> loadBeanImplBuilderClassByBeanType(String _beanTypeName) {
        String tempBuilderName = BeanUtil.typeNameToImplBuilderClassName(_beanTypeName);

        try {
            return (Class<? extends BeanImplBuilder<T>>) this.getBeanClassLoader().loadClass(tempBuilderName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    protected <T extends Bean> Class<? extends BeanImplBuilder<T>> loadBeanImplBuilderClassByBeanClass(String _beanName) {
        String tempBuilderName = BeanUtil.getImplBuilderName(_beanName);

        try {
            return (Class<? extends BeanImplBuilder<T>>) this.getBeanClassLoader().loadClass(tempBuilderName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
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

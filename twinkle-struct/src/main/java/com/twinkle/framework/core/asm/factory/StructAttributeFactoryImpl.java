package com.twinkle.framework.core.asm.factory;

import com.twinkle.framework.core.asm.classloader.StructAttributeClassLoader;
import com.twinkle.framework.core.datastruct.Bean;
import com.twinkle.framework.core.datastruct.BeanFactory;
import com.twinkle.framework.core.datastruct.builder.StructAttributeImplBuilder;
import com.twinkle.framework.core.datastruct.serialize.SerializerFactory;
import com.twinkle.framework.core.datastruct.serialize.SerializerFactoryRegistry;
import com.twinkle.framework.core.error.StructAttributeException;
import com.twinkle.framework.core.error.StructAttributeInstantiationException;
import com.twinkle.framework.core.lang.resolver.StructAttributeTypeResolver;
import com.twinkle.framework.core.lang.struct.AbstractStructAttributeFactory;
import com.twinkle.framework.core.lang.struct.StructAttribute;
import com.twinkle.framework.core.lang.struct.StructAttributeType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/2/19 6:59 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class StructAttributeFactoryImpl extends AbstractStructAttributeFactory implements BeanFactory {
    private final Lock readLock;
    private final Lock writeLock;
    private final Map<String, StructAttributeImplBuilder> builders;
    private SerializerFactoryRegistry serializerFactoryRegistry;

    public StructAttributeFactoryImpl(StructAttributeTypeResolver _resolver, ClassLoader _classLoader) {
        super(_resolver, _classLoader);
        this.builders = new ConcurrentHashMap();
        ReentrantReadWriteLock tempLock = new ReentrantReadWriteLock();
        this.readLock = tempLock.readLock();
        this.writeLock = tempLock.readLock();
    }

    public StructAttributeFactoryImpl(StructAttributeTypeResolver _resolver, ClassLoader _classLoader, SerializerFactoryRegistry _registry) {
        super(_resolver, _classLoader);
        this.serializerFactoryRegistry = _registry;
        this.builders = new ConcurrentHashMap();
        ReentrantReadWriteLock tempLock = new ReentrantReadWriteLock();
        this.readLock = tempLock.readLock();
        this.writeLock = tempLock.readLock();
    }

    protected SerializerFactoryRegistry getSerializerFactoryRegistrar() {
        return this.serializerFactoryRegistry;
    }

    @Override
    public StructAttribute newStructAttribute(StructAttributeType _saType) throws StructAttributeException {
        String tempName = _saType.getQualifiedName();
        StructAttributeImplBuilder tempBuilder = this.getStructAttributeImplBuilder(tempName);
        return (StructAttribute) tempBuilder.newInstance();
    }

    @Override
    public <T extends Bean> T newInstance(String _className) {
        StructAttributeImplBuilder tempBuilder = this.getStructAttributeImplBuilder(_className);
        return (T) tempBuilder.newInstance();
    }

    @Override
    public <T extends Bean> T newInstance(Class<T> _class) {
        StructAttributeClassLoader tempClassLoader = this.getClassLoader();
        String tempInterfaceName = tempClassLoader.getInterfaceName(_class.getName());
        String tempQualifiedName = tempClassLoader.getStructAttributeQualifiedTypeName(tempInterfaceName);
        StructAttributeImplBuilder tempBuilder = this.getStructAttributeImplBuilder(tempQualifiedName);
        return (T) tempBuilder.newInstance();
    }

    @Override
    public <T extends Bean> T[] newArray(String _className, int _length) {
        StructAttributeImplBuilder tempBuilder = this.getStructAttributeImplBuilder(_className);
        return (T[]) tempBuilder.newArray(_length);
    }

    @Override
    public <T extends Bean> T[] newArray(Class<T> _class, int _length) {
        StructAttributeClassLoader tempClassLoader = this.getClassLoader();
        String tempInterfaceName = tempClassLoader.getInterfaceName(_class.getName());
        String tempQualifiedName = tempClassLoader.getStructAttributeQualifiedTypeName(tempInterfaceName);
        StructAttributeImplBuilder tempBuilder = this.getStructAttributeImplBuilder(tempQualifiedName);
        return (T[]) tempBuilder.newArray(_length);
    }

    @Override
    public <T extends Bean> String getBeanType(Class<T> _class) {
        StructAttributeClassLoader tempClassLoader = this.getClassLoader();
        String tempInterfaceName = tempClassLoader.getInterfaceName(_class.getName());
        return tempClassLoader.getStructAttributeQualifiedTypeName(tempInterfaceName);
    }

    @Override
    public <T extends Bean> Class<T> getBeanInterface(String _interfaceName) {
        return this.loadStructAttributeInterfaceByStructAttributeType(_interfaceName);
    }

    @Override
    public <T extends Bean> Class<T> getBeanClass(String _className) {
        return (Class<T>) this.loadStructAttributeClassByStructAttributeType(_className);
    }

    @Override
    public SerializerFactory getSerializerFactory(String _formatName) {
        if (this.serializerFactoryRegistry == null) {
            throw new IllegalStateException("SerializerFactory registry not initialized");
        } else {
            return this.serializerFactoryRegistry.getSerializerFactory(_formatName);
        }
    }

    @Override
    public SerializerFactory getDefaultSerializerFactory() {
        if (this.serializerFactoryRegistry == null) {
            throw new IllegalStateException("SerializerFactory registry not initialized");
        } else {
            return this.serializerFactoryRegistry.getDefaultSerializerFactory();
        }
    }

    /**
     * Load struct attribute's interface class by its' type name.
     *
     * @param _typeName
     * @return
     * @throws StructAttributeInstantiationException
     */
    private Class loadStructAttributeInterfaceByStructAttributeType(String _typeName) throws StructAttributeInstantiationException {
        String tempInterfaceName = AbstractBeanFactory.typeNameToInterfaceName(_typeName);
        try {
            return this.getClassLoader().loadClass(tempInterfaceName);
        } catch (ClassNotFoundException e) {
            throw new StructAttributeInstantiationException(e);
        }
    }

    /**
     * Load struct attribute's class by its' type name.
     *
     * @param _typeName
     * @return
     * @throws StructAttributeInstantiationException
     */
    private Class<?> loadStructAttributeClassByStructAttributeType(String _typeName) throws StructAttributeInstantiationException {
        String tempClassName = AbstractBeanFactory.typeNameToClassName(_typeName);
        try {
            return this.getClassLoader().loadClass(tempClassName);
        } catch (ClassNotFoundException e) {
            throw new StructAttributeInstantiationException(e);
        }
    }

    @Override
    protected StructAttributeImplBuilder getStructAttributeImplBuilder(String _className) throws StructAttributeInstantiationException {
        this.readLock.lock();

        StructAttributeImplBuilder tempImplBuilder;
        try {
            tempImplBuilder = this.builders.get(_className);
        } finally {
            this.readLock.unlock();
        }
        //Build one builder instance if the builder is not found.
        if (tempImplBuilder == null) {
            tempImplBuilder = this.loadStructAttributeImplBuilder(_className);
            this.writeLock.lock();
            try {
                this.builders.put(_className, tempImplBuilder);
            } finally {
                this.writeLock.unlock();
            }
        }

        return tempImplBuilder;
    }

    private <T extends Bean> StructAttributeImplBuilder<T> loadStructAttributeImplBuilder(String _className) throws StructAttributeInstantiationException {
        Class<? extends StructAttributeImplBuilder<T>> tempBuilderClass = this.loadStructAttributeImplBuilderClass(_className);

        try {
            return tempBuilderClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new StructAttributeInstantiationException(e);
        }
    }

    private <T extends Bean> Class<? extends StructAttributeImplBuilder<T>> loadStructAttributeImplBuilderClass(String _className) throws StructAttributeInstantiationException {
        StructAttributeClassLoader tempClassLoader = this.getClassLoader();
        String tempSABuilderClassName = tempClassLoader.getStructAttributeBuilderClassName(_className);

        try {
            return (Class<? extends StructAttributeImplBuilder<T>>) tempClassLoader.loadClass(tempSABuilderClassName);
        } catch (ClassNotFoundException e) {
            throw new StructAttributeInstantiationException(e);
        }
    }
}

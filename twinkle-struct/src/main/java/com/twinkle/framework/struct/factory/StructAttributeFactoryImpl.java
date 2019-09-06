package com.twinkle.framework.struct.factory;

import com.twinkle.framework.struct.type.StructAttribute;
import com.twinkle.framework.struct.type.StructAttributeType;
import com.twinkle.framework.struct.asm.classloader.StructAttributeClassLoader;
import com.twinkle.framework.asm.factory.AbstractBeanFactory;
import com.twinkle.framework.asm.Bean;
import com.twinkle.framework.asm.BeanFactory;
import com.twinkle.framework.struct.asm.builder.StructAttributeImplBuilder;
import com.twinkle.framework.asm.serialize.SerializerFactory;
import com.twinkle.framework.struct.serialize.SerializerFactoryRegistry;
import com.twinkle.framework.struct.error.StructAttributeException;
import com.twinkle.framework.struct.error.StructAttributeInstantiationException;
import com.twinkle.framework.struct.resolver.StructAttributeTypeResolver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/4/19 10:49 AM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class StructAttributeFactoryImpl extends AbstractStructAttributeFactory implements BeanFactory {
    private final Lock readLock;
    private final Lock writeLock;
    private final Map<String, StructAttributeImplBuilder> structAttributeImplBuilderMap;
    private SerializerFactoryRegistry factoryRegistry;

    public StructAttributeFactoryImpl(StructAttributeTypeResolver _typeResolver, ClassLoader _classLoader) {
        super(_typeResolver, _classLoader);
        this.structAttributeImplBuilderMap = new ConcurrentHashMap<>();
        ReentrantReadWriteLock tempLock = new ReentrantReadWriteLock();
        this.readLock = tempLock.readLock();
        this.writeLock = tempLock.readLock();
    }

    public StructAttributeFactoryImpl(StructAttributeTypeResolver _typeResolver, ClassLoader _classLoader, SerializerFactoryRegistry _registry) {
        super(_typeResolver, _classLoader);
        this.factoryRegistry = _registry;
        this.structAttributeImplBuilderMap = new ConcurrentHashMap();
        ReentrantReadWriteLock tempLock = new ReentrantReadWriteLock();
        this.readLock = tempLock.readLock();
        this.writeLock = tempLock.readLock();
    }

    @Override
    public StructAttribute newStructAttribute(StructAttributeType _saType) throws StructAttributeException {
        String tempName = _saType.getQualifiedName();
        StructAttributeImplBuilder tempBuilder = this.getStructAttributeImplBuilder(tempName);
        return (StructAttribute)tempBuilder.newInstance();
    }
    @Override
    public <T extends Bean> T newInstance(String _className) {
        StructAttributeImplBuilder tempBuilder = this.getStructAttributeImplBuilder(_className);
        return (T)tempBuilder.newInstance();
    }
    @Override
    public <T extends Bean> T newInstance(Class<T> _class) {
        StructAttributeClassLoader tempLoader = this.getClassLoader();
        String tempInterfaceName = tempLoader.getInterfaceName(_class.getName());
        String tempQualifiedName = tempLoader.getStructAttributeQualifiedTypeName(tempInterfaceName);
        StructAttributeImplBuilder tempBuilder = this.getStructAttributeImplBuilder(tempQualifiedName);
        return (T)tempBuilder.newInstance();
    }
    @Override
    public <T extends Bean> T[] newArray(String _className, int _length) {
        StructAttributeImplBuilder tempBuilder = this.getStructAttributeImplBuilder(_className);
        return (T[])tempBuilder.newArray(_length);
    }
    @Override
    public <T extends Bean> T[] newArray(Class<T> _class, int _length) {
        StructAttributeClassLoader tempLoader = this.getClassLoader();
        String tempInterfaceName = tempLoader.getInterfaceName(_class.getName());
        String tempQualifiedName = tempLoader.getStructAttributeQualifiedTypeName(tempInterfaceName);
        StructAttributeImplBuilder tempBuilder = this.getStructAttributeImplBuilder(tempQualifiedName);
        return (T[])tempBuilder.newArray(_length);
    }
    @Override
    public <T extends Bean> String getBeanType(Class<T> _class) {
        StructAttributeClassLoader tempLoader = this.getClassLoader();
        String tempInterfaceName = tempLoader.getInterfaceName(_class.getName());
        return tempLoader.getStructAttributeQualifiedTypeName(tempInterfaceName);
    }
    @Override
    public <T extends Bean> Class<T> getBeanInterface(String _interfaceName) {
        return (Class<T>)this.loadStructAttributeInterfaceByStructAttributeType(_interfaceName);
    }
    @Override
    public <T extends Bean> Class<T> getBeanClass(String _className) {
        return (Class<T>)this.loadStructAttributeClassByStructAttributeType(_className);
    }
    @Override
    public SerializerFactory getSerializerFactory(String _factoryName) {
        if (this.factoryRegistry == null) {
            throw new IllegalStateException("SerializerFactory registry not initialized");
        } else {
            return this.factoryRegistry.getSerializerFactory(_factoryName);
        }
    }
    @Override
    public SerializerFactory getDefaultSerializerFactory() {
        if (this.factoryRegistry == null) {
            throw new IllegalStateException("SerializerFactory registry not initialized");
        } else {
            return this.factoryRegistry.getDefaultSerializerFactory();
        }
    }

    /**
     * Load Struct Attribute's interface's class.
     *
     * @param _typeName
     * @return
     * @throws StructAttributeInstantiationException
     */
    private Class<?> loadStructAttributeInterfaceByStructAttributeType(String _typeName) throws StructAttributeInstantiationException {
        String tempInterfaceName = AbstractBeanFactory.typeNameToInterfaceName(_typeName);
        try {
            return this.getClassLoader().loadClass(tempInterfaceName);
        } catch (ClassNotFoundException e) {
            throw new StructAttributeInstantiationException(e);
        }
    }

    /**
     * Load Struct Attribute's Class.
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

        StructAttributeImplBuilder tempBuilder;
        try {
            tempBuilder = this.structAttributeImplBuilderMap.get(_className);
        } finally {
            this.readLock.unlock();
        }
        if (tempBuilder == null) {
            tempBuilder = this.loadStructAttributeImplBuilder(_className);
            this.writeLock.lock();
            try {
                this.structAttributeImplBuilderMap.put(_className, tempBuilder);
            } finally {
                this.writeLock.unlock();
            }
        }

        return tempBuilder;
    }

    /**
     * Get StructAttributeImplBuilder's instance with StructAttribute's class name.
     *
     * @param _className
     * @param <T>
     * @return
     * @throws StructAttributeInstantiationException
     */
    private <T extends Bean> StructAttributeImplBuilder<T> loadStructAttributeImplBuilder(String _className) throws StructAttributeInstantiationException {
        Class<? extends StructAttributeImplBuilder<T>> tempClass = this.loadStructAttributeImplBuilderClass(_className);

        try {
            return tempClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new StructAttributeInstantiationException(e);
        }
    }

    /**
     * Get StructAttributeImplBuilder class with StructAttribute's class name.
     *
     * @param _className
     * @param <T>
     * @return
     * @throws StructAttributeInstantiationException
     */
    private <T extends Bean> Class<? extends StructAttributeImplBuilder<T>> loadStructAttributeImplBuilderClass(String _className) throws StructAttributeInstantiationException {
        StructAttributeClassLoader tempClassLoader = this.getClassLoader();
        String tempSABuilderClassName = tempClassLoader.getStructAttributeBuilderClassName(_className);
        try {
            return (Class<? extends StructAttributeImplBuilder<T>>)tempClassLoader.loadClass(tempSABuilderClassName);
        } catch (ClassNotFoundException e) {
            throw new StructAttributeInstantiationException(e);
        }
    }

    /**
     * Get Serializer factory registry.
     *
     * @return
     */
    protected SerializerFactoryRegistry getSerializerFactoryRegistrar() {
        return this.factoryRegistry;
    }
}

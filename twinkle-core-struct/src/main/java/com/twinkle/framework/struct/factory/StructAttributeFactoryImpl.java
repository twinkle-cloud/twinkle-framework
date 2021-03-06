package com.twinkle.framework.struct.factory;

import com.twinkle.framework.asm.Bean;
import com.twinkle.framework.asm.factory.BeanFactory;
import com.twinkle.framework.asm.serialize.SerializerFactory;
import com.twinkle.framework.asm.utils.BeanUtil;
import com.twinkle.framework.struct.resolver.StructAttributeTypeResolver;
import com.twinkle.framework.struct.serialize.SerializerFactoryRegistry;
import com.twinkle.framework.struct.asm.builder.StructAttributeImplBuilder;
import com.twinkle.framework.struct.asm.classloader.StructAttributeClassLoader;
import com.twinkle.framework.struct.asm.descriptor.SAAttributeDescriptor;
import com.twinkle.framework.struct.error.NamespaceNotFoundException;
import com.twinkle.framework.struct.error.StructAttributeException;
import com.twinkle.framework.struct.error.StructAttributeInstantiationException;
import com.twinkle.framework.struct.error.StructAttributeTypeNotFoundException;
import com.twinkle.framework.struct.type.ArrayType;
import com.twinkle.framework.struct.type.AttributeType;
import com.twinkle.framework.struct.lang.StructAttribute;
import com.twinkle.framework.struct.type.StructType;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
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
@Slf4j
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
    public StructAttribute newStructAttribute(StructType _saType) throws StructAttributeException {
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
        StructAttributeClassLoader tempLoader = this.getClassLoader();
        String tempInterfaceName = tempLoader.getInterfaceName(_class.getName());
        String tempQualifiedName = tempLoader.getStructAttributeQualifiedTypeName(tempInterfaceName);
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
        StructAttributeClassLoader tempLoader = this.getClassLoader();
        String tempInterfaceName = tempLoader.getInterfaceName(_class.getName());
        String tempQualifiedName = tempLoader.getStructAttributeQualifiedTypeName(tempInterfaceName);
        StructAttributeImplBuilder tempBuilder = this.getStructAttributeImplBuilder(tempQualifiedName);
        return (T[]) tempBuilder.newArray(_length);
    }

    @Override
    public <T extends Bean> String getBeanType(Class<T> _class) {
        StructAttributeClassLoader tempLoader = this.getClassLoader();
        String tempInterfaceName = tempLoader.getInterfaceName(_class.getName());
        return tempLoader.getStructAttributeQualifiedTypeName(tempInterfaceName);
    }

    @Override
    public <T extends Bean> Class<T> getBeanInterface(String _interfaceName) {
        return (Class<T>) this.loadStructAttributeInterfaceByStructAttributeType(_interfaceName);
    }

    @Override
    public <T extends Bean> Class<T> getBeanClass(String _typeName) {
        return (Class<T>) this.loadStructAttributeClassByStructAttributeType(_typeName);
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
        String tempInterfaceName = BeanUtil.typeNameToInterfaceName(_typeName);
        try {
            return this.getClassLoader().loadClass(tempInterfaceName);
        } catch (ClassNotFoundException e) {
            throw new StructAttributeInstantiationException(e);
        }
    }

    @Override
    public Class<?> loadGeneralBeanClass(StructType _saType) throws StructAttributeException {
        try {
            Iterator<SAAttributeDescriptor> tempAttrItr = _saType.getAttributes();
            while (tempAttrItr.hasNext()) {
                SAAttributeDescriptor tempDescriptor = tempAttrItr.next();
                if (tempDescriptor.getType() instanceof StructType) {
                    loadGeneralBeanClass(((StructType) tempDescriptor.getType()).getQualifiedName());
                } else if (tempDescriptor.getType() instanceof ArrayType) {
                    ArrayType tempArrayType = (ArrayType) tempDescriptor.getType();
                    AttributeType tempItemType = tempArrayType.getElementType();
                    if (tempItemType instanceof StructType) {
                        loadGeneralBeanClass(((StructType) tempItemType).getQualifiedName());
                    }
                }
            }
            String tempName = this.getGeneralClassLoader().getStructAttributeGeneralClassName(_saType.getQualifiedName());
            return this.getGeneralClassLoader().loadClass(tempName);
        } catch (ClassNotFoundException e) {
            throw new StructAttributeInstantiationException(e);
        }
    }

    @Override
    public Class<?> loadGeneralBeanClass(String _typeName) throws StructAttributeException {
        try {
            StructType tempAttributeType = this.getBeanStructAttributeSchema().getStructAttributeType(_typeName);
            return this.loadGeneralBeanClass(tempAttributeType);
        } catch (StructAttributeInstantiationException e) {
            throw new StructAttributeInstantiationException(e);
        } catch (NamespaceNotFoundException | StructAttributeTypeNotFoundException e) {
            log.info("Did not find the type [{}] in StructAttributeSchema.", _typeName);
        }
        throw new StructAttributeException("Load Struct Attribute type failed.");
    }

    /**
     * Load Struct Attribute's Class.
     *
     * @param _typeName
     * @return
     * @throws StructAttributeInstantiationException
     */
    private Class<?> loadStructAttributeClassByStructAttributeType(String _typeName) throws StructAttributeInstantiationException {
        String tempClassName = BeanUtil.structTypeNameToClassName(_typeName);
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
            return (Class<? extends StructAttributeImplBuilder<T>>) tempClassLoader.loadClass(tempSABuilderClassName);
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

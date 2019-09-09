package com.twinkle.framework.struct.factory;

import com.twinkle.framework.struct.asm.classloader.StructAttributeGeneralClassLoader;
import com.twinkle.framework.struct.error.*;
import com.twinkle.framework.struct.type.StructAttribute;
import com.twinkle.framework.struct.type.StructAttributeType;
import com.twinkle.framework.struct.asm.classloader.StructAttributeClassLoader;
import com.twinkle.framework.struct.context.BeanStructAttributeSchema;
import com.twinkle.framework.struct.context.StructAttributeSchema;
import com.twinkle.framework.struct.asm.builder.StructAttributeImplBuilder;
import com.twinkle.framework.asm.descriptor.TypeDescriptors;
import com.twinkle.framework.struct.ref.AttributeRef;
import com.twinkle.framework.struct.ref.CompositeAttributeRefFactory;
import com.twinkle.framework.struct.ref.DynamicAttributeRef;
import com.twinkle.framework.struct.resolver.DefaultStructAttributeTypeResolver;
import com.twinkle.framework.struct.resolver.StructAttributeTypeResolver;
import com.twinkle.framework.struct.util.ArrayAllocator;
import com.twinkle.framework.struct.util.ArrayAllocatorImpl;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/30/19 8:11 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public abstract class AbstractStructAttributeFactory implements StructAttributeFactory {
    private final StructAttributeClassLoader classLoader;
    private final StructAttributeGeneralClassLoader generalClassLoader;
    private final ArrayAllocator arrayallocator;
    private volatile BeanStructAttributeSchema beanStructAttributeSchema;

    protected AbstractStructAttributeFactory(StructAttributeTypeResolver _typeResolver, ClassLoader _classLoader) {
        this.classLoader = this.initStructAttributeClassLoader(_classLoader, _typeResolver);
        this.generalClassLoader = this.initStructAttributeGeneralClassLoader(_classLoader, _typeResolver);

        this.arrayallocator = new ArrayAllocatorImpl();
        this.beanStructAttributeSchema = null;
        if (_typeResolver instanceof DefaultStructAttributeTypeResolver) {
            StructAttributeSchema tempSchema = ((DefaultStructAttributeTypeResolver) _typeResolver).getSaSchema();
            if (tempSchema instanceof BeanStructAttributeSchema) {
                this.beanStructAttributeSchema = (BeanStructAttributeSchema) tempSchema;
            }
        }
    }

    /**
     * Initialize the struct attribute's class loader.
     *
     * @param _classLoader
     * @param _typeResolver
     * @return
     */
    protected StructAttributeClassLoader initStructAttributeClassLoader(ClassLoader _classLoader, StructAttributeTypeResolver _typeResolver) {
        return new StructAttributeClassLoader(_classLoader, _typeResolver);
    }

    /**
     * Initialize the general bean class loader.
     *
     * @param _classLoader
     * @param _typeResolver
     * @return
     */
    protected StructAttributeGeneralClassLoader initStructAttributeGeneralClassLoader(ClassLoader _classLoader, StructAttributeTypeResolver _typeResolver) {
        return new StructAttributeGeneralClassLoader(_classLoader, _typeResolver);
    }

    protected StructAttributeGeneralClassLoader getGeneralClassLoader(){
        return this.generalClassLoader;
    }

    protected StructAttributeClassLoader getClassLoader() {
        return this.classLoader;
    }

    @Override
    public ArrayAllocator getArrayAllocator() {
        return this.arrayallocator;
    }

    @Override
    public abstract StructAttribute newStructAttribute(StructAttributeType _saType) throws StructAttributeException;

    /**
     * Get the Attribute ref with given Struct Attribute type, and class name.
     *
     * @param _saType
     * @param _className
     * @return
     * @throws AttributeNotFoundException
     * @throws AttributeTypeMismatchException
     * @throws BadAttributeNameException
     */
    public AttributeRef _getAttributeRef(StructAttributeType _saType, String _className) throws AttributeNotFoundException, AttributeTypeMismatchException, BadAttributeNameException {
        String tempQualifiedName = _saType.getQualifiedName();
        StructAttributeImplBuilder tempBuilder = this.getStructAttributeImplBuilder(tempQualifiedName);
        return tempBuilder.getAttributeReference(_className);
    }

    @Override
    public AttributeRef getAttributeRef(StructAttributeType _saType, String _className) throws AttributeNotFoundException, AttributeTypeMismatchException, BadAttributeNameException {
        if (_className == null) {
            throw new BadAttributeNameException("Attribute name is NULL");
        }
        return !_saType.hasAttribute(_className) ? CompositeAttributeRefFactory.getCompositeAttributeRef(this, _saType, _className) : this._getAttributeRef(_saType, _className);
    }

    @Override
    public AttributeRef getCompositeAttributeRef(StructAttributeType _saType, String _compositeName) throws AttributeNotFoundException, AttributeTypeMismatchException, BadAttributeNameException {
        return CompositeAttributeRefFactory.getCompositeAttributeRef(this, _saType, _compositeName);
    }

    @Override
    public DynamicAttributeRef getDynamicAttributeRef(StructAttributeType _saType, String _compositeName) throws AttributeNotFoundException, AttributeTypeMismatchException, BadAttributeNameException {
        return CompositeAttributeRefFactory.getDynamicAttributeRef(this, _saType, _compositeName);
    }

    public ClassLoader getBeanClassLoader() {
        return this.getClassLoader();
    }

    /**
     * Get the Type descriptors in the current schema.
     *
     * @return
     */
    public TypeDescriptors getTypeDescriptors() {
        BeanStructAttributeSchema tempSchema = this.beanStructAttributeSchema;
        if (tempSchema == null) {
            throw new IllegalStateException("Bean Schema not set yet");
        }
        TypeDescriptors tempDescriptors = tempSchema.getTypeDescriptors();
        if (tempDescriptors == null) {
            throw new IllegalStateException("TypeDescriptors not set yet");
        } else {
            return tempDescriptors;
        }
    }

    /**
     * Update the current schema.
     *
     * @param _schema
     */
    public void setBeanSchema(BeanStructAttributeSchema _schema) {
        this.beanStructAttributeSchema = _schema;
    }

    /**
     * Build the Struct Attribute
     *
     * @param _className
     * @return
     * @throws StructAttributeInstantiationException
     */
    protected abstract StructAttributeImplBuilder getStructAttributeImplBuilder(String _className) throws StructAttributeInstantiationException;
}

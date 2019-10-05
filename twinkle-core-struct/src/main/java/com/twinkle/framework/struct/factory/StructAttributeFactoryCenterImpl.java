package com.twinkle.framework.struct.factory;

import com.twinkle.framework.struct.context.BeanStructAttributeSchema;
import com.twinkle.framework.struct.context.StructAttributeSchema;
import com.twinkle.framework.struct.context.StructAttributeSchemaManager;
import com.twinkle.framework.struct.resolver.DefaultStructAttributeTypeResolver;
import com.twinkle.framework.struct.resolver.StructAttributeTypeResolver;
import com.twinkle.framework.struct.serialize.SerializerFactoryRegistry;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/3/19 11:06 AM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class StructAttributeFactoryCenterImpl extends AbstractStructAttributeFactoryCenter {
    private final AbstractStructAttributeFactory structAttributeFactory;

    public StructAttributeFactoryCenterImpl(StructAttributeTypeResolver _typeResolver, ClassLoader _classLoader) {
        this.structAttributeFactory = this.initStructAttributeFactory(_typeResolver, _classLoader);
        if (_typeResolver instanceof DefaultStructAttributeTypeResolver) {
            this.setSchemaInternal(((DefaultStructAttributeTypeResolver) _typeResolver).getSaSchema());
        }
    }

    public StructAttributeFactoryCenterImpl(StructAttributeSchema _schema, ClassLoader _classLoader) {
        this.structAttributeFactory = this.initStructAttributeFactory(new DefaultStructAttributeTypeResolver(_schema), _classLoader);
        this.setSchemaInternal(_schema);
    }

    public StructAttributeFactoryCenterImpl() {
        StructAttributeSchema tempSchema = StructAttributeSchemaManager.getStructAttributeSchema();
//        CumulativeClassLoader var2 = PluginRegistry.getInstance().getCumulativeClassLoader();
        ClassLoader tempLoader = this.getClass().getClassLoader();
        this.structAttributeFactory = this.initStructAttributeFactory(new DefaultStructAttributeTypeResolver(tempSchema), tempLoader);
        this.setSchemaInternal(tempSchema);
    }

    /**
     * Initialize the Struct Attribute Factory.
     *
     * @param _resolver
     * @param _classLoader
     * @return
     */
    protected AbstractStructAttributeFactory initStructAttributeFactory(StructAttributeTypeResolver _resolver, ClassLoader _classLoader) {
        SerializerFactoryRegistry tempSerializerRegistry = this.initSerializerFactoryRegistry();
        return new StructAttributeFactoryImpl(_resolver, _classLoader, tempSerializerRegistry);
    }

    protected SerializerFactoryRegistry initSerializerFactoryRegistry() {
        SerializerFactoryRegistry tempRegistry = new SerializerFactoryRegistry();
        tempRegistry.register("JSON", "com.twinkle.framework.struct.serialize.JsonSerializerFactory");
        tempRegistry.register("JSON.INTROSPECTION", "com.twinkle.framework.struct.serialize.JsonIntrospectionSerializerFactory");
//        tempRegistry.register("DATASTORE", "com.hp.usage.nme.datastore.NMESerializerFactory");
        tempRegistry.setDefaultFormatName("JSON");
        return tempRegistry;
    }

    @Override
    public StructAttributeFactory getStructAttributeFactory() {
        return this.structAttributeFactory;
    }

    /**
     * Update the bean schema in the Struct attribute factory.
     *
     * @param _schema
     */
    protected void setBeanSchema(BeanStructAttributeSchema _schema) {
        this.structAttributeFactory.setBeanSchema(_schema);
    }

    @Override
    public void setSchema(StructAttributeSchema _schema) {
        super.setSchema(_schema);
        if (_schema instanceof BeanStructAttributeSchema) {
            this.setBeanSchema((BeanStructAttributeSchema) _schema);
        } else {
            throw new IllegalArgumentException("Expected " + BeanStructAttributeSchema.class.getName() + " found " + _schema.getClass().getName());
        }
    }
}

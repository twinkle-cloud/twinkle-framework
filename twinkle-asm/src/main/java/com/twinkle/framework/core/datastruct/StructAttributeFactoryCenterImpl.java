package com.twinkle.framework.core.datastruct;

import com.twinkle.framework.core.asm.factory.StructAttributeFactoryImpl;
import com.twinkle.framework.core.context.BeanStructAttributeSchema;
import com.twinkle.framework.core.context.StructAttributeManager;
import com.twinkle.framework.core.context.StructAttributeSchema;
import com.twinkle.framework.core.datastruct.serialize.SerializerFactoryRegistry;
import com.twinkle.framework.core.lang.resolver.StructAttributeSchemaResolver;
import com.twinkle.framework.core.lang.resolver.StructAttributeTypeResolver;
import com.twinkle.framework.core.lang.struct.AbstractStructAttributeFactory;
import com.twinkle.framework.core.lang.struct.StructAttributeFactory;

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
        if (_typeResolver instanceof StructAttributeSchemaResolver) {
            this.setSchemaInternal(((StructAttributeSchemaResolver) _typeResolver).getSaSchema());
        }
    }

    public StructAttributeFactoryCenterImpl(StructAttributeSchema _schema, ClassLoader _classLoader) {
        this.structAttributeFactory = this.initStructAttributeFactory(new StructAttributeSchemaResolver(_schema), _classLoader);
        this.setSchemaInternal(_schema);
    }

    public StructAttributeFactoryCenterImpl() {
        StructAttributeSchema tempSchema = StructAttributeManager.getStructAttributeSchema();
//        CumulativeClassLoader var2 = PluginRegistry.getInstance().getCumulativeClassLoader();
        ClassLoader tempLoader = this.getClass().getClassLoader();
        this.structAttributeFactory = this.initStructAttributeFactory(new StructAttributeSchemaResolver(tempSchema), tempLoader);
        this.setSchemaInternal(tempSchema);
    }

    /**
     * Initialize the Struct Attribute Factory.
     *
     * @param _resolver
     * @param var2
     * @return
     */
    protected AbstractStructAttributeFactory initStructAttributeFactory(StructAttributeTypeResolver _resolver, ClassLoader var2) {
        SerializerFactoryRegistry var3 = this.initSerializerFactoryRegistry();
        return new StructAttributeFactoryImpl(_resolver, var2, var3);
    }

    protected SerializerFactoryRegistry initSerializerFactoryRegistry() {
        SerializerFactoryRegistry tempRegistry = new SerializerFactoryRegistry();
        tempRegistry.register("JSON", "com.twinkle.framework.core.datastruct.serialize.JsonSerializerFactory");
        tempRegistry.register("JSON.INTROSPECTION", "com.twinkle.framework.core.datastruct.serialize.JsonIntrospectionSerializer");
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

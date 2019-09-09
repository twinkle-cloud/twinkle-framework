package com.twinkle.framework.struct.serialize;

import com.twinkle.framework.asm.serialize.Serializer;
import com.twinkle.framework.asm.serialize.SerializerFactory;
import com.twinkle.framework.struct.context.StructAttributeSchemaManager;
import com.twinkle.framework.struct.context.StructAttributeSchema;
import com.twinkle.framework.struct.context.StructAttributeSchemaFactory;
import com.twinkle.framework.asm.factory.BeanFactory;
import com.twinkle.framework.struct.factory.StructAttributeFactory;

import java.util.Objects;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/4/19 4:08 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class JsonSerializerFactory implements SerializerFactory {
    private StructAttributeSchema structAttributeSchema;
    private StructAttributeFactory structAttributeFactory;
    private BeanFactory beanFactory;
    private StructAttributeSchemaFactory structAttributeSchemaFactory;
    private boolean initialized = false;

    public JsonSerializerFactory() {
    }

    private void init() {
        if (!this.initialized) {
            this.structAttributeSchema = Objects.requireNonNull(StructAttributeSchemaManager.getStructAttributeSchema(), "StructAttribute Schema missed");
            this.structAttributeFactory = Objects.requireNonNull(StructAttributeSchemaManager.getStructAttributeFactory(), "StructAttribute Factory missed");
            this.beanFactory = (BeanFactory)this.structAttributeFactory;
            this.structAttributeSchemaFactory = new StructAttributeSchemaFactory(this.beanFactory);
            this.initialized = true;
        }

    }

    @Override
    public <T> Serializer<T> getSerializer(String var1) {
        this.init();
        return (Serializer<T>) this.structAttributeSchemaFactory.generateSerializers(this.structAttributeSchema, Objects.requireNonNull(var1, "Root type could not be null"), this.beanFactory, this.structAttributeFactory.getArrayAllocator());
    }

    @Override
    public <T> Serializer<T> getSerializer(Class<T> var1) {
        throw new RuntimeException("Method JsonSerializerFactory.getSerializer() not implemented");
    }
}

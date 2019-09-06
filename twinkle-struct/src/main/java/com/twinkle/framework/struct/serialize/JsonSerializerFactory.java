package com.twinkle.framework.struct.serialize;

import com.twinkle.framework.asm.serialize.Serializer;
import com.twinkle.framework.asm.serialize.SerializerFactory;
import com.twinkle.framework.struct.context.StructAttributeManager;
import com.twinkle.framework.struct.context.StructAttributeSchema;
import com.twinkle.framework.struct.context.StructAttributeSchemaFactory;
import com.twinkle.framework.asm.BeanFactory;
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
    private StructAttributeSchema nmeSchema;
    private StructAttributeFactory nmeFactory;
    private BeanFactory nmeBeanFactory;
    private StructAttributeSchemaFactory schemaGenerator;
    private boolean initialized = false;

    public JsonSerializerFactory() {
    }

    private void init() {
        if (!this.initialized) {
            this.nmeSchema = Objects.requireNonNull(StructAttributeManager.getStructAttributeSchema(), "StructAttribute Schema missed");
            this.nmeFactory = Objects.requireNonNull(StructAttributeManager.getStructAttributeFactory(), "StructAttribute Factory missed");
            this.nmeBeanFactory = (BeanFactory)this.nmeFactory;
            this.schemaGenerator = new StructAttributeSchemaFactory(this.nmeBeanFactory);
            this.initialized = true;
        }

    }

    @Override
    public <T> Serializer<T> getSerializer(String var1) {
        this.init();
        return (Serializer<T>) this.schemaGenerator.generateSerializers(this.nmeSchema, Objects.requireNonNull(var1, "Root type could not be null"), this.nmeBeanFactory, this.nmeFactory.getArrayAllocator());
    }

    @Override
    public <T> Serializer<T> getSerializer(Class<T> var1) {
        throw new RuntimeException("Method JsonSerializerFactory.getSerializer() not implemented");
    }
}

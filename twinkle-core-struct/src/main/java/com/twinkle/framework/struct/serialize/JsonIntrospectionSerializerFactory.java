package com.twinkle.framework.struct.serialize;

import com.twinkle.framework.asm.serialize.Serializer;
import com.twinkle.framework.asm.serialize.SerializerFactory;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/8/19 4:14 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class JsonIntrospectionSerializerFactory implements SerializerFactory {
    public JsonIntrospectionSerializerFactory() {
    }
    @Override
    public <T> Serializer<T> getSerializer(String _rootType) {
        return (Serializer<T>) (_rootType == null ? new JsonIntrospectionSerializer() : new JsonIntrospectionSerializer(_rootType));
    }
    @Override
    public <T> Serializer<T> getSerializer(Class<T> _class) {
        return (Serializer<T>) new JsonIntrospectionSerializer();
    }

}

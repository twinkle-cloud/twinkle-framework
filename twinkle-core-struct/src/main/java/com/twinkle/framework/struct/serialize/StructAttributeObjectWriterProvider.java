package com.twinkle.framework.struct.serialize;

import com.alibaba.fastjson2.PropertyNamingStrategy;
import com.alibaba.fastjson2.writer.ObjectWriter;
import com.alibaba.fastjson2.writer.ObjectWriterCreator;
import com.alibaba.fastjson2.writer.ObjectWriterProvider;
import com.twinkle.framework.struct.lang.StructAttribute;

import java.lang.reflect.Type;

public class StructAttributeObjectWriterProvider extends ObjectWriterProvider {
    private final ObjectWriter<StructAttribute> STRUCT_ATTRIBUTE_OBJECT_WRITER = new StructAttributeFastJsonObjectWriter();
    public StructAttributeObjectWriterProvider() {
        super();
    }

    public StructAttributeObjectWriterProvider(PropertyNamingStrategy namingStrategy) {
        super(namingStrategy);
    }

    public StructAttributeObjectWriterProvider(ObjectWriterCreator creator) {
        super(creator);
    }

    @Override
    public ObjectWriter getObjectWriter(Type objectType, Class objectClass, boolean fieldBased) {
        if (StructAttribute.class.isAssignableFrom(objectClass)) {
            return STRUCT_ATTRIBUTE_OBJECT_WRITER;
        }
        if (objectClass.isArray()) {
            Class<?> tempGenericClass = ((Class<?>) objectType).getComponentType();
            if (StructAttribute.class.isAssignableFrom(tempGenericClass)) {
                return STRUCT_ATTRIBUTE_OBJECT_WRITER;
            }
        }
        return super.getObjectWriter(objectType, objectClass, fieldBased);
    }
}

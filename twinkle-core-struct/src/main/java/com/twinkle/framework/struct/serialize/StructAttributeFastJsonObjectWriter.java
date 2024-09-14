package com.twinkle.framework.struct.serialize;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;
import com.twinkle.framework.asm.serialize.Serializer;
import com.twinkle.framework.asm.serialize.SerializerFactory;
import com.twinkle.framework.struct.lang.StructAttribute;
import com.twinkle.framework.struct.utils.StructTypeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.reflect.Type;

@Slf4j
public class StructAttributeFastJsonObjectWriter implements ObjectWriter<StructAttribute> {

    private final JsonSerializer tempJsonSerializer;

    public StructAttributeFastJsonObjectWriter() {
        this.tempJsonSerializer = this.getJsonSerializer(StructAttribute.class);
    }

    @Override
    public void write(JSONWriter jsonWriter, Object object, Object fieldName, Type fieldType, long features) {
        if (tempJsonSerializer != null) {
            try {
                tempJsonSerializer.write((StructAttribute)object, jsonWriter);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        log.info("The struct attribute fast json object writer.");
    }

    private JsonSerializer getJsonSerializer(Type _type) {
        String tempClassName = _type.getTypeName();
        String tempRootType = StructTypeUtil.getQualifiedName(tempClassName);
        if (StringUtils.isBlank(tempRootType)) {
            return null;
        }
        SerializerFactory tempFactory = new JsonIntrospectionSerializerFactory();
        Serializer tempSerializer = tempFactory.getSerializer(tempRootType);
        return (JsonSerializer) tempSerializer;
    }
}

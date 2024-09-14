package com.twinkle.framework.struct.serialize;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.twinkle.framework.asm.serialize.Serializer;
import com.twinkle.framework.asm.serialize.SerializerFactory;
import com.twinkle.framework.struct.lang.StructAttribute;
import com.twinkle.framework.struct.utils.StructTypeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author chenxj
 * @date 2024/09/13
 */
@Slf4j
public class StructAttributeFastJsonObjectReader implements ObjectReader<StructAttribute> {
    private final JsonSerializer tempJsonSerializer;
    public StructAttributeFastJsonObjectReader() {
        this.tempJsonSerializer = this.getJsonSerializer(StructAttribute.class);
    }
    @Override
    public StructAttribute readObject(JSONReader jsonReader, Type fieldType, Object fieldName, long features) {
        if (tempJsonSerializer != null) {
            try {
                log.info("The struct attribute fast json object writer.");
                return tempJsonSerializer.read(jsonReader);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException("Did not find the struct attribute fast json serializer.");
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

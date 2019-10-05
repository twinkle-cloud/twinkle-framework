package com.twinkle.framework.connector.http.converter;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.twinkle.framework.asm.serialize.Serializer;
import com.twinkle.framework.asm.serialize.SerializerFactory;
import com.twinkle.framework.struct.serialize.JsonIntrospectionSerializerFactory;
import com.twinkle.framework.struct.serialize.JsonSerializer;
import com.twinkle.framework.struct.type.StructAttribute;
import com.twinkle.framework.struct.utils.StructTypeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/14/19 2:25 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class TwinkleFastJsonHttpMessageConverter extends FastJsonHttpMessageConverter {
    private final static String STRUCT_ATTRIBUTE_PACKAGE = "";
    private Map<String, JsonSerializer> serializerMap;

    public TwinkleFastJsonHttpMessageConverter() {
        super();
        this.serializerMap = new HashMap<>(32);
    }

    @Override
    public Object read(Type type,
                       Class<?> contextClass,
                       HttpInputMessage inputMessage
    ) throws IOException, HttpMessageNotReadableException {
        Type tempType = this.getType(type, contextClass);
        if (tempType instanceof Class) {
            if (StructAttribute.class.isAssignableFrom((Class) tempType)) {
                JsonSerializer tempJsonSerializer = this.getJsonSerializer(type);
                if(tempJsonSerializer == null) {
                    super.read(type, contextClass, inputMessage);
                }
                StructAttribute tempAttribute = tempJsonSerializer.read(inputMessage.getBody());
                return tempAttribute;
            }
            if(((Class) tempType).isArray()) {
                Class<?> tempGenericClass = ((Class) tempType).getComponentType();
                if(StructAttribute.class.isAssignableFrom(tempGenericClass)) {
                    JsonSerializer tempJsonSerializer = this.getJsonSerializer(tempGenericClass);
                    if(tempJsonSerializer == null) {
                        super.read(type, contextClass, inputMessage);
                    }
                    List<StructAttribute> tempAttribute = tempJsonSerializer.readMultiple(inputMessage.getBody());
                    return tempAttribute.toArray((StructAttribute[])Array.newInstance(tempGenericClass, tempAttribute.size()));
                }
            }
        }
        return super.read(type, contextClass, inputMessage);
    }

    private JsonSerializer getJsonSerializer(Type _type) {
        String tempClassName = _type.getTypeName();
        String tempRootType = StructTypeUtil.getQualifiedName(tempClassName);
        if (StringUtils.isBlank(tempRootType)) {
            return null;
        }
        JsonSerializer tempJsonSerializer = this.serializerMap.get(tempRootType);
        if (tempJsonSerializer == null) {
            SerializerFactory tempFactory = new JsonIntrospectionSerializerFactory();
            Serializer tempSerializer = tempFactory.getSerializer(tempRootType);
            tempJsonSerializer = (JsonSerializer) tempSerializer;
            this.serializerMap.put(tempRootType, tempJsonSerializer);
        }
        return tempJsonSerializer;
    }

    @Override
    public void write(Object o, Type type, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        // support StreamingHttpOutputMessage in spring4.0+

        super.write(o, type, contentType, outputMessage);
        //writeInternal(o, outputMessage);
    }
}

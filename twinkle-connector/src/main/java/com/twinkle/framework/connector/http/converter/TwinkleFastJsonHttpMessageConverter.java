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
import java.lang.reflect.Type;
import java.util.HashMap;
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
        if(tempType instanceof Class) {
        if (StructAttribute.class.isAssignableFrom((Class)tempType)) {
            String tempClassName = type.getTypeName();
            String tempRootType = StructTypeUtil.getQualifiedName(tempClassName);
            if (StringUtils.isBlank(tempRootType)) {
                return super.read(type, contextClass, inputMessage);
            }

            JsonSerializer tempJsonSerializer = this.serializerMap.get(tempRootType);
            if (tempJsonSerializer == null) {
                SerializerFactory tempFactory = new JsonIntrospectionSerializerFactory();
                Serializer tempSerializer = tempFactory.getSerializer(tempRootType);
                tempJsonSerializer = (JsonSerializer) tempSerializer;
                this.serializerMap.put(tempRootType, tempJsonSerializer);
            }

            StructAttribute tempAttribute = tempJsonSerializer.read(inputMessage.getBody());
            return tempAttribute;
        }}
        return super.read(type, contextClass, inputMessage);
    }

    @Override
    public void write(Object o, Type type, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        // support StreamingHttpOutputMessage in spring4.0+

        super.write(o, type, contentType, outputMessage);
        //writeInternal(o, outputMessage);
    }
}

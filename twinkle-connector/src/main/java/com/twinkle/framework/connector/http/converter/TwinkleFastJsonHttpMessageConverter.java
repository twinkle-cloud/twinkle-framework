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
import java.util.IdentityHashMap;
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
    private Map<String, JsonSerializer> serializerMap;

    public TwinkleFastJsonHttpMessageConverter() {
        super();
        this.serializerMap = new IdentityHashMap<>(16);
    }

    @Override
    public Object read(Type type,
                       Class<?> contextClass,
                       HttpInputMessage inputMessage
    ) throws IOException, HttpMessageNotReadableException {
        if (contextClass.isAssignableFrom(StructAttribute.class)) {
            String tempClass = contextClass.getName();
            String tempRootType = StructTypeUtil.getQualifiedName(tempClass);
            if (StringUtils.isBlank(tempRootType)) {
                return super.read(type, contextClass, inputMessage);
            }
            JsonSerializer tempJsonSerializer = this.serializerMap.get(tempRootType);
            if (tempJsonSerializer == null) {
                return super.read(type, contextClass, inputMessage);
            }

            SerializerFactory tempFactory = new JsonIntrospectionSerializerFactory();
            Serializer tempSerializer = tempFactory.getSerializer(tempRootType);
            tempJsonSerializer = (JsonSerializer) tempSerializer;

            StructAttribute tempAttribute = tempJsonSerializer.read(inputMessage.getBody());
            return tempAttribute;
        }
        return super.read(type, contextClass, inputMessage);
    }

    @Override
    public void write(Object o, Type type, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        // support StreamingHttpOutputMessage in spring4.0+

        super.write(o, type, contentType, outputMessage);
        //writeInternal(o, outputMessage);
    }
}

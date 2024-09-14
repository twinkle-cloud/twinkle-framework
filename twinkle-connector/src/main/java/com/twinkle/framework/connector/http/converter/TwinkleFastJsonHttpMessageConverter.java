package com.twinkle.framework.connector.http.converter;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONPObject;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.filter.Filter;
import com.alibaba.fastjson2.support.spring6.http.converter.FastJsonHttpMessageConverter;
import com.alibaba.fastjson2.writer.ObjectWriter;
import com.alibaba.fastjson2.writer.ObjectWriterProvider;
import com.twinkle.framework.asm.serialize.Serializer;
import com.twinkle.framework.asm.serialize.SerializerFactory;
import com.twinkle.framework.struct.lang.StructAttribute;
import com.twinkle.framework.struct.serialize.JsonIntrospectionSerializerFactory;
import com.twinkle.framework.struct.serialize.JsonSerializer;
import com.twinkle.framework.struct.serialize.StructAttributeObjectWriterProvider;
import com.twinkle.framework.struct.utils.StructTypeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
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
@Slf4j
public class TwinkleFastJsonHttpMessageConverter extends FastJsonHttpMessageConverter {
    private final static String STRUCT_ATTRIBUTE_PACKAGE = "";
    private Map<String, JsonSerializer> serializerMap;
    static final ObjectWriterProvider STRUCT_ATTRIBUTE_OBJ_WRITER_PROVIDER = new StructAttributeObjectWriterProvider();

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
                if (tempJsonSerializer == null) {
                    super.read(type, contextClass, inputMessage);
                }
                StructAttribute tempAttribute = tempJsonSerializer.read(inputMessage.getBody());
                return tempAttribute;
            }
            if (((Class) tempType).isArray()) {
                Class<?> tempGenericClass = ((Class) tempType).getComponentType();
                if (StructAttribute.class.isAssignableFrom(tempGenericClass)) {
                    JsonSerializer tempJsonSerializer = this.getJsonSerializer(tempGenericClass);
                    if (tempJsonSerializer == null) {
                        super.read(type, contextClass, inputMessage);
                    }
                    List<StructAttribute> tempAttribute = tempJsonSerializer.readMultiple(inputMessage.getBody());
                    return tempAttribute.toArray((StructAttribute[]) Array.newInstance(tempGenericClass, tempAttribute.size()));
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

//    @Override
//    public void write(Object o, Type type, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
//        // support StreamingHttpOutputMessage in spring4.0+
//        super.write(o, type, contentType, outputMessage);
//        //writeInternal(o, outputMessage);
//    }

    @Override
    protected void writeInternal(Object object, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        HttpHeaders headers = outputMessage.getHeaders();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            int contentLength;
            if (object instanceof String && JSON.isValidObject((String) object)) {
                byte[] strBytes = ((String) object).getBytes(getFastJsonConfig().getCharset());
                contentLength = strBytes.length;
                baos.write(strBytes, 0, strBytes.length);
            } else if (object instanceof byte[] && JSON.isValid((byte[]) object)) {
                byte[] strBytes = (byte[]) object;
                contentLength = strBytes.length;
                baos.write(strBytes, 0, strBytes.length);
            } else {
                if (object instanceof JSONPObject) {
                    headers.setContentType(APPLICATION_JAVASCRIPT);
                }

                contentLength = writeTo(
                        baos, object,
                        getFastJsonConfig().getDateFormat(),
                        getFastJsonConfig().getWriterFilters(),
                        getFastJsonConfig().getWriterFeatures()
                );
            }

            if (headers.getContentLength() < 0 && getFastJsonConfig().isWriteContentLength()) {
                headers.setContentLength(contentLength);
            }

            baos.writeTo(outputMessage.getBody());
        } catch (JSONException ex) {
            throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
        } catch (IOException ex) {
            throw new HttpMessageNotWritableException("I/O error while writing output message", ex);
        }
    }

    private static int writeTo(
            OutputStream out,
            Object object,
            String format,
            Filter[] filters,
            JSONWriter.Feature... features
    ) {
        final JSONWriter.Context context = new JSONWriter.Context(STRUCT_ATTRIBUTE_OBJ_WRITER_PROVIDER, features);
        if (format != null && !format.isEmpty()) {
            context.setDateFormat(format);
        }
        if (filters != null && filters.length != 0) {
            context.configFilter(filters);
        }

        try (JSONWriter writer = JSONWriter.ofUTF8(context)) {
            if (object == null) {
                writer.writeNull();
            } else {
                writer.setRootObject(object);
//                writer.setPath("",JSONWriter.Path.ROOT);// = JSONWriter.Path.ROOT;

                Class<?> valueClass = object.getClass();
                ObjectWriter<?> objectWriter = context.getObjectWriter(valueClass, valueClass);
                objectWriter.write(writer, object, null, null, 0);
            }

            return writer.flushTo(out);
        } catch (Exception e) {
            throw new JSONException("JSON#writeTo cannot serialize '" + object + "' to 'OutputStream'", e);
        }
    }
}

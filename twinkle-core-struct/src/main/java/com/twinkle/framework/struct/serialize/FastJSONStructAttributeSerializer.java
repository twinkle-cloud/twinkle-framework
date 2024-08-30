//package com.twinkle.framework.struct.serialize;
//
//import com.twinkle.framework.asm.serialize.Serializer;
//import com.twinkle.framework.asm.serialize.SerializerFactory;
//import com.twinkle.framework.struct.lang.StructAttribute;
//
//import java.io.IOException;
//import java.lang.reflect.Type;
//import java.util.IdentityHashMap;
//import java.util.Map;
//
///**
// * Function: TODO ADD FUNCTION. <br/>
// * Reason:	 TODO ADD REASON. <br/>
// * Date:     9/14/19 5:23 PM<br/>
// *
// * @author chenxj
// * @see
// * @since JDK 1.8
// */
//public final class FastJSONStructAttributeSerializer implements ObjectSerializer {
//    private Map<String, JsonSerializer> serializerMap;
//
//    public FastJSONStructAttributeSerializer() {
//        this.serializerMap = new IdentityHashMap<>(16);
//    }
//    @Override
//    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
//        SerializeWriter out = serializer.getWriter();
//        if (object == null) {
//            serializer.getWriter().writeNull();
//            return;
//        }
//        StructAttribute tempAttr = (StructAttribute) object;
//        String tempRootType = tempAttr.getType().getQualifiedName();
//        JsonSerializer tempJsonSerializer = this.serializerMap.get(tempRootType);
//        if (tempJsonSerializer == null) {
//            SerializerFactory tempFactory = new JsonIntrospectionSerializerFactory();
//            Serializer tempSerializer = tempFactory.getSerializer(tempRootType);
//            tempJsonSerializer = (JsonSerializer) tempSerializer;
//            this.serializerMap.put(tempRootType, tempJsonSerializer);
//        }
//        tempJsonSerializer.write(tempAttr, out);
//    }
//}

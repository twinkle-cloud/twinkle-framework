package com.twinkle.framework.struct.serialize;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONReader;
import com.twinkle.framework.core.lang.util.MutableArray;
import com.twinkle.framework.struct.context.StructAttributeSchema;
import com.twinkle.framework.struct.context.StructAttributeSchemaManager;
import com.twinkle.framework.struct.error.StructAttributeException;
import com.twinkle.framework.struct.factory.StructAttributeFactory;
import com.twinkle.framework.struct.lang.StructAttribute;
import com.twinkle.framework.struct.ref.AttributeRef;
import com.twinkle.framework.struct.type.*;
import com.twinkle.framework.struct.util.ArrayAllocator;
import com.twinkle.framework.struct.util.MutableStructAttributeArray;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/5/19 1:44 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class IntrospectionDeserializer extends AbstractDeserializer {
    public static final int DEFAULT_CAPACITY = 8;
    private final StructAttributeFactory structAttributeFactory = Objects.requireNonNull(StructAttributeSchemaManager.getStructAttributeFactory(), "StructAttributeFactory is null, deserialisation wouldn't work");
    private final StructAttributeSchema structAttributeSchema = Objects.requireNonNull(StructAttributeSchemaManager.getStructAttributeSchema(), "StructAttributeSchema is null, deserialisation wouldn't work");
    private final ArrayAllocator arrayAllocator;

    public IntrospectionDeserializer(boolean _serializable, String _rootType) {
        super(_serializable, _rootType);
        this.arrayAllocator = Objects.requireNonNull(this.structAttributeFactory.getArrayAllocator(), "ArrayAllocator is null, deserialisation wouldn't work");
    }

    @Override
    protected StructAttribute readStructAttribute(JSONReader _reader, String _property) throws IOException {
        Map<String, Object> tempObjMap = _reader.readObject();
        if (MapUtils.isEmpty(tempObjMap)) {
            log.info("Read object is empty, so return an empty struct.");
            return this.createStructAttribute(_property);
        }

        return this.packStructAttribute(tempObjMap, _property);
    }

    /**
     * Pack the Struct Attribute.
     *
     * @param _values
     * @param _property
     * @return
     */
    private StructAttribute packStructAttribute(Object _values, String _property) {
        StructAttribute tempAttr = this.createStructAttribute(_property);
        if(_values == null) {
            return tempAttr;
        }
        if(_values instanceof Map<?, ?>) {
            Map<String, Object> tempObjMap = (Map<String, Object>) _values;
            for (Map.Entry<String, Object> tempEntry : tempObjMap.entrySet()) {
                AttributeRef tempAttrRef = tempAttr.getAttributeRef(tempEntry.getKey());
                AttributeType tempStructType = tempAttrRef.getType();
                if (!tempStructType.isPrimitiveType() && !tempStructType.isStringType()) {
                    if (tempStructType.isArrayType()) {
                        this.deserializeArrayAttribute(tempAttr, tempAttrRef, tempStructType, tempEntry.getValue());
                    } else if (tempStructType.isStructType()) {
                        tempAttr.setStruct(tempAttrRef, this.packStructAttribute(tempEntry.getValue(), ((StructType) tempStructType).getQualifiedName()));
                    } else {
                        throw new RuntimeException("Unexpected type: " + tempStructType);
                    }
                } else {
                    //
                    this.deserializePrimitiveAttribute(tempAttr, tempAttrRef, tempStructType, tempEntry.getValue());
                }
            }
        } else {
            log.debug("The given values[{}] is not suitable for struct attribute, so dismiss it.", _values);
            return tempAttr;
        }
        return tempAttr;
    }

    /**
     * Deserialize the array struct attribute.
     *
     * @param _attr
     * @param _attrRef
     * @param _type
     * @param _array
     * @throws IOException
     */
    private void deserializeArrayAttribute(StructAttribute _attr, AttributeRef _attrRef, AttributeType _type, Object _array) {
        if (_array == null) {
            _attr.setArray(_attrRef, this.arrayAllocator.newArray((ArrayType)_type, 0));
            return;
        }
        if(!(_array instanceof JSONArray)) {
            log.debug("The given value[{}] is not array, so dismiss it.", _array);
            _attr.setArray(_attrRef, this.arrayAllocator.newArray((ArrayType)_type, 0));
            return;
        }

        if (((ArrayType)_type).getElementType() instanceof StructType){
            String tempElementName = ((StructType)((ArrayType)_type).getElementType()).getQualifiedName();
            MutableStructAttributeArray tempArray = this.arrayAllocator.newStructAttributeArray(((JSONArray)_array).size());
            for(int i = 0; i < ((JSONArray)_array).size(); ++i) {
                tempArray.add(this.packStructAttribute(((JSONArray)_array).get(i), tempElementName));
            }
            _attr.setArray(_attrRef, tempArray);
        } else {
            MutableArray tempArray = this.arrayAllocator.newArray((ArrayType)_type, ((JSONArray)_array).size());
            tempArray.copyFrom(((JSONArray)_array).toArray(), 0, ((JSONArray)_array).size() -1, ((JSONArray)_array).size());
            _attr.setArray(_attrRef, tempArray);
        }
    }

    /**
     * Deserialize primitive attribute.
     *
     * @param _attr
     * @param _attrRef
     * @param _type
     * @param _obj
     * @throws IOException
     */
    private void deserializePrimitiveAttribute(StructAttribute _attr, AttributeRef _attrRef, AttributeType _type, Object _obj) {
        if (_obj == null) {
            return;
        }
        if (_type == PrimitiveType.BYTE) {
            byte tempValue = _obj instanceof Integer ? ((Integer) _obj).byteValue() : Byte.parseByte(_obj.toString());
            _attr.setByte(_attrRef, tempValue);
        } else if (_type == PrimitiveType.SHORT) {
            short tempValue = _obj instanceof Integer ? ((Integer) _obj).shortValue() : Short.parseShort(_obj.toString());
            _attr.setShort(_attrRef, tempValue);
        } else if (_type == PrimitiveType.INT) {
            int tempValue = _obj instanceof Integer ? (Integer) _obj : Integer.parseInt(_obj.toString());
            _attr.setInt(_attrRef, tempValue);
        } else if (_type == PrimitiveType.LONG) {
            long tempValue = _obj instanceof Long ? (Long) _obj : Long.parseLong(_obj.toString());
            _attr.setLong(_attrRef, tempValue);
        } else if (_type == PrimitiveType.FLOAT) {
            float tempValue = _obj instanceof Double ? ((Double) _obj).floatValue() : Float.parseFloat(_obj.toString());
            _attr.setFloat(_attrRef, tempValue);
        } else if (_type == PrimitiveType.DOUBLE) {
            double tempValue = _obj instanceof Double ? (Double) _obj : Double.parseDouble(_obj.toString());
            _attr.setDouble(_attrRef, tempValue);
        } else if (_type == PrimitiveType.BOOLEAN) {
            _attr.setBoolean(_attrRef, Boolean.parseBoolean(_obj.toString()));
        } else if (_type == PrimitiveType.CHAR) {
            _attr.setChar(_attrRef, _obj.toString().charAt(0));
        } else if (_type == StringType.STRING) {
            _attr.setString(_attrRef, _obj.toString());
        } else {
            throw new RuntimeException("Unexpected type: " + _type);
        }
    }

    protected StructAttribute createStructAttribute(String _attrName) throws StructAttributeException {
        return this.createStructAttribute(this.structAttributeSchema.getStructAttributeType(_attrName));
    }

    protected StructAttribute createStructAttribute(StructType _attrType) throws StructAttributeException {
        return this.structAttributeFactory.newStructAttribute(_attrType);
    }
}

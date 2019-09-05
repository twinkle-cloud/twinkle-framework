package com.twinkle.framework.core.datastruct.serialize;

import com.alibaba.fastjson.JSONReader;
import com.twinkle.framework.core.context.StructAttributeManager;
import com.twinkle.framework.core.context.StructAttributeSchema;
import com.twinkle.framework.core.error.StructAttributeException;
import com.twinkle.framework.core.lang.ref.AttributeRef;
import com.twinkle.framework.core.lang.struct.*;
import com.twinkle.framework.core.lang.util.*;

import java.io.IOException;
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
public class IntrospectionDeserializer extends AbstractDeserializer {
    public static final int DEFAULT_CAPACITY = 8;
    private final StructAttributeFactory structAttributeFactory = Objects.requireNonNull(StructAttributeManager.getStructAttributeFactory(), "StructAttributeFactory is null, deserialisation wouldn't work");
    private final StructAttributeSchema structAttributeSchema = Objects.requireNonNull(StructAttributeManager.getStructAttributeSchema(), "StructAttributeSchema is null, deserialisation wouldn't work");
    private final ArrayAllocator arrayAllocator;

    public IntrospectionDeserializer(boolean _serializable, String _rootType) {
        super(_serializable, _rootType);
        this.arrayAllocator = Objects.requireNonNull(this.structAttributeFactory.getArrayAllocator(), "ArrayAllocator is null, deserialisation wouldn't work");
    }
    @Override
    protected StructAttribute readStructAttribute(JSONReader _reader, String _property) throws IOException {
        StructAttribute tempAttr = this.createStructAttribute(_property);
        _reader.startObject();

        while(true) {
            while(_reader.hasNext()) {
                String tempName = _reader.readString();
                AttributeRef tempAttrRef = tempAttr.getAttributeRef(tempName);
                StructType tempStructType = tempAttrRef.getType();
                if (!tempStructType.isPrimitiveType() && !tempStructType.isStringType()) {
                    if (tempStructType.isArrayType()) {
                        this.deserializeArrayAttribute(tempAttr, tempAttrRef, tempStructType, _reader);
                    } else if (tempStructType.isStructType()) {
                        tempAttr.setStruct(tempAttrRef, this.readStructAttribute(_reader, ((StructAttributeType)tempStructType).getQualifiedName()));
                    } else {
                        throw new RuntimeException("Unexpected type: " + tempStructType);
                    }
                } else {
                    this.deserializePrimitiveAttribute(tempAttr, tempAttrRef, tempStructType, _reader);
                }
            }
            _reader.endObject();
            return tempAttr;
        }
    }

    /**
     * Deserialize the array struct attribute.
     *
     * @param _attr
     * @param _attrRef
     * @param _type
     * @param _reader
     * @throws IOException
     */
    private void deserializeArrayAttribute(StructAttribute _attr, AttributeRef _attrRef, StructType _type, JSONReader _reader) throws IOException {
        _reader.startArray();
        if (_type == ArrayType.BYTE_ARRAY) {
            MutableByteArray tempArray = this.arrayAllocator.newByteArray(DEFAULT_CAPACITY);
            while(_reader.hasNext()) {
                tempArray.add(_reader.readInteger().byteValue());
            }
            _attr.setArray(_attrRef, tempArray);
        } else if (_type == ArrayType.SHORT_ARRAY) {
            MutableShortArray tempArray = this.arrayAllocator.newShortArray(DEFAULT_CAPACITY);
            while(_reader.hasNext()) {
                tempArray.add(_reader.readInteger().shortValue());
            }
            _attr.setArray(_attrRef, tempArray);
        } else if (_type == ArrayType.INT_ARRAY) {
            MutableIntegerArray tempArray = this.arrayAllocator.newIntegerArray(DEFAULT_CAPACITY);
            while(_reader.hasNext()) {
                tempArray.add(_reader.readInteger());
            }
            _attr.setArray(_attrRef, tempArray);
        } else if (_type == ArrayType.LONG_ARRAY) {
            MutableLongArray tempArray = this.arrayAllocator.newLongArray(DEFAULT_CAPACITY);
            while(_reader.hasNext()) {
                tempArray.add(_reader.readLong());
            }
            _attr.setArray(_attrRef, tempArray);
        } else if (_type == ArrayType.FLOAT_ARRAY) {
            MutableFloatArray tempArray = this.arrayAllocator.newFloatArray(DEFAULT_CAPACITY);
            while(_reader.hasNext()) {
                tempArray.add(Float.valueOf(_reader.readString()));
            }
            _attr.setArray(_attrRef, tempArray);
        } else if (_type == ArrayType.DOUBLE_ARRAY) {
            MutableDoubleArray tempArray = this.arrayAllocator.newDoubleArray(DEFAULT_CAPACITY);
            while(_reader.hasNext()) {
                tempArray.add(Double.valueOf(_reader.readString()));
            }
            _attr.setArray(_attrRef, tempArray);
        } else if (_type == ArrayType.CHAR_ARRAY) {
            MutableCharArray tempArray = this.arrayAllocator.newCharArray(DEFAULT_CAPACITY);
            while(_reader.hasNext()) {
                tempArray.add(_reader.readString().charAt(0));
            }
            _attr.setArray(_attrRef, tempArray);
        } else if (_type == ArrayType.BOOLEAN_ARRAY) {
            MutableBooleanArray tempArray = this.arrayAllocator.newBooleanArray(DEFAULT_CAPACITY);
            while(_reader.hasNext()) {
                tempArray.add(Boolean.valueOf(_reader.readString()));
            }
            _attr.setArray(_attrRef, tempArray);
        } else if (_type == ArrayType.STRING_ARRAY) {
            MutableStringArray tempArray = this.arrayAllocator.newStringArray(DEFAULT_CAPACITY);
            while(_reader.hasNext()) {
                tempArray.add(_reader.readString());
            }
            _attr.setArray(_attrRef, tempArray);
        } else if (_type.isArrayType()) {
            ArrayType tempArrayType = (ArrayType)_type;
            String tempElementName = ((StructAttributeType)tempArrayType.getElementType()).getQualifiedName();
            MutableStructAttributeArray tempArray = this.arrayAllocator.newStructAttributeArray(DEFAULT_CAPACITY);
            while(_reader.hasNext()) {
                tempArray.add(this.readStructAttribute(_reader, tempElementName));
            }
            _attr.setArray(_attrRef, tempArray);
        } else {
            throw new RuntimeException("Unexpected type: " + _type);
        }
        _reader.endArray();
    }

    /**
     * Deserialize primitive attribute.
     *
     * @param _attr
     * @param _attrRef
     * @param _type
     * @param _reader
     * @throws IOException
     */
    private void deserializePrimitiveAttribute(StructAttribute _attr, AttributeRef _attrRef, StructType _type, JSONReader _reader) throws IOException {
        if (_type == PrimitiveType.BYTE) {
            _attr.setByte(_attrRef, (_reader.readInteger()).byteValue());
        } else if (_type == PrimitiveType.SHORT) {
            _attr.setShort(_attrRef, (_reader.readInteger()).shortValue());
        } else if (_type == PrimitiveType.INT) {
            _attr.setInt(_attrRef, _reader.readInteger());
        } else if (_type == PrimitiveType.LONG) {
            _attr.setLong(_attrRef, _reader.readLong());
        } else if (_type == PrimitiveType.FLOAT) {
            _attr.setFloat(_attrRef, Float.valueOf(_reader.readString()));
        } else if (_type == PrimitiveType.DOUBLE) {
            _attr.setDouble(_attrRef, Double.valueOf(_reader.readString()));
        } else if (_type == PrimitiveType.BOOLEAN) {
            _attr.setBoolean(_attrRef, Boolean.valueOf(_reader.readString()));
        } else if (_type == PrimitiveType.CHAR) {
            _attr.setChar(_attrRef, _reader.readString().charAt(0));
        } else if (_type == StringType.STRING) {
            _attr.setString(_attrRef, _reader.readString());
        } else {
            throw new RuntimeException("Unexpected type: " + _type);
        }
    }

    protected StructAttribute createStructAttribute(String _attrName) throws StructAttributeException {
        return this.createStructAttribute(this.structAttributeSchema.getStructAttributeType(_attrName));
    }

    protected StructAttribute createStructAttribute(StructAttributeType _attrType) throws StructAttributeException {
        return this.structAttributeFactory.newStructAttribute(_attrType);
    }
}

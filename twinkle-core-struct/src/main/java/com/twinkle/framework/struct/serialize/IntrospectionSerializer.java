package com.twinkle.framework.struct.serialize;

import com.alibaba.fastjson2.JSONWriter;
import com.twinkle.framework.core.lang.util.*;
import com.twinkle.framework.struct.asm.descriptor.SAAttributeDescriptor;
import com.twinkle.framework.struct.lang.StructAttribute;
import com.twinkle.framework.struct.ref.AttributeRef;
import com.twinkle.framework.struct.type.*;
import com.twinkle.framework.struct.util.StructAttributeArray;

import java.io.IOException;
import java.util.Iterator;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/5/19 11:42 AM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class IntrospectionSerializer extends AbstractSerializer {
    public IntrospectionSerializer(boolean _serializable, String _rootType) {
        super(_serializable, _rootType);
    }

    @Override
    protected void writeStructAttribute(StructAttribute _attr, JSONWriter _writer) throws IOException {
        if (_attr == null) {
            _writer.writeAny(null);//writeValue
        } else {
            StructType _tempSAType = _attr.getType();
            _writer.startObject();
            Iterator<SAAttributeDescriptor> tempAttrDescriptorItr = _tempSAType.getAttributes();

            while (tempAttrDescriptorItr.hasNext()) {
                this.serializeAttribute(_attr, _attr.getAttributeRef((tempAttrDescriptorItr.next()).getName()), _writer);
            }

            _writer.endObject();
        }
    }

    /**
     * Serialize the StructAttribute.
     *
     * @param _attr
     * @param _attrRef
     * @param _writer
     * @throws IOException
     */
    private void serializeAttribute(StructAttribute _attr, AttributeRef _attrRef, JSONWriter _writer) throws IOException {
        if (_attr.isAttributeSet(_attrRef)) {
            _writer.writeName(_attrRef.getName());//writeKey        _writer.writeName(_attrRef.getName());
            _writer.writeColon();
            AttributeType _tempType = _attrRef.getType();
            if (!_tempType.isPrimitiveType() && !_tempType.isStringType()) {
                if (_tempType.isArrayType()) {
                    this.serializeArrayAttribute(_attr, _attrRef, _tempType, _writer);
                } else if (_tempType.isStructType()) {
                    this.writeStructAttribute(_attr.getStruct(_attrRef), _writer);
                } else {
                    throw new RuntimeException("Unexpected type: " + _tempType);
                }
            } else {
                this.serializePrimitiveAttribute(_attr, _attrRef, _tempType, _writer);
            }
        }
    }

    /**
     * Serialize the primitive value.
     *
     * @param _attr
     * @param _attrRef
     * @param _structType
     * @param _writer
     * @throws IOException
     */
    private void serializePrimitiveAttribute(StructAttribute _attr, AttributeRef _attrRef, AttributeType _structType, JSONWriter _writer) throws IOException {
        if (_structType == PrimitiveType.BYTE) {
            _writer.writeInt64(_attr.getByte(_attrRef));
        } else if (_structType == PrimitiveType.SHORT) {
            _writer.writeInt64(_attr.getShort(_attrRef));
        } else if (_structType == PrimitiveType.INT) {
            _writer.writeInt64(_attr.getInt(_attrRef));
        } else if (_structType == PrimitiveType.LONG) {
            _writer.writeInt64(_attr.getLong(_attrRef));
        } else if (_structType == PrimitiveType.FLOAT) {
            _writer.writeDouble(_attr.getFloat(_attrRef));
        } else if (_structType == PrimitiveType.DOUBLE) {
            _writer.writeDouble(_attr.getDouble(_attrRef));
        } else if (_structType == PrimitiveType.BOOLEAN) {
            _writer.writeBool(_attr.getBoolean(_attrRef));
        } else if (_structType == PrimitiveType.CHAR) {
            _writer.writeString(Character.toString(_attr.getChar(_attrRef)));
        } else if (_structType == StringType.STRING) {
            _writer.writeString(_attr.getString(_attrRef));
        } else {
            throw new RuntimeException("Unexpected type: " + _structType);
        }

    }

    /**
     * Serialize the array attribute.
     *
     * @param _attr
     * @param _attrRef
     * @param _structType
     * @param _writer
     * @throws IOException
     */
    private void serializeArrayAttribute(StructAttribute _attr, AttributeRef _attrRef, AttributeType _structType, JSONWriter _writer) throws IOException {
        _writer.startArray();
        if (_structType == ArrayType.BYTE_ARRAY) {
            MutableByteArray tempArray = (MutableByteArray) _attr.getArray(_attrRef);
            _writer.writeInt8(tempArray.array());
        } else if (_structType == ArrayType.SHORT_ARRAY) {
            MutableShortArray tempArray = (MutableShortArray) _attr.getArray(_attrRef);
            _writer.writeInt16(tempArray.array());
        } else if (_structType == ArrayType.INT_ARRAY) {
            MutableIntegerArray tempArray = (MutableIntegerArray) _attr.getArray(_attrRef);
            _writer.writeInt32(tempArray.array());
        } else if (_structType == ArrayType.LONG_ARRAY) {
            MutableLongArray tempArray = (MutableLongArray) _attr.getArray(_attrRef);
            _writer.writeInt64(tempArray.array());
        } else if (_structType == ArrayType.FLOAT_ARRAY) {
            MutableFloatArray tempArray = (MutableFloatArray) _attr.getArray(_attrRef);
            _writer.writeFloat(tempArray.array());
        } else if (_structType == ArrayType.DOUBLE_ARRAY) {
            MutableDoubleArray tempArray = (MutableDoubleArray) _attr.getArray(_attrRef);
            _writer.writeDouble(tempArray.array());
        } else if (_structType == ArrayType.CHAR_ARRAY) {
            MutableCharArray tempArray = (MutableCharArray) _attr.getArray(_attrRef);
            _writer.writeString(tempArray.array());
        } else if (_structType == ArrayType.BOOLEAN_ARRAY) {
            MutableBooleanArray tempArray = (MutableBooleanArray) _attr.getArray(_attrRef);
            _writer.writeBool(tempArray.array());
        } else if (_structType == ArrayType.STRING_ARRAY) {
            MutableStringArray tempArray = (MutableStringArray) _attr.getArray(_attrRef);
            _writer.writeString(tempArray.array());
        } else if (_structType.isArrayType()) {
            StructAttributeArray tempArray = (StructAttributeArray) _attr.getArray(_attrRef);
            for (int i = 0; i < tempArray.length(); ++i) {
                this.writeStructAttribute(tempArray.get(i), _writer);
            }
        } else {
            throw new RuntimeException("Unexpected type: " + _structType);
        }

        _writer.endArray();
    }
}

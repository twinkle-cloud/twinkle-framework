package com.twinkle.framework.core.datastruct.serialize;

import com.alibaba.fastjson.JSONWriter;
import com.twinkle.framework.core.datastruct.descriptor.SAAttributeDescriptor;
import com.twinkle.framework.core.lang.ref.AttributeRef;
import com.twinkle.framework.core.lang.struct.*;
import com.twinkle.framework.core.lang.util.*;

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
            _writer.writeValue(null);
        } else {
            StructAttributeType _tempSAType = _attr.getType();
            _writer.startObject();
            Iterator<SAAttributeDescriptor> tempAttrDesriptorItr = _tempSAType.getAttributes();

            while (tempAttrDesriptorItr.hasNext()) {
                this.serializeAttribute(_attr, _attr.getAttributeRef((tempAttrDesriptorItr.next()).getName()), _writer);
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
            _writer.writeKey(_attrRef.getName());
            StructType _tempType = _attrRef.getType();
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
    private void serializePrimitiveAttribute(StructAttribute _attr, AttributeRef _attrRef, StructType _structType, JSONWriter _writer) throws IOException {
        if (_structType == PrimitiveType.BYTE) {
            _writer.writeValue((long) _attr.getByte(_attrRef));
        } else if (_structType == PrimitiveType.SHORT) {
            _writer.writeValue((long) _attr.getShort(_attrRef));
        } else if (_structType == PrimitiveType.INT) {
            _writer.writeValue((long) _attr.getInt(_attrRef));
        } else if (_structType == PrimitiveType.LONG) {
            _writer.writeValue(_attr.getLong(_attrRef));
        } else if (_structType == PrimitiveType.FLOAT) {
            _writer.writeValue((double) _attr.getFloat(_attrRef));
        } else if (_structType == PrimitiveType.DOUBLE) {
            _writer.writeValue(_attr.getDouble(_attrRef));
        } else if (_structType == PrimitiveType.BOOLEAN) {
            _writer.writeValue(_attr.getBoolean(_attrRef));
        } else if (_structType == PrimitiveType.CHAR) {
            _writer.writeValue(Character.toString(_attr.getChar(_attrRef)));
        } else if (_structType == StringType.STRING) {
            _writer.writeValue(_attr.getString(_attrRef));
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
    private void serializeArrayAttribute(StructAttribute _attr, AttributeRef _attrRef, StructType _structType, JSONWriter _writer) throws IOException {
        _writer.startArray();
        if (_structType == ArrayType.BYTE_ARRAY) {
            ByteArray tempArray = (ByteArray) _attr.getArray(_attrRef);
            for (int i = 0; i < tempArray.length(); ++i) {
                _writer.writeValue((long) tempArray.get(i));
            }
        } else if (_structType == ArrayType.SHORT_ARRAY) {
            ShortArray tempArray = (ShortArray) _attr.getArray(_attrRef);
            for (int i = 0; i < tempArray.length(); ++i) {
                _writer.writeValue((long) tempArray.get(i));
            }
        } else if (_structType == ArrayType.INT_ARRAY) {
            IntegerArray tempArray = (IntegerArray) _attr.getArray(_attrRef);
            for (int i = 0; i < tempArray.length(); ++i) {
                _writer.writeValue((long) tempArray.get(i));
            }
        } else if (_structType == ArrayType.LONG_ARRAY) {
            LongArray tempArray = (LongArray) _attr.getArray(_attrRef);
            for (int i = 0; i < tempArray.length(); ++i) {
                _writer.writeValue(tempArray.get(i));
            }
        } else if (_structType == ArrayType.FLOAT_ARRAY) {
            FloatArray tempArray = (FloatArray) _attr.getArray(_attrRef);
            for (int i = 0; i < tempArray.length(); ++i) {
                _writer.writeValue((double) tempArray.get(i));
            }
        } else if (_structType == ArrayType.DOUBLE_ARRAY) {
            DoubleArray tempArray = (DoubleArray) _attr.getArray(_attrRef);
            for (int i = 0; i < tempArray.length(); ++i) {
                _writer.writeValue(tempArray.get(i));
            }
        } else if (_structType == ArrayType.CHAR_ARRAY) {
            CharArray tempArray = (CharArray) _attr.getArray(_attrRef);
            for (int i = 0; i < tempArray.length(); ++i) {
                _writer.writeValue(Character.toString(tempArray.get(i)));
            }
        } else if (_structType == ArrayType.BOOLEAN_ARRAY) {
            BooleanArray tempArray = (BooleanArray) _attr.getArray(_attrRef);
            for (int i = 0; i < tempArray.length(); ++i) {
                _writer.writeValue(tempArray.get(i));
            }
        } else if (_structType == ArrayType.STRING_ARRAY) {
            StringArray tempArray = (StringArray) _attr.getArray(_attrRef);
            for (int i = 0; i < tempArray.length(); ++i) {
                _writer.writeValue(tempArray.get(i));
            }
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

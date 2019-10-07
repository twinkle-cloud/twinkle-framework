package com.twinkle.framework.struct.serialize;

import com.alibaba.fastjson.JSONWriter;
import com.twinkle.framework.asm.serialize.Serializer;
import com.twinkle.framework.core.lang.util.*;
import com.twinkle.framework.struct.error.SerializationException;
import com.twinkle.framework.struct.lang.StructAttribute;
import com.twinkle.framework.struct.util.StructAttributeArray;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/4/19 5:23 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public abstract class AbstractSchemaBasedSerializer extends AbstractSerializer {
    protected Map<String, Serializer> serializers = new HashMap<>();

    public AbstractSchemaBasedSerializer(boolean _serializable, String _rootType) {
        super(_serializable, _rootType);
        this.initSerializers();
    }

    protected abstract void initSerializers();

    @Override
    protected void writeStructAttribute(StructAttribute _attr, JSONWriter _writer) throws IOException {
        if (_attr == null) {
            _writer.writeValue(null);
            return;
        }
        String tempName = _attr.getType().getQualifiedName();
        if (tempName.equals(this.rootType)) {
            _writer.startObject();
            this.writeFields(_writer, _attr);
            _writer.endObject();
        } else {
            AbstractSerializer tempSerializer = (AbstractSerializer) this.serializers.get(tempName);
            if (tempSerializer == null) {
                throw new SerializationException("No serializer found for: " + tempName);
            }

            tempSerializer.writeStructAttribute(_attr, _writer);
        }
    }

    protected abstract void writeFields(JSONWriter _writer, StructAttribute _value) throws IOException;

    protected void writeProperty(JSONWriter _writer, String _keyName, byte _value) throws IOException {
        _writer.writeKey(_keyName);
        _writer.writeValue((long) _value);
    }

    protected void writeProperty(JSONWriter _writer, String _keyName, short _value) throws IOException {
        _writer.writeKey(_keyName);
        _writer.writeValue((long) _value);
    }

    protected void writeProperty(JSONWriter _writer, String _keyName, int _value) throws IOException {
        _writer.writeKey(_keyName);
        _writer.writeValue((long) _value);
    }

    protected void writeProperty(JSONWriter _writer, String _keyName, long _value) throws IOException {
        _writer.writeKey(_keyName);
        _writer.writeValue(_value);
    }

    protected void writeProperty(JSONWriter _writer, String _keyName, float _value) throws IOException {
        _writer.writeKey(_keyName);
        _writer.writeValue((double) _value);
    }

    protected void writeProperty(JSONWriter _writer, String _keyName, double _value) throws IOException {
        _writer.writeKey(_keyName);
        _writer.writeValue(_value);
    }

    protected void writeProperty(JSONWriter _writer, String _keyName, char _value) throws IOException {
        _writer.writeKey(_keyName);
        _writer.writeValue(String.valueOf(_value));
    }

    protected void writeProperty(JSONWriter _writer, String _keyName, boolean _value) throws IOException {
        _writer.writeKey(_keyName);
        _writer.writeValue(_value);
    }

    protected void writeProperty(JSONWriter _writer, String _keyName, String _value) throws IOException {
        _writer.writeKey(_keyName);
        _writer.writeValue(_value);
    }

    protected void writeProperty(JSONWriter _writer, String _keyName, StructAttribute _value) throws IOException {
        _writer.writeKey(_keyName);
        this.writeStructAttribute(_value, _writer);
    }

    protected void writeProperty(JSONWriter _writer, String _keyName, Array _value) throws IOException {
        _writer.writeKey(_keyName);
        _writer.startArray();
        if (_value instanceof ByteArray) {
            this.writeArray(_writer, (ByteArray) _value);
        } else if (_value instanceof ShortArray) {
            this.writeArray(_writer, (ShortArray) _value);
        } else if (_value instanceof IntegerArray) {
            this.writeArray(_writer, (IntegerArray) _value);
        } else if (_value instanceof LongArray) {
            this.writeArray(_writer, (LongArray) _value);
        } else if (_value instanceof FloatArray) {
            this.writeArray(_writer, (FloatArray) _value);
        } else if (_value instanceof DoubleArray) {
            this.writeArray(_writer, (DoubleArray) _value);
        } else if (_value instanceof CharArray) {
            this.writeArray(_writer, (CharArray) _value);
        } else if (_value instanceof StringArray) {
            this.writeArray(_writer, (StringArray) _value);
        } else if (_value instanceof BooleanArray) {
            this.writeArray(_writer, (BooleanArray) _value);
        } else {
            if (!(_value instanceof StructAttributeArray)) {
                throw new RuntimeException("Unknown array type: " + _value.getClass());
            }

            this.writeArray(_writer, (StructAttributeArray) _value);
        }

        _writer.endArray();
    }

    protected void writeArray(JSONWriter _writer, ByteArray _array) throws IOException {
        for (int i = 0; i < _array.length(); ++i) {
            _writer.writeValue((long) _array.get(i));
        }

    }

    protected void writeArray(JSONWriter _writer, ShortArray _array) throws IOException {
        for (int i = 0; i < _array.length(); ++i) {
            _writer.writeValue((long) _array.get(i));
        }

    }

    protected void writeArray(JSONWriter _writer, IntegerArray _array) throws IOException {
        for (int i = 0; i < _array.length(); ++i) {
            _writer.writeValue((long) _array.get(i));
        }

    }

    protected void writeArray(JSONWriter _writer, LongArray _array) throws IOException {
        for (int i = 0; i < _array.length(); ++i) {
            _writer.writeValue(_array.get(i));
        }

    }

    protected void writeArray(JSONWriter _writer, FloatArray _array) throws IOException {
        for (int i = 0; i < _array.length(); ++i) {
            _writer.writeValue((double) _array.get(i));
        }

    }

    protected void writeArray(JSONWriter _writer, DoubleArray _array) throws IOException {
        for (int i = 0; i < _array.length(); ++i) {
            _writer.writeValue(_array.get(i));
        }

    }

    protected void writeArray(JSONWriter _writer, CharArray _array) throws IOException {
        for (int i = 0; i < _array.length(); ++i) {
            _writer.writeValue(Character.toString(_array.get(i)));
        }

    }

    protected void writeArray(JSONWriter _writer, StringArray _array) throws IOException {
        for (int i = 0; i < _array.length(); ++i) {
            _writer.writeValue(_array.get(i));
        }

    }

    protected void writeArray(JSONWriter _writer, BooleanArray _array) throws IOException {
        for (int i = 0; i < _array.length(); ++i) {
            _writer.writeValue(_array.get(i));
        }

    }

    protected void writeArray(JSONWriter _writer, StructAttributeArray _array) throws IOException {
        for (int i = 0; i < _array.length(); ++i) {
            this.writeStructAttribute(_array.get(i), _writer);
        }

    }

    protected void writeProperty(JSONWriter _writer, String _keyName, Object _value) throws IOException {
        if (_value instanceof String) {
            this.writeProperty(_writer, _keyName, (String) _value);
        } else if (_value instanceof StructAttribute) {
            this.writeProperty(_writer, _keyName, (StructAttribute) _value);
        } else if (_value instanceof Array) {
            this.writeProperty(_writer, _keyName, (Array) _value);
        } else {
            if (_value != null) {
                throw new RuntimeException("Unknown type: " + _value.getClass());
            }

            _writer.writeKey(_keyName);
            _writer.writeValue(null);
        }
    }
}

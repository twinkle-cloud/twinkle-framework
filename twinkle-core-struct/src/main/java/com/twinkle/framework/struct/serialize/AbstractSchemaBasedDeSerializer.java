package com.twinkle.framework.struct.serialize;

import com.alibaba.fastjson.JSONReader;
import com.twinkle.framework.asm.factory.BeanFactory;
import com.twinkle.framework.struct.type.StructAttribute;
import com.twinkle.framework.core.lang.util.*;
import com.twinkle.framework.struct.util.ArrayAllocator;
import com.twinkle.framework.struct.util.MutableStructAttributeArray;
import com.twinkle.framework.struct.util.StructAttributeArray;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/4/19 6:45 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public abstract class AbstractSchemaBasedDeSerializer extends AbstractDeserializer {
    public static final int INITIAL_CAPACITY = 8;
    protected Map<String, AbstractSchemaBasedDeSerializer> deserializers = new HashMap();
    protected BeanFactory beanFactory;
    protected ArrayAllocator arrayAllocator;

    public AbstractSchemaBasedDeSerializer(boolean _serializable, String _rootType) {
        super(_serializable, _rootType);
        this.initDeserializers();
    }

    public void setStructAttributeFactory(BeanFactory _factory, ArrayAllocator _allocator) {
        this.beanFactory = _factory;
        this.arrayAllocator = _allocator;
        Iterator<AbstractSchemaBasedDeSerializer> tempItr = this.deserializers.values().iterator();
        while(tempItr.hasNext()) {
            AbstractSchemaBasedDeSerializer tempDeserializer = tempItr.next();
            tempDeserializer.setStructAttributeFactory(_factory, _allocator);
        }

    }

    protected abstract void initDeserializers();
    @Override
    protected StructAttribute readStructAttribute(JSONReader _reader, String _property) throws IOException {
        if (_property == null && this.rootType == null) {
            throw new RuntimeException("No type specified");
        } else if (_property != null && !_property.equals(this.rootType)) {
            AbstractSchemaBasedDeSerializer tempDeserializer = this.deserializers.get(_property);
            if (tempDeserializer == null) {
                throw new RuntimeException("No deserializer found for type: '" + _property + "'");
            } else {
                return tempDeserializer.readStructAttribute(_reader, _property);
            }
        } else {
            StructAttribute tempAttr = this.newStructAttribute();
            _reader.startObject();

            while(_reader.hasNext()) {
                this.onProperty(_reader.readString(), _reader, tempAttr);
            }
            _reader.endObject();
            return tempAttr;
        }
    }

    protected StructAttribute newStructAttribute() {
        return (StructAttribute)this.beanFactory.newInstance(this.rootType);
    }

    protected abstract void onProperty(String _attrName, JSONReader _reader, StructAttribute _attr) throws IOException;

    protected void unknownProperty(String _property) {
        throw new RuntimeException("Unknown property: '" + _property + "'");
    }

    protected byte readByte(JSONReader _reader) throws IOException {
        return (byte)(_reader.readInteger() & 255);
    }

    protected short readShort(JSONReader _reader) throws IOException {
        return (short)(_reader.readInteger() & '\uffff');
    }

    protected int readInt(JSONReader _reader) throws IOException {
        return _reader.readInteger();
    }

    protected long readLong(JSONReader _reader) throws IOException {
        return _reader.readLong();
    }

    protected float readFloat(JSONReader _reader) throws IOException {
        return Float.valueOf(_reader.readString());
    }

    protected double readDouble(JSONReader _reader) throws IOException {
        return Double.valueOf(_reader.readString());
    }

    protected char readChar(JSONReader _reader) throws IOException {
        return _reader.readString().charAt(0);
    }

    protected boolean readBoolean(JSONReader _reader) throws IOException {
        return Boolean.valueOf(_reader.readString());
    }

    protected String readString(JSONReader _reader) throws IOException {
        return _reader.readString();
    }

    protected ByteArray readByteArray(JSONReader _reader) throws IOException {
        MutableByteArray tempArray = this.arrayAllocator.newByteArray(INITIAL_CAPACITY);
        _reader.startArray();

        while(_reader.hasNext()) {
            tempArray.add((byte)(_reader.readInteger() & 255));
        }

        _reader.endArray();
        return tempArray;
    }

    protected ShortArray readShortArray(JSONReader _reader) throws IOException {
        MutableShortArray tempArray = this.arrayAllocator.newShortArray(INITIAL_CAPACITY);
        _reader.startArray();

        while(_reader.hasNext()) {
            tempArray.add((short)(_reader.readInteger() & '\uffff'));
        }

        _reader.endArray();
        return tempArray;
    }

    protected IntegerArray readIntegerArray(JSONReader _reader) throws IOException {
        MutableIntegerArray tempArray = this.arrayAllocator.newIntegerArray(INITIAL_CAPACITY);
        _reader.startArray();

        while(_reader.hasNext()) {
            tempArray.add(_reader.readInteger());
        }

        _reader.endArray();
        return tempArray;
    }

    protected LongArray readLongArray(JSONReader _reader) throws IOException {
        MutableLongArray tempArray = this.arrayAllocator.newLongArray(INITIAL_CAPACITY);
        _reader.startArray();

        while(_reader.hasNext()) {
            tempArray.add(_reader.readLong());
        }

        _reader.endArray();
        return tempArray;
    }

    protected FloatArray readFloatArray(JSONReader _reader) throws IOException {
        MutableFloatArray tempArray = this.arrayAllocator.newFloatArray(INITIAL_CAPACITY);
        _reader.startArray();

        while(_reader.hasNext()) {
            tempArray.add(Float.valueOf(_reader.readString()));
        }

        _reader.endArray();
        return tempArray;
    }

    protected DoubleArray readDoubleArray(JSONReader _reader) throws IOException {
        MutableDoubleArray tempArray = this.arrayAllocator.newDoubleArray(INITIAL_CAPACITY);
        _reader.startArray();

        while(_reader.hasNext()) {
            tempArray.add(Double.valueOf(_reader.readString()));
        }

        _reader.endArray();
        return tempArray;
    }

    protected CharArray readCharArray(JSONReader _reader) throws IOException {
        MutableCharArray tempArray = this.arrayAllocator.newCharArray(INITIAL_CAPACITY);
        _reader.startArray();

        while(_reader.hasNext()) {
            tempArray.add(_reader.readString().charAt(0));
        }

        _reader.endArray();
        return tempArray;
    }

    protected StringArray readStringArray(JSONReader _reader) throws IOException {
        MutableStringArray tempArray = this.arrayAllocator.newStringArray(INITIAL_CAPACITY);
        _reader.startArray();

        while(_reader.hasNext()) {
            tempArray.add(_reader.readString());
        }

        _reader.endArray();
        return tempArray;
    }

    protected BooleanArray readBooleanArray(JSONReader _reader) throws IOException {
        MutableBooleanArray tempArray = this.arrayAllocator.newBooleanArray(INITIAL_CAPACITY);
        _reader.startArray();

        while(_reader.hasNext()) {
            tempArray.add(Boolean.valueOf(_reader.readString()));
        }

        _reader.endArray();
        return tempArray;
    }

    protected StructAttributeArray readStructAttributeArray(JSONReader _reader, String _attrName) throws IOException {
        MutableStructAttributeArray tempArray = this.arrayAllocator.newStructAttributeArray(INITIAL_CAPACITY);
        _reader.startArray();

        while(_reader.hasNext()) {
            tempArray.add(this.readStructAttribute(_reader, _attrName));
        }

        _reader.endArray();
        return tempArray;
    }
}

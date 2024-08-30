package com.twinkle.framework.struct.serialize;

import com.alibaba.fastjson2.JSONReader;
import com.twinkle.framework.asm.factory.BeanFactory;
import com.twinkle.framework.core.lang.util.*;
import com.twinkle.framework.struct.lang.StructAttribute;
import com.twinkle.framework.struct.util.ArrayAllocator;
import com.twinkle.framework.struct.util.MutableStructAttributeArray;
import com.twinkle.framework.struct.util.StructAttributeArray;
import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
import java.util.*;

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
    protected Map<String, AbstractSchemaBasedDeSerializer> deserializers = new HashMap<>();
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
            while(!_reader.isEnd()) {
                this.onProperty(_reader.readString(), _reader, tempAttr);
            }
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
        return _reader.readInt8Value();
    }

    protected short readShort(JSONReader _reader) throws IOException {
//        return (short)(_reader.readInteger() & '\uffff');
        return _reader.readInt16Value();
    }

    protected int readInt(JSONReader _reader) throws IOException {
        return _reader.readInt32Value();
    }

    protected long readLong(JSONReader _reader) throws IOException {
        return _reader.readInt64Value();
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

        int[] tempResultArray = _reader.readInt32ValueArray();
        if (tempResultArray != null && tempResultArray.length > 0) {
            for (int i = 0; i < tempResultArray.length; ++i) {
                tempArray.add((byte) tempResultArray[i]);
            }
        }
        return tempArray;
    }

    protected ShortArray readShortArray(JSONReader _reader) throws IOException {
        MutableShortArray tempArray = this.arrayAllocator.newShortArray(INITIAL_CAPACITY);
        int[] tempResultArray = _reader.readInt32ValueArray();
        if (tempResultArray != null && tempResultArray.length > 0) {
            for (int i = 0; i < tempResultArray.length; ++i) {
                tempArray.add((short) tempResultArray[i]);
            }
        }
        return tempArray;
    }

    protected IntegerArray readIntegerArray(JSONReader _reader) throws IOException {
        MutableIntegerArray tempArray = this.arrayAllocator.newIntegerArray(INITIAL_CAPACITY);
        int[] tempResultArray = _reader.readInt32ValueArray();
        if (tempResultArray != null && tempResultArray.length > 0) {
            tempArray.transfer(tempResultArray, 0, 0, tempResultArray.length);
        }
        return tempArray;
    }

    protected LongArray readLongArray(JSONReader _reader) throws IOException {
        MutableLongArray tempArray = this.arrayAllocator.newLongArray(INITIAL_CAPACITY);
        long[] tempResultArray = _reader.readInt64ValueArray();
        if (tempResultArray != null && tempResultArray.length > 0) {
            tempArray.transfer(tempResultArray, 0, 0, tempResultArray.length);
        }
        return tempArray;
    }

    protected FloatArray readFloatArray(JSONReader _reader) throws IOException {
        MutableFloatArray tempArray = this.arrayAllocator.newFloatArray(INITIAL_CAPACITY);
        List<Float> tempResultList = new ArrayList<>();
        _reader.readArray(tempResultList, Float.TYPE);
        if (CollectionUtils.isNotEmpty(tempResultList)) {
            tempArray.copyFrom(tempResultList, 0, 0, tempResultList.size());
        }
        return tempArray;
    }

    protected DoubleArray readDoubleArray(JSONReader _reader) throws IOException {
        MutableDoubleArray tempArray = this.arrayAllocator.newDoubleArray(INITIAL_CAPACITY);
        List<Double> tempResultList = new ArrayList<>();
        _reader.readArray(tempResultList, Double.TYPE);
        if (CollectionUtils.isNotEmpty(tempResultList)) {
            tempArray.copyFrom(tempResultList, 0, 0, tempResultList.size());
        }
        return tempArray;
    }

    protected CharArray readCharArray(JSONReader _reader) throws IOException {
        MutableCharArray tempArray = this.arrayAllocator.newCharArray(INITIAL_CAPACITY);
        String[] tempResultArray = _reader.readStringArray();
        if (tempResultArray!= null && tempResultArray.length > 0) {
            for (int i = 0; i < tempResultArray.length; ++i) {
                tempArray.add(tempResultArray[i].charAt(0));
            }
        }
        return tempArray;
    }

    protected StringArray readStringArray(JSONReader _reader) throws IOException {
        MutableStringArray tempArray = this.arrayAllocator.newStringArray(INITIAL_CAPACITY);
        String[] tempResultArray = _reader.readStringArray();
        if (tempResultArray!= null && tempResultArray.length > 0) {
            tempArray.copyFrom(tempResultArray, 0, 0, tempResultArray.length);
        }
        return tempArray;
    }

    protected BooleanArray readBooleanArray(JSONReader _reader) throws IOException {
        MutableBooleanArray tempArray = this.arrayAllocator.newBooleanArray(INITIAL_CAPACITY);
        List<Boolean> tempResultList = new ArrayList<>();
        _reader.readArray(tempResultList, Boolean.TYPE);
        if (CollectionUtils.isNotEmpty(tempResultList)) {
            tempArray.copyFrom(tempResultList, 0, 0, tempResultList.size());
        }
        return tempArray;
    }

    protected StructAttributeArray readStructAttributeArray(JSONReader _reader, String _attrName) throws IOException {
        MutableStructAttributeArray tempArray = this.arrayAllocator.newStructAttributeArray(INITIAL_CAPACITY);
        if (_reader.nextIfArrayStart()) {
            while (!_reader.nextIfArrayEnd()) {
                tempArray.add(this.readStructAttribute(_reader, _attrName));
            }
        }
        return tempArray;
    }
}

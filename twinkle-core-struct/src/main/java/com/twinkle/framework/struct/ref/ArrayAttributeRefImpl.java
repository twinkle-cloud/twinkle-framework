package com.twinkle.framework.struct.ref;

import com.twinkle.framework.struct.asm.descriptor.SAAttributeDescriptor;
import com.twinkle.framework.struct.asm.descriptor.SAAttributeDescriptorImpl;
import com.twinkle.framework.struct.error.AttributeNotFoundException;
import com.twinkle.framework.struct.error.AttributeNotSetException;
import com.twinkle.framework.struct.error.AttributeTypeMismatchException;
import com.twinkle.framework.struct.error.StructAttributeCopyException;
import com.twinkle.framework.core.lang.util.*;
import com.twinkle.framework.struct.factory.StructAttributeFactory;
import com.twinkle.framework.struct.type.*;
import com.twinkle.framework.struct.util.MutableStructAttributeArray;
import com.twinkle.framework.struct.util.StructAttributeArray;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/2/19 5:29 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class ArrayAttributeRefImpl extends AbstractAttributeRef implements ArrayAttributeRef, Cloneable {
    /**
     * The name of the attribute ref.
     */
    private final String name;
    /**
     * The index of the attribute ref.
     */
    private int index;
    private final StructAttributeRef attributeRef;
    private final ArrayType arrayType;
    private final StructType elementType;
    private final StructAttributeFactory factory;

    protected ArrayAttributeRefImpl(StructAttributeRef _attrRef, int _attrIndex, StructAttributeFactory _saFactory) {
        super(getArrayElementDescriptor(_attrRef, _attrIndex));
        this.name = _attrRef.getName();
        if (_attrIndex < 0) {
            throw new IllegalArgumentException("Invalid index: " + _attrIndex);
        } else {
            this.index = _attrIndex;
            this.attributeRef = _attrRef;
            this.arrayType = (ArrayType) _attrRef.getType();
            this.elementType = this.arrayType.getElementType();
            this.factory = _saFactory;
        }
    }

    private static SAAttributeDescriptor getArrayElementDescriptor(StructAttributeRef _attrRef, int _attrIndex) {
        return new SAAttributeDescriptorImpl(getFullName(_attrRef.getName(), _attrIndex), ((ArrayType) _attrRef.getType()).getElementType(), _attrRef.getDescriptor().isOptional());
    }

    /**
     * Ensure the array attribute's size.
     * The _attr should be array attribute.
     *
     * @param _attr
     * @param _size
     * @return
     * @throws AttributeNotFoundException
     */
    protected Array ensureArray(StructAttribute _attr, int _size) throws AttributeNotFoundException {
        StructAttributeRef tempAttrRef = this.attributeRef;
        Array tempArray;
        if (!tempAttrRef.isAttributeSet(_attr)) {
            tempArray = this.factory.getArrayAllocator().newArray(this.arrayType, _size + 1);
            tempAttrRef.setArray(_attr, tempArray);
        } else {
            tempArray = tempAttrRef.getArray(_attr);
            tempArray.ensureCapacity(_size + 1);
        }
        return tempArray;
    }

    protected void ensureSize(ByteArray _array, int _size) {
        if (_size >= _array.length()) {
            ((MutableByteArray) _array).length(_size + 1, (byte) 0);
        }
    }

    protected void ensureSize(ShortArray _array, int _size) {
        if (_size >= _array.length()) {
            ((MutableShortArray) _array).length(_size + 1, (short) 0);
        }
    }

    protected void ensureSize(IntegerArray _array, int _size) {
        if (_size >= _array.length()) {
            ((MutableIntegerArray) _array).length(_size + 1, 0);
        }
    }

    protected void ensureSize(LongArray _array, int _size) {
        if (_size >= _array.length()) {
            ((MutableLongArray) _array).length(_size + 1, 0L);
        }
    }

    protected void ensureSize(CharArray _array, int _size) {
        if (_size >= _array.length()) {
            ((MutableCharArray) _array).length(_size + 1, '\u0000');
        }
    }

    protected void ensureSize(BooleanArray _array, int _size) {
        if (_size >= _array.length()) {
            ((MutableBooleanArray) _array).length(_size + 1, false);
        }
    }

    protected void ensureSize(FloatArray _array, int _size) {
        if (_size >= _array.length()) {
            ((MutableFloatArray) _array).length(_size + 1, 0.0F);
        }
    }

    protected void ensureSize(DoubleArray _array, int _size) {
        if (_size >= _array.length()) {
            ((MutableDoubleArray) _array).length(_size + 1, 0.0D);
        }
    }

    protected void ensureSize(StringArray _array, int _size) {
        if (_size >= _array.length()) {
            ((MutableStringArray) _array).length(_size + 1, "");
        }
    }

    protected void ensureSize(StructAttributeArray _array, int _size) {
        if (_size >= _array.length()) {
            StructAttribute tempAttr = this.factory.newStructAttribute((StructAttributeType) this.elementType);
            ((MutableStructAttributeArray) _array).length(_size + 1, tempAttr);
        }

    }

    /**
     * To ensure the array's size.
     *
     * @param _array
     * @param _size
     */
    protected void ensureSize(Array _array, int _size) {
        StructType tempType = this.elementType;
        if (tempType.isStructType()) {
            this.ensureSize((StructAttributeArray) _array, _size);
        } else if (tempType.isStringType()) {
            this.ensureSize((StringArray) _array, _size);
        } else if (tempType.isPrimitiveType()) {
            switch (tempType.getID()) {
                case PrimitiveType.BYTE_ID:
                    this.ensureSize((ByteArray) _array, _size);
                    break;
                case PrimitiveType.SHORT_ID:
                    this.ensureSize((ShortArray) _array, _size);
                    break;
                case PrimitiveType.INT_ID:
                    this.ensureSize((IntegerArray) _array, _size);
                    break;
                case PrimitiveType.LONG_ID:
                    this.ensureSize((LongArray) _array, _size);
                    break;
                case PrimitiveType.CHAR_ID:
                    this.ensureSize((CharArray) _array, _size);
                    break;
                case PrimitiveType.BOOLEAN_ID:
                    this.ensureSize((BooleanArray) _array, _size);
                case 7:
                case 8:
                default:
                    break;
                case PrimitiveType.FLOAT_ID:
                    this.ensureSize((FloatArray) _array, _size);
                    break;
                case PrimitiveType.DOUBLE_ID:
                    this.ensureSize((DoubleArray) _array, _size);
            }
        }

    }

    @Override
    public ArrayAttributeRef replicate(int _index) {
        if (_index < 0) {
            throw new IllegalArgumentException("Invalid index: " + _index);
        } else if (this.index < 0) {
            throw new IllegalStateException("Cannot replicate non-indexed name: " + this.descriptor.getName());
        }
        try {
            ArrayAttributeRefImpl tempRef = (ArrayAttributeRefImpl) super.clone();
            tempRef.index = _index;
            tempRef.descriptor = getArrayElementDescriptor(this.attributeRef, _index);
            return tempRef;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void ensureSize(StructAttribute _attr) {
        int tempIndex = this.index;
        Array tempArray = this.ensureArray(_attr, tempIndex);
        if (tempIndex >= tempArray.length()) {
            this.ensureSize(tempArray, tempIndex);
        }
    }

    @Override
    public void clear(StructAttribute _attr) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        if (this.attributeRef.isAttributeSet(_attr)) {
            Array tempArray = this.attributeRef.getArray(_attr);
            int tempIndex = this.index;
            if (tempIndex < tempArray.length()) {
                StructType tempElementType = this.elementType;
                if (tempElementType.isStructType()) {
                    StructAttribute tempAttr = ((StructAttributeArray) tempArray).get(tempIndex);
                    if (tempAttr != null) {
                        tempAttr.clear();
                    }
                } else if (tempElementType.isStringType()) {
                    ((StringArray) tempArray).put(tempIndex, "");
                } else if (tempElementType.isPrimitiveType()) {
                    switch (tempElementType.getID()) {
                        case PrimitiveType.BYTE_ID:
                            ((ByteArray) tempArray).put(tempIndex, (byte) 0);
                            break;
                        case PrimitiveType.SHORT_ID:
                            ((ShortArray) tempArray).put(tempIndex, (short) 0);
                            break;
                        case PrimitiveType.INT_ID:
                            ((IntegerArray) tempArray).put(tempIndex, 0);
                            break;
                        case PrimitiveType.LONG_ID:
                            ((LongArray) tempArray).put(tempIndex, 0L);
                            break;
                        case PrimitiveType.CHAR_ID:
                            ((CharArray) tempArray).put(tempIndex, '\u0000');
                            break;
                        case PrimitiveType.BOOLEAN_ID:
                            ((BooleanArray) tempArray).put(tempIndex, false);
                        case 7:
                        case 8:
                        default:
                            break;
                        case PrimitiveType.FLOAT_ID:
                            ((FloatArray) tempArray).put(tempIndex, 0.0F);
                            break;
                        case PrimitiveType.DOUBLE_ID:
                            ((DoubleArray) tempArray).put(tempIndex, 0.0D);
                    }
                }
            }
        }
    }

    @Override
    public boolean isAttributeSet(StructAttribute _attr) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        if (this.attributeRef.isAttributeSet(_attr)) {
            Array tempArray = this.attributeRef.getArray(_attr);
            int tempIndex = this.index;
            if (tempIndex < tempArray.length()) {
                StructType tempElementType = this.elementType;
                if (tempElementType.isStructType()) {
                    return ((StructAttributeArray) tempArray).get(tempIndex) != null;
                }
                if (tempElementType.isStringType()) {
                    return ((StringArray) tempArray).get(tempIndex) != null;
                }
                if (tempElementType.isPrimitiveType()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public byte getByte(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        try {
            ByteArray tempByteArray = (ByteArray) this.attributeRef.getArray(_attr);
            return tempByteArray.get(this.index);
        } catch (ClassCastException e) {
            throw new AttributeTypeMismatchException(this.name, this.arrayType.getName(), ArrayType.BYTE_ARRAY.getName(), e);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new AttributeNotSetException(this.getName(), e);
        }
    }

    @Override
    public void setByte(StructAttribute _attr, byte _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        try {
            ByteArray tempArray = (ByteArray) this.ensureArray(_attr, this.index);
            if (this.index >= tempArray.length()) {
                this.ensureSize(tempArray, this.index);
            }
            tempArray.put(this.index, _value);
        } catch (ClassCastException e) {
            throw new AttributeTypeMismatchException(this.name, this.arrayType.getName(), ArrayType.BYTE_ARRAY.getName(), e);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new AttributeNotSetException(this.getName(), e);
        }
    }

    @Override
    public short getShort(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        try {
            ShortArray tempArray = (ShortArray) this.attributeRef.getArray(_attr);
            return tempArray.get(this.index);
        } catch (ClassCastException e) {
            throw new AttributeTypeMismatchException(this.name, this.arrayType.getName(), ArrayType.SHORT_ARRAY.getName(), e);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new AttributeNotSetException(this.getName(), e);
        }
    }

    @Override
    public void setShort(StructAttribute _attr, short _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        try {
            ShortArray tempArray = (ShortArray) this.ensureArray(_attr, this.index);
            if (this.index >= tempArray.length()) {
                this.ensureSize(tempArray, this.index);
            }
            tempArray.put(this.index, _value);
        } catch (ClassCastException e) {
            throw new AttributeTypeMismatchException(this.name, this.arrayType.getName(), ArrayType.SHORT_ARRAY.getName(), e);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new AttributeNotSetException(this.getName(), e);
        }
    }

    @Override
    public int getInt(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        try {
            IntegerArray tempAttr = (IntegerArray) this.attributeRef.getArray(_attr);
            return tempAttr.get(this.index);
        } catch (ClassCastException e) {
            throw new AttributeTypeMismatchException(this.name, this.arrayType.getName(), ArrayType.INT_ARRAY.getName(), e);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new AttributeNotSetException(this.getName(), e);
        }
    }

    @Override
    public void setInt(StructAttribute _attr, int _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        try {
            IntegerArray tempArray = (IntegerArray) this.ensureArray(_attr, this.index);
            if (this.index >= tempArray.length()) {
                this.ensureSize(tempArray, this.index);
            }
            tempArray.put(this.index, _value);
        } catch (ClassCastException e) {
            throw new AttributeTypeMismatchException(this.name, this.arrayType.getName(), ArrayType.INT_ARRAY.getName(), e);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new AttributeNotSetException(this.getName(), e);
        }
    }

    @Override
    public long getLong(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        try {
            LongArray tempArray = (LongArray) this.attributeRef.getArray(_attr);
            return tempArray.get(this.index);
        } catch (ClassCastException e) {
            throw new AttributeTypeMismatchException(this.name, this.arrayType.getName(), ArrayType.LONG_ARRAY.getName(), e);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new AttributeNotSetException(this.getName(), e);
        }
    }

    @Override
    public void setLong(StructAttribute _attr, long _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        try {
            LongArray tempArray = (LongArray) this.ensureArray(_attr, this.index);
            if (this.index >= tempArray.length()) {
                this.ensureSize(tempArray, this.index);
            }
            tempArray.put(this.index, _value);
        } catch (ClassCastException e) {
            throw new AttributeTypeMismatchException(this.name, this.arrayType.getName(), ArrayType.LONG_ARRAY.getName(), e);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new AttributeNotSetException(this.getName(), e);
        }
    }

    @Override
    public char getChar(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        try {
            CharArray tempArray = (CharArray) this.attributeRef.getArray(_attr);
            return tempArray.get(this.index);
        } catch (ClassCastException e) {
            throw new AttributeTypeMismatchException(this.name, this.arrayType.getName(), ArrayType.CHAR_ARRAY.getName(), e);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new AttributeNotSetException(this.getName(), e);
        }
    }

    @Override
    public void setChar(StructAttribute _attr, char _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        try {
            CharArray tempArray = (CharArray) this.ensureArray(_attr, this.index);
            if (this.index >= tempArray.length()) {
                this.ensureSize(tempArray, this.index);
            }
            tempArray.put(this.index, _value);
        } catch (ClassCastException e) {
            throw new AttributeTypeMismatchException(this.name, this.arrayType.getName(), ArrayType.CHAR_ARRAY.getName(), e);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new AttributeNotSetException(this.getName(), e);
        }
    }

    @Override
    public boolean getBoolean(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        try {
            BooleanArray tempArray = (BooleanArray) this.attributeRef.getArray(_attr);
            return tempArray.get(this.index);
        } catch (ClassCastException e) {
            throw new AttributeTypeMismatchException(this.name, this.arrayType.getName(), ArrayType.BOOLEAN_ARRAY.getName(), e);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new AttributeNotSetException(this.getName(), e);
        }
    }

    @Override
    public void setBoolean(StructAttribute _attr, boolean _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        try {
            BooleanArray tempArray = (BooleanArray) this.ensureArray(_attr, this.index);
            if (this.index >= tempArray.length()) {
                this.ensureSize(tempArray, this.index);
            }
            tempArray.put(this.index, _value);
        } catch (ClassCastException e) {
            throw new AttributeTypeMismatchException(this.name, this.arrayType.getName(), ArrayType.BOOLEAN_ARRAY.getName(), e);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new AttributeNotSetException(this.getName(), e);
        }
    }

    @Override
    public float getFloat(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        try {
            FloatArray tempArray = (FloatArray) this.attributeRef.getArray(_attr);
            return tempArray.get(this.index);
        } catch (ClassCastException e) {
            throw new AttributeTypeMismatchException(this.name, this.arrayType.getName(), ArrayType.FLOAT_ARRAY.getName(), e);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new AttributeNotSetException(this.getName(), e);
        }
    }

    @Override
    public void setFloat(StructAttribute _attr, float _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        try {
            FloatArray tempArray = (FloatArray) this.ensureArray(_attr, this.index);
            if (this.index >= tempArray.length()) {
                this.ensureSize(tempArray, this.index);
            }
            tempArray.put(this.index, _value);
        } catch (ClassCastException e) {
            throw new AttributeTypeMismatchException(this.name, this.arrayType.getName(), ArrayType.FLOAT_ARRAY.getName(), e);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new AttributeNotSetException(this.getName(), e);
        }
    }

    @Override
    public double getDouble(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        try {
            DoubleArray tempArray = (DoubleArray) this.attributeRef.getArray(_attr);
            return tempArray.get(this.index);
        } catch (ClassCastException e) {
            throw new AttributeTypeMismatchException(this.name, this.arrayType.getName(), ArrayType.DOUBLE_ARRAY.getName(), e);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new AttributeNotSetException(this.getName(), e);
        }
    }

    @Override
    public void setDouble(StructAttribute _attr, double _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        try {
            DoubleArray tempArray = (DoubleArray) this.ensureArray(_attr, this.index);
            if (this.index >= tempArray.length()) {
                this.ensureSize(tempArray, this.index);
            }
            tempArray.put(this.index, _value);
        } catch (ClassCastException e) {
            throw new AttributeTypeMismatchException(this.name, this.arrayType.getName(), ArrayType.DOUBLE_ARRAY.getName(), e);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new AttributeNotSetException(this.getName(), e);
        }
    }

    @Override
    public String getString(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        try {
            StringArray tempArray = (StringArray) this.attributeRef.getArray(_attr);
            return tempArray.get(this.index);
        } catch (ClassCastException e) {
            throw new AttributeTypeMismatchException(this.name, this.arrayType.getName(), ArrayType.STRING_ARRAY.getName(), e);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new AttributeNotSetException(this.getName(), e);
        }
    }

    @Override
    public void setString(StructAttribute _attr, String _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        try {
            StringArray tempArray = (StringArray) this.ensureArray(_attr, this.index);
            if (this.index >= tempArray.length()) {
                this.ensureSize(tempArray, this.index);
            }
            tempArray.put(this.index, _value);
        } catch (ClassCastException e) {
            throw new AttributeTypeMismatchException(this.name, this.arrayType.getName(), ArrayType.STRING_ARRAY.getName(), e);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new AttributeNotSetException(this.getName(), e);
        }
    }

    @Override
    public StructAttribute getStruct(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        try {
            StructAttributeArray tempArray = (StructAttributeArray) this.attributeRef.getArray(_attr);
            return tempArray.get(this.index);
        } catch (ClassCastException e) {
            throw new AttributeTypeMismatchException(this.name, this.arrayType.getName(), "StructAttribute[]", e);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new AttributeNotSetException(this.getName(), e);
        }
    }

    @Override
    public void setStruct(StructAttribute _attr, StructAttribute _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        if (!this.descriptor.getType().equals(_value.getType())) {
            throw new AttributeTypeMismatchException(this.getName(), this.getType().getName(), _value.getType().getName());
        }
        try {
            StructAttributeArray tempArray = (StructAttributeArray) this.ensureArray(_attr, this.index);
            if (this.index >= tempArray.length()) {
                this.ensureSize(tempArray, this.index);
            }
            tempArray.put(this.index, _value);
        } catch (ClassCastException e) {
            throw new AttributeTypeMismatchException(this.name, this.arrayType.getName(), "StructAttribute[]", e);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new AttributeNotSetException(this.getName(), e);
        }
    }

    @Override
    public Array getArray(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        throw new AttributeTypeMismatchException(this.getName(), this.elementType.getName());
    }

    @Override
    public void setArray(StructAttribute _attr, Array _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException, ClassCastException {
        throw new AttributeTypeMismatchException(this.getName(), this.elementType.getName());
    }

    @Override
    public int getArraySize(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        throw new AttributeTypeMismatchException(this.getName(), this.elementType.getName());
    }

    @Override
    public void copy(StructAttribute _srcAttr, StructAttribute _destAttr) throws StructAttributeCopyException, AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        if (this.attributeRef.isAttributeSet(_srcAttr)) {
            this.attributeRef.clear(_destAttr);
        } else {
            Array tempArray = this.attributeRef.getArray(_srcAttr);
            int tempIndex = this.index;
            try {
                Array tempDestArray = this.ensureArray(_destAttr, tempIndex);
                if (tempIndex >= tempDestArray.length()) {
                    this.ensureSize(tempDestArray, tempIndex);
                }
                tempArray.copyTo(tempDestArray, tempIndex, tempIndex, 1);
            } catch (ClassCastException e) {
                throw new AttributeTypeMismatchException(this.name, this.arrayType.getName(), ArrayType.STRING_ARRAY.getName(), e);
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new AttributeNotSetException(this.getName(), e);
            }
        }
    }

    @Override
    public String getName() {
        return this.descriptor.getName();
    }

    @Override
    public boolean isComposite() {
        return false;
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public String getArrayAttributeName() {
        return this.name;
    }

    @Override
    public ArrayType getArrayType() {
        return this.arrayType;
    }

    @Override
    public int getIndex() {
        return this.index;
    }

    /**
     * Get the full name, like: aaa[1]
     *
     * @param _name
     * @param _index
     * @return
     */
    private static String getFullName(String _name, int _index) {
        return _name + CompositeName.ARRAY_BRACKETS[0] + Integer.toString(_index) + CompositeName.ARRAY_BRACKETS[1];
    }

    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

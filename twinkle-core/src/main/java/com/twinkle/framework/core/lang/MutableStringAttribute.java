package com.twinkle.framework.core.lang;

import java.io.Serializable;
import java.nio.CharBuffer;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/9/19 2:50 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public final class MutableStringAttribute implements Attribute, Cloneable, Serializable, CharSequence {
    public static final String EMPTY_VALUE = "";
    private static final int BLOCK_SIZE = 16;
    private static int type_ = 111;
    private char[] value_ = null;
    private int length_ = 0;
    private int hash_ = 0;

    public MutableStringAttribute() {
        this.ensureCapacity(BLOCK_SIZE);
    }

    public MutableStringAttribute(int _size) {
        this.ensureCapacity(_size);
    }

    public MutableStringAttribute(String _value) {
        this.setValue(_value);
    }

    @Override
    public int getPrimitiveType() {
        return Attribute.STRING_TYPE;
    }

    @Override
    public int getTypeIndex() {
        return type_;
    }

    @Override
    public void setTypeIndex(int var1) {
        type_ = var1;
    }

    @Override
    public void setEmptyValue() {
        this.length_ = 0;
        this.hash_ = 0;
        this.value_ = EMPTY_VALUE.toCharArray();
    }

    @Override
    public void setValue(String _value) {
        this.length_ = 0;
        this.appendString(_value, 0);
    }

    @Override
    public void setValue(Object _value) {
        if(_value == null) {
            this.value_ = null;
            this.length_ = 0;
            this.hash_ = 0;
            return;
        }
        if(_value instanceof Attribute) {
            this.setValue((Attribute)_value);
        } else {
            this.setValue(_value.toString());
        }
    }

    @Override
    public void setValue(Attribute _attr) {
        if (_attr == null) {
            this.value_ = null;
            this.length_ = 0;
            this.hash_ = 0;
        } else if (_attr != this) {
            if (_attr.getClass() == MutableStringAttribute.class) {
                MutableStringAttribute tempAttr = (MutableStringAttribute) _attr;
                this.setValue(tempAttr.value_, 0, tempAttr.length_);
            } else {
                this.setValue(_attr.toString());
            }

        }
    }

    /**
     * Set the sub-array as the value.
     *
     * @param _array
     * @param _srcPos
     * @param _length
     */
    public void setValue(char[] _array, int _srcPos, int _length) {
        this.ensureCapacity(_length);
        System.arraycopy(_array, _srcPos, this.value_, 0, _length);
        this.length_ = _length;
        this.hash_ = 0;
    }

    /**
     * Get value as mutable string attribute.
     *
     * @param _attr
     */
    public void getValue(MutableStringAttribute _attr) {
        _attr.setValue(this);
    }

    /**
     * Copy the value to the charbuffer.
     *
     * @param _buffer
     * @return
     */
    public int getValue(CharBuffer _buffer) {
        int tempRemainLength = Math.min(_buffer.remaining(), this.length_);
        if (_buffer.hasArray()) {
            char[] tempArray = _buffer.array();
            System.arraycopy(this.value_, 0, tempArray, _buffer.arrayOffset() + _buffer.position(), tempRemainLength);
            _buffer.position(_buffer.position() + tempRemainLength);
        } else {
            for (int i = 0; i < tempRemainLength; i++) {
                _buffer.put(this.value_[i]);
            }
        }

        return tempRemainLength;
    }

    /**
     * Get the sub-array.
     *
     * @param _destArray
     * @param _destPos
     * @param _length
     * @return
     */
    public int getValue(char[] _destArray, int _destPos, int _length) {
        _length = Math.min(_length, this.length_);
        System.arraycopy(this.value_, 0, _destArray, _destPos, _length);
        return _length;
    }

    /**
     * Update the value.
     *
     * @param _buffer
     */
    public void setValue(CharBuffer _buffer) {
        int tempRemainLength = _buffer.remaining();
        if (_buffer.hasArray()) {
            this.setValue(_buffer.array(), _buffer.arrayOffset(), tempRemainLength);
            _buffer.position(_buffer.limit());
        } else {
            this.ensureCapacity(tempRemainLength);
            _buffer.get(this.value_, 0, tempRemainLength);
            this.hash_ = 0;
        }
    }

    @Override
    public void aggregate(Operation _operation, Attribute _attr) {
        switch (_operation) {
            case ADD:
                if (_attr != null) {
                    if (_attr.getClass() == MutableStringAttribute.class) {
                        MutableStringAttribute tempAttr = (MutableStringAttribute) _attr;
                        this.ensureCapacity(this.length_ + tempAttr.length_);
                        System.arraycopy(tempAttr.value_, 0, this.value_, this.length_, tempAttr.length_);
                        this.length_ += tempAttr.length_;
                        this.hash_ = 0;
                    } else {
                        String var4 = _attr.toString();
                        this.appendString(var4, this.length_);
                    }
                }
                break;
            case SET:
                this.setValue(_attr);
        }

    }

    @Override
    public int compareTo(Object _obj) {
        MutableStringAttribute tempAttr = (MutableStringAttribute) _obj;
        int tempLength = Math.max(this.length_, tempAttr.length_);

        for (int i = 0; i < tempLength; i++) {
            if (this.value_[i] != tempAttr.value_[i]) {
                return this.value_[i] - tempAttr.value_[i];
            }
        }

        return 0;
    }

    @Override
    public boolean equals(Object _obj) {
        if (_obj == null) {
            return false;
        } else if (_obj == this) {
            return true;
        }
        if (_obj.getClass() == MutableStringAttribute.class) {
            MutableStringAttribute tempAttr = (MutableStringAttribute) _obj;
            if (this.length_ == tempAttr.length_) {
                for (int i = 0; i < this.length_; i++) {
                    if (this.value_[i] != tempAttr.value_[i]) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (this.hash_ == 0) {
            for (int i = 0; i < this.length_; ++i) {
                this.hash_ = 31 * this.hash_ + this.value_[i];
            }
        }

        return this.hash_;
    }

    @Override
    public Object clone() {
        try {
            Object tempObj = super.clone();
            ((MutableStringAttribute) tempObj).setValue(this);
            return tempObj;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    @Override
    public int length() {
        return this.length_;
    }

    @Override
    public char charAt(int _index) {
        if (_index >= 0 && _index < this.length_) {
            return this.value_[_index];
        } else {
            throw new StringIndexOutOfBoundsException(_index);
        }
    }

    /**
     * Update the _char at the _index position.
     *
     * @param _char
     * @param _index
     */
    public void putCharAt(char _char, int _index) {
        if (_index >= 0 && _index < this.length_) {
            this.value_[_index] = _char;
            this.hash_ = 0;
        } else {
            throw new StringIndexOutOfBoundsException(_index);
        }
    }

    @Override
    public CharSequence subSequence(int _beginIndex, int _endIndex) {
        throw new UnsupportedOperationException("Subsequence not supported.");
    }

    @Override
    public String toString() {
        return new String(this.value_, 0, this.length_);
    }

    /**
     * Ensure the array's length is enough.
     *
     * @param _size
     */
    private void ensureCapacity(int _size) {
        if (this.value_ == null || _size > this.value_.length) {
            int tempBlock = (_size + 16) / 16;
            this.value_ = new char[16 * tempBlock];
        }

    }

    /**
     * Append the _str to the current String, from _beginIndex.
     *
     * @param _str
     * @param _beginIndex
     */
    private void appendString(String _str, int _beginIndex) {
        int tempSize = this.length_ + _str.length();
        this.ensureCapacity(tempSize);
        int tempIndex = 0;

        for (int i = _beginIndex; i < tempSize; ++i) {
            this.value_[i] = _str.charAt(tempIndex++);
        }

        this.length_ = tempSize;
        this.hash_ = 0;
    }

    @Override
    public Object getObjectValue() {
        return this.toString();
    }
}

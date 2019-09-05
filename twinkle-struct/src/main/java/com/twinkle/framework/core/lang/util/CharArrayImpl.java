package com.twinkle.framework.core.lang.util;

import java.util.Arrays;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/31/19 10:28 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public final class CharArrayImpl extends AbstractArray implements MutableCharArray {
    private char[] _buffer;

    public CharArrayImpl(int _size) {
        this(new char[_size]);
    }

    public CharArrayImpl(char[] _array, int _size) {
        this._buffer = _array;
        this.length(_size);
    }

    public CharArrayImpl(char[] _array) {
        this(_array, 0);
    }

    @Override
    protected Object _array() {
        return this._buffer;
    }

    @Override
    public void ensureCapacity(int _capacity) {
        if (this._buffer.length < _capacity) {
            char[] tempArray = new char[_capacity];
            System.arraycopy(this._buffer, 0, tempArray, 0, this.length);
            this._buffer = tempArray;
        }
    }

    @Override
    public void reallocate(int _size) {
        this._buffer = new char[_size];
        this.length = 0;
    }

    @Override
    public void length(int _size) {
        if (_size > this._buffer.length) {
            throw new IllegalArgumentException("Length cannot be greater than capacity.");
        } else {
            this.length = _size;
        }
    }

    @Override
    public void length(int _endIndex, char _value) {
        int tempLength = this.length;
        this.length(_endIndex);
        if (_endIndex > tempLength) {
            Arrays.fill(this._buffer, tempLength, _endIndex, _value);
        }
    }

    @Override
    public void clear() {
        this.length = 0;
    }

    @Override
    public int capacity() {
        return this._buffer.length;
    }

    @Override
    public char get(int _index) throws ArrayIndexOutOfBoundsException {
        if (_index < this.length && _index >= 0) {
            return this._buffer[_index];
        } else {
            throw new ArrayIndexOutOfBoundsException("Array index out of bounds:" + _index + " The length of array is " + this.length);
        }
    }

    @Override
    public void put(int _index, char _value) throws ArrayIndexOutOfBoundsException {
        if (_index < this.length && _index >= 0) {
            this._buffer[_index] = _value;
        } else {
            throw new ArrayIndexOutOfBoundsException("Array index out of bounds:" + _index + "It must be in range of [0, " + this.length + "]");
        }
    }

    @Override
    public void transfer(char[] _srcArray, int _srcPos, int _destPos, int _length) {
        int tempContentLength = _destPos + _length;
        this.length(Math.max(tempContentLength, this.length));
        super.copyFrom(_srcArray, _srcPos, _destPos, _length);
    }

    @Override
    public void transfer(CharArray _srcArray, int _srcPos, int _destPos, int _length) {
        int tempContentLength = _destPos + _length;
        this.length(Math.max(tempContentLength, this.length));
        super.copyFrom(_srcArray, _srcPos, _destPos, _length);
    }

    @Override
    public void add(char _value) throws ArrayIndexOutOfBoundsException {
        if (this.length >= this._buffer.length) {
            throw new ArrayIndexOutOfBoundsException("Array is full. Can not add value into this array.");
        } else {
            this._buffer[this.length++] = _value;
        }
    }

    @Override
    public char remove() throws ArrayIndexOutOfBoundsException {
        if (this.length == 0) {
            throw new ArrayIndexOutOfBoundsException("No elements to remove.");
        } else {
            return this._buffer[--this.length];
        }
    }

    @Override
    public char[] array() {
        return this._buffer;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        try {
            CharArrayImpl tempArray = (CharArrayImpl) super.clone();
            tempArray._buffer = (char[]) this._buffer.clone();
            return tempArray;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }

    @Override
    public String toString() {
        if (this._buffer.length == 0) {
            return "[]";
        }
        StringBuilder tempBuilder = new StringBuilder();
        tempBuilder.append('[');
        tempBuilder.append(this._buffer[0]);

        for (int i = 1; i < this.length; ++i) {
            tempBuilder.append(", ");
            tempBuilder.append(this._buffer[i]);
        }

        tempBuilder.append("]");
        return tempBuilder.toString();
    }

    @Override
    public boolean equals(Object _obj) {
        if (_obj == this) {
            return true;
        } else if (!(_obj instanceof CharArray)) {
            return false;
        }

        CharArray tempArray = (CharArray) _obj;
        int tempLength = tempArray.length();
        if (this.length != tempLength) {
            return false;
        } else {
            for (int i = 0; i < this.length; ++i) {
                if (this._buffer[i] != tempArray.get(i)) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public int hashCode() {
        int tempCode = 1;
        for (int i = this.length - 1; i >= 0; --i) {
            tempCode = 31 * tempCode + this._buffer[i];
        }
        return tempCode;
    }

    /**
     * Update the value at _index position.
     *
     * @param _index
     * @param _value
     */
    public void putAt(int _index, char _value) {
        this.put(_index, _value);
    }

    /**
     * Get the value at _index position.
     *
     * @param _index
     * @return
     */
    public char getAt(int _index) {
        return this.get(_index);
    }

    /**
     * Add value into this array.
     *
     * @param _value
     */
    public void plus(char _value) {
        this.ensureCapacity(this.length() + 1);
        this.add(_value);
    }

    public int size() {
        return this.length();
    }
}

package com.twinkle.framework.core.lang.util;

import java.util.Arrays;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/1/19 6:00 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public final class LongArrayImpl extends AbstractArray implements MutableLongArray {
    private long[] _buffer;

    public LongArrayImpl(int _size) {
        this(new long[_size]);
    }

    public LongArrayImpl(long[] _array, int _size) {
        this._buffer = _array;
        this.length(_size);
    }

    public LongArrayImpl(long[] _array) {
        this(_array, 0);
    }

    @Override
    protected Object _array() {
        return this._buffer;
    }

    @Override
    public void ensureCapacity(int _capacity) {
        if (this._buffer.length < _capacity) {
            long[] tempArray = new long[_capacity];
            System.arraycopy(this._buffer, 0, tempArray, 0, this.length);
            this._buffer = tempArray;
        }
    }

    @Override
    public void reallocate(int _size) {
        this._buffer = new long[_size];
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
    public void length(int _endIndex, long _value) {
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
    public long get(int _index) throws ArrayIndexOutOfBoundsException {
        if (_index < this.length && _index >= 0) {
            return this._buffer[_index];
        } else {
            throw new ArrayIndexOutOfBoundsException("Array index out of bounds:" + _index + " The length of array is " + this.length);
        }
    }

    @Override
    public void put(int _index, long _value) throws ArrayIndexOutOfBoundsException {
        if (_index < this.length && _index >= 0) {
            this._buffer[_index] = _value;
        } else {
            throw new ArrayIndexOutOfBoundsException("Array index out of bounds:" + _index + "It must be in range of [0, " + this.length + "]");
        }
    }

    @Override
    public void transfer(long[] _srcArray, int _srcPos, int _destPos, int _length) {
        int tempContentLength = _destPos + _length;
        this.length(Math.max(tempContentLength, this.length));
        super.copyFrom(_srcArray, _srcPos, _destPos, _length);
    }

    @Override
    public void transfer(LongArray _srcArray, int _srcPos, int _destPos, int _length) {
        int tempContentLength = _destPos + _length;
        this.length(Math.max(tempContentLength, this.length));
        super.copyFrom(_srcArray, _srcPos, _destPos, _length);
    }

    @Override
    public void add(long _value) throws ArrayIndexOutOfBoundsException {
        if (this.length >= this._buffer.length) {
            throw new ArrayIndexOutOfBoundsException("Array is full. Can not add value into this array.");
        } else {
            this._buffer[this.length++] = _value;
        }
    }

    @Override
    public long remove() throws ArrayIndexOutOfBoundsException {
        if (this.length == 0) {
            throw new ArrayIndexOutOfBoundsException("No elements to remove.");
        } else {
            return this._buffer[--this.length];
        }
    }

    @Override
    public long[] array() {
        return this._buffer;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        try {
            LongArrayImpl tempArray = (LongArrayImpl) super.clone();
            tempArray._buffer = this._buffer.clone();
            return tempArray;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }

    @Override
    public String toString() {
        if (this.length == 0) {
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
        } else if (!(_obj instanceof LongArray)) {
            return false;
        }
        LongArray tempObjArray = (LongArray) _obj;
        int tempLength = tempObjArray.length();
        if (this.length != tempLength) {
            return false;
        } else {
            for (int i = 0; i < this.length; ++i) {
                if (this._buffer[i] != tempObjArray.get(i)) {
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
            tempCode = 31 * tempCode + (int) (this._buffer[i] ^ this._buffer[i] >>> 32);
        }
        return tempCode;
    }

    /**
     * Update the value at _index position.
     *
     * @param _index
     * @param _value
     */
    public void putAt(int _index, long _value) {
        this.put(_index, _value);
    }

    /**
     * Get the value at _index position.
     *
     * @param _index
     * @return
     */
    public long getAt(int _index) {
        return this.get(_index);
    }

    /**
     * Add value into this array.
     *
     * @param _value
     */
    public void plus(long _value) {
        this.ensureCapacity(this.length() + 1);
        this.add(_value);
    }

    public int size() {
        return this.length();
    }
}

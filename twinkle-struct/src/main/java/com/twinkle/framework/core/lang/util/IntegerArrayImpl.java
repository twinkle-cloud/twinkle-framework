package com.twinkle.framework.core.lang.util;

import java.util.Arrays;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/1/19 5:17 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public final class IntegerArrayImpl extends AbstractArray implements MutableIntegerArray {
    private int[] _buffer;

    public IntegerArrayImpl(int _size) {
        this(new int[_size]);
    }

    public IntegerArrayImpl(int[] _array, int _size) {
        this._buffer = _array;
        this.length(_size);
    }

    public IntegerArrayImpl(int[] _array) {
        this(_array, 0);
    }

    @Override
    protected Object _array() {
        return this._buffer;
    }

    @Override
    public void ensureCapacity(int _capacity) {
        if (this._buffer.length < _capacity) {
            int[] tempArray = new int[_capacity];
            System.arraycopy(this._buffer, 0, tempArray, 0, this.length);
            this._buffer = tempArray;
        }
    }

    @Override
    public void reallocate(int _size) {
        this._buffer = new int[_size];
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
    public void length(int _endIndex, int _value) {
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
    public int get(int _index) throws ArrayIndexOutOfBoundsException {
        if (_index < this.length && _index >= 0) {
            return this._buffer[_index];
        } else {
            throw new ArrayIndexOutOfBoundsException("Array index out of bounds:" + _index + " The length of array is " + this.length);
        }
    }

    @Override
    public void put(int _index, int _value) throws ArrayIndexOutOfBoundsException {
        if (_index < this.length && _index >= 0) {
            this._buffer[_index] = _value;
        } else {
            throw new ArrayIndexOutOfBoundsException("Array index out of bounds:" + _index + "It must be in range of [0, " + this.length + "]");
        }
    }

    @Override
    public void transfer(int[] _srcArray, int _srcPos, int _destPos, int _length) {
        int tempContentLength = _destPos + _length;
        this.length(Math.max(tempContentLength, this.length));
        super.copyFrom(_srcArray, _srcPos, _destPos, _length);
    }

    @Override
    public void transfer(IntegerArray _srcArray, int _srcPos, int _destPos, int _length) {
        int tempContentLength = _destPos + _length;
        this.length(Math.max(tempContentLength, this.length));
        super.copyFrom(_srcArray, _srcPos, _destPos, _length);
    }

    @Override
    public void add(int _value) throws ArrayIndexOutOfBoundsException {
        if (this.length >= this._buffer.length) {
            throw new ArrayIndexOutOfBoundsException("Array is full. Can not add value into this array.");
        } else {
            this._buffer[this.length++] = _value;
        }
    }

    @Override
    public int remove() throws ArrayIndexOutOfBoundsException {
        if (this.length == 0) {
            throw new ArrayIndexOutOfBoundsException("No elements to remove.");
        } else {
            return this._buffer[--this.length];
        }
    }

    @Override
    public int[] array() {
        return this._buffer;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        try {
            IntegerArrayImpl tempArray = (IntegerArrayImpl) super.clone();
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
        } else if (!(_obj instanceof IntegerArray)) {
            return false;
        }
        IntegerArray tempArray = (IntegerArray) _obj;
        int tempObjLength = tempArray.length();
        if (this.length != tempObjLength) {
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
    public void putAt(int _index, int _value) {
        this.put(_index, _value);
    }

    /**
     * Get the value at _index position.
     *
     * @param _index
     * @return
     */
    public int getAt(int _index) {
        return this.get(_index);
    }

    /**
     * Add value into this array.
     *
     * @param _value
     */
    public void plus(int _value) {
        this.ensureCapacity(this.length() + 1);
        this.add(_value);
    }

    public int size() {
        return this.length();
    }
}

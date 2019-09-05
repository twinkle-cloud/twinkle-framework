package com.twinkle.framework.core.lang.util;

import java.util.Arrays;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/31/19 6:21 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public final class BooleanArrayImpl extends AbstractArray implements MutableBooleanArray {
    private boolean[] buffer;

    public BooleanArrayImpl(int _size) {
        this(new boolean[_size]);
    }

    public BooleanArrayImpl(boolean[] _array, int _size) {
        this.buffer = _array;
        this.length(_size);
    }

    public BooleanArrayImpl(boolean[] _array) {
        this(_array, 0);
    }

    @Override
    protected Object _array() {
        return this.buffer;
    }

    @Override
    public void ensureCapacity(int _capacity) {
        if (this.buffer.length < _capacity) {
            boolean[] tempArray = new boolean[_capacity];
            System.arraycopy(this.buffer, 0, tempArray, 0, this.length);
            this.buffer = tempArray;
        }
    }

    @Override
    public void reallocate(int _size) {
        this.buffer = new boolean[_size];
        this.length = 0;
    }

    @Override
    public void length(int _size) {
        if (_size > this.buffer.length) {
            throw new IllegalArgumentException("Length cannot be greater than capacity.");
        } else {
            this.length = _size;
        }
    }

    @Override
    public void length(int _endIndex, boolean _value) {
        int tempLength = this.length;
        this.length(_endIndex);
        if (_endIndex > tempLength) {
            Arrays.fill(this.buffer, tempLength, _endIndex, _value);
        }
    }

    @Override
    public void clear() {
        this.length = 0;
    }

    @Override
    public int capacity() {
        return this.buffer.length;
    }

    @Override
    public boolean get(int _index) throws ArrayIndexOutOfBoundsException {
        if (_index < this.length && _index >= 0) {
            return this.buffer[_index];
        } else {
            throw new ArrayIndexOutOfBoundsException("Array index out of bounds:" + _index + " The length of array is " + this.length);
        }
    }

    @Override
    public void put(int _index, boolean _value) throws ArrayIndexOutOfBoundsException {
        if (_index < this.length && _index >= 0) {
            this.buffer[_index] = _value;
        } else {
            throw new ArrayIndexOutOfBoundsException("Array index out of bounds:" + _index + "It must be in range of [0, " + this.length + "]");
        }
    }

    @Override
    public void transfer(boolean[] _srcArray, int _srcPos, int _destPos, int _length) {
        int tempContentLength = _destPos + _length;
        this.length(Math.max(tempContentLength, this.length));
        super.copyFrom(_srcArray, _srcPos, _destPos, _length);
    }

    @Override
    public void transfer(BooleanArray _srcArray, int _srcPos, int _destPos, int _length) {
        int tempContentLength = _destPos + _length;
        this.length(Math.max(tempContentLength, this.length));
        super.copyFrom(_srcArray, _srcPos, _destPos, _length);
    }

    @Override
    public void add(boolean _value) throws ArrayIndexOutOfBoundsException {
        if (this.length >= this.buffer.length) {
            throw new ArrayIndexOutOfBoundsException("Array is full. Can not add value into this array.");
        } else {
            this.buffer[this.length++] = _value;
        }
    }

    @Override
    public boolean remove() throws ArrayIndexOutOfBoundsException {
        if (this.length == 0) {
            throw new ArrayIndexOutOfBoundsException("No elements to remove.");
        } else {
            return this.buffer[--this.length];
        }
    }

    @Override
    public boolean[] array() {
        return this.buffer;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        try {
            BooleanArrayImpl tempArray = (BooleanArrayImpl) super.clone();
            tempArray.buffer = this.buffer.clone();
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
        tempBuilder.append(this.buffer[0]);

        for (int i = 1; i < this.length; ++i) {
            tempBuilder.append(", ");
            tempBuilder.append(this.buffer[i]);
        }

        tempBuilder.append("]");
        return tempBuilder.toString();
    }

    @Override
    public boolean equals(Object _obj) {
        if (_obj == this) {
            return true;
        } else if (!(_obj instanceof BooleanArray)) {
            return false;
        }
        BooleanArray tempObjArray = (BooleanArray) _obj;
        int tempLength = tempObjArray.length();
        if (this.length != tempLength) {
            return false;
        } else {
            for (int i = 0; i < this.length; ++i) {
                if (this.buffer[i] != tempObjArray.get(i)) {
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
            tempCode = 31 * tempCode + (this.buffer[i] ? 1231 : 1237);
        }
        return tempCode;
    }

    /**
     * Update the value at _index position.
     *
     * @param _index
     * @param _value
     */
    public void putAt(int _index, boolean _value) {
        this.put(_index, _value);
    }

    /**
     * Get the value at _index position.
     *
     * @param _index
     * @return
     */
    public boolean getAt(int _index) {
        return this.get(_index);
    }

    /**
     * Add value into this array.
     *
     * @param _value
     */
    public void plus(boolean _value) {
        this.ensureCapacity(this.length() + 1);
        this.add(_value);
    }

    public int size() {
        return this.length();
    }
}

package com.twinkle.framework.core.lang.util;

import com.twinkle.framework.core.error.NoSpaceException;

import java.util.Arrays;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/1/19 6:18 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class ObjectArrayImpl<E> extends AbstractArray implements MutableObjectArray<E> {
    private E[] buffer;

    public ObjectArrayImpl(int _size) {
        this.buffer = this.newArray(_size);
    }

    public ObjectArrayImpl(E[] _array, int _size) {
        this.buffer = _array;
        this.length(_size);
    }

    public ObjectArrayImpl(E[] _array) {
        this(_array, 0);
    }

    @Override
    protected Object _array() {
        return this.buffer;
    }

    protected E[] newArray(int _size) {
        E[] tempArray = (E[]) (new Object[_size]);
        return tempArray;
    }

    @Override
    public void ensureCapacity(int _capacity) throws NoSpaceException {
        if (this.buffer.length < _capacity) {
            E[] tempArray = this.newArray(_capacity);
            System.arraycopy(this.buffer, 0, tempArray, 0, this.length);
            this.buffer = tempArray;
        }
    }

    @Override
    public void reallocate(int _size) {
        this.buffer = this.newArray(_size);
        this.length = 0;
    }

    @Override
    public void length(int _size) throws IllegalArgumentException {
        int tempLength = this.buffer.length;
        if (_size > tempLength) {
            throw new IllegalArgumentException("Length provided <" + _size + "> is greater than array capacity <" + this.buffer.length + ">.");
        }
        this.length = _size;
        if (_size < tempLength) {
            for (int i = _size; i < tempLength; ++i) {
                this.buffer[i] = null;
            }
        }
    }

    @Override
    public void length(int _endIndex, Object _value) {
        int tempLength = this.length;
        this.length(_endIndex);
        if (_endIndex > tempLength) {
            Arrays.fill(this.buffer, tempLength, _endIndex, _value);
        }

    }

    @Override
    public void clear() {
        Arrays.fill(this.buffer, 0, this.length, (Object) null);
        this.length = 0;
    }

    @Override
    public int capacity() {
        return this.buffer.length;
    }

    @Override
    public E get(int _index) throws ArrayIndexOutOfBoundsException {
        if (_index < this.length && _index >= 0) {
            return this.buffer[_index];
        } else {
            throw new ArrayIndexOutOfBoundsException("Array index [" + _index + "] is out of bounds. It must be in range of [0, " + this.length + ")");
        }
    }

    @Override
    public void put(int _index, E _value) throws ArrayIndexOutOfBoundsException {
        if (_index < this.length && _index >= 0) {
            this.buffer[_index] = _value;
        } else {
            throw new ArrayIndexOutOfBoundsException("Array index [" + _index + "] is out of bounds. It must be in range of [0, " + this.length + ")");
        }
    }

    @Override
    public void transfer(E[] _srcArray, int _srcPos, int _destPos, int _length) throws ArrayIndexOutOfBoundsException, NullPointerException {
        int tempContentLength = _destPos + _length;
        this.length(Math.max(tempContentLength, this.length));
        super.copyFrom(_srcArray, _srcPos, _destPos, _length);
    }

    @Override
    public void transfer(ObjectArray<E> _srcArray, int _srcPos, int _destPos, int _length) throws ArrayIndexOutOfBoundsException, NullPointerException {
        int tempContentLength = _destPos + _length;
        this.length(Math.max(tempContentLength, this.length));
        super.copyFrom(_srcArray, _srcPos, _destPos, _length);
    }

    @Override
    public void add(E _value) throws ArrayIndexOutOfBoundsException {
        if (this.length >= this.buffer.length) {
            throw new ArrayIndexOutOfBoundsException("Cannot add value into this array. Array capacity of <" + this.buffer.length + "> is reached.");
        } else {
            this.buffer[this.length++] = _value;
        }
    }

    @Override
    public E remove() throws ArrayIndexOutOfBoundsException {
        if (this.length == 0) {
            throw new ArrayIndexOutOfBoundsException("Cannot remove from an empty array");
        } else {
            E tempItem = this.buffer[--this.length];
            this.buffer[this.length] = null;
            return tempItem;
        }
    }

    @Override
    public E[] array() throws UnsupportedOperationException {
        return this.buffer;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        try {
            ObjectArrayImpl tempArray = (ObjectArrayImpl) super.clone();
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
        } else if (!(_obj instanceof ObjectArray)) {
            return false;
        }
        ObjectArray tempArray = (ObjectArray) _obj;
        int tempLength = tempArray.length();
        if (this.length != tempLength) {
            return false;
        } else {
            int tempIndex = 0;
            while (true) {
                if (tempIndex >= this.length) {
                    return true;
                }
                Object tempThisObj = this.buffer[tempIndex];
                Object tempSecondObj = tempArray.get(tempIndex);
                if (tempThisObj == null) {
                    if (tempSecondObj != null) {
                        break;
                    }
                } else if (!tempThisObj.equals(tempSecondObj)) {
                    break;
                }
                ++tempIndex;
            }
            return false;
        }
    }

    @Override
    public int hashCode() {
        int tempCode = 1;
        for (int i = this.length - 1; i >= 0; --i) {
            tempCode = 31 * tempCode + (this.buffer[i] == null ? 0 : this.buffer[i].hashCode());
        }
        return tempCode;
    }

    /**
     * Update the value at _index position.
     *
     * @param _index
     * @param _value
     */
    public void putAt(int _index, E _value) {
        this.put(_index, _value);
    }

    /**
     * Get the value at _index position.
     *
     * @param _index
     * @return
     */
    public E getAt(int _index) {
        return this.get(_index);
    }

    /**
     * Add value into this array.
     *
     * @param _value
     */
    public void plus(E _value) {
        this.ensureCapacity(this.size() + 1);
        this.add(_value);
    }

    public int size() {
        return this.length;
    }
}

package com.twinkle.framework.struct.util;

import com.twinkle.framework.core.lang.util.MutableObjectArray;
import com.twinkle.framework.core.lang.util.ObjectArray;
import com.twinkle.framework.core.lang.util.ObjectArrayImpl;
import com.twinkle.framework.struct.type.StructAttribute;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/1/19 6:41 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public final class StructAttributeArrayImpl<T> extends ObjectArrayImpl<StructAttribute> implements MutableObjectArray<StructAttribute>, MutableStructAttributeArray<T> {
    protected StructAttributeArrayImpl(int _size) {
        super(_size);
    }

    protected StructAttributeArrayImpl(StructAttribute[] _array, int _size) {
        super(_array, _size);
    }

    protected StructAttributeArrayImpl(StructAttribute[] _array) {
        super(_array);
    }

    @Override
    protected StructAttribute[] newArray(int _size) {
        return new StructAttribute[_size];
    }

    @Override
    public void transfer(StructAttributeArray _srcArray, int _srcPos, int _destPos, int _length) throws ArrayIndexOutOfBoundsException, NullPointerException {
        super.transfer((ObjectArray) _srcArray, _srcPos, _destPos, _length);
    }

    @Override
    public void length(int _endIndex, StructAttribute _value) {
        int tempLength = this.length;
        this.length(_endIndex);
        if (_endIndex > tempLength) {
            StructAttribute[] tempArray = this.array();
            int tempEndIndex = _endIndex - 1;
            for (int i = tempLength; i < tempEndIndex; ++i) {
                tempArray[i] = _value.duplicate();
            }
            tempArray[tempEndIndex] = _value;
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        try {
            StructAttributeArrayImpl tempArray = (StructAttributeArrayImpl) super.clone();
            int tempLength = this.length;
            for (int i = 0; i < tempLength; ++i) {
                StructAttribute tempItem = this.get(i);
                tempArray.put(i, tempItem == null ? null : tempItem.duplicate());
            }
            return tempArray;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }
}

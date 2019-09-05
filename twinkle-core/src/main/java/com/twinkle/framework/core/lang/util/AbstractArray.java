package com.twinkle.framework.core.lang.util;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/31/19 6:12 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public abstract class AbstractArray implements Array {
    protected int length = 0;

    protected abstract Object _array();

    @Override
    public Object clone() throws CloneNotSupportedException {
        try {
            AbstractArray tempArray = (AbstractArray) super.clone();
            tempArray.length = this.length;
            return tempArray;
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    @Override
    public int length() {
        return this.length;
    }

    @Override
    public void copyTo(Object _destObj, int _destPos, int _srcPos, int _length) throws ArrayIndexOutOfBoundsException, ArrayStoreException, NullPointerException, ClassCastException {
        if (_srcPos >= 0 && _srcPos + _length <= this.length) {
            System.arraycopy(this._array(), _srcPos, _destObj, _destPos, _length);
        } else {
            throw new ArrayIndexOutOfBoundsException(_srcPos + " exceeds the limits of source array.");
        }
    }

    @Override
    public void copyFrom(Object _srcArray, int _srcPos, int _destPos, int _length) throws ArrayIndexOutOfBoundsException, ArrayStoreException, NullPointerException, ClassCastException {
        if (_destPos >= 0 && _destPos + _length <= this.length) {
            System.arraycopy(_srcArray, _srcPos, this._array(), _destPos, _length);
        } else {
            throw new ArrayIndexOutOfBoundsException(_destPos + " exceeds the limits of destination array.");
        }
    }

    @Override
    public void copyTo(Array _destArray, int _destPos, int _srcPos, int _length) throws ArrayIndexOutOfBoundsException, ArrayStoreException, NullPointerException, ClassCastException {
        if (_srcPos >= 0 && _srcPos + _length <= this.length) {
            _destArray.copyFrom(this._array(), _srcPos, _destPos, _length);
        } else {
            throw new ArrayIndexOutOfBoundsException(_srcPos + " exceeds the limits of source array.");
        }
    }

    @Override
    public void copyFrom(Array _srcArray, int _srcPos, int _destPos, int _length) throws ArrayIndexOutOfBoundsException, ArrayStoreException, NullPointerException, ClassCastException {
        if (_destPos >= 0 && _destPos + _length <= this.length) {
            _srcArray.copyTo(this._array(), _destPos, _srcPos, _length);
        } else {
            throw new ArrayIndexOutOfBoundsException(_destPos + " exceeds the limits of destination array.");
        }
    }

    /**
     * Always has array.
     *
     * @return
     */
    public boolean hasArray() {
        return true;
    }

    /**
     * The offset of this array is 0.
     *
     * @return
     */
    public int arrayOffset() {
        return 0;
    }
}

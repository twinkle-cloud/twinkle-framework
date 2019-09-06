package com.twinkle.framework.core.lang.util;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/1/19 6:53 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public final class StringArrayImpl extends ObjectArrayImpl<String> implements MutableObjectArray<String>, MutableStringArray {
    public StringArrayImpl(int _size) {
        super(_size);
    }

    public StringArrayImpl(String[] _array, int _size) {
        super(_array, _size);
    }

    protected StringArrayImpl(String[] _array) {
        super(_array);
    }
    @Override
    protected String[] newArray(int _size) {
        return new String[_size];
    }
    @Override
    public void transfer(StringArray _srcArray, int _srcPos, int _destPos, int _length) throws ArrayIndexOutOfBoundsException, NullPointerException {
        super.transfer((ObjectArray) _srcArray, _srcPos, _destPos, _length);
    }
    @Override
    public void length(int _endIndex, String _value) {
        super.length(_endIndex, _value);
    }
}

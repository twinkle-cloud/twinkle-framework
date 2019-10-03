package com.twinkle.framework.core.lang.util;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/30/19 5:54 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface MutableObjectArray<E> extends ObjectArray<E>, MutableArray, ArrayWrapper<E[]> {
    /**
     * Update from array.length - _endIndex items' value to _value.
     *
     * @param _endIndex
     * @param _value
     * @throws IllegalArgumentException
     */
    void length(int _endIndex, Object _value) throws IllegalArgumentException;

    /**
     * Copy the src array items to this array.
     *
     * @param _srcArray
     * @param _srcPos
     * @param _destPos
     * @param _length
     * @throws ArrayIndexOutOfBoundsException
     * @throws NullPointerException
     */
    void transfer(E[] _srcArray, int _srcPos, int _destPos, int _length) throws ArrayIndexOutOfBoundsException, NullPointerException;

    /**
     * Copy the src array items to this array.
     *
     * @param _srcArray
     * @param _srcPos
     * @param _destPos
     * @param _length
     * @throws ArrayIndexOutOfBoundsException
     * @throws NullPointerException
     */
    void transfer(ObjectArray<E> _srcArray, int _srcPos, int _destPos, int _length) throws ArrayIndexOutOfBoundsException, NullPointerException;

    /**
     * Add the value into this array.
     *
     * @param _value
     * @throws ArrayIndexOutOfBoundsException
     */
    void add(E _value) throws ArrayIndexOutOfBoundsException;

    /**
     * Remove the last item of this array, and return the removed value.
     *
     * @return
     * @throws ArrayIndexOutOfBoundsException
     */
    E remove() throws ArrayIndexOutOfBoundsException;

    /**
     * Get the current array.
     *
     * @return
     * @throws UnsupportedOperationException
     */
    @Override
    E[] array() throws UnsupportedOperationException;

    /**
     * Has array or not?
     *
     * @return
     */
    boolean hasArray();

    /**
     * Array's offset.
     *
     * @return
     */
    int arrayOffset();
}

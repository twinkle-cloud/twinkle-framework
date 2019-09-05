package com.twinkle.framework.core.lang.util;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/30/19 5:51 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface MutableIntegerArray extends IntegerArray, MutableArray, ArrayWrapper<int[]> {
    /**
     * Update from array.length - _endIndex items' value to _value.
     *
     * @param _endIndex
     * @param _value
     * @throws IllegalArgumentException
     */
    void length(int _endIndex, int _value) throws IllegalArgumentException;

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
    void transfer(int[] _srcArray, int _srcPos, int _destPos, int _length) throws ArrayIndexOutOfBoundsException, NullPointerException;

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
    void transfer(IntegerArray _srcArray, int _srcPos, int _destPos, int _length) throws ArrayIndexOutOfBoundsException, NullPointerException;

    /**
     * Add the value into this array.
     *
     * @param _value
     * @throws ArrayIndexOutOfBoundsException
     */
    void add(int _value) throws ArrayIndexOutOfBoundsException;

    /**
     * Remove the last item of this array, and return the removed value.
     *
     * @return
     * @throws ArrayIndexOutOfBoundsException
     */
    int remove() throws ArrayIndexOutOfBoundsException;

    /**
     * Get the current array.
     *
     * @return
     * @throws UnsupportedOperationException
     */
    int[] array() throws UnsupportedOperationException;

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

package com.twinkle.framework.core.lang.util;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/30/19 7:47 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface ArrayWrapperFactory<A> {
    /**
     * Build an array wrapper with given Java array and size.
     *
     * @param _array
     * @param _size
     * @return
     */
    ArrayWrapper<A> wrap(A _array, int _size);

    /**
     * Build an array wrapper with given size.
     *
     * @param _size
     * @return
     */
    ArrayWrapper<A> newArrayWrapper(int _size);

    /**
     * Build a new array with given size.
     *
     * @param _size
     * @return
     */
    A newArray(int _size);

    /**
     * Get Array's Class.
     *
     * @return
     */
    Class<A> getArrayClass();
}

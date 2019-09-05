package com.twinkle.framework.core.lang.util;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/30/19 11:30 AM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface MutableArray extends Array {
    /**
     * Reallocate an empty array.
     *
     * @param _size
     */
    void reallocate(int _size);

    /**
     * Reset the array's length.
     *
     * @param _size
     * @throws IllegalArgumentException
     */
    void length(int _size) throws IllegalArgumentException;

    /**
     * Clear the array.
     */
    void clear();

    /**
     * Get the real capacity of this array.
     *
     * @return
     */
    int capacity();
}

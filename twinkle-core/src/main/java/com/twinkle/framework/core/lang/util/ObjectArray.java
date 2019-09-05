package com.twinkle.framework.core.lang.util;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/30/19 5:53 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface ObjectArray<E> extends Array {
    /**
     * Get the array item with the given index.
     *
     * @param _index
     * @return
     * @throws ArrayIndexOutOfBoundsException
     */
    E get(int _index) throws ArrayIndexOutOfBoundsException;

    /**
     * Update the index's item with the given attribute.
     *
     * @param _index
     * @param _value
     * @throws ArrayIndexOutOfBoundsException
     */
    void put(int _index, E _value) throws ArrayIndexOutOfBoundsException;
}

package com.twinkle.framework.core.lang.util;

import com.twinkle.framework.core.error.NoSpaceException;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/30/19 10:17 AM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface Array extends Cloneable {
    /**
     * Get the length of this array.
     *
     * @return
     */
    int length();

    /**
     * To ensure the capacity of this array.
     *
     * @param _capacity
     * @throws NoSpaceException
     */
    void ensureCapacity(int _capacity) throws NoSpaceException;

    /**
     * Copy the src array to dest array.
     *
     * @param _destArray
     * @param _destPos
     * @param _srcPos
     * @param _length
     * @throws ArrayIndexOutOfBoundsException
     * @throws ArrayStoreException
     * @throws NullPointerException
     * @throws ClassCastException
     */
    void copyTo(Array _destArray, int _destPos, int _srcPos, int _length) throws ArrayIndexOutOfBoundsException, ArrayStoreException, NullPointerException, ClassCastException;

    /**
     * Copy the src array to dest array.
     *
     * @param _destObj
     * @param _destPos
     * @param _srcPos
     * @param _length
     * @throws ArrayIndexOutOfBoundsException
     * @throws ArrayStoreException
     * @throws NullPointerException
     * @throws ClassCastException
     */
    void copyTo(Object _destObj, int _destPos, int _srcPos, int _length) throws ArrayIndexOutOfBoundsException, ArrayStoreException, NullPointerException, ClassCastException;

    /**
     * Copy the src array to dest array.
     *
     * @param _srcArray
     * @param _srcPos
     * @param _destPos
     * @param _length
     * @throws ArrayIndexOutOfBoundsException
     * @throws ArrayStoreException
     * @throws NullPointerException
     * @throws ClassCastException
     */
    void copyFrom(Array _srcArray, int _srcPos, int _destPos, int _length) throws ArrayIndexOutOfBoundsException, ArrayStoreException, NullPointerException, ClassCastException;

    /**
     * Copy the src array to dest array.
     *
     * @param _srcArray
     * @param _srcPos
     * @param _destPos
     * @param _length
     * @throws ArrayIndexOutOfBoundsException
     * @throws ArrayStoreException
     * @throws NullPointerException
     * @throws ClassCastException
     */
    void copyFrom(Object _srcArray, int _srcPos, int _destPos, int _length) throws ArrayIndexOutOfBoundsException, ArrayStoreException, NullPointerException, ClassCastException;

    /**
     * Clone method,
     *
     * @return
     * @throws CloneNotSupportedException
     */
    Object clone() throws CloneNotSupportedException;
}

package com.twinkle.framework.struct.util;

import com.twinkle.framework.core.lang.util.*;
import com.twinkle.framework.struct.type.ArrayType;
import com.twinkle.framework.struct.type.StructAttribute;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/30/19 7:48 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface ArrayAllocator extends ArrayWrappersFactory {
    /**
     * Build an array with given array type, and the initial size.
     *
     * @param _arrayType
     * @param _size
     * @return
     */
    MutableArray newArray(ArrayType _arrayType, int _size);

    /**
     * Build a boolean array with initial size.
     *
     * @param _size
     * @return
     */
    MutableBooleanArray newBooleanArray(int _size);

    /**
     * Build a byte array with initial size.
     *
     * @param _size
     * @return
     */
    MutableByteArray newByteArray(int _size);

    /**
     * Build a char array with initial size.
     *
     * @param _size
     * @return
     */
    MutableCharArray newCharArray(int _size);

    /**
     * Build a double array with initial size.
     *
     * @param _size
     * @return
     */
    MutableDoubleArray newDoubleArray(int _size);

    /**
     * Build a float array with initial size.
     *
     * @param _size
     * @return
     */
    MutableFloatArray newFloatArray(int _size);

    /**
     * Build a integer array with initial size.
     *
     * @param _size
     * @return
     */
    MutableIntegerArray newIntegerArray(int _size);

    /**
     * Build a long array with initial size.
     *
     * @param _size
     * @return
     */
    MutableLongArray newLongArray(int _size);

    /**
     * Build a short array with initial size.
     *
     * @param _size
     * @return
     */
    MutableShortArray newShortArray(int _size);

    /**
     * Build a String array with initial size.
     *
     * @param _size
     * @return
     */
    MutableStringArray newStringArray(int _size);

    /**
     * Build a Struct Attribute array with initial size.
     *
     * @param _size
     * @return
     */
    MutableStructAttributeArray newStructAttributeArray(int _size);

    /**
     * Wrap a boolean array with given array, and initial size.
     *
     * @param _array
     * @param _size
     * @return
     */
    MutableBooleanArray wrap(boolean[] _array, int _size);

    /**
     * Wrap a boolean array with given array.
     *
     * @param _array
     * @return
     */
    MutableBooleanArray wrap(boolean[] _array);

    /**
     * Wrap a byte array with given array, and initial size.
     *
     * @param _array
     * @param _size
     * @return
     */
    MutableByteArray wrap(byte[] _array, int _size);

    /**
     * Wrap a byte array with given array.
     *
     * @param _array
     * @return
     */
    MutableByteArray wrap(byte[] _array);

    /**
     * Wrap a char array with given array, and initial size.
     *
     * @param _array
     * @param _size
     * @return
     */
    MutableCharArray wrap(char[] _array, int _size);

    /**
     * Wrap a char array with given array.
     *
     * @param _array
     * @return
     */
    MutableCharArray wrap(char[] _array);

    /**
     * Wrap a double array with given array, and initial size.
     *
     * @param _array
     * @param _size
     * @return
     */
    MutableDoubleArray wrap(double[] _array, int _size);

    /**
     * Wrap a double array with given array.
     *
     * @param _array
     * @return
     */
    MutableDoubleArray wrap(double[] _array);

    /**
     * Wrap a float array with given array, and initial size.
     *
     * @param _array
     * @param _size
     * @return
     */
    MutableFloatArray wrap(float[] _array, int _size);

    /**
     * Wrap a float array with given array.
     *
     * @param _array
     * @return
     */
    MutableFloatArray wrap(float[] _array);

    /**
     * Wrap an int array with given array, and initial size.
     *
     * @param _array
     * @param _size
     * @return
     */
    MutableIntegerArray wrap(int[] _array, int _size);

    /**
     * Wrap an int array with given array.
     *
     * @param _array
     * @return
     */
    MutableIntegerArray wrap(int[] _array);

    /**
     * Wrap a long array with given array, and initial size.
     *
     * @param _array
     * @param _size
     * @return
     */
    MutableLongArray wrap(long[] _array, int _size);

    /**
     * Wrap a long array with given array.
     *
     * @param _array
     * @return
     */
    MutableLongArray wrap(long[] _array);

    /**
     * Wrap a short array with given array, and initial size.
     *
     * @param _array
     * @param _size
     * @return
     */
    MutableShortArray wrap(short[] _array, int _size);

    /**
     * Wrap a short array with given array.
     *
     * @param _array
     * @return
     */
    MutableShortArray wrap(short[] _array);

    /**
     * Wrap a String array with given array, and initial size.
     *
     * @param _array
     * @param _size
     * @return
     */
    MutableStringArray wrap(String[] _array, int _size);

    /**
     * Wrap a String array with given array.
     *
     * @param _array
     * @return
     */
    MutableStringArray wrap(String[] _array);

    /**
     * Wrap a Struct Attribute array with given array, and initial size.
     *
     * @param _array
     * @param _size
     * @return
     */
    MutableStructAttributeArray wrap(StructAttribute[] _array, int _size);

    /**
     * Wrap a Struct Attribute array with given array.
     *
     * @param _array
     * @return
     */
    MutableStructAttributeArray wrap(StructAttribute[] _array);
}

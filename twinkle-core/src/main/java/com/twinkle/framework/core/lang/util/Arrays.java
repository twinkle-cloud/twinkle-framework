package com.twinkle.framework.core.lang.util;

import java.lang.reflect.Array;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/30/19 11:34 AM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class Arrays {
    private static boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];
    private static char[] EMPTY_CHAR_ARRAY = new char[0];
    private static byte[] EMPTY_BYTE_ARRAY = new byte[0];
    private static short[] EMPTY_SHORT_ARRAY = new short[0];
    private static int[] EMPTY_INT_ARRAY = new int[0];
    private static long[] EMPTY_LONG_ARRAY = new long[0];
    private static float[] EMPTY_FLOAT_ARRAY = new float[0];
    private static double[] EMPTY_DOUBLE_ARRAY = new double[0];

    public Arrays() {
    }

    /**
     * Build a boolean array with given size.
     *
     * @param _size
     * @return
     */
    public static boolean[] newBooleanArray(int _size) {
        return new boolean[_size];
    }

    /**
     * Build a boolean array with given size and initial value.
     *
     * @param _size
     * @param _value
     * @return
     */
    public static boolean[] newBooleanArray(int _size, boolean _value) {
        return fill(newBooleanArray(_size), _value);
    }

    /**
     * Get an empty boolean array.
     *
     * @return
     */
    public static boolean[] emptyBooleanArray() {
        return EMPTY_BOOLEAN_ARRAY;
    }

    /**
     * Fill the array with given value.
     *
     * @param _array
     * @param _value
     * @return
     */
    public static boolean[] fill(boolean[] _array, boolean _value) {
        java.util.Arrays.fill(_array, _value);
        return _array;
    }

    /**
     * To resize the given array with new size.
     *
     * @param _array
     * @param _size
     * @return
     */
    public static boolean[] resize(boolean[] _array, int _size) {
        if (_array == null) {
            throw new NullPointerException("Source array is null");
        } else if (_size != _array.length) {
            boolean[] tempArray = newBooleanArray(_size);
            System.arraycopy(_array, 0, tempArray, 0, Math.min(_size, _array.length));
            return tempArray;
        } else {
            return _array;
        }
    }

    /**
     * To ensure the boolean array having the given size at least.
     *
     * @param _array
     * @param _size
     * @return
     */
    public static boolean[] ensureSize(boolean[] _array, int _size) {
        if (_array == null) {
            throw new NullPointerException("Source array is null");
        } else if (_size > _array.length) {
            boolean[] tempArray = newBooleanArray(_size);
            System.arraycopy(_array, 0, tempArray, 0, _array.length);
            return tempArray;
        } else {
            return _array;
        }
    }

    /**
     * Copy sub-boolean array from src array to dest array.
     *
     * @param _srcArray
     * @param _srcPos
     * @param _destArray
     * @param _destPos
     * @param _length
     * @return
     */
    public static boolean[] copy(boolean[] _srcArray, int _srcPos, boolean[] _destArray, int _destPos, int _length) {
        System.arraycopy(_srcArray, _srcPos, _destArray, _destPos, _length);
        return _destArray;
    }

    /**
     * Copy boolean array from src array to dest array.
     *
     * @param _srcArray
     * @param _destArray
     * @param _length
     * @return
     */
    public static boolean[] copy(boolean[] _srcArray, boolean[] _destArray, int _length) {
        System.arraycopy(_srcArray, 0, _destArray, 0, _length);
        return _destArray;
    }

    /**
     * Append sub-src array to the dest pos of the dest array.
     *
     * @param _destArray
     * @param _destPos
     * @param _srcArray
     * @param _srcPos
     * @param _length
     * @return
     */
    public static boolean[] append(boolean[] _destArray, int _destPos, boolean[] _srcArray, int _srcPos, int _length) {
        if (_destArray.length < _destPos + _length) {
            boolean[] tempArray = newBooleanArray(_destPos + _length);
            return copy(_srcArray, _srcPos, copy(_destArray, 0, tempArray, 0, _destPos), _destPos, _length);
        } else {
            return copy(_srcArray, _srcPos, _destArray, _destPos, _length);
        }
    }

    /**
     * Append the src array to the dest array.
     *
     * @param _destArray
     * @param _srcArray
     * @return
     */
    public static boolean[] append(boolean[] _destArray, boolean[] _srcArray) {
        return append(_destArray, _destArray.length, _srcArray, 0, _srcArray.length);
    }

    /**
     * Clone sub-src array.
     *
     * @param _srcArray
     * @param _srcPos
     * @param _length
     * @return
     */
    public static boolean[] clone(boolean[] _srcArray, int _srcPos, int _length) {
        if (_srcArray == null) {
            return null;
        } else {
            boolean[] tempArray = newBooleanArray(_length);
            return copy(_srcArray, _srcPos, tempArray, 0, _length);
        }
    }

    /**
     * Clone the entire src array.
     *
     * @param _srcArray
     * @return
     */
    public static boolean[] clone(boolean[] _srcArray) {
        return clone(_srcArray, 0, _srcArray.length);
    }

    /**
     * Build a char array with given size.
     *
     * @param _size
     * @return
     */
    public static char[] newCharArray(int _size) {
        return new char[_size];
    }

    /**
     * Build a char array with given size and initial value.
     *
     * @param _size
     * @param _value
     * @return
     */
    public static char[] newCharArray(int _size, char _value) {
        return fill(newCharArray(_size), _value);
    }

    /**
     * Get an empty char array.
     *
     * @return
     */
    public static char[] emptyCharArray() {
        return EMPTY_CHAR_ARRAY;
    }

    /**
     * Fill the array with given value.
     *
     * @param _array
     * @param _value
     * @return
     */
    public static char[] fill(char[] _array, char _value) {
        java.util.Arrays.fill(_array, _value);
        return _array;
    }

    /**
     * To resize the given array with new size.
     *
     * @param _array
     * @param _size
     * @return
     */
    public static char[] resize(char[] _array, int _size) {
        if (_array == null) {
            throw new NullPointerException("Source array is null");
        } else if (_size != _array.length) {
            char[] tempArray = newCharArray(_size);
            System.arraycopy(_array, 0, tempArray, 0, Math.min(_size, _array.length));
            return tempArray;
        } else {
            return _array;
        }
    }

    /**
     * To ensure the char array having the given size at least.
     *
     * @param _array
     * @param _size
     * @return
     */
    public static char[] ensureSize(char[] _array, int _size) {
        if (_array == null) {
            throw new NullPointerException("Source array is null");
        } else if (_size > _array.length) {
            char[] tempArray = newCharArray(_size);
            System.arraycopy(_array, 0, tempArray, 0, _array.length);
            return tempArray;
        } else {
            return _array;
        }
    }

    /**
     * Copy sub-char array from src array to dest array.
     *
     * @param _srcArray
     * @param _srcPos
     * @param _destArray
     * @param _destPos
     * @param _length
     * @return
     */
    public static char[] copy(char[] _srcArray, int _srcPos, char[] _destArray, int _destPos, int _length) {
        System.arraycopy(_srcArray, _srcPos, _destArray, _destPos, _length);
        return _destArray;
    }

    /**
     * Copy entire array from src array to dest array.
     *
     * @param _srcArray
     * @param _destArray
     * @param _length
     * @return
     */
    public static char[] copy(char[] _srcArray, char[] _destArray, int _length) {
        System.arraycopy(_srcArray, 0, _destArray, 0, _length);
        return _destArray;
    }

    /**
     * Append sub-src array to the dest pos of the dest array.
     *
     * @param _destArray
     * @param _destPos
     * @param _srcArray
     * @param _srcPos
     * @param _length
     * @return
     */
    public static char[] append(char[] _destArray, int _destPos, char[] _srcArray, int _srcPos, int _length) {
        if (_destArray.length < _destPos + _length) {
            char[] tempArray = newCharArray(_destPos + _length);
            return copy(_srcArray, _srcPos, copy(_destArray, 0, tempArray, 0, _destPos), _destPos, _length);
        } else {
            return copy(_srcArray, _srcPos, _destArray, _destPos, _length);
        }
    }

    /**
     * Append entire src array to the dest array.
     *
     * @param _destArray
     * @param _srcArray
     * @return
     */
    public static char[] append(char[] _destArray, char[] _srcArray) {
        return append(_destArray, _destArray.length, _srcArray, 0, _srcArray.length);
    }

    /**
     * Clone the sub-src array.
     *
     * @param _srcArray
     * @param _srcPos
     * @param _length
     * @return
     */
    public static char[] clone(char[] _srcArray, int _srcPos, int _length) {
        if (_srcArray == null) {
            return null;
        } else {
            char[] tempArray = newCharArray(_length);
            return copy(_srcArray, _srcPos, tempArray, 0, _length);
        }
    }

    /**
     * Clone the entire array.
     *
     * @param _array
     * @return
     */
    public static char[] clone(char[] _array) {
        return clone(_array, 0, _array.length);
    }

    /**
     * Build a byte array with given size.
     *
     * @param _size
     * @return
     */
    public static byte[] newByteArray(int _size) {
        return new byte[_size];
    }

    /**
     * Build a byte array with given size and initial value.
     *
     * @param _size
     * @param _value
     * @return
     */
    public static byte[] newByteArray(int _size, byte _value) {
        return fill(newByteArray(_size), _value);
    }

    /**
     * Get an empty byte array.
     *
     * @return
     */
    public static byte[] emptyByteArray() {
        return EMPTY_BYTE_ARRAY;
    }

    /**
     * Fill the array with given value.
     *
     * @param _array
     * @param _value
     * @return
     */
    public static byte[] fill(byte[] _array, byte _value) {
        java.util.Arrays.fill(_array, _value);
        return _array;
    }

    /**
     * To resize the given array with new size.
     *
     * @param _array
     * @param _size
     * @return
     */
    public static byte[] resize(byte[] _array, int _size) {
        if (_array == null) {
            throw new NullPointerException("Source array is null");
        } else if (_size != _array.length) {
            byte[] tempArray = newByteArray(_size);
            System.arraycopy(_array, 0, tempArray, 0, Math.min(_size, _array.length));
            return tempArray;
        } else {
            return _array;
        }
    }

    /**
     * To ensure the byte array having the given size at least.
     *
     * @param _array
     * @param _size
     * @return
     */
    public static byte[] ensureSize(byte[] _array, int _size) {
        if (_array == null) {
            throw new NullPointerException("Source array is null");
        } else if (_size > _array.length) {
            byte[] tempArray = newByteArray(_size);
            System.arraycopy(_array, 0, tempArray, 0, _array.length);
            return tempArray;
        } else {
            return _array;
        }
    }

    /**
     * Copy sub-byte array from src array to dest array.
     *
     * @param _srcArray
     * @param _srcPos
     * @param _destArray
     * @param _destPos
     * @param _length
     * @return
     */
    public static byte[] copy(byte[] _srcArray, int _srcPos, byte[] _destArray, int _destPos, int _length) {
        System.arraycopy(_srcArray, _srcPos, _destArray, _destPos, _length);
        return _destArray;
    }

    /**
     * Copy the src array (length's items.) to the dest array.
     *
     * @param _srcArray
     * @param _destArray
     * @param _length
     * @return
     */
    public static byte[] copy(byte[] _srcArray, byte[] _destArray, int _length) {
        System.arraycopy(_srcArray, 0, _destArray, 0, _length);
        return _destArray;
    }

    /**
     * Append sub-src array to the dest pos of the dest array.
     *
     * @param _destArray
     * @param _destPos
     * @param _srcArray
     * @param _srcPos
     * @param _length
     * @return
     */
    public static byte[] append(byte[] _destArray, int _destPos, byte[] _srcArray, int _srcPos, int _length) {
        if (_destArray.length < _destPos + _length) {
            byte[] tempArray = newByteArray(_destPos + _length);
            return copy(_srcArray, _srcPos, copy(_destArray, 0, tempArray, 0, _destPos), _destPos, _length);
        } else {
            return copy(_srcArray, _srcPos, _destArray, _destPos, _length);
        }
    }

    /**
     * Append the src array to the dest array.
     *
     * @param _destArray
     * @param _srcArray
     * @return
     */
    public static byte[] append(byte[] _destArray, byte[] _srcArray) {
        return append(_destArray, _destArray.length, _srcArray, 0, _srcArray.length);
    }

    /**
     * Clone the sub-src array.
     *
     * @param _srcArray
     * @param _srcPos
     * @param _length
     * @return
     */
    public static byte[] clone(byte[] _srcArray, int _srcPos, int _length) {
        if (_srcArray == null) {
            return null;
        } else {
            byte[] tempArray = newByteArray(_length);
            return copy(_srcArray, _srcPos, tempArray, 0, _length);
        }
    }

    /**
     * Clone the given array.
     *
     * @param _srcArray
     * @return
     */
    public static byte[] clone(byte[] _srcArray) {
        return clone(_srcArray, 0, _srcArray.length);
    }

    /**
     * Build a short array with given size.
     *
     * @param _size
     * @return
     */
    public static short[] newShortArray(int _size) {
        return new short[_size];
    }

    /**
     * Build a short array with given size and initial value.
     *
     * @param _size
     * @param _value
     * @return
     */
    public static short[] newShortArray(int _size, short _value) {
        return fill(newShortArray(_size), _value);
    }

    /**
     * Get an empty short array.
     *
     * @return
     */
    public static short[] emptyShortArray() {
        return EMPTY_SHORT_ARRAY;
    }

    /**
     * Fill the array with given value.
     *
     * @param _array
     * @param _value
     * @return
     */
    public static short[] fill(short[] _array, short _value) {
        java.util.Arrays.fill(_array, _value);
        return _array;
    }

    /**
     * To resize the given array with new size.
     *
     * @param _array
     * @param _size
     * @return
     */
    public static short[] resize(short[] _array, int _size) {
        if (_array == null) {
            throw new NullPointerException("Source array is null");
        } else if (_size != _array.length) {
            short[] tempArray = newShortArray(_size);
            System.arraycopy(_array, 0, tempArray, 0, Math.min(_size, _array.length));
            return tempArray;
        } else {
            return _array;
        }
    }

    /**
     * To ensure the short array having the given size at least.
     *
     * @param _array
     * @param _size
     * @return
     */
    public static short[] ensureSize(short[] _array, int _size) {
        if (_array == null) {
            throw new NullPointerException("Source array is null");
        } else if (_size > _array.length) {
            short[] tempArray = newShortArray(_size);
            System.arraycopy(_array, 0, tempArray, 0, _array.length);
            return tempArray;
        } else {
            return _array;
        }
    }

    /**
     * Copy sub-short array from src array to dest array.
     *
     * @param _srcArray
     * @param _srcPos
     * @param _destArray
     * @param _destPos
     * @param _length
     * @return
     */
    public static short[] copy(short[] _srcArray, int _srcPos, short[] _destArray, int _destPos, int _length) {
        System.arraycopy(_srcArray, _srcPos, _destArray, _destPos, _length);
        return _destArray;
    }

    /**
     * Copy the src array (length's items.) to the dest array.
     *
     * @param _srcArray
     * @param _destArray
     * @param _length
     * @return
     */
    public static short[] copy(short[] _srcArray, short[] _destArray, int _length) {
        System.arraycopy(_srcArray, 0, _destArray, 0, _length);
        return _destArray;
    }

    /**
     * Append sub-src array to the dest pos of the dest array.
     *
     * @param _destArray
     * @param _destPos
     * @param _srcArray
     * @param _srcPos
     * @param _length
     * @return
     */
    public static short[] append(short[] _destArray, int _destPos, short[] _srcArray, int _srcPos, int _length) {
        if (_destArray.length < _destPos + _length) {
            short[] tempArray = newShortArray(_destPos + _length);
            return copy(_srcArray, _srcPos, copy(_destArray, 0, tempArray, 0, _destPos), _destPos, _length);
        } else {
            return copy(_srcArray, _srcPos, _destArray, _destPos, _length);
        }
    }

    /**
     * Append the src array to the dest array.
     *
     * @param _destArray
     * @param _srcArray
     * @return
     */
    public static short[] append(short[] _destArray, short[] _srcArray) {
        return append(_destArray, _destArray.length, _srcArray, 0, _srcArray.length);
    }

    /**
     * Clone the sub-src array.
     *
     * @param _srcArray
     * @param _srcPos
     * @param _length
     * @return
     */
    public static short[] clone(short[] _srcArray, int _srcPos, int _length) {
        if (_srcArray == null) {
            return null;
        } else {
            short[] tempArray = newShortArray(_length);
            return copy(_srcArray, _srcPos, tempArray, 0, _length);
        }
    }

    /**
     * Clone the given array.
     *
     * @param _srcArray
     * @return
     */
    public static short[] clone(short[] _srcArray) {
        return clone(_srcArray, 0, _srcArray.length);
    }

    /**
     * Build an int array with given size.
     *
     * @param _size
     * @return
     */
    public static int[] newIntArray(int _size) {
        return new int[_size];
    }

    /**
     * Build an int array with given size and initial value.
     *
     * @param _size
     * @param _value
     * @return
     */
    public static int[] newIntArray(int _size, int _value) {
        return fill(newIntArray(_size), _value);
    }

    /**
     * Get an empty int array.
     *
     * @return
     */
    public static int[] emptyIntArray() {
        return EMPTY_INT_ARRAY;
    }

    /**
     * Fill the array with given value.
     *
     * @param _array
     * @param _value
     * @return
     */
    public static int[] fill(int[] _array, int _value) {
        java.util.Arrays.fill(_array, _value);
        return _array;
    }

    /**
     * To resize the given array with new size.
     *
     * @param _array
     * @param _size
     * @return
     */
    public static int[] resize(int[] _array, int _size) {
        if (_array == null) {
            throw new NullPointerException("Source array is null");
        } else if (_size != _array.length) {
            int[] tempArray = newIntArray(_size);
            System.arraycopy(_array, 0, tempArray, 0, Math.min(_size, _array.length));
            return tempArray;
        } else {
            return _array;
        }
    }

    /**
     * To ensure the int array having the given size at least.
     *
     * @param _array
     * @param _size
     * @return
     */
    public static int[] ensureSize(int[] _array, int _size) {
        if (_array == null) {
            throw new NullPointerException("Source array is null");
        } else if (_size > _array.length) {
            int[] tempArray = newIntArray(_size);
            System.arraycopy(_array, 0, tempArray, 0, _array.length);
            return tempArray;
        } else {
            return _array;
        }
    }

    /**
     * Copy sub-int array from src array to dest array.
     *
     * @param _srcArray
     * @param _srcPos
     * @param _destArray
     * @param _destPos
     * @param _length
     * @return
     */
    public static int[] copy(int[] _srcArray, int _srcPos, int[] _destArray, int _destPos, int _length) {
        System.arraycopy(_srcArray, _srcPos, _destArray, _destPos, _length);
        return _destArray;
    }

    /**
     * Copy the src array (length's items.) to the dest array.
     *
     * @param _srcArray
     * @param _destArray
     * @param _length
     * @return
     */
    public static int[] copy(int[] _srcArray, int[] _destArray, int _length) {
        System.arraycopy(_srcArray, 0, _destArray, 0, _length);
        return _destArray;
    }

    /**
     * Append sub-src array to the dest pos of the dest array.
     *
     * @param _destArray
     * @param _destPos
     * @param _srcArray
     * @param _srcPos
     * @param _length
     * @return
     */
    public static int[] append(int[] _destArray, int _destPos, int[] _srcArray, int _srcPos, int _length) {
        if (_destArray.length < _destPos + _length) {
            int[] tempArray = newIntArray(_destPos + _length);
            return copy(_srcArray, _srcPos, copy(_destArray, 0, tempArray, 0, _destPos), _destPos, _length);
        } else {
            return copy(_srcArray, _srcPos, _destArray, _destPos, _length);
        }
    }

    /**
     * Append the src array to the dest array.
     *
     * @param _destArray
     * @param _srcArray
     * @return
     */
    public static int[] append(int[] _destArray, int[] _srcArray) {
        return append(_destArray, _destArray.length, _srcArray, 0, _srcArray.length);
    }

    /**
     * Clone the sub-src array.
     *
     * @param _srcArray
     * @param _srcPos
     * @param _length
     * @return
     */
    public static int[] clone(int[] _srcArray, int _srcPos, int _length) {
        if (_srcArray == null) {
            return null;
        } else {
            int[] tempArray = newIntArray(_length);
            return copy(_srcArray, _srcPos, tempArray, 0, _length);
        }
    }

    /**
     * Clone the given array.
     *
     * @param _srcArray
     * @return
     */
    public static int[] clone(int[] _srcArray) {
        return clone(_srcArray, 0, _srcArray.length);
    }

    /**
     * Build an long array with given size.
     *
     * @param _size
     * @return
     */
    public static long[] newLongArray(int _size) {
        return new long[_size];
    }

    /**
     * Build an long array with given size and initial value.
     *
     * @param _size
     * @param _value
     * @return
     */
    public static long[] newLongArray(int _size, long _value) {
        return fill(newLongArray(_size), _value);
    }

    /**
     * Get an empty long array.
     *
     * @return
     */
    public static long[] emptyLongArray() {
        return EMPTY_LONG_ARRAY;
    }

    /**
     * Fill the array with given value.
     *
     * @param _array
     * @param _value
     * @return
     */
    public static long[] fill(long[] _array, long _value) {
        java.util.Arrays.fill(_array, _value);
        return _array;
    }

    /**
     * To resize the given array with new size.
     *
     * @param _array
     * @param _size
     * @return
     */
    public static long[] resize(long[] _array, int _size) {
        if (_array == null) {
            throw new NullPointerException("Source array is null");
        } else if (_size != _array.length) {
            long[] tempArray = newLongArray(_size);
            System.arraycopy(_array, 0, tempArray, 0, Math.min(_size, _array.length));
            return tempArray;
        } else {
            return _array;
        }
    }

    /**
     * To ensure the long array having the given size at least.
     *
     * @param _array
     * @param _size
     * @return
     */
    public static long[] ensureSize(long[] _array, int _size) {
        if (_array == null) {
            throw new NullPointerException("Source array is null");
        } else if (_size > _array.length) {
            long[] tempArray = newLongArray(_size);
            System.arraycopy(_array, 0, tempArray, 0, _array.length);
            return tempArray;
        } else {
            return _array;
        }
    }

    /**
     * Copy sub-long array from src array to dest array.
     *
     * @param _srcArray
     * @param _srcPos
     * @param _destArray
     * @param _destPos
     * @param _length
     * @return
     */
    public static long[] copy(long[] _srcArray, int _srcPos, long[] _destArray, int _destPos, int _length) {
        System.arraycopy(_srcArray, _srcPos, _destArray, _destPos, _length);
        return _destArray;
    }

    /**
     * Copy the src array (length's items.) to the dest array.
     *
     * @param _srcArray
     * @param _destArray
     * @param _length
     * @return
     */
    public static long[] copy(long[] _srcArray, long[] _destArray, int _length) {
        System.arraycopy(_srcArray, 0, _destArray, 0, _length);
        return _destArray;
    }

    /**
     * Append sub-src array to the dest pos of the dest array.
     *
     * @param _destArray
     * @param _destPos
     * @param _srcArray
     * @param _srcPos
     * @param _length
     * @return
     */
    public static long[] append(long[] _destArray, int _destPos, long[] _srcArray, int _srcPos, int _length) {
        if (_destArray.length < _destPos + _length) {
            long[] tempArray = newLongArray(_destPos + _length);
            return copy(_srcArray, _srcPos, copy(_destArray, 0, tempArray, 0, _destPos), _destPos, _length);
        } else {
            return copy(_srcArray, _srcPos, _destArray, _destPos, _length);
        }
    }

    /**
     * Append the src array to the dest array.
     *
     * @param _destArray
     * @param _srcArray
     * @return
     */
    public static long[] append(long[] _destArray, long[] _srcArray) {
        return append(_destArray, _destArray.length, _srcArray, 0, _srcArray.length);
    }

    /**
     * Clone the sub-src array.
     *
     * @param _srcArray
     * @param _srcPos
     * @param _length
     * @return
     */
    public static long[] clone(long[] _srcArray, int _srcPos, int _length) {
        if (_srcArray == null) {
            return null;
        } else {
            long[] tempArray = newLongArray(_length);
            return copy(_srcArray, _srcPos, tempArray, 0, _length);
        }
    }

    /**
     * Clone the given array.
     *
     * @param _srcArray
     * @return
     */
    public static long[] clone(long[] _srcArray) {
        return clone(_srcArray, 0, _srcArray.length);
    }

    /**
     * Build an float array with given size.
     *
     * @param _size
     * @return
     */
    public static float[] newFloatArray(int _size) {
        return new float[_size];
    }

    /**
     * Build an float array with given size and initial value.
     *
     * @param _size
     * @param _value
     * @return
     */
    public static float[] newFloatArray(int _size, float _value) {
        return fill(newFloatArray(_size), _value);
    }

    /**
     * Get an empty float array.
     *
     * @return
     */
    public static float[] emptyFloatArray() {
        return EMPTY_FLOAT_ARRAY;
    }

    /**
     * Fill the array with given value.
     *
     * @param _array
     * @param _value
     * @return
     */
    public static float[] fill(float[] _array, float _value) {
        java.util.Arrays.fill(_array, _value);
        return _array;
    }

    /**
     * To resize the given array with new size.
     *
     * @param _array
     * @param _size
     * @return
     */
    public static float[] resize(float[] _array, int _size) {
        if (_array == null) {
            throw new NullPointerException("Source array is null");
        } else if (_size != _array.length) {
            float[] tempArray = newFloatArray(_size);
            System.arraycopy(_array, 0, tempArray, 0, Math.min(_size, _array.length));
            return tempArray;
        } else {
            return _array;
        }
    }

    /**
     * To ensure the float array having the given size at least.
     *
     * @param _array
     * @param _size
     * @return
     */
    public static float[] ensureSize(float[] _array, int _size) {
        if (_array == null) {
            throw new NullPointerException("Source array is null");
        } else if (_size > _array.length) {
            float[] tempArray = newFloatArray(_size);
            System.arraycopy(_array, 0, tempArray, 0, _array.length);
            return tempArray;
        } else {
            return _array;
        }
    }

    /**
     * Copy the sub-src array to the dest array.
     *
     * @param _srcArray
     * @param _srcPos
     * @param _destArray
     * @param _destPos
     * @param _length
     * @return
     */
    public static float[] copy(float[] _srcArray, int _srcPos, float[] _destArray, int _destPos, int _length) {
        System.arraycopy(_srcArray, _srcPos, _destArray, _destPos, _length);
        return _destArray;
    }

    /**
     * Copy the entire src array to the dest array.
     *
     * @param _srcArray
     * @param _destArray
     * @param _length
     * @return
     */
    public static float[] copy(float[] _srcArray, float[] _destArray, int _length) {
        System.arraycopy(_srcArray, 0, _destArray, 0, _length);
        return _destArray;
    }

    /**
     * Append the sub array to the dest pos of the dest array.
     *
     * @param _destArray
     * @param _destPos
     * @param _srcArray
     * @param _srcPos
     * @param _length
     * @return
     */
    public static float[] append(float[] _destArray, int _destPos, float[] _srcArray, int _srcPos, int _length) {
        if (_destArray.length < _destPos + _length) {
            float[] tempArray = newFloatArray(_destPos + _length);
            return copy(_srcArray, _srcPos, copy(_destArray, 0, tempArray, 0, _destPos), _destPos, _length);
        } else {
            return copy(_srcArray, _srcPos, _destArray, _destPos, _length);
        }
    }

    /**
     * Append the src array to the dest array.
     *
     * @param _destArray
     * @param _srcArray
     * @return
     */
    public static float[] append(float[] _destArray, float[] _srcArray) {
        return append(_destArray, _destArray.length, _srcArray, 0, _srcArray.length);
    }

    /**
     * Clone the sub-array.
     *
     * @param _array
     * @param _pos
     * @param _length
     * @return
     */
    public static float[] clone(float[] _array, int _pos, int _length) {
        if (_array == null) {
            return null;
        } else {
            float[] tempArray = newFloatArray(_length);
            return copy(_array, _pos, tempArray, 0, _length);
        }
    }

    /**
     * Clone the given array.
     *
     * @param _array
     * @return
     */
    public static float[] clone(float[] _array) {
        return clone(_array, 0, _array.length);
    }

    /**
     * Build a new double array with given length.
     *
     * @param _length
     * @return
     */
    public static double[] newDoubleArray(int _length) {
        return new double[_length];
    }

    /**
     * Build a new double array with the default value for each item.
     *
     * @param _length
     * @param _value
     * @return
     */
    public static double[] newDoubleArray(int _length, double _value) {
        return fill(newDoubleArray(_length), _value);
    }

    /**
     * Get empty double array.
     *
     * @return
     */
    public static double[] emptyDoubleArray() {
        return EMPTY_DOUBLE_ARRAY;
    }

    /**
     * To fill all of the items with the given value.
     *
     * @param _array
     * @param _value
     * @return
     */
    public static double[] fill(double[] _array, double _value) {
        java.util.Arrays.fill(_array, _value);
        return _array;
    }

    /**
     * Resize the given array.
     *
     * @param _array
     * @param _size
     * @return
     */
    public static double[] resize(double[] _array, int _size) {
        if (_array == null) {
            throw new NullPointerException("Source array is null");
        } else if (_size != _array.length) {
            double[] tempArray = newDoubleArray(_size);
            System.arraycopy(_array, 0, tempArray, 0, Math.min(_size, _array.length));
            return tempArray;
        } else {
            return _array;
        }
    }

    /**
     * To ensure the double array having the given size at least.
     *
     * @param _array
     * @param _size
     * @return
     */
    public static double[] ensureSize(double[] _array, int _size) {
        if (_array == null) {
            throw new NullPointerException("Source array is null");
        } else if (_size > _array.length) {
            double[] tempArray = newDoubleArray(_size);
            System.arraycopy(_array, 0, tempArray, 0, _array.length);
            return tempArray;
        } else {
            return _array;
        }
    }

    /**
     * Copy double array from src array to dest array.
     *
     * @param _srcArray
     * @param _srcPos
     * @param _destArray
     * @param _destPos
     * @param _length
     * @return
     */
    public static double[] copy(double[] _srcArray, int _srcPos, double[] _destArray, int _destPos, int _length) {
        System.arraycopy(_srcArray, _srcPos, _destArray, _destPos, _length);
        return _destArray;
    }

    public static double[] copy(double[] _srcArray, double[] _destArray, int _length) {
        System.arraycopy(_srcArray, 0, _destArray, 0, _length);
        return _destArray;
    }

    /**
     * Append the sub-src array to the dest's position of the dest array.
     *
     * @param _destArray
     * @param _destPos
     * @param _srcArray
     * @param _srcPos
     * @param _length
     * @return
     */
    public static double[] append(double[] _destArray, int _destPos, double[] _srcArray, int _srcPos, int _length) {
        if (_destArray.length < _destPos + _length) {
            double[] tempDestArray = newDoubleArray(_destPos + _length);
            return copy(_srcArray, _srcPos, copy(_destArray, 0, tempDestArray, 0, _destPos), _destPos, _length);
        } else {
            return copy(_srcArray, _srcPos, _destArray, _destPos, _length);
        }
    }

    /**
     * Append the source array to dest array.
     *
     * @param _destArray
     * @param _srcArray
     * @return
     */
    public static double[] append(double[] _destArray, double[] _srcArray) {
        return append(_destArray, _destArray.length, _srcArray, 0, _srcArray.length);
    }

    /**
     * Clone the sub array( with given start pos and length)
     *
     * @param _srcArray
     * @param _srcPos
     * @param _length
     * @return
     */
    public static double[] clone(double[] _srcArray, int _srcPos, int _length) {
        if (_srcArray == null) {
            return null;
        } else {
            double[] tempDestArray = newDoubleArray(_length);
            return copy(_srcArray, _srcPos, tempDestArray, 0, _length);
        }
    }

    /**
     * Clone the given double array.
     *
     * @param _srcArray
     * @return
     */
    public static double[] clone(double[] _srcArray) {
        return clone(_srcArray, 0, _srcArray.length);
    }

    /**
     * Build a new array.
     *
     * @param _componentType
     * @param _length
     * @param <T>
     * @return
     */
    public static <T> T[] newArray(Class<?> _componentType, int _length) {
        return (T[]) Array.newInstance(_componentType, _length);
    }

    /**
     * Build a new array, and update all of the items' value = given value.
     *
     * @param _componentType
     * @param _length
     * @param _value
     * @param <T>
     * @return
     */
    public static <T> T[] newArray(Class<T> _componentType, int _length, T _value) {
        return fill(newArray(_componentType, _length), _value);
    }

    private static <T> T[] newArrayOfType(Class<?> _componentType, int _length) {
        return newArray(_componentType.getComponentType(), _length);
    }

    private static <T> T[] newArrayByExample(T[] _array, int _length) {
        return newArrayOfType(_array.getClass(), _length);
    }

    /**
     * Get an empty array with given item class.
     *
     * @param _class
     * @param <T>
     * @return
     */
    public static <T> T[] emptyArray(Class<T> _class) {
        return newArray(_class, 0);
    }

    /**
     * Fill the array with given value.
     *
     * @param _array
     * @param _value
     * @param <T>
     * @return
     */
    public static <T> T[] fill(T[] _array, T _value) {
        java.util.Arrays.fill(_array, _value);
        return _array;
    }

    /**
     * Clone a new array with given size.
     *
     * @param _array
     * @param _size
     * @param <T>
     * @return
     */
    public static <T> T[] resize(T[] _array, int _size) {
        if (_array == null) {
            throw new NullPointerException("Source array is null");
        } else if (_size != _array.length) {
            T[] tempDestArray = newArrayByExample(_array, _size);
            System.arraycopy(_array, 0, tempDestArray, 0, Math.min(_size, _array.length));
            return tempDestArray;
        } else {
            return _array;
        }
    }

    /**
     * Ensure the array's size, if current size < given size,
     * then will clone a new array with the given size.
     *
     * @param _array
     * @param _size
     * @param <T>
     * @return
     */
    public static <T> T[] ensureSize(T[] _array, int _size) {
        if (_array == null) {
            throw new NullPointerException("Source array is null");
        } else if (_size > _array.length) {
            T[] tempDestArray = newArrayByExample(_array, _size);
            System.arraycopy(_array, 0, tempDestArray, 0, _array.length);
            return tempDestArray;
        } else {
            return _array;
        }
    }

    /**
     * Refer to system.arraycopy();
     *
     * @param _srcArray
     * @param _srcPos
     * @param _destArray
     * @param _destPos
     * @param _length
     * @param <T>
     * @return
     */
    public static <T> T[] copy(T[] _srcArray, int _srcPos, T[] _destArray, int _destPos, int _length) {
        System.arraycopy(_srcArray, _srcPos, _destArray, _destPos, _length);
        return _destArray;
    }

    /**
     * Copy array.
     *
     * @param _srcArray
     * @param _destArray
     * @param _length
     * @param <T>
     * @return
     */
    public static <T> T[] copy(T[] _srcArray, T[] _destArray, int _length) {
        System.arraycopy(_srcArray, 0, _destArray, 0, _length);
        return _destArray;
    }

    /**
     * Append src array to dest array.
     *
     * @param _destArray
     * @param _destPos
     * @param _srcArray
     * @param _srcPos
     * @param _length
     * @param <T>
     * @return
     */
    public static <T> T[] append(T[] _destArray, int _destPos, T[] _srcArray, int _srcPos, int _length) {
        if (_destArray.length < _destPos + _length) {
            T[] tempArray = newArrayByExample(_destArray, _destPos + _length);
            return copy(_srcArray, _srcPos, copy(_destArray, 0, tempArray, 0, _destPos), _destPos, _length);
        } else {
            return copy(_srcArray, _srcPos, _destArray, _destPos, _length);
        }
    }

    /**
     * Append entire src Array to dest array.
     *
     * @param _destArray
     * @param _srcArray
     * @param <T>
     * @return
     */
    public static <T> T[] append(T[] _destArray, T[] _srcArray) {
        return append(_destArray, _destArray.length, _srcArray, 0, _srcArray.length);
    }

    /**
     * Clone sub-src array with given start pos and length.
     *
     * @param _srcArray
     * @param _srcPos
     * @param _length
     * @param <T>
     * @return
     */
    public static <T> T[] clone(T[] _srcArray, int _srcPos, int _length) {
        if (_srcArray == null) {
            return null;
        } else {
            T[] tempDestArray = newArrayByExample(_srcArray, _length);
            return copy(_srcArray, _srcPos, tempDestArray, 0, _length);
        }
    }

    /**
     * Clone entire src array.
     *
     * @param _srcArray
     * @param <T>
     * @return
     */
    public static <T> T[] clone(T[] _srcArray) {
        return clone(_srcArray, 0, _srcArray.length);
    }
}

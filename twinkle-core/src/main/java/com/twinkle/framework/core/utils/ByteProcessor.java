package com.twinkle.framework.core.utils;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/8/19 3:41 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class ByteProcessor {
    private static final String HEX_STR = "0123456789ABCDEF";

    public ByteProcessor() {
    }

    /**
     * Convert the given byte array's [index] to int.
     *
     * @param _byteArray
     * @param _index
     * @param _bit:      bits type.
     * @return
     */
    public static long readLong(byte[] _byteArray, int _index, int _bit) {
        long tempValue = 0L;
        if (_bit == Long.BYTES) {
            tempValue = readLong(_byteArray, _index);
        } else if (_bit == Integer.BYTES) {
            tempValue = readUnsignedInt(_byteArray, _index);
        } else if (_bit == Short.BYTES) {
            tempValue = readUnsignedShort(_byteArray, _index);
        } else if (_bit == Byte.BYTES) {
            tempValue = readUnsignedByte(_byteArray, _index);
        }

        return tempValue;
    }

    /**
     * Convert give byte arrays' index item to long.
     *
     * @param _byteArray
     * @param _index
     * @param _bit
     * @return
     */
    public static int readInt(byte[] _byteArray, int _index, int _bit) {
        int tempValue = 0;
        if (_bit == Integer.BYTES) {
            tempValue = readInt(_byteArray, _index);
        } else if (_bit == Short.BYTES) {
            tempValue = readUnsignedShort(_byteArray, _index);
        } else if (_bit == Byte.BYTES) {
            tempValue = readUnsignedByte(_byteArray, _index);
        }

        return tempValue;
    }

    /**
     * Convert the given byte array's index byte to long.
     *
     * @param _byteArray
     * @param _index
     * @return
     */
    public static long readLong(byte[] _byteArray, int _index) {
        long tempResultValue = ((_byteArray[_index] & 255) << 24 | (_byteArray[_index + 1] & 255) << 16 | (_byteArray[_index + 2] & 255) << 8 | _byteArray[_index + 3] & 255);
        return tempResultValue << 32 | (long) (_byteArray[_index + 4] << 24 | (_byteArray[_index + 5] & 255) << 16 | (_byteArray[_index + 6] & 255) << 8 | _byteArray[_index + 7] & 255);
    }

    /**
     * Convert the given byte array's index byte to int.
     *
     * @param _byteArray
     * @param _index
     * @return
     */
    public static int readInt(byte[] _byteArray, int _index) {
        return _byteArray[_index] << 24 | (_byteArray[_index + 1] & 255) << 16 | (_byteArray[_index + 2] & 255) << 8 | _byteArray[_index + 3] & 255;
    }

    /**
     * Convert the given byte array's index byte to unsigned int.
     *
     * @param _byteArray
     * @param _index
     * @return
     */
    public static long readUnsignedInt(byte[] _byteArray, int _index) {
        return (long) (_byteArray[_index] & 255) << 24 | (long) ((_byteArray[_index + 1] & 255) << 16) | (long) ((_byteArray[_index + 2] & 255) << 8) | (long) (_byteArray[_index + 3] & 255);
    }

    /**
     * Convert the given byte array's index byte to short.
     *
     * @param _byteArray
     * @param _index
     * @return
     */
    public static short readShort(byte[] _byteArray, int _index) {
        return (short) ((_byteArray[_index] & 255) << 8 | _byteArray[_index + 1] & 255);
    }

    /**
     * Convert the given byte array's index byte to unsigned short.
     *
     * @param _byteArray
     * @param _index
     * @return
     */
    public static int readUnsignedShort(byte[] _byteArray, int _index) {
        return (_byteArray[_index] & 255) << 8 | _byteArray[_index + 1] & 255;
    }

    /**
     * Convert the given byte array's index byte to byte.
     *
     * @param _byteArray
     * @param _index
     * @return
     */
    public static byte readByte(byte[] _byteArray, int _index) {
        return (byte) (_byteArray[_index] & 255);
    }

    /**
     * Convert the given byte array's index byte to unsigned byte.
     *
     * @param _byteArray
     * @param _index
     * @return
     */
    public static int readUnsignedByte(byte[] _byteArray, int _index) {
        return _byteArray[_index] & 255;
    }

    /**
     * Write int into the dest byte array.
     *
     * @param _byteArray
     * @param _index
     * @param _value
     * @return
     */
    public static int writeInt(byte[] _byteArray, int _index, int _value) {
        _byteArray[_index++] = (byte) (_value >>> 24 & 255);
        _byteArray[_index++] = (byte) (_value >>> 16 & 255);
        _byteArray[_index++] = (byte) (_value >>> 8 & 255);
        _byteArray[_index++] = (byte) (_value >>> 0 & 255);
        return _index;
    }

    /**
     * Write long into the dest byte array.
     *
     * @param _byteArray
     * @param _index:    the start index.
     * @param _value:    the source value.
     * @return
     */
    public static int writeLong(byte[] _byteArray, int _index, long _value) {
        _byteArray[_index++] = (byte) ((int) (_value >>> 56 & 255L));
        _byteArray[_index++] = (byte) ((int) (_value >>> 48 & 255L));
        _byteArray[_index++] = (byte) ((int) (_value >>> 40 & 255L));
        _byteArray[_index++] = (byte) ((int) (_value >>> 32 & 255L));
        _byteArray[_index++] = (byte) ((int) (_value >>> 24 & 255L));
        _byteArray[_index++] = (byte) ((int) (_value >>> 16 & 255L));
        _byteArray[_index++] = (byte) ((int) (_value >>> 8 & 255L));
        _byteArray[_index++] = (byte) ((int) (_value >>> 0 & 255L));
        return _index;
    }

    /**
     * Write short into the dest byte array.
     *
     * @param _byteArray
     * @param _index
     * @param _value
     * @return
     */
    public static int writeShort(byte[] _byteArray, int _index, short _value) {
        _byteArray[_index++] = (byte) (_value >>> 8 & 255);
        _byteArray[_index++] = (byte) (_value >>> 0 & 255);
        return _index;
    }

    /**
     * Convert sub Hex string to byte array.
     *
     * @param _str
     * @param _beginIndex
     * @param _length
     * @return
     * @throws NumberFormatException
     */
    public static byte[] getBytesFromHexString(String _str, int _beginIndex, int _length) throws NumberFormatException {
        if (_length % 2 != 0) {
            throw new NumberFormatException("The hex string to process must be an even number of bytes long");
        }
        int tempFinalLength = _length / 2;
        byte[] tempResultArray = new byte[tempFinalLength];
        int tempBeginIndex = _beginIndex;

        for (int i = 0; i < tempFinalLength; i++) {
            char tempChar = _str.charAt(tempBeginIndex++);
            int tempByteItemValue = getCharValue(tempChar);
            if (tempByteItemValue < 0) {
                return null;
            }
            tempResultArray[i] = (byte) (tempByteItemValue << 4);
            tempChar = _str.charAt(tempBeginIndex++);
            tempByteItemValue = getCharValue(tempChar);
            tempResultArray[i] += (byte) tempByteItemValue;
        }

        return tempResultArray;
    }

    /**
     * Get the int value for specified char.
     *
     * @param _char
     * @return
     */
    private static int getCharValue(char _char) {
        int tempValue = -1;
        if (_char >= '0' && _char <= '9') {
            tempValue = _char - 48;
        } else if (_char >= 'a' && _char <= 'f') {
            tempValue = 10 + _char - 97;
        } else {
            if (_char < 'A' || _char > 'F') {
                return tempValue;
            }
            tempValue = 10 + _char - 65;
        }
        return tempValue;
    }

    /**
     * Convert sub source array to Hex String.
     *
     * @param _byteArray
     * @param _beginIndex
     * @param _length
     * @return
     */
    public static String getHexStringFromBytes(byte[] _byteArray, int _beginIndex, int _length) {
        StringBuilder tempBuilder = new StringBuilder(_length * 2);
        int tempTotalSize = _beginIndex + _length;

        for (int i = _beginIndex; i < tempTotalSize; i++) {
            tempBuilder.append(HEX_STR.charAt(_byteArray[i] >> 4 & 15));
            tempBuilder.append(HEX_STR.charAt(_byteArray[i] & 15));
        }

        return tempBuilder.toString();
    }

    /**
     * Get HexString from sub byte Array.
     *
     * @param _byteArray:  the source array.
     * @param _beginIndex: the start index.
     * @param _length:     the length of the array will be converted.
     * @param _value:      an integer to be converted to a string.
     * @param _radix:      the radix to use in the string representation.
     * @return
     */
    public static String getHexStringFromBytes(byte[] _byteArray, int _beginIndex, int _length, int _value, int _radix) {
        StringBuilder tempBuilder = new StringBuilder(_length * 2 + _length + 8);
        String tempStr = Integer.toString(_value, _radix);
        int tempLength = tempStr.length();
        int i;
        if (tempLength < 5) {
            for (i = 0; i < 5 - tempLength; i++) {
                tempBuilder.append('0');
            }
        }

        tempBuilder.append(tempStr);
        tempBuilder.append(":  ");
        i = _beginIndex + _length;

        for (int j = _beginIndex; j < i; j++) {
            tempBuilder.append(HEX_STR.charAt(_byteArray[j] >> 4 & 15));
            tempBuilder.append(HEX_STR.charAt(_byteArray[j++] & 15));
            if (j < i) {
                tempBuilder.append(HEX_STR.charAt(_byteArray[j] >> 4 & 15));
                tempBuilder.append(HEX_STR.charAt(_byteArray[j] & 15));
                tempBuilder.append(" ");
            }
        }

        return tempBuilder.toString();
    }

    /**
     * Reverse the sub array.
     *
     * @param _byteArray
     * @param _beginIndex
     * @param _length
     */
    public static void reverseBytes(byte[] _byteArray, int _beginIndex, int _length) {
        for (int i = _beginIndex + _length - 1; _beginIndex < i; --i) {
            byte tempByte = _byteArray[_beginIndex];
            _byteArray[_beginIndex] = _byteArray[i];
            _byteArray[i] = tempByte;
            ++_beginIndex;
        }

    }

    /**
     * Swap the byte.
     *
     * @param _byteArray
     * @param _beginIndex
     * @param _length
     */
    public static void swapNibbles(byte[] _byteArray, int _beginIndex, int _length) {
        int tempSize = _beginIndex + _length;

        for (int i = _beginIndex; i < tempSize; ++i) {
            _byteArray[i] = (byte) ((_byteArray[i] & 15) << 4 | (_byteArray[i] & 240) >>> 4);
        }

    }

    /**
     * Neglect padding digits.
     *
     * @param _byteArray
     * @param _beginIndex
     * @param _length
     * @param _key
     * @return
     */
    public static int neglectPaddingDigits(byte[] _byteArray, int _beginIndex, int _length, int _key) {
        int tempSize = _beginIndex + _length - 1;
        int tempResult = 0;

        for (int i = tempSize; i >= _beginIndex; --i) {
            if ((_byteArray[i] & 240) >>> 4 != _key || (_byteArray[i] & 15) != _key) {
                return tempResult;
            }
            ++tempResult;
        }
        return tempResult;
    }

    /**
     * Neglect padding chars.
     *
     * @param _byteArray
     * @param _beginIndex
     * @param _length
     * @param _key
     * @return
     */
    public static final int neglectPaddingChars(byte[] _byteArray, int _beginIndex, int _length, int _key) {
        int tempSize = _beginIndex + _length - 1;
        int tempResult = 0;

        for (int i = tempSize; i >= _beginIndex; --i) {
            if (_byteArray[i] != _key) {
                return tempResult;
            }
            ++tempResult;
        }

        return tempResult;
    }

    /**
     * Convert sub array to long.
     *
     * @param _byteArray
     * @param _beginIndex
     * @param _length
     * @return
     */
    public static long readLongFromBCDBytes(byte[] _byteArray, int _beginIndex, int _length) {
        int tempSize = _beginIndex + _length - 1;
        long tempResultValue = 0L;

        for (int i = tempSize; i >= _beginIndex; --i) {
            int tempAndValue = _byteArray[i] & 15;
            int tempRValue = (_byteArray[i] & 240) >>> 4;
            if (tempAndValue > 9 || tempRValue > 9) {
                return -1L;
            }
            tempResultValue = tempResultValue * 100L + (long) (tempRValue * 10) + (long) tempAndValue;
        }

        return tempResultValue;
    }

    /**
     * Convert sub array to int.
     *
     * @param _byteArray
     * @param _beginIndex
     * @param _length
     * @return
     */
    public static int readIntFromBCDBytes(byte[] _byteArray, int _beginIndex, int _length) {
        return (int) readLongFromBCDBytes(_byteArray, _beginIndex, _length);
    }

    public static void main(String[] _args) {
        byte[] tempArray = new byte[8];

        for (int i = 1; i < 9; ++i) {
            tempArray[i - 1] = (byte) i;
        }

        long var4 = readLong(tempArray, 0);
    }
}

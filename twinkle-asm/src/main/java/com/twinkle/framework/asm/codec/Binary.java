package com.twinkle.framework.asm.codec;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-01 11:29<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class Binary {
    private static final byte[] EMPTY_BYTES = new byte[0];
    private static final int[] BITS = new int[]{1, 2, 4, 8, 16, 32, 64, 128};

    public Binary() {
    }

    /**
     * Encode the byte Array to String.
     *
     * @param _byteArray
     * @return
     */
    public static String encode(byte[] _byteArray) {
        if (_byteArray != null && _byteArray.length != 0) {
            char[] tempCharArray = new char[_byteArray.length << 3];
            int tempByteArrayIndex = 0;
            //Convert the byte to 0/1 char.
            for(int i = tempCharArray.length - 1; tempByteArrayIndex < _byteArray.length; i -= 8) {
                for(int j = 0; j < BITS.length; ++j) {
                    if ((_byteArray[tempByteArrayIndex] & BITS[j]) == 0) {
                        tempCharArray[i - j] = '0';
                    } else {
                        tempCharArray[i - j] = '1';
                    }
                }
                tempByteArrayIndex++;
            }

            return new String(tempCharArray);
        } else {
            return "";
        }
    }

    /**
     * Encode the byte array to 0/1 array.
     *
     * @param _byteArray
     * @return
     */
    public static byte[] encodeBytes(byte[] _byteArray) {
        return encodeBytes(_byteArray, 0, _byteArray.length);
    }

    /**
     * Encode the given array to 0/1 array.
     *
     * @param _byteArray
     * @param _offset
     * @param _length
     * @return
     */
    private static byte[] encodeBytes(byte[] _byteArray, int _offset, int _length) {
        if (_byteArray == null) {
            return EMPTY_BYTES;
        } else {
            int tempLength = _byteArray.length - _offset;
            if (tempLength <= 0) {
                return EMPTY_BYTES;
            } else {
                if (tempLength < _length) {
                    _length = tempLength;
                }

                byte[] tempResultArray = new byte[_length << 3];
                int tempIndex = _offset;

                for(int i = tempResultArray.length - 1; tempIndex < _offset + _length; i -= 8) {
                    for(int j = 0; j < BITS.length; ++j) {
                        if ((_byteArray[tempIndex] & BITS[j]) == 0) {
                            //Char(48) = 0
                            tempResultArray[i - j] = 48;
                        } else {
                            //Char(49) = 1
                            tempResultArray[i - j] = 49;
                        }
                    }
                    tempIndex++;
                }

                return tempResultArray;
            }
        }
    }

    /**
     * Decode the 0/1 String to normal byte array.
     *
     * @param _str
     * @return
     */
    public static byte[] decode(String _str) {
        if (_str != null && _str.length() != 0) {
            int tempLength = _str.length();
            if (tempLength == 0) {
                return EMPTY_BYTES;
            } else {
                byte[] tempByteArray = new byte[tempLength >> 3];
                int tempIndex = 0;
                // Convert 0/1 to normal byte.
                for(int i = tempLength - 1; tempIndex < tempByteArray.length; i -= 8) {
                    for(int j = 0; j < BITS.length; ++j) {
                        if (_str.charAt(i - j) == '1') {
                            tempByteArray[tempIndex] = (byte)(tempByteArray[tempIndex] | BITS[j]);
                        }
                    }
                    ++tempIndex;
                }
                return tempByteArray;
            }
        } else {
            return EMPTY_BYTES;
        }
    }

    /**
     * Convert the 0/1 array to normal byte array.
     *
     * @param _byteArray
     * @return
     */
    public static byte[] decodeBytes(byte[] _byteArray) {
        if (_byteArray != null && _byteArray.length != 0) {
            byte[] tempResultArray = new byte[_byteArray.length >> 3];
            int tempIndex = 0;
            // Convert 0/1 to normal byte.
            for(int i = _byteArray.length - 1; tempIndex < tempResultArray.length; i -= 8) {
                for(int j = 0; j < BITS.length; ++j) {
                    //char(49) = 1
                    if (_byteArray[i - j] == 49) {
                        tempResultArray[tempIndex] = (byte)(tempResultArray[tempIndex] | BITS[j]);
                    }
                }

                tempIndex++;
            }

            return tempResultArray;
        } else {
            return EMPTY_BYTES;
        }
    }
}

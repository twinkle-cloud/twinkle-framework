package com.twinkle.framework.core.utils;

import sun.net.util.IPAddressUtil;

import java.util.Arrays;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/9/19 6:30 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class IPv6AddrHelper {
    private static final int INADDR16SZ = 16;
    private static final byte[] ZERO16SZ = newArray(INADDR16SZ);

    public IPv6AddrHelper() {
    }

    /**
     * Build an new empty array.
     *
     * @param _size
     * @return
     */
    private static byte[] newArray(int _size) {
        byte[] tempArray = new byte[_size];
        Arrays.fill(tempArray, (byte) 0);
        return tempArray;
    }

    /**
     * Build an new 16 bits empty array.
     *
     * @return
     */
    public static byte[] newByteArray() {
        return ZERO16SZ.clone();
    }

    /**
     * Build a new 16 bits array.
     *
     * @param _array
     * @return
     */
    public static byte[] newByteArray(byte[] _array) {
        byte[] tempResultArray = newByteArray();
        if (_array != null) {
            if (_array.length > INADDR16SZ) {
                System.arraycopy(_array, _array.length - INADDR16SZ, tempResultArray, 0, INADDR16SZ);
            } else {
                System.arraycopy(_array, 0, tempResultArray, INADDR16SZ - _array.length, _array.length);
            }
        }

        return tempResultArray;
    }

    /**
     * Convert IPv4 String to Numeric format byte array.
     *
     * @param _text
     * @return
     */
    private static byte[] textToNumericFormatV4(String _text) {
        return IPAddressUtil.textToNumericFormatV4(_text);
    }

    /**
     * Convert IPv6 String to Numeric format byte array.
     *
     * @param _text
     * @return
     */
    private static byte[] textToNumericFormatV6(String _text) {
        return IPAddressUtil.textToNumericFormatV6(_text);
    }

    /**
     * Check the array is IPv4 or not? if IPv4, will return the array.
     *
     * @param _array
     * @return
     */
    private static byte[] convertFromIPv4MappedAddress(byte[] _array) {
        return isIPv4MappedAddress(_array) ? _array : null;
    }

    /**
     * Check the give byte array is Ipv4 or not?
     *
     * @param _array
     * @return
     */
    private static boolean isIPv4MappedAddress(byte[] _array) {
        if (_array.length < INADDR16SZ) {
            return false;
        } else {
            return _array[0] == 0 && _array[1] == 0 && _array[2] == 0 && _array[3] == 0 && _array[4] == 0 && _array[5] == 0 && _array[6] == 0 && _array[7] == 0 && _array[8] == 0 && _array[9] == 0 && _array[10] == -1 && _array[11] == -1;
        }
    }

    /**
     * Parse the give string as IPv6.
     *
     * @param _ipv6Address
     * @return
     * @throws NumberFormatException
     */
    public static byte[] parseIPv6Address(String _ipv6Address) throws NumberFormatException {
        if (_ipv6Address == null) {
            throw new NumberFormatException("IPv6 Address string is null");
        } else {
            _ipv6Address = _ipv6Address.trim();
            byte[] tempArray = textToNumericFormatV6(_ipv6Address);
            if (tempArray == null) {
                throw new NumberFormatException("Invalid IPv6 Address " + _ipv6Address);
            } else {
                return tempArray.length == 16 ? tempArray : newByteArray(tempArray);
            }
        }
    }

    /**
     * Convert IPv6 Address to byte array.
     *
     * @param _ipv6Address
     * @return
     * @throws NumberFormatException
     */
    public static byte[] toByteArray(String _ipv6Address) throws NumberFormatException {
        if (_ipv6Address == null) {
            throw new NumberFormatException("Invalid IPv6 Address, the value is null.");
        }
        _ipv6Address = _ipv6Address.trim();
        byte[] tempResultArray;
        if (!_ipv6Address.isEmpty() && !_ipv6Address.contains(":") && !_ipv6Address.contains(".")) {
            if (_ipv6Address.length() % 2 != 0) {
                _ipv6Address = '0' + _ipv6Address;
            }
            tempResultArray = ByteProcessor.getBytesFromHexString(_ipv6Address, 0, _ipv6Address.length());
        } else {
            tempResultArray = textToNumericFormatV6(_ipv6Address);
        }

        if (tempResultArray == null) {
            throw new NumberFormatException("Invalid IPv6 Address " + _ipv6Address);
        } else {
            return tempResultArray.length == 16 ? tempResultArray : newByteArray(tempResultArray);
        }
    }

    /**
     * Convert byte array to IPv6 address.
     *
     * @param _array
     * @return
     */
    public static String toString(byte[] _array) {
        StringBuilder tempBuilder = new StringBuilder(39);

        for (int i = 0; i < 8; ++i) {
            tempBuilder.append(Integer.toHexString(_array[i << 1] << 8 & '\uff00' | _array[(i << 1) + 1] & 255));
            if (i < 7) {
                tempBuilder.append(":");
            }
        }

        return tempBuilder.toString();
    }
}

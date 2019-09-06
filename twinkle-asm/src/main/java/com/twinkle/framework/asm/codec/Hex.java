package com.twinkle.framework.asm.codec;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-01 11:30<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class Hex {

    private static final Charset ISO88591 = Charset.forName("ISO-8859-1");
    private static final byte[] HEX_ENCODING_TABLE = new byte[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};
    private static final byte[] HEX_DECODING_TABLE = new byte[128];

    public Hex() {
    }

    /**
     * Encode the hex byte array to String.
     *
     * @param _byteArray
     * @return
     */
    public static String encode(byte[] _byteArray) {
        return new String(encodeBytes(_byteArray, 0, _byteArray.length), ISO88591);
    }

    /**
     * Encode Byte array.
     *
     * @param _byteArray
     * @return
     */
    public static byte[] encodeBytes(byte[] _byteArray) {
        return encodeBytes(_byteArray, 0, _byteArray.length);
    }

    /**
     * Encode the byte array.
     *
     * @param _byteArray
     * @param _offset : begin index of the array.
     * @param _length : encode length.
     * @return
     */
    private static byte[] encodeBytes(byte[] _byteArray, int _offset, int _length) {
        ByteArrayOutputStream tempOutputStream = new ByteArrayOutputStream();

        try {
            encode(_byteArray, _offset, _length, tempOutputStream);
        } catch (IOException e) {
            throw new RuntimeException("exception encoding Hex string: " + e);
        }

        return tempOutputStream.toByteArray();
    }

    /**
     * Decode the Hex String to byte array.
     *
     * @param _str
     * @return
     */
    public static byte[] decode(String _str) {
        ByteArrayOutputStream tempOutputStream = new ByteArrayOutputStream();

        try {
            decode(_str, tempOutputStream);
        } catch (IOException e) {
            throw new RuntimeException("exception decoding Hex string: " + e);
        }

        return tempOutputStream.toByteArray();
    }

    /**
     * Decode the Hex byteArray.
     *
     * @param _byteArray
     * @return
     */
    public static byte[] decodeBytes(byte[] _byteArray) {
        ByteArrayOutputStream tempOutputStream = new ByteArrayOutputStream();

        try {
            decodeBytes(_byteArray, 0, _byteArray.length, tempOutputStream);
        } catch (IOException var3) {
            throw new RuntimeException("exception decoding Hex string: " + var3);
        }

        return tempOutputStream.toByteArray();
    }

    /**
     * Encode the byte Array to Hex Array.
     *
     * @param _byteArray
     * @param _offset, start encoding index.
     * @param _legnth, encoding length.
     * @param _outputStream, output the encode result.
     * @return
     * @throws IOException
     */
    private static int encode(byte[] _byteArray, int _offset, int _legnth, OutputStream _outputStream) throws IOException {
        for(int i = _offset; i < _offset + _legnth; ++i) {
            int tempEndoceValue = _byteArray[i] & 255;
            _outputStream.write(HEX_ENCODING_TABLE[tempEndoceValue >>> 4]);
            _outputStream.write(HEX_ENCODING_TABLE[tempEndoceValue & 15]);
        }

        return _legnth * 2;
    }

    /**
     * Decode Hex String, and output the result into the _outputsteam.
     * and Return the decode count.
     *
     * @param _str
     * @param _outputStream
     * @return
     * @throws IOException
     */
    private static int decode(String _str, OutputStream _outputStream) throws IOException {
        int tempDecodeCount = 0;

        int i;
        for(i = _str.length(); i > 0 && isIgnored(_str.charAt(i - 1)); --i) {
        }

        int tempIndex = 0;
        byte tempFirstByte;
        byte tempFollowingByte;
        if (tempIndex < i) {
            tempIndex = getNextSkipIgnored(_str, tempIndex, i);
            char tempZeroChar = _str.charAt(tempIndex++);
            tempIndex = getNextSkipIgnored(_str, tempIndex, i);
            char tempXChar = _str.charAt(tempIndex++);
            if (tempZeroChar != '0' || tempXChar != 'x') {
                tempFirstByte = HEX_DECODING_TABLE[tempZeroChar];
                tempFollowingByte = HEX_DECODING_TABLE[tempXChar];
                _outputStream.write(tempFirstByte << 4 | tempFollowingByte);
                tempDecodeCount++;
            }
        }

        while(tempIndex < i) {
            tempIndex = getNextSkipIgnored(_str, tempIndex, i);
            tempFirstByte = HEX_DECODING_TABLE[_str.charAt(tempIndex++)];
            tempIndex = getNextSkipIgnored(_str, tempIndex, i);
            tempFollowingByte = HEX_DECODING_TABLE[_str.charAt(tempIndex++)];
            _outputStream.write(tempFirstByte << 4 | tempFollowingByte);
            tempDecodeCount++;
        }

        return tempDecodeCount;
    }

    /**
     * Decode the given byte array.
     *
     * @param _byteArray
     * @param _offset
     * @param _legnth
     * @param _outputStream
     * @return
     * @throws IOException
     */
    private static int decodeBytes(byte[] _byteArray, int _offset, int _legnth, OutputStream _outputStream) throws IOException {
        int tempDecodeCount = 0;

        int i;
        for(i = _offset + _legnth; i > _offset && isIgnored(_byteArray[i - 1]); --i) {
        }

        int tempIndex = _offset;
        byte tempFirstByte;
        byte tempFollowingByte;
        if (_offset < i) {
            tempIndex = getNextSkipIgnored(_byteArray, _offset, i);
            byte tempZeroByte = _byteArray[tempIndex++];
            tempIndex = getNextSkipIgnored(_byteArray, tempIndex, i);
            byte tempXByte = _byteArray[tempIndex++];
            // Char(48) = 0, Char(120) = x
            //Here to judge the byte is Hex or Not.
            if (tempZeroByte != 48 || tempXByte != 120) {
                tempFirstByte = HEX_DECODING_TABLE[tempZeroByte];
                tempFollowingByte = HEX_DECODING_TABLE[tempXByte];
                _outputStream.write(tempFirstByte << 4 | tempFollowingByte);
                tempDecodeCount++;
            }
        }
        //Continue to decode the left bytes in the array.
        while(tempIndex < i) {
            tempIndex = getNextSkipIgnored(_byteArray, tempIndex, i);
            tempFirstByte = HEX_DECODING_TABLE[_byteArray[tempIndex++]];
            tempIndex = getNextSkipIgnored(_byteArray, tempIndex, i);
            tempFollowingByte = HEX_DECODING_TABLE[_byteArray[tempIndex++]];
            _outputStream.write(tempFirstByte << 4 | tempFollowingByte);
            tempDecodeCount++;
        }

        return tempDecodeCount;
    }

    /**
     * Get the next char index in the string which needs to be ignored.
     *
     * @param _str
     * @param _offset
     * @param _endIndex
     * @return
     */
    private static int getNextSkipIgnored(String _str, int _offset, int _endIndex) {
        while(_offset < _endIndex && isIgnored(_str.charAt(_offset))) {
            ++_offset;
        }

        return _offset;
    }

    /**
     * Check the Byte need to be ignored or not.
     * Char("10") New Line.
     * Char("13") Enter in keyboard.
     * Char("9") Tab
     * Char("32") Space.
     *
     * @param _char
     * @return
     */
    private static boolean isIgnored(char _char) {
        return _char == '\n' || _char == '\r' || _char == '\t' || _char == ' ';
    }

    /**
     * Get the next byte index which needs to be ignored.
     *
     * @param _byteArray
     * @param _offset
     * @param _endIndex
     * @return
     */
    private static int getNextSkipIgnored(byte[] _byteArray, int _offset, int _endIndex) {
        while(_offset < _endIndex && isIgnored(_byteArray[_offset])) {
            _offset++;
        }

        return _offset;
    }

    /**
     * Check the Byte need to be ignored or not.
     * Char("10") New Line.
     * Char("13") Enter in keyboard.
     * Char("9") Tab
     * Char("32") Space.
     *
     * @param _byte
     * @return
     */
    private static boolean isIgnored(byte _byte) {
        return _byte == 10 || _byte == 13 || _byte == 9 || _byte == 32;
    }

    static {
        for(int i = 0; i < HEX_ENCODING_TABLE.length; i++) {
            HEX_DECODING_TABLE[HEX_ENCODING_TABLE[i]] = (byte)i;
        }
        //Hex: 0-10,A,B,C,D,E,F
        //Char A = a
        HEX_DECODING_TABLE[65] = HEX_DECODING_TABLE[97];
        //Char B = b
        HEX_DECODING_TABLE[66] = HEX_DECODING_TABLE[98];
        //Char C = c
        HEX_DECODING_TABLE[67] = HEX_DECODING_TABLE[99];
        //Char D = d
        HEX_DECODING_TABLE[68] = HEX_DECODING_TABLE[100];
        //Char E = e
        HEX_DECODING_TABLE[69] = HEX_DECODING_TABLE[101];
        //Char F = f
        HEX_DECODING_TABLE[70] = HEX_DECODING_TABLE[102];
    }
}

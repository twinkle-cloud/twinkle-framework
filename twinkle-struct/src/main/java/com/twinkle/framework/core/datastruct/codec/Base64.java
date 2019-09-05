package com.twinkle.framework.core.datastruct.codec;

import java.util.Base64.Encoder;
import java.util.Base64.Decoder;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-01 11:32<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class Base64 {
    private static final Encoder encoder = java.util.Base64.getEncoder();
    private static final Decoder decoder = java.util.Base64.getMimeDecoder();

    public Base64() {
    }

    /**
     * Encode the base64 array to String.
     *
     * @param _byteArray
     * @return
     */
    public static String encode(byte[] _byteArray) {
        return encoder.encodeToString(_byteArray);
    }

    /**
     * Encode the base64 array to normal byte array.
     *
     * @param _byteArray
     * @return
     */
    public static byte[] encodeBytes(byte[] _byteArray) {
        return encoder.encode(_byteArray);
    }

    /**
     * Decode the String to base64 array.
     *
     * @param _str
     * @return
     */
    public static byte[] decode(String _str) {
        return decoder.decode(_str);
    }

    /**
     * Decode the normal byte array to base64 array.
     *
     * @param _byteArray
     * @return
     */
    public static byte[] decodeBytes(byte[] _byteArray) {
        return decoder.decode(_byteArray);
    }

}

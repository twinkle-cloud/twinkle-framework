package com.twinkle.framework.asm.codec;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-01 11:27<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface BinCodec {
    /**
     * Encode Byte Array to String.
     * @param _byteArray
     * @return
     */
    String encode(byte[] _byteArray);

    /**
     * Decode String to Byte Array.
     * @param _str
     * @return
     */
    byte[] decode(String _str);
}

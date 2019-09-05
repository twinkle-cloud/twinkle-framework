package com.twinkle.framework.core.lang;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/27/19 11:46 AM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface IBinaryAttribute extends Attribute {
    /**
     * Get the value as byte array.
     *
     * @return
     */
    byte[] getByteArray();

    /**
     * Set value.
     *
     * @param _value
     */
    void setValue(byte[] _value);
}

package com.twinkle.framework.core.datastruct.serialize;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-11 11:50<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface BinarySerializer<T> extends Serializer<T> {
    byte[] write(T var1);

    T read(byte[] var1);
}

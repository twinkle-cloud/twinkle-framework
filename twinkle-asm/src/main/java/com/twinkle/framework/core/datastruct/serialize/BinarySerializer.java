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
    /**
     * Write the given entity to byte array.
     *
     * @param _entity
     * @return
     */
    byte[] write(T _entity);

    /**
     * Read the given byte array as T entity.
     * @param _byteArray
     * @return
     */
    T read(byte[] _byteArray);
}

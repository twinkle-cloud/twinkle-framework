package com.twinkle.framework.asm.serialize;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-11 11:49<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface SerializerFactory {
    <T> Serializer<T> getSerializer(String _factoryName);

    <T> Serializer<T> getSerializer(Class<T> _class);
}

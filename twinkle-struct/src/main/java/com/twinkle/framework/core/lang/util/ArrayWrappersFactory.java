package com.twinkle.framework.core.lang.util;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/30/19 7:46 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface ArrayWrappersFactory {
    ArrayWrapperFactory<?> getArrayWrapperFactory(Class<?> var1);

    ArrayWrapperFactory<?> getArrayWrapperFactory(String var1);

    ArrayWrapperFactory<boolean[]> getBooleanArrayWrapperFactory();

    ArrayWrapperFactory<char[]> getCharArrayWrapperFactory();

    ArrayWrapperFactory<byte[]> getByteArrayWrapperFactory();

    ArrayWrapperFactory<short[]> getShortArrayWrapperFactory();

    ArrayWrapperFactory<int[]> getIntegerArrayWrapperFactory();

    ArrayWrapperFactory<long[]> getLongArrayWrapperFactory();

    ArrayWrapperFactory<float[]> getFloatArrayWrapperFactory();

    ArrayWrapperFactory<double[]> getDoubleArrayWrapperFactory();

    ArrayWrapperFactory<String[]> getStringArrayWrapperFactory();

    <T> ArrayWrapperFactory<T[]> getObjectArrayWrapperFactory(Class<T[]> var1);

}

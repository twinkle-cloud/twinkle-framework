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
    /**
     * Get array wrapper factory with given class.
     *
     * @param _class
     * @return
     */
    ArrayWrapperFactory<?> getArrayWrapperFactory(Class<?> _class);

    /**
     * Get array wrapper factory with given class's name.
     *
     * @param _className
     * @return
     */
    ArrayWrapperFactory<?> getArrayWrapperFactory(String _className);

    /**
     * Get boolean array wrapper factory.
     *
     * @return
     */
    ArrayWrapperFactory<boolean[]> getBooleanArrayWrapperFactory();

    /**
     * Get char array wrapper factory.
     *
     * @return
     */
    ArrayWrapperFactory<char[]> getCharArrayWrapperFactory();

    /**
     * Get byte array wrapper factory.
     *
     * @return
     */
    ArrayWrapperFactory<byte[]> getByteArrayWrapperFactory();

    /**
     * Get short array wrapper factory.
     *
     * @return
     */
    ArrayWrapperFactory<short[]> getShortArrayWrapperFactory();

    /**
     * Get int array wrapper factory.
     *
     * @return
     */
    ArrayWrapperFactory<int[]> getIntegerArrayWrapperFactory();

    /**
     * Get long array wrapper factory.
     *
     * @return
     */
    ArrayWrapperFactory<long[]> getLongArrayWrapperFactory();

    /**
     * Get float array wrapper factory.
     *
     * @return
     */
    ArrayWrapperFactory<float[]> getFloatArrayWrapperFactory();

    /**
     * Get double array wrapper factory.
     *
     * @return
     */
    ArrayWrapperFactory<double[]> getDoubleArrayWrapperFactory();

    /**
     * Get String array wrapper factory.
     *
     * @return
     */
    ArrayWrapperFactory<String[]> getStringArrayWrapperFactory();

    /**
     * Get Object array wrapper factory.
     *
     * @param var1
     * @param <T>
     * @return
     */
    <T> ArrayWrapperFactory<T[]> getObjectArrayWrapperFactory(Class<T[]> var1);

}

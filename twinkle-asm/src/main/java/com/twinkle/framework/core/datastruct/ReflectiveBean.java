package com.twinkle.framework.core.datastruct;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-03 16:00<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface ReflectiveBean extends SimpleReflectiveBean{
    /**
     * Get given attribute's Boolean Array Value
     *
     * @param _name
     * @return
     */
    boolean[] getBooleanArrayAttribute(String _name);

    /**
     * Set the attribute' value with given boolean array value.
     *
     * @param _name
     * @param _value
     */
    void setBooleanArrayAttribute(String _name, boolean[] _value);

    /**
     * Get given attribute's Byte Array Value
     *
     * @param _name
     * @return
     */
    byte[] getByteArrayAttribute(String _name);

    /**
     * Set the attribute' value with given byte array value.
     *
     * @param _name
     * @param _value
     */
    void setByteArrayAttribute(String _name, byte[] _value);

    /**
     * Get given attribute's Short Array Value
     *
     * @param _name
     * @return
     */
    short[] getShortArrayAttribute(String _name);

    /**
     * Set the attribute' value with given short array value.
     *
     * @param _name
     * @param _value
     */
    void setShortArrayAttribute(String _name, short[] _value);

    /**
     * Get given attribute's Int Array Value
     *
     * @param _name
     * @return
     */
    int[] getIntArrayAttribute(String _name);

    /**
     * Set the attribute' value with given int array value.
     *
     * @param _name
     * @param _value
     */
    void setIntArrayAttribute(String _name, int[] _value);

    /**
     * Get given attribute's Long Array Value
     *
     * @param _name
     * @return
     */
    long[] getLongArrayAttribute(String _name);

    /**
     * Set the attribute' value with given long array value.
     *
     * @param _name
     * @param _value
     */
    void setLongArrayAttribute(String _name, long[] _value);

    /**
     * Get given attribute's char Array Value
     *
     * @param _name
     * @return
     */
    char[] getCharArrayAttribute(String _name);

    /**
     * Set the attribute' value with given char array value.
     *
     * @param _name
     * @param _value
     */
    void setCharArrayAttribute(String _name, char[] _value);

    /**
     * Get given attribute's Float Array Value
     *
     * @param _name
     * @return
     */
    float[] getFloatArrayAttribute(String _name);

    /**
     * Set the attribute' value with given float array value.
     *
     * @param _name
     * @param _value
     */
    void setFloatArrayAttribute(String _name, float[] _value);

    /**
     * Get given attribute's Double Array Value
     *
     * @param _name
     * @return
     */
    double[] getDoubleArrayAttribute(String _name);

    /**
     * Set the attribute' value with given double array value.
     *
     * @param _name
     * @param _value
     */
    void setDoubleArrayAttribute(String _name, double[] _value);

    /**
     * Get given attribute's String Array Value
     *
     * @param _name
     * @return
     */
    String[] getStringArrayAttribute(String _name);

    /**
     * Set the attribute' value with given String array value.
     *
     * @param _name
     * @param _value
     */
    void setStringArrayAttribute(String _name, String[] _value);

    /**
     * Get given attribute's Object Array Value
     *
     * @param _name
     * @return
     */
    Object[] getObjectArrayAttribute(String _name);

    /**
     * Set the attribute' value with given Object array value.
     *
     * @param _name
     * @param _value
     */
    void setObjectArrayAttribute(String _name, Object[] _value);
}

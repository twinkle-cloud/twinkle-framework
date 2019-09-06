package com.twinkle.framework.asm;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-03 16:01<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface SimpleReflectiveBean extends RecyclableBean {
    /**
     * Get attribute's value as boolean type.
     *
     * @param _name
     * @return
     */
    boolean getBooleanAttribute(String _name);

    /**
     * Set the attribute' value with given boolean value.
     *
     * @param _name
     * @param _value
     */
    void setBooleanAttribute(String _name, boolean _value);

    /**
     * Get attribute's value as byte type.
     *
     * @param _name
     * @return
     */
    byte getByteAttribute(String _name);

    /**
     * Set the attribute' value with given byte value.
     *
     * @param _name
     * @param _value
     */
    void setByteAttribute(String _name, byte _value);

    /**
     * Get attribute's value as short type.
     *
     * @param _name
     * @return
     */
    short getShortAttribute(String _name);

    /**
     * Set the attribute' value with given short value.
     *
     * @param _name
     * @param _value
     */
    void setShortAttribute(String _name, short _value);

    /**
     * Get attribute's value as int type.
     *
     * @param _name
     * @return
     */
    int getIntAttribute(String _name);

    /**
     * Set the attribute' value with given int value.
     *
     * @param _name
     * @param _value
     */
    void setIntAttribute(String _name, int _value);

    /**
     * Get attribute's value as long type.
     *
     * @param _name
     * @return
     */
    long getLongAttribute(String _name);

    /**
     * Set the attribute' value with given long value.
     *
     * @param _name
     * @param _value
     */
    void setLongAttribute(String _name, long _value);

    /**
     * Get attribute's value as char type.
     *
     * @param _name
     * @return
     */
    char getCharAttribute(String _name);

    /**
     * Set the attribute' value with given char value.
     *
     * @param _name
     * @param _value
     */
    void setCharAttribute(String _name, char _value);

    /**
     * Get attribute's value as float type.
     *
     * @param _name
     * @return
     */
    float getFloatAttribute(String _name);

    /**
     * Set the attribute' value with given float value.
     *
     * @param _name
     * @param _value
     */
    void setFloatAttribute(String _name, float _value);

    /**
     * Get attribute's value as double type.
     *
     * @param _name
     * @return
     */
    double getDoubleAttribute(String _name);

    /**
     * Set the attribute' value with given double value.
     *
     * @param _name
     * @param _value
     */
    void setDoubleAttribute(String _name, double _value);

    /**
     * Get attribute's value as String type.
     *
     * @param _name
     * @return
     */
    String getStringAttribute(String _name);

    /**
     * Set the attribute' value with given String value.
     *
     * @param _name
     * @param _value
     */
    void setStringAttribute(String _name, String _value);

    /**
     * Get attribute's value as Object type.
     *
     * @param _name
     * @return
     */
    Object getObjectAttribute(String _name);

    /**
     * Set the attribute' value with given Object value.
     *
     * @param _name
     * @param _value
     */
    void setObjectAttribute(String _name, Object _value);

    /**
     * Judge the give name is attribute set or not.
     *
     * @param _name
     * @return
     */
    boolean isAttributeSet(String _name);

    /**
     * Clear some given attribute.
     *
     * @param _name
     */
    void clear(String _name);
}

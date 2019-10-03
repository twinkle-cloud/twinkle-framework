package com.twinkle.framework.struct.ref;

import com.twinkle.framework.struct.error.AttributeNotFoundException;
import com.twinkle.framework.struct.error.AttributeNotSetException;
import com.twinkle.framework.struct.error.AttributeTypeMismatchException;
import com.twinkle.framework.struct.error.StructAttributeCopyException;
import com.twinkle.framework.struct.type.StructAttribute;
import com.twinkle.framework.core.lang.util.Array;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/30/19 10:24 AM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface StructAttributeRef extends AttributeRef {
    /**
     * Clear the struct attribute.
     *
     * @param _attr
     * @throws AttributeNotFoundException
     * @throws AttributeTypeMismatchException
     * @throws AttributeNotSetException
     */
    void clear(StructAttribute _attr) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException;

    /**
     * Check the attribute can be accessed by setxxx method or not?
     *
     * @param _attr
     * @return
     * @throws AttributeNotFoundException
     * @throws AttributeTypeMismatchException
     * @throws AttributeNotSetException
     */
    boolean isAttributeSet(StructAttribute _attr) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException;

    /**
     * Get byte value from the given attribute.
     *
     * @param _attr
     * @return
     * @throws AttributeNotFoundException
     * @throws AttributeNotSetException
     * @throws AttributeTypeMismatchException
     */
    byte getByte(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException;

    /**
     * Update the value for the given attribute.
     *
     * @param _attr
     * @param _value
     * @throws AttributeNotFoundException
     * @throws AttributeTypeMismatchException
     * @throws AttributeNotSetException
     */
    void setByte(StructAttribute _attr, byte _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException;

    /**
     * Get short value from the given attribute.
     *
     * @param _attr
     * @return
     * @throws AttributeNotFoundException
     * @throws AttributeNotSetException
     * @throws AttributeTypeMismatchException
     */
    short getShort(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException;

    /**
     * Update the value for the given attribute.
     *
     * @param _attr
     * @param _value
     * @throws AttributeNotFoundException
     * @throws AttributeTypeMismatchException
     * @throws AttributeNotSetException
     */
    void setShort(StructAttribute _attr, short _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException;

    /**
     * Get short value from the given attribute.
     *
     * @param _attr
     * @return
     * @throws AttributeNotFoundException
     * @throws AttributeNotSetException
     * @throws AttributeTypeMismatchException
     */
    int getInt(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException;

    /**
     * Update the value for the given attribute.
     *
     * @param _attr
     * @param _value
     * @throws AttributeNotFoundException
     * @throws AttributeTypeMismatchException
     * @throws AttributeNotSetException
     */
    void setInt(StructAttribute _attr, int _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException;

    /**
     * Get short value from the given attribute.
     *
     * @param _attr
     * @return
     * @throws AttributeNotFoundException
     * @throws AttributeNotSetException
     * @throws AttributeTypeMismatchException
     */
    long getLong(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException;

    /**
     * Update the value for the given attribute.
     *
     * @param _attr
     * @param _value
     * @throws AttributeNotFoundException
     * @throws AttributeTypeMismatchException
     * @throws AttributeNotSetException
     */
    void setLong(StructAttribute _attr, long _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException;

    /**
     * Get short value from the given attribute.
     *
     * @param _attr
     * @return
     * @throws AttributeNotFoundException
     * @throws AttributeNotSetException
     * @throws AttributeTypeMismatchException
     */
    char getChar(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException;

    /**
     * Update the value for the given attribute.
     *
     * @param _attr
     * @param _value
     * @throws AttributeNotFoundException
     * @throws AttributeTypeMismatchException
     * @throws AttributeNotSetException
     */
    void setChar(StructAttribute _attr, char _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException;

    /**
     * Get short value from the given attribute.
     *
     * @param _attr
     * @return
     * @throws AttributeNotFoundException
     * @throws AttributeNotSetException
     * @throws AttributeTypeMismatchException
     */
    boolean getBoolean(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException;

    /**
     * Update the value for the given attribute.
     *
     * @param _attr
     * @param _value
     * @throws AttributeNotFoundException
     * @throws AttributeTypeMismatchException
     * @throws AttributeNotSetException
     */
    void setBoolean(StructAttribute _attr, boolean _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException;

    /**
     * Get short value from the given attribute.
     *
     * @param _attr
     * @return
     * @throws AttributeNotFoundException
     * @throws AttributeNotSetException
     * @throws AttributeTypeMismatchException
     */
    float getFloat(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException;

    /**
     * Update the value for the given attribute.
     *
     * @param _attr
     * @param _value
     * @throws AttributeNotFoundException
     * @throws AttributeTypeMismatchException
     * @throws AttributeNotSetException
     */
    void setFloat(StructAttribute _attr, float _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException;

    /**
     * Get short value from the given attribute.
     *
     * @param _attr
     * @return
     * @throws AttributeNotFoundException
     * @throws AttributeNotSetException
     * @throws AttributeTypeMismatchException
     */
    double getDouble(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException;

    /**
     * Update the value for the given attribute.
     *
     * @param _attr
     * @param _value
     * @throws AttributeNotFoundException
     * @throws AttributeTypeMismatchException
     * @throws AttributeNotSetException
     */
    void setDouble(StructAttribute _attr, double _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException;

    /**
     * Get short value from the given attribute.
     *
     * @param _attr
     * @return
     * @throws AttributeNotFoundException
     * @throws AttributeNotSetException
     * @throws AttributeTypeMismatchException
     */
    String getString(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException;

    /**
     * Update the value for the given attribute.
     *
     * @param _attr
     * @param _value
     * @throws AttributeNotFoundException
     * @throws AttributeTypeMismatchException
     * @throws AttributeNotSetException
     */
    void setString(StructAttribute _attr, String _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException;

    /**
     * Get short value from the given attribute.
     *
     * @param _attr
     * @return
     * @throws AttributeNotFoundException
     * @throws AttributeNotSetException
     * @throws AttributeTypeMismatchException
     */
    StructAttribute getStruct(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException;

    /**
     * Update the value for the given attribute.
     *
     * @param _attr
     * @param _value
     * @throws AttributeNotFoundException
     * @throws AttributeTypeMismatchException
     * @throws AttributeNotSetException
     */
    void setStruct(StructAttribute _attr, StructAttribute _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException;

    /**
     * Get Array value from the given attribute.
     *
     * @param _attr
     * @return
     * @throws AttributeNotFoundException
     * @throws AttributeNotSetException
     * @throws AttributeTypeMismatchException
     */
    Array getArray(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException;

    /**
     * Update the value for the given attribute.
     *
     * @param _attr
     * @param _value
     * @throws AttributeNotFoundException
     * @throws AttributeTypeMismatchException
     * @throws AttributeNotSetException
     * @throws ClassCastException
     */
    void setArray(StructAttribute _attr, Array _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException, ClassCastException;

    /**
     * Get short value from the given attribute.
     *
     * @param _attr
     * @return
     * @throws AttributeNotFoundException
     * @throws AttributeNotSetException
     * @throws AttributeTypeMismatchException
     */
    int getArraySize(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException;

    /**
     * Copy the src attribute to the dest attribute.
     *
     * @param _srcAttr
     * @param _destAttr
     * @throws StructAttributeCopyException
     * @throws AttributeNotFoundException
     * @throws AttributeNotSetException
     * @throws AttributeTypeMismatchException
     */
    void copy(StructAttribute _srcAttr, StructAttribute _destAttr) throws StructAttributeCopyException, AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException;
}

package com.twinkle.framework.struct.type;

import com.twinkle.framework.struct.error.*;
import com.twinkle.framework.struct.ref.AttributeRef;
import com.twinkle.framework.core.lang.util.Array;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/30/19 10:27 AM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface StructAttribute extends Cloneable {
    /**
     * Get this struct attribute's type.
     *
     * @return
     */
    StructAttributeType getType();

    /**
     * The attribute with name exists or not?
     *
     * @param _attrName
     * @return
     */
    boolean hasAttribute(String _attrName);

    /**
     * Get Attribute ref with ref's name.
     *
     * @param _attrName
     * @return
     * @throws AttributeNotFoundException
     * @throws AttributeTypeMismatchException
     * @throws BadAttributeNameException
     */
    AttributeRef getAttributeRef(String _attrName) throws AttributeNotFoundException, AttributeTypeMismatchException, BadAttributeNameException;

    /**
     * Judge the attribute is set ro not?
     *
     * @param _attrRef
     * @return
     * @throws AttributeNotFoundException
     * @throws AttributeTypeMismatchException
     * @throws AttributeNotSetException
     */
    boolean isAttributeSet(AttributeRef _attrRef) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException;

    /**
     * Duplicate this attribute.
     *
     * @return
     * @throws StructAttributeCopyException
     */
    StructAttribute duplicate() throws StructAttributeCopyException;

    /**
     * Copy the given attribute to the current attribute.
     *
     * @param _attribute
     * @throws StructAttributeCopyException
     */
    void copy(StructAttribute _attribute) throws StructAttributeCopyException;

    /**
     * Clear the given sub-attribute.
     *
     * @param _attrRef
     * @throws AttributeNotFoundException
     * @throws AttributeTypeMismatchException
     * @throws AttributeNotSetException
     */
    void clear(AttributeRef _attrRef) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException;

    /**
     * Clear the current attribute.
     */
    void clear();

    /**
     * Get the referred attribute value.
     *
     * @param _attrRef
     * @return
     * @throws AttributeNotFoundException
     * @throws AttributeNotSetException
     * @throws AttributeTypeMismatchException
     */
    byte getByte(AttributeRef _attrRef) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException;

    /**
     * Set the referred attribute value with given value.
     *
     * @param _attrRef
     * @param _value
     * @throws AttributeNotFoundException
     * @throws AttributeTypeMismatchException
     * @throws AttributeNotSetException
     */
    void setByte(AttributeRef _attrRef, byte _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException;

    /**
     * Get the referred attribute value.
     *
     * @param _attrRef
     * @return
     * @throws AttributeNotFoundException
     * @throws AttributeNotSetException
     * @throws AttributeTypeMismatchException
     */
    short getShort(AttributeRef _attrRef) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException;

    /**
     * Set the referred attribute value with given value.
     *
     * @param _attrRef
     * @param _value
     * @throws AttributeNotFoundException
     * @throws AttributeTypeMismatchException
     * @throws AttributeNotSetException
     */
    void setShort(AttributeRef _attrRef, short _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException;

    /**
     * Get the referred attribute value.
     *
     * @param _attrRef
     * @return
     * @throws AttributeNotFoundException
     * @throws AttributeNotSetException
     * @throws AttributeTypeMismatchException
     */
    int getInt(AttributeRef _attrRef) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException;

    /**
     * Set the referred attribute value with given value.
     *
     * @param _attrRef
     * @param _value
     * @throws AttributeNotFoundException
     * @throws AttributeTypeMismatchException
     * @throws AttributeNotSetException
     */
    void setInt(AttributeRef _attrRef, int _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException;

    /**
     * Get the referred attribute value.
     *
     * @param _attrRef
     * @return
     * @throws AttributeNotFoundException
     * @throws AttributeNotSetException
     * @throws AttributeTypeMismatchException
     */
    long getLong(AttributeRef _attrRef) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException;

    /**
     * Set the referred attribute value with given value.
     *
     * @param _attrRef
     * @param _value
     * @throws AttributeNotFoundException
     * @throws AttributeTypeMismatchException
     * @throws AttributeNotSetException
     */
    void setLong(AttributeRef _attrRef, long _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException;

    /**
     * Get the referred attribute value.
     *
     * @param _attrRef
     * @return
     * @throws AttributeNotFoundException
     * @throws AttributeNotSetException
     * @throws AttributeTypeMismatchException
     */
    char getChar(AttributeRef _attrRef) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException;

    /**
     * Set the referred attribute value with given value.
     *
     * @param _attrRef
     * @param _value
     * @throws AttributeNotFoundException
     * @throws AttributeTypeMismatchException
     * @throws AttributeNotSetException
     */
    void setChar(AttributeRef _attrRef, char _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException;

    /**
     * Get the referred attribute value.
     *
     * @param _attrRef
     * @return
     * @throws AttributeNotFoundException
     * @throws AttributeNotSetException
     * @throws AttributeTypeMismatchException
     */
    boolean getBoolean(AttributeRef _attrRef) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException;

    /**
     * Set the referred attribute value with given value.
     *
     * @param _attrRef
     * @param _value
     * @throws AttributeNotFoundException
     * @throws AttributeTypeMismatchException
     * @throws AttributeNotSetException
     */
    void setBoolean(AttributeRef _attrRef, boolean _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException;

    /**
     * Get the referred attribute value.
     *
     * @param _attrRef
     * @return
     * @throws AttributeNotFoundException
     * @throws AttributeNotSetException
     * @throws AttributeTypeMismatchException
     */
    float getFloat(AttributeRef _attrRef) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException;

    /**
     * Set the referred attribute value with given value.
     *
     * @param _attrRef
     * @param _value
     * @throws AttributeNotFoundException
     * @throws AttributeTypeMismatchException
     * @throws AttributeNotSetException
     */
    void setFloat(AttributeRef _attrRef, float _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException;

    /**
     * Get the referred attribute value.
     *
     * @param _attrRef
     * @return
     * @throws AttributeNotFoundException
     * @throws AttributeNotSetException
     * @throws AttributeTypeMismatchException
     */
    double getDouble(AttributeRef _attrRef) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException;

    /**
     * Set the referred attribute value with given value.
     *
     * @param _attrRef
     * @param _value
     * @throws AttributeNotFoundException
     * @throws AttributeTypeMismatchException
     * @throws AttributeNotSetException
     */
    void setDouble(AttributeRef _attrRef, double _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException;

    /**
     * Get the referred attribute value.
     *
     * @param _attrRef
     * @return
     * @throws AttributeNotFoundException
     * @throws AttributeNotSetException
     * @throws AttributeTypeMismatchException
     */
    String getString(AttributeRef _attrRef) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException;

    /**
     * Set the referred attribute value with given value.
     *
     * @param _attrRef
     * @param _value
     * @throws AttributeNotFoundException
     * @throws AttributeTypeMismatchException
     * @throws AttributeNotSetException
     */
    void setString(AttributeRef _attrRef, String _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException;

    /**
     * Get the referred attribute value.
     *
     * @param _attrRef
     * @return
     * @throws AttributeNotFoundException
     * @throws AttributeNotSetException
     * @throws AttributeTypeMismatchException
     */
    StructAttribute getStruct(AttributeRef _attrRef) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException;

    /**
     * Set the referred attribute value with given value.
     *
     * @param _attrRef
     * @param _value
     * @throws AttributeNotFoundException
     * @throws AttributeTypeMismatchException
     * @throws AttributeNotSetException
     */
    void setStruct(AttributeRef _attrRef, StructAttribute _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException;

    /**
     * Get the array size of the referred attribute if the referred attribute is an array one.
     *
     * @param _attrRef
     * @return
     * @throws AttributeNotFoundException
     * @throws AttributeNotSetException
     * @throws AttributeTypeMismatchException
     */
    int getArraySize(AttributeRef _attrRef) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException;

    /**
     * Get the referred attribute as an array if the referred attribute is an array one.
     *
     * @param _attrRef
     * @return
     * @throws AttributeNotFoundException
     * @throws AttributeNotSetException
     * @throws AttributeTypeMismatchException
     */
    Array getArray(AttributeRef _attrRef) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException;

    /**
     * Set the referred attribute value with given value.
     *
     * @param _attrRef
     * @param _value
     * @throws AttributeNotFoundException
     * @throws AttributeTypeMismatchException
     * @throws AttributeNotSetException
     */
    void setArray(AttributeRef _attrRef, Array _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException, ClassCastException;
}

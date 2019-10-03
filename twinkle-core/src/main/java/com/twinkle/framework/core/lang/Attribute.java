package com.twinkle.framework.core.lang;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-10 17:43<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface Attribute extends Cloneable, Serializable {
    String EXT_INFO_NC_INDEX = "NCIndex";

    /**
     * Integer Type.
     */
    int INTEGER_TYPE = 1;
    /**
     * String Type.
     */
    int STRING_TYPE = 2;
    /**
     * Long Type.
     */
    int LONG_TYPE = 3;
    /**
     * Float Type.
     */
    int FLOAT_TYPE = 4;
    /**
     * Double Type.
     */
    int DOUBLE_TYPE = 5;
    /**
     * Byte Array Type.
     */
    int BYTE_ARRAY_TYPE = 6;
    /**
     * Unicode String Type.
     */
    int UNICODE_STRING_TYPE = 7;
    /**
     * Object Type.
     */
    int OBJECT_TYPE = 8;

    int LIST_ATTRIBUTE_TYPE = 110;

    /**
     * Get the attribute's primitive type.
     *
     * @return
     */
    @ApiModelProperty(hidden = true)
    int getPrimitiveType();

    /**
     * Get the attribute's type.
     *
     * @return
     */
    @ApiModelProperty(hidden = true)
    int getType();

    /**
     * Update the attribute's type.
     *
     * @param _type
     */
    void setType(int _type);

    /**
     * Update the value to be empty.
     */
    void setEmptyValue();

    /**
     * Update the value with given String.
     * The String should be able converted to the destination value.
     *
     * @param _value
     */
    void setValue(String _value);

    /**
     * Update the value with given Object.
     *
     * @param _value
     */
    void setValue(Object _value);

    /**
     * Copy the given attribute value to current instance.
     *
     * @param _attr
     */
    void setValue(Attribute _attr);

    /**
     * Do the operation.
     *
     * @param _operation
     * @param _attr
     */
    void aggregate(Operation _operation, Attribute _attr);

    /**
     * Clone the current instance.
     * @return
     */
    Object clone();

    /**
     * Override the toString() method.
     *
     * @return
     */
    @Override
    String toString();

    /**
     * Compare with the given object.
     *
     * @param _obj
     * @return
     */
    int compareTo(Object _obj);

    /**
     * Get the value as Object.
     *
     * @return
     */
    @ApiModelProperty(hidden = true)
    Object getObjectValue();
}

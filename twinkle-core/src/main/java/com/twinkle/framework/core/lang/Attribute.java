package com.twinkle.framework.core.lang;

import com.alibaba.fastjson.JSONObject;

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

    int OPERATION_ADD = 1;
    int OPERATION_SUBTRACT = 2;
    int OPERATION_MIN = 3;
    int OPERATION_MAX = 4;
    int OPERATION_SET = 5;
    String OPERATION_NAME_ADD = "add";
    String OPERATION_NAME_SUBTRACT = "subtract";
    String OPERATION_NAME_MIN = "min";
    String OPERATION_NAME_MAX = "max";
    String OPERATION_NAME_SET = "set";

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
    /**
     * Json Object Type.
     */
    int JSON_TYPE = 9;

    int LIST_ATTRIBUTE_TYPE = 110;

    int getPrimitiveType();

    int getType();

    void setType(int _type);

    void setEmptyValue();

    void setValue(String _value);

    void setValue(Attribute _attr);

    void aggregate(int _operation, Attribute _attr);

    int getOperationID(String _operationName);

    Object clone();

    String toString();

    int compareTo(Object _obj);

    Object getObjectValue();

    JSONObject getJsonObjectValue();
}

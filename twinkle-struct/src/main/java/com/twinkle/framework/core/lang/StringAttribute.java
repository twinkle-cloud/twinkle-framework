package com.twinkle.framework.core.lang;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-10 18:10<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class StringAttribute implements Attribute, Comparable {
    public static final String EMPTY_VALUE = "";
    private static int type = 102;
    private String value = null;

    public StringAttribute() {
        this.value = EMPTY_VALUE;
    }

    public StringAttribute(String _attr) {
        this.setValue(_attr);
    }

    @Override
    public final int getPrimitiveType() {
        return STRING_TYPE;
    }

    @Override
    public final int getType() {
        return type;
    }

    public static int getTypeID() {
        return type;
    }

    public final String getValue() {
        return this.value;
    }

    @Override
    public final void setType(int _type) {
        type = _type;
    }

    @Override
    public final void setEmptyValue() {
        this.value = EMPTY_VALUE;
    }

    @Override
    public final void setValue(String _value) {
        this.value = _value;
        if (StringUtils.isEmpty(_value)) {
            this.value = EMPTY_VALUE;
        }
    }

    @Override
    public void setValue(Object _value) {
        if(_value != null) {
            this.value = _value.toString();
        } else {
            this.value = EMPTY_VALUE;
        }
    }

    @Override
    public void setValue(Attribute _attr) {
        if (this != _attr) {
            this.setValue(_attr.toString());
        }
    }

    @Override
    public int hashCode() {
        return this.value == null ? 0 : this.value.hashCode();
    }

    @Override
    public boolean equals(Object _obj) {
        if (this == _obj) {
            return true;
        } else if (!(_obj instanceof StringAttribute)) {
            return false;
        } else {
            StringAttribute tempAttr = (StringAttribute)_obj;
            return this.value != null ? this.value.equals(tempAttr.getValue()) : false;
        }
    }

    @Override
    public void aggregate(int _operation, Attribute _attr) {
        switch(_operation) {
            case OPERATION_ADD:
                if (_attr != null) {
                    this.value = this.value + _attr.toString();
                }
                break;
            case OPERATION_SET:
                if (_attr == null) {
                    this.value = EMPTY_VALUE;
                } else {
                    this.value = _attr.toString();
                }
        }
        if (this.value == null) {
            this.value = EMPTY_VALUE;
        }
    }

    @Override
    public int getOperationID(String _operationName) {
        if (_operationName.equals(OPERATION_NAME_SET)) {
            return OPERATION_SET;
        } else {
            return _operationName.equals(OPERATION_NAME_ADD) ? 1 : -1;
        }
    }

    @Override
    public String toString() {
        return this.value;
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new Error("Assertion failure: " + ex);
        }
    }

    @Override
    public int compareTo(Object _obj) {
        String tempAttr = ((StringAttribute) _obj).value;
        return this.value.compareTo(tempAttr);
    }
    @Override
    public Object getObjectValue() {
        return this.value;
    }

    @Override
    public JSONObject getJsonObjectValue() {
        return JSON.parseObject(this.value);
    }
}

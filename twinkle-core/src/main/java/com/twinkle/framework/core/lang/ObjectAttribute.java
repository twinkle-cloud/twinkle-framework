package com.twinkle.framework.core.lang;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-11 17:37<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class ObjectAttribute implements Attribute {
    private static int type;
    private Object value;

    public ObjectAttribute() {
        this.value = null;
    }

    public ObjectAttribute(Object _value) {
        this.value = _value;
    }

    @Override
    public int getPrimitiveType() {
        return OBJECT_TYPE;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public void setType(int _type) {
        type = _type;
    }

    @Override
    public void setEmptyValue() {
        this.value = null;
    }

    @Override
    public void setValue(String _value) {
        this.value = _value;
    }

    @Override
    public void setValue(Attribute _attr) {
        if (this != _attr) {
            this.value = ((ObjectAttribute)_attr).getObject();
        }
    }

    public Object getObject() {
        return this.value;
    }

    public void setObject(Object _obj) {
        this.value = _obj;
    }

    @Override
    public void aggregate(int _operation, Attribute _attr) {
        switch(_operation) {
            case OPERATION_SET:
                this.setValue(_attr);
            default:
        }
    }

    @Override
    public int getOperationID(String _operationName) {
        return _operationName != null && _operationName.equals(OPERATION_NAME_SET) ? OPERATION_SET : -1;
    }

    @Override
    public Object clone() {
        try {
            Object var1 = super.clone();
            ((ObjectAttribute)var1).setObject(this.value);
            return var1;
        } catch (CloneNotSupportedException var2) {
            throw new Error("Assertion failure: " + var2);
        }
    }
    @Override
    public boolean equals(Object _obj) {
        if (_obj != null && _obj instanceof ObjectAttribute) {
            Object tempObj = ((ObjectAttribute)_obj).getObject();
            if (tempObj == null && this.value == null) {
                return true;
            } else {
                return tempObj != null && this.value != null ? this.value.equals(tempObj) : false;
            }
        } else {
            return false;
        }
    }
    @Override
    public int compareTo(Object _obj) {
        if (!this.equals(_obj)) {
            Object tempObj = ((ObjectAttribute)_obj).getObject();
            if (this.value == null && tempObj != null) {
                return -1;
            } else {
                return this.value != null && tempObj == null ? 1 : ((Comparable)this.value).compareTo(tempObj);
            }
        } else {
            return 0;
        }
    }

    @Override
    public Object getObjectValue() {
        return this.value;
    }

    @Override
    public JSONObject getJsonObjectValue() {
        return JSON.parseObject(this.value.toString());
    }
}

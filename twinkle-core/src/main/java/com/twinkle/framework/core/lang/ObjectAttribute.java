package com.twinkle.framework.core.lang;

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
    public int getTypeIndex() {
        return type;
    }

    @Override
    public void setTypeIndex(int _index) {
        type = _index;
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
    public void setValue(Object _value) {
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
    public void aggregate(Operation _operation, Attribute _attr) {
        switch(_operation) {
            case SET:
                this.setValue(_attr);
            default:
        }
    }

    @Override
    public Object clone() {
        try {
            Object tempDestObj = super.clone();
            ((ObjectAttribute)tempDestObj).setObject(this.value);
            return tempDestObj;
        } catch (CloneNotSupportedException e) {
            throw new Error("Assertion failure: " + e);
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
}

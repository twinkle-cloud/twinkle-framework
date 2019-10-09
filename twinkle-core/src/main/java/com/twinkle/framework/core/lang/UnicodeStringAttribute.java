package com.twinkle.framework.core.lang;

import java.io.Serializable;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/9/19 1:53 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class UnicodeStringAttribute implements Attribute, Cloneable, Serializable {
    public static final String EMPTY_VALUE = "";
    private static int type = 107;
    private String value = null;

    public UnicodeStringAttribute() {
        this.value = "";
    }

    public UnicodeStringAttribute(String _value) {
        this.value = _value;
    }

    @Override
    public final int getPrimitiveType() {
        return Attribute.UNICODE_STRING_TYPE;
    }

    @Override
    public int getTypeIndex() {
        return type;
    }

    @Override
    public void setTypeIndex(int _index) {
        type = _index;
    }

    public static int getTypeID() {
        return type;
    }

    public final String getValue() {
        return this.value;
    }

    @Override
    public final void setEmptyValue() {
        this.value = EMPTY_VALUE;
    }

    @Override
    public final void setValue(String _value) {
        this.value = _value;
    }

    @Override
    public void setValue(Object _value) {
        if (this != _value) {
            this.value = _value.toString();
        }
    }

    @Override
    public void setValue(Attribute _attr) {
        if (this != _attr) {
            this.value = _attr.toString();
        }
    }

    @Override
    public int hashCode() {
        return this.value == null ? 0 : this.value.hashCode();
    }

    @Override
    public boolean equals(Object _obj) {
        return _obj != null && this.value != null ? ((UnicodeStringAttribute) _obj).getValue().equals(this.value) : false;
    }

    @Override
    public void aggregate(Operation _operation, Attribute _attr) {
        switch (_operation) {
            case SET:
                this.setValue(_attr);
            default:
        }
    }

    @Override
    public String toString() {
        return this.value;
    }

    @Override
    public Object clone() {
        return new UnicodeStringAttribute(this.getValue());
    }

    @Override
    public int compareTo(Object _obj) {
        String tempStr = ((UnicodeStringAttribute) _obj).value;
        return this.value.compareTo(tempStr);
    }

    @Override
    public Object getObjectValue() {
        return this.value;
    }
}

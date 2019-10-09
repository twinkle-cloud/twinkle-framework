package com.twinkle.framework.core.lang;

import com.twinkle.framework.core.utils.IPv6AddrHelper;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/9/19 6:29 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class IPv6AddrAttribute extends BinaryAttribute implements Comparable {
    private static int type = 112;

    public IPv6AddrAttribute() {
        super(IPv6AddrHelper.newByteArray());
    }

    public IPv6AddrAttribute(byte[] _value) {
        super(_value);
    }

    public IPv6AddrAttribute(String _value) {
        this.setValue(_value);
    }

    @Override
    public final int getTypeIndex() {
        return type;
    }

    @Override
    public final void setTypeIndex(int _index) {
        type = _index;
    }

    public static int getTypeID() {
        return type;
    }

    @Override
    public void setValue(String _value) {
        this.setValue(IPv6AddrHelper.toByteArray(_value));
    }

    @Override
    public void setValue(Attribute _attr) {
        if (this != _attr) {
            if (_attr.getPrimitiveType() == Attribute.STRING_TYPE) {
                this.setValue(_attr.toString());
            } else {
                this.setValue(((BinaryAttribute)_attr).getValue());
            }
        }
    }

    @Override
    public void setValue(byte[] _value) {
        _value = IPv6AddrHelper.newByteArray(_value);
        super.setValue(_value);
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
    public String toString() {
        return IPv6AddrHelper.toString(this.getValue());
    }

    @Override
    public int compareTo(Object _obj) {
        byte[] temp1Value = this.getValue();
        byte[] temp2Value = ((IPv6AddrAttribute)_obj).getValue();

        assert temp1Value.length == temp2Value.length;

        for(int i = 0; i < temp1Value.length; ++i) {
            short temp1Item = (short)(temp1Value[i] & 255);
            short temp2Item = (short)(temp2Value[i] & 255);
            if (temp1Item < temp2Item) {
                return -1;
            }
            if (temp1Item > temp2Item) {
                return 1;
            }
        }
        return 0;
    }

    @Override
    public Object getObjectValue() {
        return super.getObjectValue();
    }

    @Override
    public String toLogString() {
        return this.toString();
    }
}

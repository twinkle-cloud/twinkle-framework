package com.twinkle.framework.core.lang;

import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/27/19 11:48 AM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Data
public class BinaryAttribute implements IBinaryAttribute, ILogAttribute, Cloneable, Serializable {
    private static final long serialVersionUID = -2568298075291373246L;
    private static int type = 109;
    private byte[] value;

    public BinaryAttribute(){
        this.value = null;
    }

    public BinaryAttribute(byte[] _value){
        this.value = _value;
    }

    @Override
    public byte[] getByteArray() {
        return this.value;
    }

    @Override
    public void setValue(byte[] _value) {
        byte[] tempValue = null;
        if (_value != null) {
            tempValue = new byte[_value.length];
            System.arraycopy(_value, 0, tempValue, 0, _value.length);
        }
        this.value = tempValue;
    }

    @Override
    public int getPrimitiveType() {
        return Attribute.BYTE_ARRAY_TYPE;
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
        if (_value == null) {
            this.value = null;
        } else {
            byte[] tempValue = new byte[_value.length() / 2];
            int tempLength = _value.length();
            int tempByteIndex = 0;

            for(int i = 0; tempByteIndex + 2 <= tempLength; ++i) {
                String tempStr = _value.substring(tempByteIndex, tempByteIndex + 2);
                tempValue[i] = (byte)Integer.parseInt(tempStr, 16);
                tempByteIndex += 2;
            }
            this.value = tempValue;
        }
    }

    @Override
    public void setValue(Object _value) {
        if(_value == null) {
            this.value = null;
            return;
        }
        if(_value instanceof byte[]) {
            this.value = (byte[])_value;
        } else {
            this.setValue(_value.toString());
        }
    }

    @Override
    public void setValue(Attribute _attr) {
        if (this != _attr) {
            byte[] tempValue = null;
            if (_attr != null) {
                switch(_attr.getPrimitiveType()) {
                    case INTEGER_TYPE:
                        tempValue = (new BigInteger("" + ((IIntegerAttribute)_attr).getInt())).toByteArray();
                        break;
                    case LONG_TYPE:
                        tempValue = (new BigInteger("" + ((ILongAttribute)_attr).getLong())).toByteArray();
                        break;
                    case BYTE_ARRAY_TYPE:
                        tempValue = ((IBinaryAttribute)_attr).getByteArray();
                        break;
                    default:
                        String tempStr = _attr.toString();
                        if (tempStr.matches("\\p{XDigit}+")) {
                            this.setValue(tempStr);
                            return;
                        }
                        tempValue = tempStr.getBytes();
                }
            }

            this.setValue(tempValue);
        }
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
    public int compareTo(Object _obj) {
        if (!this.equals(_obj)) {
            byte[] tempByteArray = ((BinaryAttribute)_obj).getByteArray();
            if (this.value == null && tempByteArray != null) {
                return -1;
            }
            if (this.value != null && tempByteArray == null) {
                return 1;
            }
            if (this.value.length > tempByteArray.length) {
                return 1;
            }
            if (this.value.length < tempByteArray.length) {
                return -1;
            }
            for(int i = 0; i < tempByteArray.length; ++i) {
                if (this.value[i] > tempByteArray[i]) {
                    return 1;
                }
                if (this.value[i] < tempByteArray[i]) {
                    return -1;
                }
            }
        }
        return 0;
    }

    @Override
    public int hashCode() {
        if (this.value == null) {
            return 0;
        } else {
            int tempCode = 0;

            for(int i = 0; i < this.value.length; ++i) {
                tempCode = 31 * tempCode + this.value[i];
            }

            return tempCode;
        }
    }

    @Override
    public boolean equals(Object _obj){
        if (_obj != null && _obj instanceof BinaryAttribute) {
            byte[] tempByteArray = ((BinaryAttribute)_obj).getByteArray();
            if (tempByteArray == null && this.value == null) {
                return true;
            } else if (tempByteArray != null && this.value != null) {
                if (tempByteArray.length != this.value.length) {
                    return false;
                } else {
                    for(int i = 0; i < tempByteArray.length; ++i) {
                        if (tempByteArray[i] != this.value[i]) {
                            return false;
                        }
                    }
                    return true;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public Object getObjectValue() {
        return this.value;
    }

    @Override
    public String toLogString() {
        return this.toString(true);
    }

    @Override
    public String toString() {
        return this.toString(false);
    }

    public String toString(boolean _nullable) {
        if (this.value == null) {
            return _nullable ? "<empty>" : null;
        } else {
            StringBuffer tempBuffer = new StringBuffer("");
            for(int i = 0; i < this.value.length; ++i) {
                byte tempByte = this.value[i];
                tempBuffer.append(Character.forDigit(tempByte >> 4 & 15, 16));
                tempBuffer.append(Character.forDigit(tempByte & 15, 16));
            }

            return tempBuffer.toString();
        }
    }

    @Override
    public Object clone() {
        try {
            Object tempObj = super.clone();
            ((BinaryAttribute)tempObj).setValue(this.value);
            return tempObj;
        } catch (CloneNotSupportedException e) {
            throw new Error("Assertion failure: " + e);
        }
    }
}

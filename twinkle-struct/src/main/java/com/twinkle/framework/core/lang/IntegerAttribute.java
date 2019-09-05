package com.twinkle.framework.core.lang;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-10 17:56<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class IntegerAttribute extends AbstractNumericAttribute implements IIntegerAttribute {
    private static int type = 101;
    protected int value;

    public IntegerAttribute() {
        this.value = 0;
    }

    public IntegerAttribute(int _value) {
        this.value = _value;
    }

    @Override
    public int getPrimitiveType() {
        return INTEGER_TYPE;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public void setType(int _type) {
        type = _type;
    }

    public static int getTypeID() {
        return type;
    }

    @Override
    public final int getInt() {
        return this.value;
    }

    @Override
    public final long getLong() {
        return (long)this.value;
    }

    @Override
    public final float getFloat() {
        return (float)this.value;
    }

    @Override
    public final double getDouble() {
        return (double)this.value;
    }

    public final int getValue() {
        return this.getInt();
    }

    @Override
    public final void setEmptyValue() {
        this.value = 0;
    }

    @Override
    public void setValue(int _value) {
        this.value = _value;
    }

    @Override
    public void setValue(Object _value) {
        if(_value == null) {
            this.setEmptyValue();
            return;
        }
        if(_value instanceof Attribute) {
            this.setValue((Attribute) _value);
            return;
        }
        this.setValue(_value.toString());
    }

    @Override
    public void setValue(String _value) {
        boolean operationFlag = false;
        int tempIndex = 0;
        if (_value.length() == 0) {
            throw new NumberFormatException(_value);
        } else {
            if (_value.charAt(tempIndex) == '-') {
                operationFlag = true;
                ++tempIndex;
            }
            // set to decimal by default.
            byte tempHex = 10;
            if (_value.charAt(tempIndex) == '#') {
                // set to hexadecimal if find #
                tempHex = 16;
                ++tempIndex;
            } else if (_value.charAt(tempIndex) == '0' && _value.length() > tempIndex + 1 && Character.toUpperCase(_value.charAt(tempIndex + 1)) == 'X') {
                // set to hexadecimal if find 0x/0X
                tempHex = 16;
                tempIndex += 2;
            }

            if (tempIndex >= _value.length()) {
                throw new NumberFormatException(_value);
            } else if (_value.startsWith("-", tempIndex)) {
                throw new NumberFormatException("Negative sign must be at the beginning of value: " + _value);
            } else {
                try {
                    this.value = Integer.parseInt(_value.substring(tempIndex), tempHex);
                    if (operationFlag) {
                        this.value =- this.value;
                    }
                } catch (NumberFormatException ex) {
                    if (!operationFlag) {
                        throw ex;
                    }
                    String tempFinalValue = new String("-" + _value.substring(tempIndex));
                    this.value = Integer.parseInt(tempFinalValue, tempHex);
                }
            }
        }
    }

    @Override
    public void setValue(Attribute _attr) {
        if (this != _attr) {
            if (_attr.getPrimitiveType() == STRING_TYPE) {
                this.setValue(_attr.toString());
            } else {
                this.value = ((IIntegerAttribute) _attr).getInt();
            }
        }
    }

    @Override
    public int hashCode() {
        return this.value;
    }

    @Override
    public boolean equals(Object _obj) {
        if (_obj != null && _obj instanceof IIntegerAttribute) {
            return this.value == ((IIntegerAttribute)_obj).getInt();
        } else {
            return false;
        }
    }

    @Override
    public final void add(Attribute _attr) {
        this.value += ((IntegerAttribute)_attr).getInt();
    }

    @Override
    public final void subtract(Attribute _attr) {
        this.value -= ((IntegerAttribute)_attr).getInt();
    }

    @Override
    public final void min(Attribute _attr) {
        int tempAttr = ((IntegerAttribute)_attr).getInt();
        if (tempAttr < this.value) {
            this.value = tempAttr;
        }
    }
    @Override
    public final void max(Attribute _attr) {
        int tempAttr = ((IntegerAttribute)_attr).getInt();
        if (tempAttr > this.value) {
            this.value = tempAttr;
        }
    }

    @Override
    public final boolean add(INumericAttribute _var1, INumericAttribute _var2) {
        this.value = _var1.getInt() + _var2.getInt();
        return true;
    }

    @Override
    public final boolean subtract(INumericAttribute _var1, INumericAttribute _var2) {
        this.value = _var1.getInt() - _var2.getInt();
        return true;
    }

    @Override
    public final boolean multiply(INumericAttribute _var1, INumericAttribute _var2) {
        this.value = _var1.getInt() * _var2.getInt();
        return true;
    }

    @Override
    public final boolean divide(INumericAttribute _var1, INumericAttribute _var2) {
        this.value = _var1.getInt() / _var2.getInt();
        return true;
    }

    @Override
    public final boolean and(IScalarAttribute _var1, IScalarAttribute _var2) {
        this.value = _var1.getInt() & _var2.getInt();
        return true;
    }

    @Override
    public final boolean or(IScalarAttribute _var1, IScalarAttribute _var2) {
        this.value = _var1.getInt() | _var2.getInt();
        return true;
    }

    @Override
    public final boolean xor(IScalarAttribute _var1, IScalarAttribute _var2) {
        this.value = _var1.getInt() ^ _var2.getInt();
        return true;
    }

    @Override
    public final boolean mod(INumericAttribute _var1, INumericAttribute _var2) {
        this.value = _var1.getInt() % _var2.getInt();
        return true;
    }

    @Override
    public final boolean shiftl(IScalarAttribute _var1, IScalarAttribute _var2) {
        this.value = _var1.getInt() << _var2.getInt();
        return true;
    }

    @Override
    public final boolean shiftr(IScalarAttribute _var1, IScalarAttribute var2) {
        this.value = _var1.getInt() >> var2.getInt();
        return true;
    }

    @Override
    public String toString() {
        return Integer.toString(this.value);
    }

    @Override
    public int compareTo(Object _obj) {
        int tempValue = ((IntegerAttribute) _obj).value;
        if (this.value < tempValue) {
            return -1;
        } else {
            return this.value == tempValue ? 0 : 1;
        }
    }

    public static void main(String[] var0) {
        IntegerAttribute var1 = new IntegerAttribute();
        var1.setValue("090");
        if (var1.getValue() == 90) {
            System.out.println("Great! 090 is intrepreted properly");
        } else {
            System.err.println("Error! 090 is not intrepreted properly");
        }

        var1.setValue("0x19");
        if (var1.getValue() == 25) {
            System.out.println("Great! 0x19 is intrepreted properly");
        } else {
            System.err.println("Error! 0x19 is not intrepreted properly");
        }

        var1.setValue("123456789");
        if (var1.getValue() == 123456789) {
            System.out.println("Great! 123456789 is intrepreted properly");
        } else {
            System.err.println("Error! 123456789 is not intrepreted properly");
        }

        var1.setValue("-987654321");
        if (var1.getValue() == -987654321) {
            System.out.println("Great! -987654321 is intrepreted properly");
        } else {
            System.err.println("Error! -987654321 is not intrepreted properly");
        }

        var1.setValue((new Integer(-2147483648)).toString());
        if (var1.getValue() == -2147483648) {
            System.out.println("Great! Integer.MIN_VALUE is intrepreted properly");
        } else {
            System.err.println("Error! Integer.MIN_VALUE is not intrepreted properly");
        }

        var1.setValue((new Integer(2147483647)).toString());
        if (var1.getValue() == 2147483647) {
            System.out.println("Great! Integer.MAX_VALUE is intrepreted properly");
        } else {
            System.err.println("Error! Integer.MAX_VALUE is not intrepreted properly");
        }

        StringAttribute var2 = new StringAttribute();
        var2.setValue(new String("100"));

        try {
            var1.setValue((Attribute)var2);
            if (var1.getValue() == 100) {
                System.out.println("Good! string attribute setting worked");
            }
        } catch (Exception var5) {
            System.err.println("Error! while setting a string attribute with int value to IntegerAttribute");
        }

        var2.setValue(new String("1a0"));

        try {
            var1.setValue((Attribute)var2);
            System.err.println("Error! an exception expected when setting a string of non int value in IntegerAttribute, but it succeeded");
        } catch (Exception var4) {
            System.out.println("Good! exception " + var4 + " occured  while setting a string attribute with non int value to IntegerAttribute as expected");
        }

    }
    @Override
    public Object getObjectValue() {
        return this.value;
    }

    @Override
    public JSONObject getJsonObjectValue() {
        return JSON.parseObject("" + this.value);
    }
}

package com.twinkle.framework.core.lang;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-11 14:34<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class FloatAttribute extends AbstractNumericAttribute implements IFloatAttribute {
    private static int type = 105;
    private float value;

    public FloatAttribute() {
        this.value = 0.0F;
    }

    public FloatAttribute(float _value) {
        this.value = _value;
    }

    @Override
    public int getPrimitiveType() {
        return FLOAT_TYPE;
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
        this.value = 0.0F;
    }

    @Override
    public void setValue(float _value) {
        this.value = _value;
    }

    @Override
    public void setValue(String _value) {
        this.value = Float.parseFloat(_value);
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
    public void setValue(Attribute _attr) {
        if (this != _attr) {
            int tempPrimitiveType = _attr.getPrimitiveType();
            switch(tempPrimitiveType) {
                case INTEGER_TYPE:
                    this.value = (float)((IIntegerAttribute)_attr).getInt();
                    return;
                case STRING_TYPE:
                    this.setValue(_attr.toString());
                    return;
                case LONG_TYPE:
                    this.value = (float)((ILongAttribute)_attr).getLong();
                    return;
                default:
                    this.value = ((IFloatAttribute)_attr).getFloat();
            }
        }
    }

    @Override
    public int getInt() {
        return (int)this.value;
    }

    @Override
    public long getLong() {
        return (long)this.value;
    }

    @Override
    public float getFloat() {
        return this.value;
    }

    @Override
    public double getDouble() {
        return (double)this.value;
    }

    @Override
    public void add(Attribute _attr) {
        this.value += ((INumericAttribute)_attr).getFloat();
    }

    @Override
    public void subtract(Attribute _attr) {
        this.value -= ((INumericAttribute)_attr).getFloat();
    }

    @Override
    public void min(Attribute _attr) {
        float tempValue = ((INumericAttribute)_attr).getFloat();
        if (tempValue < this.value) {
            this.value = tempValue;
        }
    }

    @Override
    public void max(Attribute _attr) {
        float tempValue = ((INumericAttribute)_attr).getFloat();
        if (tempValue > this.value) {
            this.value = tempValue;
        }
    }

    @Override
    public boolean add(INumericAttribute _attr1, INumericAttribute _attr2) {
        this.value = _attr1.getFloat() + _attr2.getFloat();
        return true;
    }

    @Override
    public boolean subtract(INumericAttribute _attr1, INumericAttribute _attr2) {
        this.value = _attr1.getFloat() - _attr2.getFloat();
        return true;
    }

    @Override
    public boolean multiply(INumericAttribute _attr1, INumericAttribute _attr2) {
        this.value = _attr1.getFloat() * _attr2.getFloat();
        return true;
    }

    @Override
    public boolean divide(INumericAttribute _attr1, INumericAttribute _attr2) {
        this.value = _attr1.getFloat() / _attr2.getFloat();
        return true;
    }

    @Override
    public boolean mod(INumericAttribute _attr1, INumericAttribute _attr2) {
        this.value = _attr1.getFloat() % _attr2.getFloat();
        return true;
    }

    @Override
    public int compareTo(Object _obj) {
        float tempValue = ((FloatAttribute)_obj).value;
        if (this.value < tempValue) {
            return -1;
        } else {
            return this.value == tempValue ? 0 : 1;
        }
    }
    @Override
    public String toString(){
        return Float.toString(this.value);
    }

    @Override
    public Object getObjectValue() {
        return this.value;
    }

    public static void main(String[] var0) {
        FloatAttribute var1 = new FloatAttribute();
        StringAttribute var2 = new StringAttribute();
        var2.setValue(new String("1000.9080009"));

        try {
            var1.setValue((Attribute)var2);
            if ((double)var1.getFloat() == 1000.9080009D) {
                System.out.println("Good! string attribute setting worked");
            } else {
                System.out.println("string attribute setting worked but value of float is " + var1.getFloat() + " instead of expected 1000.9080009");
            }
        } catch (Exception var9) {
            System.err.println("Error! while setting a string attribute with long value to FloatAttribute");
        }

        var2.setValue(new String("1a0"));

        try {
            var1.setValue((Attribute)var2);
            System.err.println("Error! an exception expected when setting a string of non long value in FloatAttribute, but it succeeded");
        } catch (Exception var8) {
            System.out.println("Good! exception " + var8 + " occured  while setting a string attribute with non long value to FloatAttribute as expected");
        }

        IntegerAttribute var3 = new IntegerAttribute();
        ((IIntegerAttribute)var3).setValue(1000);

        try {
            var1.setValue((Attribute)var3);
            if (var1.getFloat() == 1000.0F) {
                System.out.println("Good! int attribute setting worked");
            }
        } catch (Exception var7) {
            System.err.println("Error! while setting a IntAttribute to FloatAttribute");
        }

        LongAttribute var4 = new LongAttribute();
        ((ILongAttribute)var4).setValue(1000000000L);

        try {
            var1.setValue((Attribute)var4);
            if (var1.getFloat() == 1.0E9F) {
                System.out.println("Good! long attribute setting worked");
            }
        } catch (Exception var6) {
            System.err.println("Error! while setting a LongAttribute to FloatAttribute");
        }

    }
}

package com.twinkle.framework.core.lang;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-11 15:40<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class DoubleAttribute extends AbstractNumericAttribute implements IFloatAttribute, IDoubleAttribute {
    private static int type = 106;
    private double value;

    public DoubleAttribute() {
        this.value = 0.0D;
    }

    public DoubleAttribute(double _value) {
        this.value = _value;
    }

    @Override
    public int getPrimitiveType() {
        return DOUBLE_TYPE;
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
        this.value = 0.0D;
    }

    @Override
    public void setValue(double _value) {
        this.value = _value;
    }

    @Override
    public void setValue(float _value) {
        this.value = (double)_value;
    }

    @Override
    public void setValue(String _value) {
        this.value = Double.parseDouble(_value);
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
                    this.value = (double)((IIntegerAttribute)_attr).getInt();
                    return;
                case STRING_TYPE:
                    this.setValue(_attr.toString());
                    return;
                case LONG_TYPE:
                    this.value = (double)((ILongAttribute)_attr).getLong();
                    return;
                case FLOAT_TYPE:
                    this.value = (double)((IFloatAttribute)_attr).getFloat();
                    return;
                default:
                    this.value = ((IDoubleAttribute)_attr).getDouble();
            }
        }
    }

    @Override
    public void add(Attribute _attr) {
        this.value += ((INumericAttribute)_attr).getDouble();
    }

    @Override
    public void subtract(Attribute _attr) {
        this.value -= ((INumericAttribute)_attr).getDouble();
    }

    @Override
    public void min(Attribute _attr) {
        double tempValue = ((INumericAttribute)_attr).getDouble();
        if (tempValue < this.value) {
            this.value = tempValue;
        }
    }

    @Override
    public void max(Attribute _attr) {
        double tempValue = ((INumericAttribute)_attr).getDouble();
        if (tempValue > this.value) {
            this.value = tempValue;
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
        return (float)this.value;
    }

    @Override
    public double getDouble() {
        return this.value;
    }

    @Override
    public boolean add(INumericAttribute _attr1, INumericAttribute _attr2) {
        this.value = _attr1.getDouble() + _attr2.getDouble();
        return true;
    }

    @Override
    public boolean subtract(INumericAttribute _attr1, INumericAttribute _attr2) {
        this.value = _attr1.getDouble() - _attr2.getDouble();
        return true;
    }

    @Override
    public boolean multiply(INumericAttribute _attr1, INumericAttribute _attr2) {
        this.value = _attr1.getDouble() * _attr2.getDouble();
        return true;
    }

    @Override
    public boolean divide(INumericAttribute _attr1, INumericAttribute _attr2) {
        this.value = _attr1.getDouble() / _attr2.getDouble();
        return true;
    }

    @Override
    public boolean mod(INumericAttribute _attr1, INumericAttribute _attr2) {
        this.value = _attr1.getDouble() % _attr2.getDouble();
        return true;
    }

    @Override
    public boolean equals(Object _obj) {
        if (_obj != null && _obj instanceof IDoubleAttribute) {
            return this.value == ((IDoubleAttribute)_obj).getDouble();
        } else {
            return false;
        }
    }

    @Override
    public String toString(){
        return Double.toString(this.value);
    }

    @Override
    public int compareTo(Object _obj) {
        double tempValue = ((DoubleAttribute)_obj).value;
        if (this.value < tempValue) {
            return -1;
        } else {
            return this.value == tempValue ? 0 : 1;
        }
    }

    @Override
    public Object getObjectValue() {
        return this.value;
    }
}

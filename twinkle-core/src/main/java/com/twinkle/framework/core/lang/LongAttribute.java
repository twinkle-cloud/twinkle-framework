package com.twinkle.framework.core.lang;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-10 18:50<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class LongAttribute extends AbstractNumericAttribute implements ILongAttribute, IIntegerAttribute {
    private static int type = 104;
    private long value;

    public LongAttribute() {
        this.value = 0L;
    }

    public LongAttribute(long _value) {
        this.value = _value;
    }

    @Override
    public final int getPrimitiveType() {
        return LONG_TYPE;
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
    public final int getInt() {
        return (int)this.value;
    }

    @Override
    public final long getLong() {
        return this.value;
    }

    public final long getValue() {
        return this.value;
    }

    @Override
    public final float getFloat() {
        return (float)this.value;
    }

    @Override
    public final double getDouble() {
        return (double)this.value;
    }

    @Override
    public final void setEmptyValue() {
        this.value = 0L;
    }

    @Override
    public final void setValue(int _value) {
        this.value = (long)_value;
    }

    @Override
    public final void setValue(long _value) {
        this.value = _value;
    }

    @Override
    public final void setValue(String _value) {
        this.value = Long.parseLong(_value);
    }

    @Override
    public void setValue(Object _value) {
        if(_value == null) {
            this.setEmptyValue();
            return;
        }
        if(_value instanceof Attribute) {
            this.setValue((Attribute)_value);
            return;
        }
        this.setValue(_value.toString());
    }

    @Override
    public final void setValue(Attribute _attr) {
        if (this != _attr) {
            int var2 = _attr.getPrimitiveType();
            if (var2 == STRING_TYPE) {
                this.setValue(_attr.toString());
            } else if (var2 == INTEGER_TYPE) {
                this.value = (long)((IIntegerAttribute)_attr).getInt();
            } else {
                this.value = ((ILongAttribute)_attr).getLong();
            }
        }
    }

    @Override
    public int hashCode() {
        return (int)this.value;
    }

    @Override
    public boolean equals(Object _obj) {
        if (_obj != null && _obj instanceof ILongAttribute) {
            return this.value == ((ILongAttribute)_obj).getLong();
        } else {
            return false;
        }
    }

    @Override
    public final void add(Attribute _attr) {
        this.value += ((INumericAttribute)_attr).getLong();
    }

    @Override
    public final void subtract(Attribute _attr) {
        this.value -= ((INumericAttribute)_attr).getLong();
    }

    @Override
    public final void min(Attribute _attr) {
        long var2 = ((INumericAttribute)_attr).getLong();
        if (var2 < this.value) {
            this.value = var2;
        }
    }

    @Override
    public final void max(Attribute _attr) {
        long var2 = ((INumericAttribute)_attr).getLong();
        if (var2 > this.value) {
            this.value = var2;
        }
    }

    @Override
    public final boolean add(INumericAttribute _attr1, INumericAttribute _attr2) {
        this.value = _attr1.getLong() + _attr2.getLong();
        return true;
    }

    @Override
    public final boolean subtract(INumericAttribute _attr1, INumericAttribute _attr2) {
        this.value = _attr1.getLong() - _attr2.getLong();
        return true;
    }

    @Override
    public final boolean multiply(INumericAttribute _attr1, INumericAttribute _attr2) {
        this.value = _attr1.getLong() * _attr2.getLong();
        return true;
    }

    @Override
    public final boolean divide(INumericAttribute _attr1, INumericAttribute _attr2) {
        this.value = _attr1.getLong() / _attr2.getLong();
        return true;
    }

    @Override
    public final boolean and(IScalarAttribute _attr1, IScalarAttribute _attr2) {
        this.value = _attr1.getLong() & _attr2.getLong();
        return true;
    }

    @Override
    public final boolean or(IScalarAttribute _attr1, IScalarAttribute _attr2) {
        this.value = _attr1.getLong() | _attr2.getLong();
        return true;
    }

    @Override
    public final boolean xor(IScalarAttribute _attr1, IScalarAttribute _attr2) {
        this.value = _attr1.getLong() ^ _attr2.getLong();
        return true;
    }

    @Override
    public final boolean mod(INumericAttribute _attr1, INumericAttribute _attr2) {
        this.value = _attr1.getLong() % _attr2.getLong();
        return true;
    }

    @Override
    public final boolean shiftl(IScalarAttribute _attr1, IScalarAttribute _attr2) {
        this.value = _attr1.getLong() << (int) _attr2.getLong();
        return true;
    }

    @Override
    public final boolean shiftr(IScalarAttribute _attr1, IScalarAttribute _attr2) {
        this.value = _attr1.getLong() >> (int) _attr2.getLong();
        return true;
    }

    @Override
    public String toString() {
        return Long.toString(this.value);
    }

    @Override
    public int compareTo(Object var1) {
        long var2 = ((LongAttribute)var1).value;
        if (this.value < var2) {
            return -1;
        } else {
            return this.value == var2 ? 0 : 1;
        }
    }

    public static void main(String[] var0) {
        LongAttribute var1 = new LongAttribute();
        StringAttribute var2 = new StringAttribute();
        var2.setValue(new String("1000000000"));

        try {
            var1.setValue((Attribute)var2);
            if (var1.getLong() == 1000000000L) {
                System.out.println("Good! string attribute setting worked");
            }
        } catch (Exception var9) {
            System.err.println("Error! while setting a string attribute with long value to LongAttribute");
        }

        var2.setValue(new String("1a0"));

        try {
            var1.setValue((Attribute)var2);
            System.err.println("Error! an exception expected when setting a string of non long value in LongAttribute, but it succeeded");
        } catch (Exception var8) {
            System.out.println("Good! exception " + var8 + " occured  while setting a string attribute with non long value to LongAttribute as expected");
        }

        IntegerAttribute var3 = new IntegerAttribute();
        ((IIntegerAttribute)var3).setValue(1000);

        try {
            var1.setValue((Attribute)var3);
            if (var1.getLong() == 1000L) {
                System.out.println("Good! int attribute setting worked");
            }
        } catch (Exception var7) {
            System.err.println("Error! while setting a IntAttribute to LongAttribute");
        }

        FloatAttribute var4 = new FloatAttribute();
        ((IFloatAttribute)var4).setValue(1.2343F);

        try {
            var1.setValue((Attribute)var4);
            System.out.println("Error! float attribute setting worked (shdnot have)");
        } catch (Exception var6) {
            System.err.println("Good! exception " + var6 + " occured while setting a FloatAttribute to LongAttribute as expected");
        }

    }
    @Override
    public Object getObjectValue() {
        return this.value;
    }
}

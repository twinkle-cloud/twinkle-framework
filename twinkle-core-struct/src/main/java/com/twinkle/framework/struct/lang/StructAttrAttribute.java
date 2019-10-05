package com.twinkle.framework.struct.lang;

import com.twinkle.framework.core.lang.Attribute;
import com.twinkle.framework.core.lang.Operation;
import com.twinkle.framework.struct.type.StructAttribute;
import lombok.Data;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/4/19 2:07 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Data
public class StructAttrAttribute implements Attribute {
    private int typeIndex;
    private StructAttribute structAttribute;

    @Override
    public int getPrimitiveType() {
        return Attribute.STRUCT_TYPE;
    }

    @Override
    public void setEmptyValue() {
        this.structAttribute.clear();
    }

    @Override
    public void setValue(String _value) {

    }

    @Override
    public void setValue(Object _value) {
        if(_value instanceof StructAttrAttribute) {
            this.structAttribute = ((StructAttrAttribute)_value).structAttribute;
        } else if (_value instanceof StructAttribute) {
            this.structAttribute = (StructAttribute)_value;
        }
    }

    @Override
    public void setValue(Attribute _attr) {
        if(_attr instanceof StructAttrAttribute) {
            this.structAttribute = ((StructAttrAttribute)_attr).structAttribute;
        }
    }

    @Override
    public void aggregate(Operation _operation, Attribute _attr) {

    }

    @Override
    public Object clone() {
        return this.structAttribute.duplicate();
    }

    @Override
    public int compareTo(Object _obj) {
        return 0;
    }

    @Override
    public Object getObjectValue() {
        return this.structAttribute;
    }
}

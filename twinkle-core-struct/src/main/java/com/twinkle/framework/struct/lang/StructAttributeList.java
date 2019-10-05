package com.twinkle.framework.struct.lang;

import com.twinkle.framework.core.lang.ListAttribute;
import com.twinkle.framework.core.lang.ObjectAttribute;
import com.twinkle.framework.struct.type.StructAttribute;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/4/19 10:51 AM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class StructAttributeList extends ListAttribute implements IStructAttributeList {
    @Override
    public int getPrimitiveType() {
        return LIST_STRUCT_ATTRIBUTE_TYPE;
    }

    @Override
    public void add(int _index, StructAttribute _attr) {
        ObjectAttribute tempAttr = new ObjectAttribute();
        tempAttr.setObject(_attr);
        this.add(_index, tempAttr);
    }

    @Override
    public void add(StructAttribute _attr) {
        StructAttrAttribute tempAttr = new StructAttrAttribute();
        tempAttr.setValue(_attr);
        this.add(tempAttr);
    }
    @Override
    public void setValue(Object _value) {
        if (_value.getClass().isArray()) {
            StructAttribute[] tempArray = (StructAttribute[])_value;
            for(StructAttribute tempItem : tempArray) {
                this.add(tempItem);
            }
            return;
        }
        super.setValue(_value);
    }

    @Override
    public void addAll(List<StructAttribute> _collection) {
        if(CollectionUtils.isEmpty(_collection)) {
            return;
        }
        for(StructAttribute tempItem : _collection) {
            this.add(tempItem);
        }
    }
}

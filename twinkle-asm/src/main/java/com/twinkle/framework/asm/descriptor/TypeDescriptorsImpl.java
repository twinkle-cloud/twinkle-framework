package com.twinkle.framework.asm.descriptor;

import java.util.*;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-09 15:27<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class TypeDescriptorsImpl implements TypeDescriptors {
    protected Map<String, TypeDescriptor> _beansByClass;

    public TypeDescriptorsImpl(List<TypeDescriptor> _typeDescriptorList) {
        this.init(_typeDescriptorList);
    }

    protected void init(List<TypeDescriptor> _typeDescriptorList) {
        this._beansByClass = new HashMap(8);
        for(TypeDescriptor tempItem : _typeDescriptorList) {
            this._beansByClass.put(tempItem.getClassName(), tempItem);
        }
    }
    @Override
    public List<TypeDescriptor> getTypes() {
        return new ArrayList<>(this._beansByClass.values());
    }

    @Override
    public TypeDescriptor getType(String var1) {
        return (TypeDescriptor)this._beansByClass.get(var1);
    }
}

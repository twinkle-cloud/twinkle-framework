package com.twinkle.framework.struct.ref;

import com.twinkle.framework.struct.asm.descriptor.SAAttributeDescriptor;
import com.twinkle.framework.struct.type.AttributeType;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/1/19 10:08 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public abstract class AbstractAttributeRef implements StructAttributeRef, Cloneable {
    protected SAAttributeDescriptor descriptor;

    protected AbstractAttributeRef(SAAttributeDescriptor _descriptor) {
        this.descriptor = _descriptor;
    }

    @Override
    public String getName() {
        return this.descriptor.getName();
    }

    @Override
    public AttributeType getType() {
        return this.descriptor.getType();
    }

    @Override
    public SAAttributeDescriptor getDescriptor() {
        return this.descriptor;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public int hashCode() {
        return this.descriptor.hashCode();
    }

    @Override
    public boolean equals(Object _obj) {
        if (this == _obj) {
            return true;
        } else if (_obj != null && this.getClass() == _obj.getClass()) {
            AbstractAttributeRef tempAttrRef = (AbstractAttributeRef) _obj;
            return this.descriptor.equals(tempAttrRef.descriptor);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return this.getName();
    }
}

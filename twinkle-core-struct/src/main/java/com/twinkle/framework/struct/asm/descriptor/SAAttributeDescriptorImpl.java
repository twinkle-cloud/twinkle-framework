package com.twinkle.framework.struct.asm.descriptor;

import com.twinkle.framework.struct.type.StructType;
import com.twinkle.framework.core.type.AttributeType;
import lombok.Getter;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/2/19 5:34 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Getter
public class SAAttributeDescriptorImpl implements SAAttributeDescriptor, Cloneable {
    private final String name;
    private final AttributeType type;
    private final boolean optional;

    public SAAttributeDescriptorImpl(String _name, AttributeType _type, boolean _optional) {
        this.name = _name;
        this.type = _type;
        this.optional = _optional;
    }

    public SAAttributeDescriptorImpl(SAAttributeDescriptor _srcDescriptor, String _name) {
        this.name = _name;
        this.type = _srcDescriptor.getType();
        this.optional = _srcDescriptor.isOptional();
    }
    @Override
    public String getTypeName() {
        AttributeType tempType = this.type;
        return tempType.isStructType() ? ((StructType)tempType).getQualifiedName() : tempType.getName();
    }
    @Override
    public boolean equals(Object _obj) {
        if (this == _obj) {
            return true;
        } else if (_obj != null && this.getClass() == _obj.getClass()) {
            SAAttributeDescriptor tempDescriptor = (SAAttributeDescriptorImpl)_obj;
            if (this.optional != tempDescriptor.isOptional()) {
                return false;
            } else if (!this.name.equals(tempDescriptor.getName())) {
                return false;
            } else {
                return this.type.equals(tempDescriptor.getType());
            }
        } else {
            return false;
        }
    }
    @Override
    public int hashCode() {
        int tempCode = this.name.hashCode();
        tempCode = 31 * tempCode + this.type.hashCode();
        tempCode = 31 * tempCode + (this.optional ? 1 : 0);
        return tempCode;
    }
    @Override
    public String toString() {
        StringBuilder tempBuilder = new StringBuilder(this.name);
        tempBuilder.append("{").append(this.type.getName());
        tempBuilder.append(", ").append(this.optional);
        tempBuilder.append('}');
        return tempBuilder.toString();
    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

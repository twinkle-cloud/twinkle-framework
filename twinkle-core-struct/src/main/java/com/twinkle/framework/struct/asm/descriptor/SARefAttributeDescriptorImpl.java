package com.twinkle.framework.struct.asm.descriptor;

import com.twinkle.framework.struct.type.StructType;
import com.twinkle.framework.core.type.AttributeType;
import lombok.Getter;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/30/19 8:52 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Getter
public class SARefAttributeDescriptorImpl implements SAAttributeDescriptor, Cloneable {
    /**
     * StructAttribute's attribute name.
     */
    private final String name;
    /**
     * StructAttribute's attribute type.
     */
    private final AttributeType type;
    /**
     * StructAttribute's attribute is required or not.
     */
    private final boolean optional;

    protected SARefAttributeDescriptorImpl(String _name, AttributeType _type, boolean _optional) {
        this.name = _name;
        this.type = _type;
        this.optional = _optional;
    }

    protected SARefAttributeDescriptorImpl(SAAttributeDescriptor _descriptor, String _name) {
        this.name = _name;
        this.type = _descriptor.getType();
        this.optional = _descriptor.isOptional();
    }

    @Override
    public String getTypeName() {
        AttributeType tempType = this.type;
        return tempType.isStructType() ? ((StructType) tempType).getQualifiedName() : tempType.getName();
    }

    @Override
    public boolean equals(Object _obj) {
        if (this == _obj) {
            return true;
        } else if (_obj != null && this.getClass() == _obj.getClass()) {
            SAAttributeDescriptor tempDescriptorImpl = (SARefAttributeDescriptorImpl) _obj;
            if (this.optional != tempDescriptorImpl.isOptional()) {
                return false;
            } else if (!this.name.equals(tempDescriptorImpl.getName())) {
                return false;
            } else {
                return this.type.equals(tempDescriptorImpl.getType());
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

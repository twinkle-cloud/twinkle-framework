package com.twinkle.framework.core.datastruct.descriptor;

import com.twinkle.framework.core.context.StructAttributeManager;
import com.twinkle.framework.core.context.StructAttributeSchema;
import com.twinkle.framework.core.error.BadAttributeNameException;
import com.twinkle.framework.core.error.NamespaceNotFoundException;
import com.twinkle.framework.core.error.StructAttributeTypeNotFoundException;
import com.twinkle.framework.core.error.TypeNotFoundException;
import com.twinkle.framework.core.lang.struct.ArrayType;
import com.twinkle.framework.core.lang.struct.StructAttributeType;
import com.twinkle.framework.core.lang.struct.StructType;
import com.twinkle.framework.core.lang.struct.StructTypeManager;
import com.twinkle.framework.core.utils.StructAttributeNameValidator;
import lombok.Getter;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/30/19 9:02 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Getter
public class DefaultSAAttributeDescriptorImpl implements SAAttributeDescriptor {
    protected String name;
    protected String typeName;
    protected boolean optional = false;
    protected StructType type;
    protected StructAttributeSchema structAttributeSchema;

    public DefaultSAAttributeDescriptorImpl(String _name, String _typeName, StructTypeManager _typeManager, boolean _optional) throws TypeNotFoundException, StructAttributeTypeNotFoundException, BadAttributeNameException, NamespaceNotFoundException {
        StructAttributeNameValidator.checkName(_name);
        this.name = _name;
        this.typeName = _typeName;
        this.optional = _optional;
        this.structAttributeSchema = StructAttributeManager.getStructAttributeSchema();

        String tempTypeName;
        try {
            int tempTypeIndex = _typeName.indexOf(":");
            if (tempTypeIndex != -1) {
                tempTypeName = _typeName.substring(0, tempTypeIndex);
                String tempAttrName = _typeName.substring(tempTypeIndex + 1);
                this.type = this.structAttributeSchema.getTypeManager(tempTypeName).getType(tempAttrName);
            } else {
                this.type = _typeManager.getType(_typeName);
            }
        } catch (TypeNotFoundException e) {
            if (_typeName.endsWith("[]")) {
                tempTypeName = _typeName.substring(0, _typeName.length() - 2);
                StructAttributeType tempType = this.structAttributeSchema.getStructAttributeType(tempTypeName);
                this.type = ArrayType.getStructAttributeTypeArray(_typeName, tempType);
            } else {
                this.type = this.structAttributeSchema.getStructAttributeType(_typeName);
            }
        }

    }
    @Override
    public boolean equals(Object _obj) {
        if (_obj != null && _obj instanceof DefaultSAAttributeDescriptorImpl) {
            if (this == _obj) {
                return true;
            } else {
                SAAttributeDescriptor tempDescriptor = (SAAttributeDescriptor) _obj;
                return this.name.equals(tempDescriptor.getName()) && this.typeName.equals(tempDescriptor.getTypeName()) && this.optional == tempDescriptor.isOptional();
            }
        } else {
            return false;
        }
    }
}

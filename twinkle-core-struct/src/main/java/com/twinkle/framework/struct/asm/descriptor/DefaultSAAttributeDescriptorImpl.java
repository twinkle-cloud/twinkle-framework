package com.twinkle.framework.struct.asm.descriptor;

import com.twinkle.framework.struct.context.StructAttributeSchema;
import com.twinkle.framework.struct.context.StructAttributeSchemaManager;
import com.twinkle.framework.struct.error.BadAttributeNameException;
import com.twinkle.framework.struct.error.NamespaceNotFoundException;
import com.twinkle.framework.struct.error.StructAttributeTypeNotFoundException;
import com.twinkle.framework.struct.error.TypeNotFoundException;
import com.twinkle.framework.struct.type.AttributeType;
import com.twinkle.framework.struct.type.AttributeTypeManager;
import com.twinkle.framework.struct.utils.StructAttributeNameValidator;
import com.twinkle.framework.struct.utils.StructTypeUtil;
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
    protected AttributeType type;
    protected StructAttributeSchema structAttributeSchema;

    public DefaultSAAttributeDescriptorImpl(String _name, String _typeName, AttributeTypeManager _typeManager, boolean _optional) throws TypeNotFoundException, StructAttributeTypeNotFoundException, BadAttributeNameException, NamespaceNotFoundException {
        StructAttributeNameValidator.checkName(_name);
        this.name = _name;
        this.typeName = _typeName;
        this.optional = _optional;
        this.structAttributeSchema = StructAttributeSchemaManager.getStructAttributeSchema();
        this.type = StructTypeUtil.getStructTypeByName(_typeName, _typeManager);
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

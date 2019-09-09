package com.twinkle.framework.struct.asm.define;

import com.twinkle.framework.asm.define.AnnotationDef;
import com.twinkle.framework.asm.define.AttributeDefImpl;
import com.twinkle.framework.asm.define.TypeDef;
import com.twinkle.framework.asm.descriptor.AttributeDescriptor;
import com.twinkle.framework.struct.utils.StructTypeUtil;
import org.objectweb.asm.Type;

import java.util.List;

/**
 * Function: Struct Attribute define impl. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/9/19 11:54 AM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class StructAttributeAttributeDefImpl extends AttributeDefImpl {
    public StructAttributeAttributeDefImpl(String _name, TypeDef _type, boolean _isReadOnly, boolean _isRequired, Object _value) {
        super(_name, _type, _isReadOnly, _isRequired, _value);
    }

    public StructAttributeAttributeDefImpl(AttributeDescriptor _attrDesp, TypeDef _type, List<AnnotationDef> _annotationDefList) {
        super(_attrDesp, _type, _annotationDefList);
    }

    @Override
    protected Object initDefaultValue(String _attrName, Type _type, Object _value) {
        return super.initDefaultValue(_attrName, StructTypeUtil.mapStructAttributeType(_type), _value);
    }
}

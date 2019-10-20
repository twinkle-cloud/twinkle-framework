package com.twinkle.framework.context.model;

import com.twinkle.framework.api.context.AttributeInfo;
import com.twinkle.framework.context.PrimitiveAttributeSchema;
import com.twinkle.framework.core.lang.Attribute;
import com.twinkle.framework.struct.lang.StructAttribute;
import com.twinkle.framework.struct.utils.StructAttributeUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.objectweb.asm.Type;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-12 10:48<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Data
@Slf4j
public class DefaultAttributeInfo implements AttributeInfo {
    /**
     * Attribute's TYPE index.
     */
    private int typeIndex;
    /**
     * Map to PRIMITIVE TYE of the attribute.
     */
    private int primitiveType;
    /**
     * Name of the attribute.
     */
    private String name;
    /**
     * The index of the attribute.
     */
    private int index;
    /**
     * The class name of the attribute
     */
    private String className;
    /**
     * The class of the attribute.
     */
    private Class<?> attributeClass;
    /**
     * The description of the attribute class.
     */
    private String description;
    /**
     * If the Attribute is StructAttrAttribute, then this info should be StructAttribute Qualified Name.
     * if the attribute is ListAttribute, then this info should be item's name
     * - can be Primitive Attribute.
     * - can be StructAttribute's Qualified name.
     */
    private String extraInfo;
    /**
     * The value class for the attribute.
     */
    private Class<?> valueClass;
    /**
     * The value class's description.
     */
    private String valueDescription;
    /**
     * The value's attribute info.
     * ONLY for ListAttribute.
     */
    private AttributeInfo valueAttributeInfo;

    public DefaultAttributeInfo(int _typeIndex, int _primitiveType, String _name, int _index, Class<?> _class) {
        this.typeIndex = _typeIndex;
        this.primitiveType = _primitiveType;
        this.name = _name;
        this.index = _index;
        this.className = _class.getName();
        this.attributeClass = _class;
        this.description = Type.getDescriptor(_class);
    }

    public DefaultAttributeInfo(int _typeIndex, int _primitiveType, String _name, int _index, String _className) {
        this(_typeIndex, _primitiveType, _name, _index, _className, null);
    }

    public DefaultAttributeInfo(int _typeIndex, int _primitiveType, String _name, int _index, String _className, String _extraInfo) {
        this.typeIndex = _typeIndex;
        this.primitiveType = _primitiveType;
        this.name = _name;
        this.index = _index;
        this.className = _className;
        this.extraInfo = _extraInfo;
        try {
            this.attributeClass = Class.forName(_className);
        } catch (Exception ex) {
            log.debug("Cannot get new instance of {}, Exception: {} ", _className, ex);
            throw new RuntimeException("Unsupported NE type: [" + _className + "]");
        }
        this.description = Type.getDescriptor(this.attributeClass);
//        if (StringUtils.isNotBlank(_extraInfo)) {
//            if(!_extraInfo.startsWith(this.description)) {
//               log.warn("The given description [{}] is incorrect, so dismiss it.", _extraInfo);
//            } else {
//                this.description = _extraInfo;
//            }
//        }
    }

    /**
     * Build a new instance.
     *
     * @return
     */
    @Override
    public Attribute newAttributeInstance() {
        Attribute tempAttr = null;

        try {
            tempAttr = (Attribute) this.attributeClass.newInstance();
            if (this.primitiveType == Attribute.STRUCT_TYPE) {
                StructAttribute tempStructAttr = StructAttributeUtil.newStructAttribute(this.extraInfo);
                tempAttr.setValue(tempStructAttr);
            }
        } catch (Exception ex) {
            log.debug("Cannot get new instance of {}.", this.className, ex);
        }
        return tempAttr;
    }

    /**
     * Get value class for ListAttribute and StructAttrAttribute.
     *
     * @return
     */
    @Override
    public Class<?> getValueClass() {
        if (this.primitiveType == Attribute.STRUCT_TYPE) {
            return StructAttributeUtil.getStructAttributeClass(this.extraInfo);
        } else if (this.primitiveType == Attribute.LIST_STRUCT_ATTRIBUTE_TYPE) {
            return StructAttributeUtil.getStructAttributeClass(this.extraInfo);
        } else if (this.primitiveType == Attribute.LIST_ATTRIBUTE_TYPE) {
            this.valueAttributeInfo = this.getValueAttributeInfo();
            return this.valueAttributeInfo.getAttributeClass();
        }
        return null;
    }

    @Override
    public AttributeInfo getValueAttributeInfo() {
//        if(this.valueAttributeInfo != null) {
//            return this.valueAttributeInfo;
//        }
        if (this.primitiveType == Attribute.LIST_ATTRIBUTE_TYPE) {
            if (StringUtils.isBlank(this.extraInfo)) {
                return null;
            }
            AttributeInfo tempItemInfo = PrimitiveAttributeSchema.getInstance().getAttribute(this.extraInfo);
            this.valueAttributeInfo = tempItemInfo;
        }
        return this.valueAttributeInfo;
    }

    @Override
    public String getValueType() {
        return this.extraInfo;
    }
}

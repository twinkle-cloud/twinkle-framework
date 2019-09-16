package com.twinkle.framework.struct.converter;

import com.twinkle.framework.core.lang.Attribute;
import com.twinkle.framework.core.lang.AttributeInfo;
import com.twinkle.framework.core.lang.JavaAttributeInfo;
import com.twinkle.framework.struct.utils.StructAttributeUtil;
import com.twinkle.framework.struct.type.StructAttribute;
import org.objectweb.asm.Type;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/25/19 6:21 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class JavaAttributeConverter {
    /**
     * Convert AttributeInfo to Java Attribute Info.
     *
     * @param _attrInfo
     * @return
     */
    public static JavaAttributeInfo convertToJavaAttribute(AttributeInfo _attrInfo) {
        JavaAttributeInfo tempInfo = new JavaAttributeInfo();
        tempInfo.setPrimitiveType(_attrInfo.getPrimitiveType());
        switch (_attrInfo.getPrimitiveType()) {
            case Attribute.INTEGER_TYPE:
                tempInfo.setName("Integer");
                tempInfo.setClassName(Integer.class.getName());
                tempInfo.setAttributeClass(Integer.class);
                tempInfo.setDescription(Type.getDescriptor(Integer.TYPE));
                break;
            case Attribute.UNICODE_STRING_TYPE:
            case Attribute.STRING_TYPE:
                tempInfo.setName("String");
                tempInfo.setClassName(String.class.getName());
                tempInfo.setAttributeClass(String.class);
                tempInfo.setDescription(Type.getDescriptor(String.class));
                break;
            case Attribute.LONG_TYPE:
                tempInfo.setName("Long");
                tempInfo.setClassName(Long.class.getName());
                tempInfo.setAttributeClass(Long.class);
                tempInfo.setDescription(Type.getDescriptor(Long.TYPE));
                break;
            case Attribute.FLOAT_TYPE:
                tempInfo.setName("Float");
                tempInfo.setClassName(Float.class.getName());
                tempInfo.setAttributeClass(Float.class);
                tempInfo.setDescription(Type.getDescriptor(Float.TYPE));
                break;
            case Attribute.DOUBLE_TYPE:
                tempInfo.setName("Double");
                tempInfo.setClassName(Double.class.getName());
                tempInfo.setAttributeClass(Double.class);
                tempInfo.setDescription(Type.getDescriptor(Double.TYPE));
                break;
            case Attribute.OBJECT_TYPE:
                tempInfo.setName(_attrInfo.getName());
                if(_attrInfo.getName().indexOf(":") > 0) {
                    StructAttribute tempAttr = StructAttributeUtil.newStructAttribute(_attrInfo.getName());
                    tempInfo.setClassName(tempAttr.getClass().getName());
                    tempInfo.setAttributeClass(tempAttr.getClass());
                    tempInfo.setDescription(Type.getDescriptor(tempAttr.getClass()));
                } else {
                    tempInfo.setClassName(_attrInfo.getClassName());
                    tempInfo.setAttributeClass(_attrInfo.getAttributeClass());
                    tempInfo.setDescription(_attrInfo.getDescription());
                }
                break;
            default:
                throw new RuntimeException("Encountered unsupported attribute type [{" + _attrInfo + "}].");
        }
        return tempInfo;
    }
}

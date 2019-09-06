package com.twinkle.framework.core.converter;

import com.twinkle.framework.core.lang.Attribute;
import com.twinkle.framework.core.lang.AttributeInfo;
import com.twinkle.framework.core.lang.JavaAttributeInfo;
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
                tempInfo.setClassName(_attrInfo.getClassName());
                tempInfo.setAttributeClass(_attrInfo.getAttributeClass());
                tempInfo.setDescription(_attrInfo.getDescription());
                break;
            default:
                throw new RuntimeException("Encountered unsupported attribute type [{" + _attrInfo + "}].");
        }
        return tempInfo;
    }
}

package com.twinkle.framework.struct.utils;

import com.twinkle.framework.core.type.PrimitiveType;
import com.twinkle.framework.core.lang.util.*;
import com.twinkle.framework.core.type.StringType;
import com.twinkle.framework.core.type.AttributeType;
import com.twinkle.framework.struct.type.*;
import com.twinkle.framework.struct.util.StructAttributeArray;
import org.apache.commons.lang3.StringUtils;
import org.objectweb.asm.Type;

import java.util.HashMap;
import java.util.Map;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/6/19 11:39 AM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class StructTypeUtil {
    public final static String STRUCT_ATTRIBUTE_IMPL_SUFFIX = "Impl";
    public static final Type STRUCT_ATTRIBUTE_TYPE = Type.getType(StructAttribute.class);
    private static Map<Type, Type> SA_ARRAY_TO_JAVA = new HashMap<>(9);

    static {
        SA_ARRAY_TO_JAVA.put(Type.getType(BooleanArray.class), Type.getType(boolean[].class));
        SA_ARRAY_TO_JAVA.put(Type.getType(CharArray.class), Type.getType(char[].class));
        SA_ARRAY_TO_JAVA.put(Type.getType(ByteArray.class), Type.getType(byte[].class));
        SA_ARRAY_TO_JAVA.put(Type.getType(ShortArray.class), Type.getType(short[].class));
        SA_ARRAY_TO_JAVA.put(Type.getType(IntegerArray.class), Type.getType(int[].class));
        SA_ARRAY_TO_JAVA.put(Type.getType(LongArray.class), Type.getType(long[].class));
        SA_ARRAY_TO_JAVA.put(Type.getType(FloatArray.class), Type.getType(float[].class));
        SA_ARRAY_TO_JAVA.put(Type.getType(DoubleArray.class), Type.getType(double[].class));
        SA_ARRAY_TO_JAVA.put(Type.getType(StringArray.class), Type.getType(String[].class));
    }

    /**
     * Get the given struct type's class type.
     *
     * @param _type
     * @return
     * @throws ClassNotFoundException
     */
    public static Class<?> getTypeClass(AttributeType _type) throws ClassNotFoundException {
        if (_type.isPrimitiveType()) {
            switch (_type.getID()) {
                case PrimitiveType.BYTE_ID:
                    return Byte.TYPE;
                case PrimitiveType.SHORT_ID:
                    return Short.TYPE;
                case PrimitiveType.INT_ID:
                    return Integer.TYPE;
                case PrimitiveType.LONG_ID:
                    return Long.TYPE;
                case PrimitiveType.CHAR_ID:
                    return Character.TYPE;
                case PrimitiveType.BOOLEAN_ID:
                    return Boolean.TYPE;
                case 7:
                case 8:
                default:
                    break;
                case PrimitiveType.FLOAT_ID:
                    return Float.TYPE;
                case PrimitiveType.DOUBLE_ID:
                    return Double.TYPE;
            }
        } else {
            if (_type.isStringType()) {
                return String.class;
            }

            if (_type.isArrayType()) {
                switch (((ArrayType) _type).getElementType().getID()) {
                    case PrimitiveType.BYTE_ID:
                        return ByteArray.class;
                    case PrimitiveType.SHORT_ID:
                        return ShortArray.class;
                    case PrimitiveType.INT_ID:
                        return IntegerArray.class;
                    case PrimitiveType.LONG_ID:
                        return LongArray.class;
                    case PrimitiveType.CHAR_ID:
                        return CharArray.class;
                    case PrimitiveType.BOOLEAN_ID:
                        return BooleanArray.class;
                    case PrimitiveType.FLOAT_ID:
                        return FloatArray.class;
                    case PrimitiveType.DOUBLE_ID:
                        return DoubleArray.class;
                    case StringType.STRING_ID:
                        return StringArray.class;
                    case StructType.STRUCT_ID:
                        return StructAttributeArray.class;
                }
            }
        }

        throw new ClassNotFoundException("Cannot load class for type: " + _type.getName());
    }

    /**
     * Get Mapped java type class with given struct type.
     *
     * @param _type
     * @return
     * @throws ClassNotFoundException
     */
    public static Class getMappedTypeClass(AttributeType _type) throws ClassNotFoundException {
        if (_type.isPrimitiveType()) {
            switch (_type.getID()) {
                case PrimitiveType.BYTE_ID:
                    return Byte.TYPE;
                case PrimitiveType.SHORT_ID:
                    return Short.TYPE;
                case PrimitiveType.INT_ID:
                    return Integer.TYPE;
                case PrimitiveType.LONG_ID:
                    return Long.TYPE;
                case PrimitiveType.CHAR_ID:
                    return Character.TYPE;
                case PrimitiveType.BOOLEAN_ID:
                    return Boolean.TYPE;
                case 7:
                case 8:
                default:
                    break;
                case PrimitiveType.FLOAT_ID:
                    return Float.TYPE;
                case PrimitiveType.DOUBLE_ID:
                    return Double.TYPE;
            }
        } else {
            if (_type.isStringType()) {
                return String.class;
            }

            if (_type.isArrayType()) {
                switch (((ArrayType) _type).getElementType().getID()) {
                    case PrimitiveType.BYTE_ID:
                        return byte[].class;
                    case PrimitiveType.SHORT_ID:
                        return short[].class;
                    case PrimitiveType.INT_ID:
                        return int[].class;
                    case PrimitiveType.LONG_ID:
                        return long[].class;
                    case PrimitiveType.CHAR_ID:
                        return char[].class;
                    case PrimitiveType.BOOLEAN_ID:
                        return boolean[].class;
                    case PrimitiveType.FLOAT_ID:
                        return float[].class;
                    case PrimitiveType.DOUBLE_ID:
                        return double[].class;
                    case StringType.STRING_ID:
                        return String[].class;
                    case StructType.STRUCT_ID:
                        throw new UnsupportedOperationException("Cannot map type " + _type.getName() + " to any Java primitive or array type");
                }
            }
        }
        throw new ClassNotFoundException("Cannot load class for type: " + _type.getName());
    }

    /**
     * Get the mapped java type with the given Struct Attribute Type.
     *
     * @param _saType
     * @return
     */
    public static Type mapStructAttributeType(Type _saType) {
        Type tempJavaType = SA_ARRAY_TO_JAVA.get(_saType);
        return tempJavaType != null ? tempJavaType : _saType;
    }

    /**
     * Get the qualified name with the given class name.
     *
     * @param _className
     * @return
     */
    public static String getQualifiedName(String _className) {
        if (StringUtils.isBlank(_className)) {
            return "";
        }
        int tempNameIndex = _className.lastIndexOf(".");
        if (tempNameIndex <= 0) {
            return _className;
        }
        int tempNamespaceIndex = _className.substring(0, tempNameIndex).lastIndexOf(".");
        String tempClassName = _className;
        if (_className.endsWith(STRUCT_ATTRIBUTE_IMPL_SUFFIX)) {
            tempClassName = _className.substring(0, _className.indexOf(STRUCT_ATTRIBUTE_IMPL_SUFFIX));
        }
        if (tempNamespaceIndex > 0) {
            tempClassName = tempClassName.substring(tempNamespaceIndex + 1);
            return tempClassName.replace(".", ":");
        }
        return tempClassName;
    }
}

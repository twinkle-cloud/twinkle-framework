package com.twinkle.framework.asm.utils;

import com.twinkle.framework.asm.Bean;
import com.twinkle.framework.asm.annotation.Key;
import com.twinkle.framework.asm.builder.AnnotationDefBuilder;
import com.twinkle.framework.asm.constants.AsmConstant;
import com.twinkle.framework.asm.define.*;
import lombok.extern.slf4j.Slf4j;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utilities for handling ASM's Type.
 *
 * @author Matt
 */
@Slf4j
public class TypeUtil {
    /**
     * private sort denoting an object type, such as "com/Example" versus the
     * standard "Lcom/Example;".
     */
    private static final int INTERNAL = 12;
    /**
     * Field access to Type's sort.
     */
    private static Field sortF;
    public static final Type BOOLEAN_ARRAY_TYPE = Type.getType(boolean[].class);
    public static final Type CHAR_ARRAY_TYPE = Type.getType(char[].class);
    public static final Type BYTE_ARRAY_TYPE = Type.getType(byte[].class);
    public static final Type SHORT_ARRAY_TYPE = Type.getType(short[].class);
    public static final Type INT_ARRAY_TYPE = Type.getType(int[].class);
    public static final Type LONG_ARRAY_TYPE = Type.getType(long[].class);
    public static final Type FLOAT_ARRAY_TYPE = Type.getType(float[].class);
    public static final Type DOUBLE_ARRAY_TYPE = Type.getType(double[].class);
    public static final Type STRING_ARRAY_TYPE = Type.getType(String[].class);
    public static final Type OBJECT_TYPE = Type.getType(Object.class);
    public static final Type STRING_TYPE = Type.getType(String.class);
    public static final Type CLASS_TYPE = Type.getType(Class.class);
    public static final Type ENUM_TYPE = Type.getType(Enum.class);
    public static final Type ARRAYS_TYPE = Type.getType(Arrays.class);
    public static final Type CLONEABLE_TYPE = Type.getType(Cloneable.class);
    public static final Type CLONE_NOT_SUPPORTED_EX_TYPE = Type.getType(CloneNotSupportedException.class);
    public static final Type STRING_BUILDER_TYPE = Type.getType(StringBuilder.class);
    public static final Type BEAN_TYPE = Type.getType(Bean.class);
    public static final Type KEY_ANNOTATION_TYPE = Type.getType(Key.class);
    public static final Type DOUBLE_OBJECT_TYPE = Type.getType(Double.class);
    public static final Type FLOAT_OBJECT_TYPE = Type.getType(Float.class);

    /**
     * @param text Text to parse as Type.
     * @return Type from text.
     */
    public static Type parse(String text) {
        Type t = null;
        if (text.isEmpty()) {
            return t;
        }
        if (isPrimitive(text)) {
            t = Type.getType(text);
        } else if (isMethod(text)) {
            t = Type.getMethodType(text);
        } else if (isObject(text)) {
            t = Type.getObjectType(text);
        } else {
            t = Type.getType(text);
        }
        return t;
    }

    /**
     * @param t Type to convert.
     * @return Formatted Type as string.
     */
    public static String toString(Type t) {
        if (isInternal(t)) {
            return t.getInternalName();
        } else {
            return t.toString();
        }
    }

    /**
     * @param s Type to check.
     * @return Type denotes a primitive type.
     */
    public static boolean isPrimitive(String s) {
        if (s.length() != 1) {
            return false;
        }
        switch (s.charAt(0)) {
            case 'Z':
            case 'C':
            case 'B':
            case 'S':
            case 'I':
            case 'F':
            case 'J':
            case 'D':
                return true;
        }
        return false;
    }

    /**
     * Judge the given type is primitive type or not.
     *
     * @param _type
     * @return Type denotes a primitive type.
     */
    public static boolean isPrimitive(Type _type) {
        return _type.getSort() > 0 && _type.getSort() < 9;
    }

    /**
     * @param s Text to check.
     * @return Type is method format, "(Ltype/args;)Lreturn;"
     */
    public static boolean isMethod(String s) {
        // This assumes a lot, but hey, it serves our purposes.
        return s.startsWith("(");
    }

    /**
     * @param s Text to check.
     * @return Type is standard format of "Lcom/Example;".
     */
    public static boolean isStandard(String s) {
        return s.length() > 2 && s.charAt(0) == 'L' && s.charAt(s.length() - 1) == ';';
    }

    /**
     * @param s Text to check.
     * @return Type is object format of "com/Example".
     */
    public static boolean isObject(String s) {
        return !isMethod(s) && !isStandard(s);
    }

    /**
     * @param t Type to check.
     * @return Type is an object type, format of "com/Example"
     */
    public static boolean isInternal(Type t) {
        try {
            return (int) sortF.get(t) == INTERNAL;
        } catch (Exception e) {
            return false;
        }
    }

    static {
        // get raw access to sort so we can check for INTERNAL sort.
        try {
            sortF = Type.class.getDeclaredField("sort");
            sortF.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * Filters a non-method descriptor based on display configuration
     * <i>(simplification)</i>
     *
     * @param type AsmInput type.
     * @return Filtered version of given type.
     */
    public static String filter(Type type) {
        if (type.getSort() == Type.METHOD) {
            Thread.dumpStack();
        }
        if (AsmConstant.TYPE_DISPLAY_SIMPLY) {
            if (type.getSort() == Type.ARRAY) {
                String base = filter(type.getElementType());
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < type.getDimensions(); i++) {
                    sb.append("[]");
                }
                return base + sb.toString();
            }
            String name = type.getClassName();
            if (name.contains(".")) {
                // substring package name away
                name = name.substring(name.lastIndexOf(".") + 1);
            }
            return name;
        }
        // No simplification
        return toString(type);
    }

    /**
     * Pack the Type signature with the given Type Define.
     *
     * @param _typeDef
     * @return
     */
    public static String getTypeSignature(TypeDef _typeDef) {
        StringBuilder tempBuilder = new StringBuilder();
        if (_typeDef == null) {
            return tempBuilder.toString();
        }

        if (_typeDef instanceof GenericTypeDef) {
            tempBuilder.append(((GenericTypeDefImpl) _typeDef).getFieldSignature());
            return tempBuilder.toString();
        }
        if(_typeDef instanceof ClassTypeDef) {
            tempBuilder.append(Type.getDescriptor(((ClassTypeDef)_typeDef).getTypeClass()));
            return tempBuilder.toString();
        }
        tempBuilder.append(_typeDef.getType().getDescriptor());
        return tempBuilder.toString();
    }

    /**
     * To get the getterName from the given attribute define.
     *
     * @param _attrDef
     * @return
     */
    public static String getGetterName(AttributeDef _attrDef) {
        return _attrDef.getGetterName();
    }

    /**
     * To get the setterName from the given attribute define.
     *
     * @param _attrDef
     * @return
     */
    public static String getSetterName(AttributeDef _attrDef) {
        return _attrDef.getSetterName();
    }

    /**
     * To get the default getterName from the given attribute define.
     *
     * @param _attrDef
     * @return
     */
    public static String getDefaultGetterName(AttributeDef _attrDef) {
        return _attrDef.getGetterName() + "Default";
    }

    /**
     * To get the field descriptor from the given field type.
     *
     * @param _type field type.
     * @return
     */
    public static String getFieldDescriptor(Type _type) {
        return _type.getDescriptor();
    }

    /**
     * To get the field signature from for the generic field.
     *
     * @param _mainType
     * @param _genericTypeArray
     * @return
     */
    public static String getFieldDescriptor(Type _mainType, Type[] _genericTypeArray) {
        return getGenericDescriptor(_mainType, _genericTypeArray);
    }

    /**
     * To get the generic field's signature.
     *
     * @param _typeDef
     * @return
     */
    public static String getGenericFieldSignature(TypeDef _typeDef) {
        return ((GenericTypeDefImpl) _typeDef).getFieldSignature();
    }

    /**
     * To get the class's signature with class name.
     *
     * @param _className
     * @return
     */
    public static String getDescriptorByClassName(String _className) {
        StringBuilder tempBuilder = new StringBuilder("L");
        tempBuilder.append(_className);
        tempBuilder.append(";");
        return tempBuilder.toString();
    }
    /**
     * To get the getter signature from the given type.
     *
     * @param _type
     * @return
     */
    public static String getGetterSignature(Type _type) {
        return "()" + _type.getDescriptor();
    }

    /**
     * To get the setterName from the given type.
     *
     * @param _type
     * @return
     */
    public static String getSetterSignature(Type _type) {
        return "(" + _type.getDescriptor() + ")V";
    }

    /**
     * To get the getter signature from the given types.
     *
     * @param _type
     * @param _genericTypeArray
     * @return
     */
    public static String getGetterSignature(Type _type, Type[] _genericTypeArray) {
        return "()" + getGenericDescriptor(_type, _genericTypeArray);
    }

    /**
     * To get the setter signature from the given types.
     *
     * @param _Type
     * @param _genericTypeArray
     * @return
     */
    public static String getSetterSignature(Type _Type, Type[] _genericTypeArray) {
        return "(" + getGenericDescriptor(_Type, _genericTypeArray) + ")V";
    }

    /**
     * To get the generic getter signature from the given type define.
     *
     * @param _typeDef
     * @return
     */
    public static String getGenericGetterSignature(TypeDef _typeDef) {
        return ((GenericTypeDefImpl) _typeDef).getGetterSignature();
    }

    /**
     * To get the generic setter signature from the given type define.
     *
     * @param _typeDef
     * @return
     */
    public static String getGenericSetterSignature(TypeDef _typeDef) {
        return ((GenericTypeDefImpl) _typeDef).getSetterSignature();
    }

    /**
     * Get the main type's the descriptor with generic types.
     *
     * @param _mainType
     * @param _genericTyeArray
     * @return
     */
    public static String getGenericDescriptor(Type _mainType, Type[] _genericTyeArray) {
        StringBuilder tempBuilder = new StringBuilder();
        tempBuilder.append('L');
        tempBuilder.append(_mainType.getInternalName());
        if (_genericTyeArray != null && _genericTyeArray.length > 0) {
            tempBuilder.append('<');
            for (int i = 0; i < _genericTyeArray.length; i++) {
                Type tempType = _genericTyeArray[i];
                tempBuilder.append(tempType.getDescriptor());
            }
            tempBuilder.append('>');
        }

        tempBuilder.append(';');
        return tempBuilder.toString();
    }

    /**
     * Get Boolean Flag getterName.
     *
     * @param _attrDef
     * @return
     */
    public static String getFlagGetterName(AttributeDef _attrDef) {
        return getGetterName(_attrDef) + "Flag";
    }

    /**
     * Get Boolean Flag setterName.
     *
     * @param _attrDef
     * @return
     */
    public static String getFlagSetterName(AttributeDef _attrDef) {
        return getSetterName(_attrDef) + "Flag";
    }

    /**
     * Get Boolean Getter descriptor.
     *
     * @return
     */
    public static String getFlagGetterDescriptor() {
        return "()Z";
    }

    /**
     * Get Boolean Setter descriptor.
     *
     * @return
     */
    public static String getFlagSetterDescriptor() {
        return "(Z)V";
    }

    /**
     * Get the method's signature with given parameters' type and return type.
     *
     * @param _parameterTypes
     * @param _returnType
     * @return
     */
    public static String getMethodDescriptor(Type[] _parameterTypes, Type _returnType) {
        StringBuilder tempBuilder = new StringBuilder();
        tempBuilder.append('(');
        for (int i = 0; i < _parameterTypes.length; i++) {
            Type tempType = _parameterTypes[i];
            tempBuilder.append(tempType.getDescriptor());
        }
        tempBuilder.append(')');
        tempBuilder.append(_returnType.getDescriptor());
        return tempBuilder.toString();
    }

    /**
     * Get method descriptor with given parameters' classes and return type class.
     *
     * @param _parameterClassArray
     * @param _returnClass
     * @return
     */
    public static String getMethodDescriptor(Class[] _parameterClassArray, Class _returnClass) {
        StringBuilder tempBuilder = new StringBuilder();
        tempBuilder.append('(');
        for (int i = 0; i < _parameterClassArray.length; i++) {
            Class tempClass = _parameterClassArray[i];
            tempBuilder.append(Type.getType(tempClass).getDescriptor());
        }

        tempBuilder.append(')');
        tempBuilder.append(Type.getType(_returnClass).getDescriptor());
        return tempBuilder.toString();
    }

    /**
     * Get the array type with element type.
     *
     * @param _elementType
     * @return
     */
    public static Type getArrayType(Type _elementType) {
        return Type.getType("[" + _elementType.getDescriptor());
    }

    /**
     * Get the array type with elementClass.
     *
     * @param _elementClass
     * @return
     */
    public static Type getArrayType(Class _elementClass) {
        return Type.getType("[" + Type.getType(_elementClass).getDescriptor());
    }

    /**
     * Returns a JVM instruction opcode adapted to this {@link Type}. This method must not be used for
     * method types.
     *
     * @param _type   the given type.
     * @param _opcode a JVM instruction opcode. This opcode must be one of ILOAD, ISTORE, IALOAD,
     *                IASTORE, IADD, ISUB, IMUL, IDIV, IREM, INEG, ISHL, ISHR, IUSHR, IAND, IOR, IXOR and
     *                IRETURN.
     * @return an opcode that is similar to the given opcode, but adapted to this {@link Type}. For
     * example, if this type is {@code float} and {@code opcode} is IRETURN, this method returns
     * FRETURN.
     */
    public static int getOpcode(Type _type, int _opcode) {
        return _type.getOpcode(_opcode);
    }

    /**
     * Possible values for the type operand of the NEWARRAY instruction.
     * See https://docs.oracle.com/javase/specs/jvms/se9/html/jvms-6.html#jvms-6.5.newarray.
     *
     * @param _type
     * @return
     */
    public static int getNewArrayOpcode(Type _type) {
        switch (_type.getSort()) {
            case Type.BOOLEAN:
                return Opcodes.T_BOOLEAN;
            case Type.CHAR:
                return Opcodes.T_CHAR;
            case Type.BYTE:
                return Opcodes.T_BYTE;
            case Type.SHORT:
                return Opcodes.T_SHORT;
            case Type.INT:
                return Opcodes.T_INT;
            case Type.FLOAT:
                return Opcodes.T_FLOAT;
            case Type.LONG:
                return Opcodes.T_LONG;
            case Type.DOUBLE:
                return Opcodes.T_DOUBLE;
            default:
                throw new IllegalArgumentException(_type.toString() + " is unsupported type for NEWARRAY operation");
        }
    }

    /**
     * Get Null Opcode dconst.
     * The JVM opcode values (with the MethodVisitor method name used to visit them in comment, and
     * where '-' means 'same method name as on the previous line').
     * See https://docs.oracle.com/javase/specs/jvms/se9/html/jvms-6.html.
     *
     * @param _type
     * @return
     */
    public static int getNullOpcode(Type _type) {
        switch (_type.getSort()) {
            case Type.BOOLEAN:
            case Type.CHAR:
            case Type.BYTE:
            case Type.SHORT:
            case Type.INT:
                return Opcodes.ICONST_0;//3
            case Type.FLOAT:
                return Opcodes.FCONST_0;
            case Type.LONG:
                return Opcodes.LCONST_0;
            case Type.DOUBLE:
                return Opcodes.DCONST_0;
            case Type.ARRAY:
            case Type.OBJECT:
            default:
                return Opcodes.ACONST_NULL;
        }
    }

    /**
     * Get StringBuilder append signature.
     *
     * @param _type
     * @return
     */
    public static String getStringBuilderAppendSignature(Type _type) {
        switch (_type.getSort()) {
            case Type.BOOLEAN:
                return getMethodDescriptor(new Class[]{Boolean.TYPE}, StringBuilder.class);
            case Type.CHAR:
                return getMethodDescriptor(new Class[]{Character.TYPE}, StringBuilder.class);
            case Type.BYTE:
            case Type.SHORT:
            case Type.INT:
                return getMethodDescriptor(new Class[]{Integer.TYPE}, StringBuilder.class);
            case Type.FLOAT:
                return getMethodDescriptor(new Class[]{Float.TYPE}, StringBuilder.class);
            case Type.LONG:
                return getMethodDescriptor(new Class[]{Long.TYPE}, StringBuilder.class);
            case Type.DOUBLE:
                return getMethodDescriptor(new Class[]{Double.TYPE}, StringBuilder.class);
            case Type.ARRAY:
                return getMethodDescriptor(new Class[]{Object.class}, StringBuilder.class);
            default:
                return STRING_TYPE.equals(_type) ? getMethodDescriptor(new Class[]{String.class}, StringBuilder.class) : getMethodDescriptor(new Class[]{Object.class}, StringBuilder.class);
        }
    }

    /**
     * Get Arrays to string signature.
     *
     * @param _type
     * @return
     */
    public static String getArraysToStringSignature(Type _type) {
        if (BOOLEAN_ARRAY_TYPE.equals(_type)) {
            return getMethodDescriptor(new Class[]{boolean[].class}, String.class);
        } else if (CHAR_ARRAY_TYPE.equals(_type)) {
            return getMethodDescriptor(new Class[]{char[].class}, String.class);
        } else if (BYTE_ARRAY_TYPE.equals(_type)) {
            return getMethodDescriptor(new Class[]{byte[].class}, String.class);
        } else if (SHORT_ARRAY_TYPE.equals(_type)) {
            return getMethodDescriptor(new Class[]{short[].class}, String.class);
        } else if (INT_ARRAY_TYPE.equals(_type)) {
            return getMethodDescriptor(new Class[]{int[].class}, String.class);
        } else if (LONG_ARRAY_TYPE.equals(_type)) {
            return getMethodDescriptor(new Class[]{long[].class}, String.class);
        } else if (FLOAT_ARRAY_TYPE.equals(_type)) {
            return getMethodDescriptor(new Class[]{float[].class}, String.class);
        } else {
            return DOUBLE_ARRAY_TYPE.equals(_type) ? getMethodDescriptor(new Class[]{double[].class}, String.class) : getMethodDescriptor(new Class[]{Object[].class}, String.class);
        }
    }

    /**
     * Get Array equals signature.
     *
     * @param _type
     * @return
     */
    public static String getArraysEqualsSignature(Type _type) {
        Type tempType = _type.getElementType();
        if (TypeUtil.isPrimitive(tempType)) {
            String typeDescriptor = tempType.getDescriptor();
            return "([" + typeDescriptor + "[" + typeDescriptor + ")Z";
        } else {
            return TypeUtil.getMethodDescriptor(new Class[]{Object[].class, Object[].class}, Boolean.TYPE);
        }
    }

    /**
     * Convert the class name to internal class name.
     *
     * @param _className
     * @return
     */
    public static String getInternalNameByClassName(String _className) {
        return _className.replace(".", "/");
    }

    /**
     * For flag attribute, need set swagger ignore
     * @param _fieldName
     * @return
     */
    public static List<AnnotationDef> getHideFieldAnnotationList(String _fieldName) {
        List<AnnotationDef> tempDefList = new ArrayList<>(1);

        try {
            AnnotationDef tempDefine = AnnotationDefBuilder.getAnnotationDef("@io.swagger.v3.oas.annotations.media.Schema(hidden = true)", AnnotationDef.class.getClassLoader());
            tempDefList.add(tempDefine);
        } catch (ClassNotFoundException e) {
            log.warn("Failed to build the Flag field [{}]'s annotation list.", _fieldName);
        }

        return tempDefList;
    }
}

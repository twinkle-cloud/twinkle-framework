package com.twinkle.framework.asm.utils;

import com.twinkle.framework.asm.builder.AnnotationDefBuilder;
import com.twinkle.framework.asm.builder.TypeDefBuilder;
import com.twinkle.framework.asm.define.*;
import com.twinkle.framework.asm.descriptor.AttributeDescriptor;
import com.twinkle.framework.asm.descriptor.BeanTypeDescriptor;
import com.twinkle.framework.asm.descriptor.TypeDescriptor;
import org.apache.commons.collections4.CollectionUtils;
import org.objectweb.asm.Type;

import java.util.*;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/4/19 9:57 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class ClassDesignerUtil {
    public static final String FLAG_SUFFIX = "Flag";
    public static final String DEFAULT_SUFFIX = "Default";
    private static final Map<String, Class<?>> primitiveClasses = new HashMap();

    static {
        Class[] tempPrimitiveTypeArray = new Class[]{Void.TYPE, Boolean.TYPE, Byte.TYPE, Character.TYPE, Short.TYPE, Integer.TYPE, Float.TYPE, Double.TYPE, Long.TYPE};
        for (int i = 0; i < tempPrimitiveTypeArray.length; ++i) {
            Class tempClass = tempPrimitiveTypeArray[i];
            primitiveClasses.put(tempClass.getName(), tempClass);
        }
    }

    public ClassDesignerUtil() {
    }

    public static String getInternalName(Class<?> _class) {
        return Type.getInternalName(_class);
    }

    public static String getInternalName(String _className) {
        return _className.replace('.', '/');
    }

    public static String getGetterName(AttributeDef _attrDef) {
        return _attrDef.getGetterName();
    }

    protected static String getSetterName(AttributeDef _attrDef) {
        return _attrDef.getSetterName();
    }

    public static String getDefaultGetterName(AttributeDef _attrDef) {
        return _attrDef.getGetterName() + DEFAULT_SUFFIX;
    }

    public static String getGenericFieldSignature(TypeDef _typeDef) {
        return ((GenericTypeDefImpl) _typeDef).getFieldSignature();
    }

    public static String getGetterSignature(Type _type) {
        return "()" + _type.getDescriptor();
    }

    public static String getSetterSignature(Type _type) {
        return "(" + _type.getDescriptor() + ")V";
    }

    public static String getGetterSignature(Type _type, Type[] _genericTypes) {
        return "()" + TypeUtil.getGenericDescriptor(_type, _genericTypes);
    }

    public static String getSetterSignature(Type _type, Type[] _genericTypes) {
        return "(" + TypeUtil.getGenericDescriptor(_type, _genericTypes) + ")V";
    }

    public static String getGenericGetterSignature(TypeDef _typeDef) {
        return ((GenericTypeDefImpl) _typeDef).getGetterSignature();
    }

    public static String getGenericSetterSignature(TypeDef _typeDef) {
        return ((GenericTypeDefImpl) _typeDef).getSetterSignature();
    }

    public static String getFlagGetterName(AttributeDef _attrDef) {
        return getGetterName(_attrDef) + FLAG_SUFFIX;
    }

    public static String getFlagSetterName(AttributeDef _attrDef) {
        return getSetterName(_attrDef) + FLAG_SUFFIX;
    }

    public static String getFlagGetterSignature() {
        return "()Z";
    }

    public static String getFlagSetterSignature() {
        return "(Z)V";
    }

    public static String getMethodSignature(Type _returnType, Type... _parameterTypes) {
        StringBuilder tempBuilder = new StringBuilder();
        tempBuilder.append('(');
        if (_parameterTypes != null) {
            for (int i = 0; i < _parameterTypes.length; ++i) {
                Type tempType = _parameterTypes[i];
                tempBuilder.append(tempType.getDescriptor());
            }
        }

        tempBuilder.append(')');
        tempBuilder.append(_returnType.getDescriptor());
        return tempBuilder.toString();
    }

    public static String getMethodSignature(Class<?> _returnClass, Class<?>... _parameterClasses) {
        StringBuilder tempBuilder = new StringBuilder();
        tempBuilder.append('(');
        if (_parameterClasses != null) {
            for (int i = 0; i < _parameterClasses.length; ++i) {
                Class<?> tempClass = _parameterClasses[i];
                tempBuilder.append(Type.getType(tempClass).getDescriptor());
            }
        }

        tempBuilder.append(')');
        tempBuilder.append(Type.getType(_returnClass).getDescriptor());
        return tempBuilder.toString();
    }

    public static Type getArrayType(Type _elementType) {
        return Type.getType("[" + _elementType.getDescriptor());
    }

    public static Type getArrayType(Class _elementClass) {
        return Type.getType("[" + Type.getType(_elementClass).getDescriptor());
    }

    public static Class<?> primitiveClassForName(String _className) {
        if (primitiveClasses.containsKey(_className)) {
            return primitiveClasses.get(_className);
        } else if (_className.equals("java.lang.String")) {
            return String.class;
        } else {
            throw new RuntimeException("Class: '" + _className + "' not primitive type");
        }
    }

    public static Class<?> classForName(String _className) {
        try {
            return primitiveClasses.containsKey(_className) ? primitiveClasses.get(_className) : Class.forName(_className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toBeanNormalForm(String _attrName) {
        return _attrName.length() > 0 ? _attrName.substring(0, 1).toUpperCase() + _attrName.substring(1) : _attrName;
    }

    public static String getGetterName(String _attrName) {
        return "get" + toBeanNormalForm(_attrName);
    }

    public static String getSetterName(String _attrName) {
        return "set" + toBeanNormalForm(_attrName);
    }

    public static String getConstantName(String _attrName) {
        return _attrName.toUpperCase();
    }

    public static String getFieldName(String _attrName) {
        return _attrName;
    }

    public static String getFlagGetterName(String _attrName) {
        return getGetterName(_attrName) + "Flag";
    }

    public static String getFlagSetterName(String _attrName) {
        return getSetterName(_attrName) + "Flag";
    }

    public static String getFlagFieldName(String _attrName) {
        return getFieldName(_attrName) + FLAG_SUFFIX;
    }

    /**
     * Get Annotation List.
     *
     * @param _annotations
     * @param _classLoader
     * @return
     * @throws ClassNotFoundException
     */
    public static List<AnnotationDef> getAnnotations(Set<String> _annotations, ClassLoader _classLoader) throws ClassNotFoundException {
        if (CollectionUtils.isEmpty(_annotations)) {
            return Collections.EMPTY_LIST;
        }
        List<AnnotationDef> tempResultList = new ArrayList<>(_annotations.size());
        for (String tempItem : _annotations) {
            tempResultList.add(AnnotationDefBuilder.getAnnotationDef(tempItem, _classLoader));
        }
        return tempResultList;
    }

    /**
     * Get attributes list with given attribute descriptors.
     *
     * @param _descriptors
     * @param _classLoader
     * @param _typeDefineMap
     * @return
     * @throws ClassNotFoundException
     */
    public static List<AttributeDef> getAttributes(List<AttributeDescriptor> _descriptors, ClassLoader _classLoader, Map<String, TypeDef> _typeDefineMap) throws ClassNotFoundException {
        if (CollectionUtils.isEmpty(_descriptors)) {
            return Collections.EMPTY_LIST;
        }
        List<AttributeDef> tempResultList = new ArrayList<>(_descriptors.size());
        for (AttributeDescriptor tempItem : _descriptors) {
            List<AnnotationDef> tempAnnotationDefineList = getAnnotations(tempItem.getAnnotations(), _classLoader);
            tempResultList.add(new AttributeDefImpl(tempItem, TypeDefBuilder.getTypeDef(tempItem.getType(), _classLoader, _typeDefineMap), tempAnnotationDefineList));
        }
        return tempResultList;
    }

    /**
     * Get attributes list with given attribute descriptors.
     *
     * @param _descriptors
     * @param _classLoader
     * @return
     * @throws ClassNotFoundException
     */
    public static List<AttributeDef> getAttributes(List<AttributeDescriptor> _descriptors, ClassLoader _classLoader) throws ClassNotFoundException {
        return getAttributes(_descriptors, _classLoader, new HashMap<>(6));
    }

    /**
     * Get the Bean or general class 's parents.
     *
     * @param _descriptors
     * @param _classLoader
     * @param _typeDefineMap
     * @return
     * @throws ClassNotFoundException
     */
    public static List<TypeDef> getParents(Set<BeanTypeDescriptor> _descriptors, ClassLoader _classLoader, Map<String, TypeDef> _typeDefineMap) throws ClassNotFoundException {
        if (CollectionUtils.isEmpty(_descriptors)) {
            return Collections.EMPTY_LIST;
        }
        List<TypeDef> tempResultList = new ArrayList<>(_descriptors.size());
        for (BeanTypeDescriptor tempItem : _descriptors) {
            tempResultList.add(TypeDefBuilder.getTypeDef(tempItem, _classLoader, _typeDefineMap));
        }
        return tempResultList;
    }

    /**
     * Get the Bean or general class 's exceptions.
     *
     * @param _descriptors
     * @param _classLoader
     * @param _typeDefineMap
     * @return
     * @throws ClassNotFoundException
     */
    public static List<TypeDef> getExceptions(List<TypeDescriptor> _descriptors, ClassLoader _classLoader, Map<String, TypeDef> _typeDefineMap) throws ClassNotFoundException {
        if (CollectionUtils.isEmpty(_descriptors)) {
            return Collections.EMPTY_LIST;
        }
        List<TypeDef> tempResultList = new ArrayList<>(_descriptors.size());
        for (TypeDescriptor tempItem : _descriptors) {
            tempResultList.add(TypeDefBuilder.getTypeDef(tempItem, _classLoader, _typeDefineMap));
        }
        return tempResultList;
    }
}

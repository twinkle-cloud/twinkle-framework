package com.twinkle.framework.core.datastruct.builder;

import com.twinkle.framework.core.datastruct.schema.AnnotationDef;
import com.twinkle.framework.core.datastruct.schema.AnnotationDefImpl;
import com.twinkle.framework.core.datastruct.schema.AnnotationElementDef;
import com.twinkle.framework.core.datastruct.schema.AnnotationElementDefImpl;
import com.twinkle.framework.core.utils.ListParser;
import org.objectweb.asm.Type;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-02 18:06<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class AnnotationDefBuilder {
    private static final List<AnnotationElementDef> NO_ELEMENTS = Collections.emptyList();
    private static final String ANNOTATION_BRACKETS = "{}()";
    private static final String COMMA_SEPARATOR = ",";
    public static final String LEFT_PARENTHESIS = "(";

    public AnnotationDefBuilder() {
    }

    /**
     * Get annotation define.
     *
     * @param _annotation
     * @param _classLoader
     * @return
     * @throws ClassNotFoundException
     */
    public static AnnotationDef getAnnotationDef(String _annotation, ClassLoader _classLoader) throws ClassNotFoundException {
        byte tempBeginIndex = 0;
        AnnotationDef.Kind tempKind = null;
        if (_annotation.startsWith("@set:")) {
            tempBeginIndex = 5;
            tempKind = AnnotationDef.Kind.SETTER;
        } else if (_annotation.startsWith("@get:")) {
            tempBeginIndex = 5;
            tempKind = AnnotationDef.Kind.GETTER;
        } else if (_annotation.startsWith("@")) {
            tempBeginIndex = 1;
        }

        int tempLeftPthIndex = _annotation.indexOf(LEFT_PARENTHESIS);

        String tempAnnotationName;
        Class tempAnnotationClass;
        List<AnnotationElementDef> tempDefineList;
        if (tempLeftPthIndex > 0) {
            tempAnnotationName = _annotation.substring(tempBeginIndex, tempLeftPthIndex).trim();
            tempAnnotationClass = _classLoader.loadClass(tempAnnotationName);
            List<String> tempAnnotationElementList = ListParser.parseList(ListParser.substringBetweenParentheses(_annotation), COMMA_SEPARATOR, ANNOTATION_BRACKETS);
            if (tempAnnotationElementList.size() > 0) {
                tempDefineList = tempAnnotationElementList.stream().map(item -> {
                    try {
                        return getAnnotationElementDef(tempAnnotationClass, item, _classLoader);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
            } else {
                tempDefineList = NO_ELEMENTS;
            }
        } else {
            tempAnnotationName = _annotation.substring(tempBeginIndex).trim();
            tempAnnotationClass = _classLoader.loadClass(tempAnnotationName);
            tempDefineList = NO_ELEMENTS;
        }

        return new AnnotationDefImpl(tempAnnotationClass, tempDefineList, tempKind);
    }

    /**
     * Get annotation's elements defines.
     *
     * @param _annotationClass
     * @param var1
     * @param _classLoader
     * @return
     * @throws ClassNotFoundException
     */
    protected static AnnotationElementDef getAnnotationElementDef(Class<? extends Annotation> _annotationClass, String var1, ClassLoader _classLoader) throws ClassNotFoundException {
        List<String> tempAnnotationParamterList = ListParser.parseList(var1, "=", ANNOTATION_BRACKETS);
        String tempParameter;
        String tempValue;
        if (tempAnnotationParamterList.size() > 1) {
            tempParameter = tempAnnotationParamterList.get(0).trim();
            tempValue = tempAnnotationParamterList.get(1).trim();
        } else {
            tempParameter = "value";
            tempValue = tempAnnotationParamterList.get(0).trim();
        }

        Method tempMethod;
        try {
            tempMethod = _annotationClass.getMethod(tempParameter);
        } catch (NoSuchMethodException e) {
            throw new ClassNotFoundException("Could not find [" + tempParameter + "] in " + _annotationClass.getName(), e);
        }

        Class tempReturnTypeClass = tempMethod.getReturnType();
        Type tempValueType = Type.getType(tempReturnTypeClass);
        Object tempObjValue;
        if (tempReturnTypeClass.isAnnotation()) {
            tempObjValue = getAnnotationDef(tempValue, _classLoader);
        } else if (tempReturnTypeClass.isPrimitive()) {
            tempValue = ListParser.stripQuotes(tempValue);
            switch(tempValueType.getSort()) {
                case Type.BOOLEAN:
                    tempObjValue = Boolean.valueOf(tempValue);
                    break;
                case Type.CHAR:
                    tempObjValue = tempValue.charAt(0);
                    break;
                case Type.BYTE:
                    tempObjValue = Byte.valueOf(tempValue);
                    break;
                case Type.SHORT:
                    tempObjValue = Short.valueOf(tempValue);
                    break;
                case Type.INT:
                    tempObjValue = Integer.valueOf(tempValue);
                    break;
                case Type.FLOAT:
                    tempObjValue = Float.valueOf(tempValue);
                    break;
                case Type.LONG:
                    tempObjValue = Long.valueOf(tempValue);
                    break;
                case Type.DOUBLE:
                    tempObjValue = Double.valueOf(tempValue);
                    break;
                default:
                    throw new UnsupportedOperationException();
            }
        } else if (String.class.equals(tempReturnTypeClass)) {
            tempObjValue = ListParser.stripQuotes(tempValue);
        } else {
            if (!tempReturnTypeClass.isArray()) {
                throw new UnsupportedOperationException(tempReturnTypeClass.getName());
            }

            List<String> tempValueList = ListParser.parseList(ListParser.substringBetweenCurlyBraces(tempValue), COMMA_SEPARATOR, ANNOTATION_BRACKETS);
            Class tempReturnComponentClass = tempReturnTypeClass.getComponentType();
            int i;
            if (tempReturnComponentClass.isAnnotation()) {
                tempObjValue = Array.newInstance(AnnotationDef.class, tempValueList.size());

                for(i = 0; i < tempValueList.size(); i++) {
                    Array.set(tempObjValue, i, getAnnotationDef(tempValueList.get(i), _classLoader));
                }
            } else {
                String tempValueItem;
                if (tempReturnComponentClass.isPrimitive()) {
                    switch(Type.getType(tempReturnComponentClass).getSort()) {
                        case Type.BOOLEAN:
                            tempObjValue = Array.newInstance(Boolean.TYPE, tempValueList.size());
                            for(i = 0; i < tempValueList.size(); i++) {
                                tempValueItem = ListParser.stripQuotes(tempValueList.get(i));
                                Array.setBoolean(tempObjValue, i, Boolean.valueOf(tempValueItem));
                            }
                            return new AnnotationElementDefImpl(tempValueType, tempParameter, tempObjValue);
                        case Type.CHAR:
                            tempObjValue = Array.newInstance(Character.TYPE, tempValueList.size());
                            for(i = 0; i < tempValueList.size(); i++) {
                                tempValueItem = ListParser.stripQuotes(tempValueList.get(i));
                                Array.setChar(tempObjValue, i, Character.valueOf(tempValueItem.charAt(0)));
                            }
                            return new AnnotationElementDefImpl(tempValueType, tempParameter, tempObjValue);
                        case Type.BYTE:
                            tempObjValue = Array.newInstance(Byte.TYPE, tempValueList.size());
                            for(i = 0; i < tempValueList.size(); i++) {
                                tempValueItem = ListParser.stripQuotes(tempValueList.get(i));
                                Array.setByte(tempObjValue, i, Byte.valueOf(tempValueItem));
                            }
                            return new AnnotationElementDefImpl(tempValueType, tempParameter, tempObjValue);
                        case Type.SHORT:
                            tempObjValue = Array.newInstance(Short.TYPE, tempValueList.size());
                            for(i = 0; i < tempValueList.size(); i++) {
                                tempValueItem = ListParser.stripQuotes(tempValueList.get(i));
                                Array.setShort(tempObjValue, i, Short.valueOf(tempValueItem));
                            }
                            return new AnnotationElementDefImpl(tempValueType, tempParameter, tempObjValue);
                        case Type.INT:
                            tempObjValue = Array.newInstance(Integer.TYPE, tempValueList.size());
                            for(i = 0; i < tempValueList.size(); i++) {
                                tempValueItem = ListParser.stripQuotes(tempValueList.get(i));
                                Array.setInt(tempObjValue, i, Integer.valueOf(tempValueItem));
                            }
                            return new AnnotationElementDefImpl(tempValueType, tempParameter, tempObjValue);
                        case Type.FLOAT:
                            tempObjValue = Array.newInstance(Float.TYPE, tempValueList.size());
                            for(i = 0; i < tempValueList.size(); i++) {
                                tempValueItem = ListParser.stripQuotes(tempValueList.get(i));
                                Array.setFloat(tempObjValue, i, Float.valueOf(tempValueItem));
                            }
                            return new AnnotationElementDefImpl(tempValueType, tempParameter, tempObjValue);
                        case Type.LONG:
                            tempObjValue = Array.newInstance(Long.TYPE, tempValueList.size());
                            for(i = 0; i < tempValueList.size(); i++) {
                                tempValueItem = ListParser.stripQuotes(tempValueList.get(i));
                                Array.setLong(tempObjValue, i, Long.valueOf(tempValueItem));
                            }
                            return new AnnotationElementDefImpl(tempValueType, tempParameter, tempObjValue);
                        case Type.DOUBLE:
                            tempObjValue = Array.newInstance(Double.TYPE, tempValueList.size());
                            for(i = 0; i < tempValueList.size(); i++) {
                                tempValueItem = ListParser.stripQuotes(tempValueList.get(i));
                                Array.setDouble(tempObjValue, i, Double.valueOf(tempValueItem));
                            }
                            return new AnnotationElementDefImpl(tempValueType, tempParameter, tempObjValue);
                        default:
                            throw new UnsupportedOperationException();
                    }
                } else {
                    if (!String.class.equals(tempReturnComponentClass)) {
                        throw new UnsupportedOperationException(tempReturnComponentClass.getName());
                    }

                    tempObjValue = Array.newInstance(String.class, tempValueList.size());
                    for(i = 0; i < tempValueList.size(); i++) {
                        tempValueItem = ListParser.stripQuotes(tempValueList.get(i));
                        Array.set(tempObjValue, i, tempValueItem);
                    }
                }
            }
        }

        return new AnnotationElementDefImpl(tempValueType, tempParameter, tempObjValue);
    }
}

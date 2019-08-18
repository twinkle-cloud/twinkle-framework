package com.twinkle.framework.core.datastruct.builder;

import com.twinkle.framework.core.asm.classloader.EnhancedClassLoader;
import com.twinkle.framework.core.datastruct.Bean;
import com.twinkle.framework.core.datastruct.descriptor.ArrayTypeDescriptor;
import com.twinkle.framework.core.datastruct.descriptor.BeanTypeDescriptor;
import com.twinkle.framework.core.datastruct.descriptor.EnumTypeDescriptor;
import com.twinkle.framework.core.datastruct.descriptor.TypeDescriptor;
import com.twinkle.framework.core.datastruct.schema.*;
import com.twinkle.framework.core.utils.ListParser;
import org.objectweb.asm.Type;

import java.util.List;
import java.util.Map;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-01 22:06<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class TypeDefBuilder {
    private static final String TWINKLE_BEAN_PATH="com.twinkle.framework.core.datastruct.beans.";
    private static final String GENERIC_DELIMITERS = ";,";
    private static final String TYPE_TERMINATOR = ";";
    public static final String ARRAY_DESIGNATOR = "[";
    public static final String OBJECT_DESIGNATOR = "L";

    public TypeDefBuilder() {
    }

    public static TypeDef getTypeDef(TypeDescriptor _descriptor, ClassLoader _classLoader, Map<String, TypeDef> _typeDefineMap) throws ClassNotFoundException {
        TypeDef tempTypeDef;
        if (!_descriptor.isBean() && !(_descriptor instanceof BeanTypeDescriptor)) {
            Class tempClass;
            if (_descriptor instanceof EnumTypeDescriptor) {
                tempTypeDef = (TypeDef)_typeDefineMap.get(_descriptor.getClassName());
                if (tempTypeDef != null) {
                    return tempTypeDef;
                } else {
                    EnumTypeDescriptor tempDescriptor = (EnumTypeDescriptor)_descriptor;
                    tempClass = _classLoader.loadClass(tempDescriptor.getValueClassName());
                    if (!tempDescriptor.getClassName().equals(tempDescriptor.getValueClassName())) {
                        EnumTypeDefImpl tempEnumTypeDef = new EnumTypeDefImpl(tempDescriptor, tempClass);
                        _typeDefineMap.put(_descriptor.getClassName(), tempEnumTypeDef);
                        return tempEnumTypeDef;
                    } else {
                        throw new UnsupportedOperationException("Enumerated constraints not yet supported!");
                    }
                }
            } else if (_descriptor instanceof ArrayTypeDescriptor) {
                ArrayTypeDescriptor tempDescriptor = (ArrayTypeDescriptor)_descriptor;
                if (tempDescriptor.getElementType() instanceof BeanTypeDescriptor) {
                    BeanTypeDescriptor tempBeanDescriptor = (BeanTypeDescriptor)tempDescriptor.getElementType();
                    Object tempObj = _typeDefineMap.get(tempBeanDescriptor.getClassName());
                    if (tempObj == null) {
                        tempObj = new BeanTypeDefImpl(tempBeanDescriptor, _classLoader, _typeDefineMap);
                    }

                    return new BeanArrayTypeDefImpl(tempDescriptor.getName(), BeanArrayTypeDefImpl.getArrayType(((BeanRefTypeDef)tempObj).getType()), (BeanRefTypeDef)tempObj);
                } else {
                    Class tempArrayTypeClass = _classLoader.loadClass(tempDescriptor.getClassName());
                    return new ClassArrayTypeDefImpl(tempDescriptor.getName(), tempArrayTypeClass);
                }
            } else {
                String[] tempGeneric = parseGenericDef(_descriptor.getClassName());
                String tempMainClass = tempGeneric[0];
                if (tempGeneric.length > 1) {
                    tempClass = _classLoader.loadClass(tempMainClass);
                    Type[] tempGenericTypeArray = new Type[tempGeneric.length - 1];

                    for(int i = 1; i < tempGeneric.length; i++) {
                        TypeDef tempTypeDefine = _typeDefineMap.get(tempGeneric[i]);
                        if (tempTypeDefine != null) {
                            tempGenericTypeArray[i - 1] = tempTypeDefine.getType();
                        } else if (tempGeneric[i].startsWith(TWINKLE_BEAN_PATH)) {
                            tempGenericTypeArray[i - 1] = getObjectType(tempGeneric[i]);
                        } else {
                            tempGenericTypeArray[i - 1] = Type.getType(_classLoader.loadClass(tempGeneric[i]));
                        }
                    }

                    return new GenericTypeDefImpl(_descriptor.getName(), tempClass, tempGenericTypeArray);
                } else {
                    Object tempObj;
                    TypeDef tempMainClassTypeDefine;
                    if (tempMainClass.startsWith(TWINKLE_BEAN_PATH)) {
                        Type tempMainClassType = getObjectType(tempMainClass);
                        if (tempMainClassType.getSort() == Type.ARRAY) {
                            Type tempElementType = tempMainClassType.getElementType();
                            tempObj = (BeanRefTypeDef)_typeDefineMap.get(tempElementType.getClassName());
                            if (tempObj == null) {
                                tempObj = new BeanRefTypeDefImpl(tempElementType.getClassName(), tempElementType);
                            }

                            return new BeanArrayTypeDefImpl(_descriptor.getName(), tempMainClassType, (BeanRefTypeDef)tempObj);
                        } else {
                            tempMainClassTypeDefine = _typeDefineMap.get(tempMainClass);
                            return (TypeDef)(tempMainClassTypeDefine != null ? tempMainClassTypeDefine : new BeanRefTypeDefImpl(_descriptor.getName(), tempMainClassType));
                        }
                    } else {
                        tempClass = _classLoader.loadClass(tempMainClass);
                        if (Bean.class.isAssignableFrom(tempClass)) {
                            tempMainClassTypeDefine = (TypeDef)_typeDefineMap.get(_descriptor.getClassName());
                            return (TypeDef)(tempMainClassTypeDefine != null ? tempMainClassTypeDefine : new BeanRefTypeDefImpl(_descriptor.getName(), Type.getType(tempClass)));
                        } else if (tempClass.isArray()) {
                            Class tempClassComponent = tempClass.getComponentType();
                            if (Bean.class.isAssignableFrom(tempClassComponent)) {
                                tempObj = _typeDefineMap.get(tempClassComponent.getName());
                                if (tempObj == null) {
                                    tempObj = new BeanRefTypeDefImpl(tempClassComponent.getName(), Type.getType(tempClassComponent));
                                }

                                return new BeanArrayTypeDefImpl(_descriptor.getName(), Type.getType(tempClass), (BeanRefTypeDef)tempObj);
                            } else {
                                return new ClassArrayTypeDefImpl(_descriptor.getName(), tempClass);
                            }
                        } else {
                            return new ClassTypeDefImpl(_descriptor.getName(), tempClass);
                        }
                    }
                }
            }
        } else {
            tempTypeDef = (TypeDef)_typeDefineMap.get(_descriptor.getClassName());
            if (tempTypeDef != null) {
                return tempTypeDef;
            } else {
                return (TypeDef)(_descriptor instanceof BeanTypeDescriptor ? new BeanTypeDefImpl((BeanTypeDescriptor)_descriptor, _classLoader, _typeDefineMap) : new BeanRefTypeDefImpl(_descriptor.getName(), getObjectType(_descriptor.getClassName())));
            }
        }
    }

    public static Type getObjectType(String var0) {
        String var1 = EnhancedClassLoader.getInternalNormalizedClassName(var0).replace('.', '/');
        return Type.getObjectType(var1);
    }

    /**
     * Parse generic define.
     *
     * @param _className: such as:
     *   Lcom/twinkle/cloud/common/data/GeneralContentResult<Ljava/lang/String;>;
     *
     * @return
     */
    public static String[] parseGenericDef(String _className) {
        int tempBeginIndex = _className.indexOf("<");
        List<String> tempResultList;
        if (tempBeginIndex > 0) {
            String tempMainClassName = _className.substring(0, tempBeginIndex).trim();
            if (!tempMainClassName.startsWith(OBJECT_DESIGNATOR) && !tempMainClassName.startsWith(ARRAY_DESIGNATOR)) {
                tempResultList = ListParser.parseList(ListParser.substringBetweenAngleBrackets(_className), GENERIC_DELIMITERS, false);
                tempResultList.add(0, tempMainClassName);
            } else {
                tempResultList = ListParser.parseList(ListParser.substringBetweenAngleBrackets(_className), TYPE_TERMINATOR, true);
                if (!tempMainClassName.endsWith(TYPE_TERMINATOR)) {
                    tempResultList.add(0, tempMainClassName + TYPE_TERMINATOR);
                } else {
                    tempResultList.add(0, tempMainClassName);
                }
            }
        } else if (!_className.startsWith(OBJECT_DESIGNATOR) && !_className.startsWith(ARRAY_DESIGNATOR)) {
            tempResultList = ListParser.parseList(_className, GENERIC_DELIMITERS, false);
        } else {
            tempResultList = ListParser.parseList(_className, TYPE_TERMINATOR, true);
        }

        return tempResultList.toArray(new String[tempResultList.size()]);
    }
}

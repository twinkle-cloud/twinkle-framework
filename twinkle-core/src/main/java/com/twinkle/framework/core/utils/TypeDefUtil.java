package com.twinkle.framework.core.utils;

import com.twinkle.framework.core.datastruct.builder.AnnotationDefBuilder;
import com.twinkle.framework.core.datastruct.builder.TypeDefBuilder;
import com.twinkle.framework.core.datastruct.descriptor.AttributeDescriptor;
import com.twinkle.framework.core.datastruct.descriptor.BeanTypeDescriptor;
import com.twinkle.framework.core.datastruct.descriptor.TypeDescriptor;
import com.twinkle.framework.core.datastruct.define.AnnotationDef;
import com.twinkle.framework.core.datastruct.define.AttributeDef;
import com.twinkle.framework.core.datastruct.define.AttributeDefImpl;
import com.twinkle.framework.core.datastruct.define.TypeDef;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-05 15:35<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class TypeDefUtil {
    protected static final String FLAG_SUFFIX = "Flag";
    protected static final String DEFAULT_SUFFIX = "Default";

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
        return TypeDefUtil.getFieldName(_attrName) + FLAG_SUFFIX;
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
        if(CollectionUtils.isEmpty(_annotations)){
            return Collections.EMPTY_LIST;
        }
        List<AnnotationDef> tempResultList = new ArrayList<>(_annotations.size());
        for(String tempItem : _annotations) {
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
        if(CollectionUtils.isEmpty(_descriptors)){
            return Collections.EMPTY_LIST;
        }
        List<AttributeDef> tempResultList = new ArrayList<>(_descriptors.size());
        for(AttributeDescriptor tempItem : _descriptors) {
            List<AnnotationDef> tempAnnotationDefineList = TypeDefUtil.getAnnotations(tempItem.getAnnotations(), _classLoader);
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
        if(CollectionUtils.isEmpty(_descriptors)){
            return Collections.EMPTY_LIST;
        }
        List<TypeDef> tempResultList = new ArrayList<>(_descriptors.size());
        for(BeanTypeDescriptor tempItem : _descriptors) {
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
        if(CollectionUtils.isEmpty(_descriptors)){
            return Collections.EMPTY_LIST;
        }
        List<TypeDef> tempResultList = new ArrayList<>(_descriptors.size());
        for(TypeDescriptor tempItem : _descriptors) {
            tempResultList.add(TypeDefBuilder.getTypeDef(tempItem, _classLoader, _typeDefineMap));
        }
        return tempResultList;
    }
}

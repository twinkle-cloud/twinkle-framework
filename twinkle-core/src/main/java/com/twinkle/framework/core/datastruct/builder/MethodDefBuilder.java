package com.twinkle.framework.core.datastruct.builder;

import com.twinkle.framework.core.datastruct.descriptor.MethodTypeDescriptor;
import com.twinkle.framework.core.datastruct.schema.MethodDef;
import com.twinkle.framework.core.datastruct.schema.MethodDefImpl;
import com.twinkle.framework.core.datastruct.schema.TypeDef;
import com.twinkle.framework.core.utils.TypeDefUtil;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-13 17:54<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class MethodDefBuilder {
    /**
     * Build a method def.
     *
     * @param _descriptor
     * @param _classLoader
     * @param _typeDefMap
     * @return
     * @throws ClassNotFoundException
     */
    public static MethodDef getMethodDef(MethodTypeDescriptor _descriptor, ClassLoader _classLoader, Map<String, TypeDef> _typeDefMap) throws ClassNotFoundException {
        MethodDef tempMethodDef = MethodDefImpl.builder()
                .access(_descriptor.getAccess())
                .name(_descriptor.getName())
                .annotations(TypeDefUtil.getAnnotations(_descriptor.getAnnotations(), _classLoader))
                .parameterAttrs(TypeDefUtil.getAttributes(_descriptor.getParameterAttrs(), _classLoader, _typeDefMap))
                .localParameterAttrs(TypeDefUtil.getAttributes(_descriptor.getLocalParameterAttrs(), _classLoader, _typeDefMap))
                .returnType(TypeDefBuilder.getTypeDef(_descriptor.getReturnType(), _classLoader, _typeDefMap))
                .exceptions(TypeDefUtil.getExceptions(_descriptor.getExceptions(), _classLoader, _typeDefMap))
                .instructionHandler(_descriptor.getInstructionHandler())
                .extraInfo(_descriptor.getExtraInfo())
                .build();
        return tempMethodDef;
    }

    /**
     * Build the method def list.
     *
     * @param _descriptors
     * @param _classLoader
     * @param _typeDefMap
     * @return
     * @throws ClassNotFoundException
     */
    public static List<MethodDef> getMethodDefs(List<MethodTypeDescriptor> _descriptors, ClassLoader _classLoader, Map<String, TypeDef> _typeDefMap) throws ClassNotFoundException {
        if(CollectionUtils.isEmpty(_descriptors)) {
            return Collections.EMPTY_LIST;
        }
        List<MethodDef> tempResultList = new ArrayList<>(_descriptors.size());
        for(MethodTypeDescriptor tempItem : _descriptors) {
            tempResultList.add(getMethodDef(tempItem, _classLoader, _typeDefMap));
        }
        return tempResultList;
    }
}

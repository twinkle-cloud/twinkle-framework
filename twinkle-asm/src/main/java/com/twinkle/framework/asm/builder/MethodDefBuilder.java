package com.twinkle.framework.asm.builder;

import com.twinkle.framework.asm.descriptor.MethodTypeDescriptor;
import com.twinkle.framework.asm.define.MethodDef;
import com.twinkle.framework.asm.define.MethodDefImpl;
import com.twinkle.framework.asm.define.TypeDef;
import com.twinkle.framework.asm.utils.ClassDesignerUtil;
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
                .annotations(ClassDesignerUtil.getAnnotations(_descriptor.getAnnotations(), _classLoader))
                .parameterAttrs(ClassDesignerUtil.getAttributes(_descriptor.getParameterAttrs(), _classLoader, _typeDefMap))
                .localParameterAttrs(ClassDesignerUtil.getAttributes(_descriptor.getLocalParameterAttrs(), _classLoader, _typeDefMap))
                .returnType(TypeDefBuilder.getTypeDef(_descriptor.getReturnType(), _classLoader, _typeDefMap))
                .exceptions(ClassDesignerUtil.getExceptions(_descriptor.getExceptions(), _classLoader, _typeDefMap))
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

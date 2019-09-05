package com.twinkle.framework.core.datastruct.builder;

import com.twinkle.framework.core.datastruct.descriptor.AttributeDescriptor;
import com.twinkle.framework.core.datastruct.descriptor.AttributeDescriptorImpl;
import com.twinkle.framework.core.datastruct.descriptor.TypeDescriptor;
import com.twinkle.framework.core.datastruct.descriptor.TypeDescriptorImpl;
import com.twinkle.framework.core.lang.AttributeInfo;
import org.objectweb.asm.Opcodes;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-16 22:34<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class AttributeDescriptorBuilder {

    public static AttributeDescriptor getMethodLocalParameter(AttributeInfo _attr) {
        AttributeDescriptor tempAttr = AttributeDescriptorImpl.builder()
                .type(getAttrTypeDescriptor(_attr))
                .name(_attr.getName())
                .annotations(Collections.emptySet())
                .build();
        return tempAttr;
    }

    public static AttributeDescriptor getRequestParamMethodParameter(AttributeInfo _attr) {
        Set<String> tempAnnotationList = new HashSet<>();
        tempAnnotationList.add("@io.swagger.annotations.ApiParam(value = \""+_attr.getName()+"\")");
        tempAnnotationList.add("@org.springframework.web.bind.annotation.RequestParam");

        AttributeDescriptor tempAttr = AttributeDescriptorImpl.builder()
                .type(getAttrTypeDescriptor(_attr))
                .name(_attr.getName())
                .access(Opcodes.ACC_FINAL)
                .annotations(tempAnnotationList)
                .build();
        return tempAttr;
    }

    public static AttributeDescriptor getPathVarMethodParameter(AttributeInfo _attr) {
        Set<String> tempAnnotationList = new HashSet<>();
        tempAnnotationList.add("@io.swagger.annotations.ApiParam(value = \""+_attr.getName()+"\")");
        tempAnnotationList.add("@org.springframework.web.bind.annotation.PathVariable(value = \"_userName\")");

        AttributeDescriptor tempAttr = AttributeDescriptorImpl.builder()
                .type(getAttrTypeDescriptor(_attr))
                .name(_attr.getName())
                .access(Opcodes.ACC_FINAL)
                .annotations(tempAnnotationList)
                .build();
        return tempAttr;
    }

    public static AttributeDescriptor getRequestBodyMethodParameter(AttributeInfo _attr) {
        Set<String> tempAnnotationList = new HashSet<>();
        tempAnnotationList.add("@io.swagger.annotations.ApiParam(value = \""+_attr.getName()+"\")");
        tempAnnotationList.add("@org.springframework.web.bind.annotation.RequestBody");

        AttributeDescriptor tempAttr = AttributeDescriptorImpl.builder()
                .type(getAttrTypeDescriptor(_attr))
                .name(_attr.getName())
                .access(Opcodes.ACC_FINAL)
                .annotations(tempAnnotationList)
                .build();
        return tempAttr;
    }

    /**
     * Get attribute's Type descriptor with given attribute info.
     *
     * @param _attr
     * @return
     */
    private static TypeDescriptor getAttrTypeDescriptor(AttributeInfo _attr){
        TypeDescriptor tempDescriptor = TypeDescriptorImpl.builder()
                .className(_attr.getClassName())
                .description(_attr.getDescription())
                .name(_attr.getClass().getName())
                .build();
        return tempDescriptor;
    }
}

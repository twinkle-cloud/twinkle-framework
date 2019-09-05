package com.twinkle.framework.core.datastruct.descriptor;

import com.alibaba.fastjson.JSONObject;
import com.twinkle.framework.core.datastruct.handler.MethodInstructionHandler;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-13 13:46<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Data
@Builder
public class MethodTypeDescriptorImpl implements MethodTypeDescriptor {
    /**
     * Method's access.
     *
     */
    private final int access;
    /**
     * Method's name.
     */
    private final String name;
    /**
     * Method's return type.
     */
    private final TypeDescriptor returnType;
    /**
     * Method's annotations.
     */
    private Set<String> annotations;
    /**
     * Method's parameters.
     */
    private List<AttributeDescriptor> parameterAttrs;
    /**
     * Method's local parameters.
     *
     */
    private List<AttributeDescriptor> localParameterAttrs;
    /**
     * Method's exceptions.
     */
    private List<TypeDescriptor> exceptions;
    /**
     * The name of the designer's method that will be used to pack the
     * instructions of this method.
     */
    private final MethodInstructionHandler instructionHandler;

    /**
     * ExtraInfo for this attribute.
     */
    private JSONObject extraInfo;

    /**
     * Add annotation to this method.
     *
     * @param _annotation
     */
    public void addAnnotations(String _annotation) {
        if(this.annotations == null) {
            this.annotations = new HashSet<>(4);
        }
        this.annotations.add(_annotation);
    }

    /**
     * Add parameter to this method.
     *
     * @param _attr
     */
    public void addParameter(AttributeDescriptor _attr) {
        if(this.parameterAttrs == null) {
            this.parameterAttrs = new ArrayList<>(6);
        }
        this.parameterAttrs.add(_attr);
    }

    /**
     * Add local parameter to this method.
     *
     * @param _attr
     */
    public void addLocalParameter(AttributeDescriptor _attr) {
        if(this.localParameterAttrs == null) {
            this.localParameterAttrs = new ArrayList<>(6);
        }
        this.localParameterAttrs.add(_attr);
    }

    /**
     * Add throws exception declare to this method.
     *
     * @param _exception
     */
    public void addException(TypeDescriptor _exception){
        if(this.exceptions == null) {
            this.exceptions = new ArrayList<>(2);
        }
        this.exceptions.add(_exception);
    }

    @Override
    public Object getExtraInfoByKey(String _key) {
        if (this.extraInfo == null)
            return null;
        return this.extraInfo.get(_key);
    }
}

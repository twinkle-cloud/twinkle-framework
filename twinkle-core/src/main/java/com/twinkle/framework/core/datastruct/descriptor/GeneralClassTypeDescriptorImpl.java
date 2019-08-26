package com.twinkle.framework.core.datastruct.descriptor;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-13 14:34<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Data
public class GeneralClassTypeDescriptorImpl extends BeanTypeDescriptorImpl implements GeneralClassTypeDescriptor {
    /**
     * Class's methods.
     */
    private List<MethodTypeDescriptor> methods;

    public GeneralClassTypeDescriptorImpl(String _className, String _name, String _description, List<AttributeDescriptor> _attributes, Set<String> _annotations, List<MethodTypeDescriptor> _methods) {
        super(_className, _name, _description, _attributes, _annotations);
        this.methods = methods;
    }

    public GeneralClassTypeDescriptorImpl(String _className, String _name, String _description, List<AttributeDescriptor> _attributes, Set<BeanTypeDescriptor> _interfaces, Set<String> _annotations, List<MethodTypeDescriptor> _methods) {
        super(_className, _name, _description, _attributes, _interfaces, _annotations);
        this.methods = methods;
    }

    public GeneralClassTypeDescriptorImpl(String _className, String _name, String _description, List<AttributeDescriptor> _attributes, TypeDescriptor _superClass, Set<BeanTypeDescriptor> _interfaces, Set<String> _annotations, List<MethodTypeDescriptor> methods) {
        super(_className, _name, _description, _attributes, _superClass, _interfaces, _annotations);
        this.methods = methods;
    }

    /**
     * Add method into the method list.
     *
     * @param _method
     */
    public void addMethod(MethodTypeDescriptor _method){
        if(this.methods == null){
            this.methods = new ArrayList<>(8);
        }
        this.methods.add(_method);
    }
}

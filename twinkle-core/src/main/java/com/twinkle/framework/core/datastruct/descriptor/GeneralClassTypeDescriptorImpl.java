package com.twinkle.framework.core.datastruct.descriptor;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
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
@Builder
public class GeneralClassTypeDescriptorImpl extends BeanTypeDescriptorImpl implements GeneralClassTypeDescriptor {
    /**
     * Class's methods.
     */
    private List<MethodTypeDescriptor> methods;

    public GeneralClassTypeDescriptorImpl(String className, String name, String description, List<AttributeDescriptor> attributes, List<String> parentNames, Set<BeanTypeDescriptor> parents, Set<String> annotations, List<BeanTypeDescriptor> interfaces) {
        super(className, name, description, attributes, parentNames, parents, annotations, interfaces);
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

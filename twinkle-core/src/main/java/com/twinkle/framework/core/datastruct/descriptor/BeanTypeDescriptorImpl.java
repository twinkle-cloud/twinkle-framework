package com.twinkle.framework.core.datastruct.descriptor;

import lombok.Builder;
import lombok.Data;

import javax.xml.namespace.QName;
import java.util.*;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-09 14:36<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Data
@Builder
public class BeanTypeDescriptorImpl implements BeanTypeDescriptor {
    private List<AttributeDescriptor> attributes;
    private final String className;
    private final String description;
    private final String name;
    private final List<String> parentNames;
    private final Set<BeanTypeDescriptor> parents;
    private Set<String> annotations;
    private final List<BeanTypeDescriptor> interfaces;

    private BeanTypeDescriptorImpl(String _className, String _name, String _description, List<AttributeDescriptor> _attributes, List<String> _parentNames, QName var7, Set<String> _annotations, List<BeanTypeDescriptor> _interfaces) {
        this.className = _className;
        this.name = _name;
        this.description = _description;
        this.attributes = _attributes;
        this.parentNames = _parentNames;
        this.annotations = _annotations;
        this.parents = new HashSet(_interfaces.size() + this.parentNames.size());
        this.interfaces = _interfaces;
        this.parents.addAll(_interfaces);
    }
    @Override
    public AttributeDescriptor getAttribute(String _attrName) {
        Optional<AttributeDescriptor> tempResult = this.attributes.stream().parallel().filter(item -> item.getName().equals(_attrName)).findFirst();
        if(tempResult.isPresent()) {
            return tempResult.get();
        }
        return null;
    }

    @Override
    public boolean isBean() {
        return true;
    }
    @Override
    public boolean isPrimitive() {
        return false;
    }

    public void addParents(Collection<BeanTypeDescriptor> _parentDescriptors) {
        this.parents.addAll(_parentDescriptors);
    }
    @Override
    public String toString() {
        StringBuilder var1 = new StringBuilder();
        var1.append("BeanTypeDescriptorImpl [\n_typeName=").append(this.name).append(", \n_className=").append(this.className).append(", \n_attributes=").append(this.attributes).append(", \n_parents=").append(this.parentNames).append(", \n_description=").append(this.description).append("\n]");
        return var1.toString();
    }
}

package com.twinkle.framework.core.datastruct.descriptor;

import lombok.Builder;
import lombok.Data;

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
    private final String className;
    private final String name;
    private final String description;
    private List<AttributeDescriptor> attributes;
    private final List<String> parentNames;
    private final Set<BeanTypeDescriptor> parents;
    private Set<String> annotations;
    private final List<BeanTypeDescriptor> interfaces;

    public BeanTypeDescriptorImpl(String _className, String _name, String _description, List<AttributeDescriptor> _attributes, Set<String> _annotations) {
        this(_className, _name, _description, _attributes, Collections.EMPTY_LIST,
                new HashSet(4), _annotations,
                Collections.EMPTY_LIST
        );
    }

    public BeanTypeDescriptorImpl(String _className, String _name, String _description, List<AttributeDescriptor> _attributes, Set<String> _annotations, List<BeanTypeDescriptor> _interfaces) {
        this(_className, _name, _description, _attributes, Collections.EMPTY_LIST,
                new HashSet(_interfaces.size()), _annotations,
                _interfaces
        );
    }

    public BeanTypeDescriptorImpl(String _className, String _name, String _description, List<AttributeDescriptor> _attributes, List<String> _parentNames, Set<String> _annotations, List<BeanTypeDescriptor> _interfaces) {
        this(_className, _name, _description, _attributes, _parentNames,
                new HashSet(_interfaces.size() + _parentNames.size()), _annotations,
                _interfaces
        );
    }

    private BeanTypeDescriptorImpl(String _className, String _name, String _description, List<AttributeDescriptor> _attributes, List<String> _parentNames, Set<BeanTypeDescriptor> _parents, Set<String> _annotations, List<BeanTypeDescriptor> _interfaces) {
        this.className = _className;
        this.name = _name;
        this.description = _description;
        this.attributes = _attributes;
        this.parentNames = _parentNames;
        this.annotations = _annotations;
        this.parents = _parents == null? Collections.EMPTY_SET : _parents;
        this.interfaces = _interfaces == null ? Collections.EMPTY_LIST : _interfaces;
        this.parents.addAll(_interfaces);
    }

    @Override
    public AttributeDescriptor getAttribute(String _attrName) {
        Optional<AttributeDescriptor> tempResult = this.attributes.stream().parallel().filter(item -> item.getName().equals(_attrName)).findFirst();
        if (tempResult.isPresent()) {
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
        StringBuilder tempBuilder = new StringBuilder();
        tempBuilder.append("BeanTypeDescriptorImpl [\n_typeName=").append(this.name).append(", \n_className=").append(this.className).append(", \n_attributes=").append(this.attributes).append(", \n_parents=").append(this.parentNames).append(", \n_description=").append(this.description).append("\n]");
        return tempBuilder.toString();
    }
}

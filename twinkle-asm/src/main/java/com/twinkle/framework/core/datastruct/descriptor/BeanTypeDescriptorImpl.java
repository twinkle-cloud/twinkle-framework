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
    private final TypeDescriptor superDescriptor;
    private final Set<BeanTypeDescriptor> interfaceDescriptors;
    private Set<String> annotations;

    public BeanTypeDescriptorImpl(String _className, String _name, String _description, List<AttributeDescriptor> _attributes, Set<String> _annotations) {
        this(_className, _name, _description, _attributes, new HashSet(4), _annotations);
    }

    public BeanTypeDescriptorImpl(String _className, String _name, String _description, List<AttributeDescriptor> _attributes, Set<BeanTypeDescriptor> _interfaces, Set<String> _annotations) {
        this(_className, _name, _description, _attributes, new TypeDescriptorImpl(Object.class),
                _interfaces, _annotations
        );
    }

    public BeanTypeDescriptorImpl(String _className, String _name, String _description, List<AttributeDescriptor> _attributes, TypeDescriptor _superClass, Set<BeanTypeDescriptor> _interfaces, Set<String> _annotations) {
        this.className = _className;
        this.name = _name;
        this.description = _description;
        this.attributes = _attributes;
        this.superDescriptor = _superClass;
        this.annotations = _annotations;
        this.interfaceDescriptors = _interfaces == null ? new HashSet<>(2) : _interfaces;
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

    public void addInterfaceDescriptors(Collection<BeanTypeDescriptor> _parentDescriptors) {
        this.interfaceDescriptors.addAll(_parentDescriptors);
    }

    @Override
    public String toString() {
        StringBuilder tempBuilder = new StringBuilder();
        tempBuilder.append("BeanTypeDescriptorImpl [\n_typeName=").append(this.name).append(", \n_className=").append(this.className).append(", \n_attributes=").append(this.attributes).append(", \n_interfaces=").append(this.getInterfaceDescriptors()).append(", \n_description=").append(this.description).append("\n]");
        return tempBuilder.toString();
    }
}

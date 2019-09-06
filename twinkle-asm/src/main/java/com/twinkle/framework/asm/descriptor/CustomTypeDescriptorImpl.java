package com.twinkle.framework.asm.descriptor;

import java.util.Set;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-09 14:27<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class CustomTypeDescriptorImpl implements TypeDescriptor {
    private final TypeDescriptor typeDescriptor;
    private final String className;

    public CustomTypeDescriptorImpl(TypeDescriptor _typeDescriptor, String _className) {
        this.typeDescriptor = _typeDescriptor;
        this.className = _className;
    }
    @Override
    public String getClassName() {
        return this.className;
    }
    @Override
    public Set<String> getAnnotations() {
        return this.typeDescriptor.getAnnotations();
    }
    @Override
    public String getDescription() {
        return this.typeDescriptor.getDescription();
    }
    @Override
    public String getName() {
        return this.typeDescriptor.getName();
    }
    @Override
    public boolean isBean() {
        return this.typeDescriptor.isBean();
    }
    @Override
    public boolean isPrimitive() {
        return this.typeDescriptor.isPrimitive();
    }
    @Override
    public String toString() {
        StringBuilder tempBuilder = new StringBuilder();
        tempBuilder.append("CustomTypeDescriptorImpl [\n_className=").append(this.className).append(", \n_typeDescriptor=").append(this.typeDescriptor).append("\n]");
        return tempBuilder.toString();
    }
}

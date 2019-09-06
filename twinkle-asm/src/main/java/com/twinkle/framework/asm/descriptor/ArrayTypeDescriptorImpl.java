package com.twinkle.framework.asm.descriptor;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-09 14:05<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Data
@Builder
public class ArrayTypeDescriptorImpl implements ArrayTypeDescriptor {
    public static final String ARRAY_CLASS_SUFFIX = "[]";
    private final Set<String> annotations;
    private final String description;
    private final String name;
    private TypeDescriptor elementType;
    private final String className;

    private ArrayTypeDescriptorImpl(Set<String> _annotations, String _description, String _name, TypeDescriptor _elementType, String _className) {
        this.annotations = _annotations;
        this.description = _description;
        this.name = _name;
        this.elementType = _elementType;
        this.className = _className;
    }
    @Override
    public String getClassName() {
        if (this.className != null) {
            return this.className;
        } else {
            return this.elementType != null ? this.elementType.getClassName() + ARRAY_CLASS_SUFFIX : null;
        }
    }

    @Override
    public boolean isBean() {
        return false;
    }
    @Override
    public boolean isPrimitive() {
        return false;
    }

    @Override
    public String toString() {
        StringBuilder tempBuilder = new StringBuilder();
        tempBuilder.append("ArrayTypeDescriptorImpl [\n_name=").append(this.name).append(", \n_javaClassName=").append(this.className).append(", \n_annotations=").append(this.annotations).append(", \n_description=").append(this.description).append("\n]");
        return tempBuilder.toString();
    }
}

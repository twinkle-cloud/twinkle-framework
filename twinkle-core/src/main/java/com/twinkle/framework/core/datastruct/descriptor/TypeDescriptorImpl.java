package com.twinkle.framework.core.datastruct.descriptor;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-09 15:18<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Data
@Builder
public class TypeDescriptorImpl implements TypeDescriptor {
    private final String className;
    private final String description;
    private final String name;

    private TypeDescriptorImpl(String _className, String _description, String _name) {
        this.className = _className;
        this.description = _description;
        this.name = _name;
    }

    @Override
    public boolean isBean() {
        return false;
    }

    @Override
    public boolean isPrimitive() {
        return true;
    }

    @Override
    public Set<String> getAnnotations() {
        throw new UnsupportedOperationException("TypeDescriptor.getAnnotations not implemented");
    }
    @Override
    public String toString() {
        StringBuilder tempBuilder = new StringBuilder();
        tempBuilder.append("TypeDescriptorImpl [\n_name=").append(this.name)
                .append(", \n_className=").append(this.className)
                .append(", \n_description=").append(this.description)
                .append("\n]");
        return tempBuilder.toString();
    }
}

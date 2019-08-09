package com.twinkle.framework.core.datastruct.descriptor;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-09 14:18<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Data
@Builder
public class AttributeDescriptorImpl implements AttributeDescriptor {
    private final String name;
    private final Object defaultValue;
    private final boolean isRequired;
    private final boolean isReadOnly;
    private final String elementClassName;
    private final Set<String> annotations;
    private TypeDescriptor type;
    private final BeanTypeDescriptor owner;

    private AttributeDescriptorImpl(String _name, Object _defaultValue, boolean _isRequired, boolean _isReadOnly, String _className, Set<String> _annotations, TypeDescriptor _type, BeanTypeDescriptor _woner) {
        this.name = _name;
        this.defaultValue = _defaultValue;
        this.isRequired = _isRequired;
        this.isReadOnly = _isReadOnly;
        this.elementClassName = _className;
        this.annotations = _annotations;
        this.type = _type;
        this.owner = _woner;
    }

    /**
     * Update the type.
     *
     * @param _type
     */
    public void setType(TypeDescriptor _type) {
        if (this.elementClassName != null && !this.elementClassName.equals(_type.getClassName())) {
            this.type = new CustomTypeDescriptorImpl(_type, this.elementClassName);
        } else {
            this.type = _type;
        }
    }

    @Override
    public String toString() {
        StringBuilder var1 = new StringBuilder();
        var1.append("AttributeDescriptorImpl [\n_name=").append(this.name).append(", \n_elementJavaClassName=").append(this.elementClassName).append(", \n_annotations=").append(this.annotations).append(", \n_defaultValue=").append(this.defaultValue).append(", \n_isReadOnly=").append(this.isReadOnly).append(", \n_isRequired=").append(this.isRequired).append("\n]");
        return var1.toString();
    }
}

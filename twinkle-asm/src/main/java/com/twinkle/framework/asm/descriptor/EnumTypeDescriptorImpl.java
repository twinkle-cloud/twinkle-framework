package com.twinkle.framework.asm.descriptor;

import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.Set;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-09 15:10<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Data
@Builder
public class EnumTypeDescriptorImpl implements EnumTypeDescriptor {
    private final String className;
    private final String name;
    private final Map<String, Object> enumerationValues;
    private EnumHandler enumHandler;
    private final String valueClassName;

    private EnumTypeDescriptorImpl(String _className, String _name, Map<String, Object> _enumerationValues, EnumHandler _enumHandler, String _valueClassName) {
        this.className = _className;
        this.name = _name;
        this.enumerationValues = _enumerationValues;
        this.enumHandler = _enumHandler;
        this.valueClassName = _valueClassName;
    }
    @Override
    public String getDescription() {
        return "";
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
    public Set<String> getAnnotations() {
        return null;
    }
    @Override
    public String toString() {
        StringBuilder var1 = new StringBuilder();
        var1.append("EnumTypeDescriptorImpl [\n_typeName=").append(this.name).append(", \n_className=").append(this.className).append(", \n_enumerations=").append(this.enumerationValues).append(", \n_enumHandler=").append(this.enumHandler).append("\n]");
        return var1.toString();
    }
}

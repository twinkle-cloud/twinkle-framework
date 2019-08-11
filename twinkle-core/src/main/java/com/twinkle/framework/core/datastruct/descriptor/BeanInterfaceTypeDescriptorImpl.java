package com.twinkle.framework.core.datastruct.descriptor;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-09 14:42<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class BeanInterfaceTypeDescriptorImpl implements BeanTypeDescriptor {
    private final String interfaceName;

    public BeanInterfaceTypeDescriptorImpl(String _interfaceName) {
        this.interfaceName = _interfaceName;
    }
    @Override
    public boolean isPrimitive() {
        return false;
    }
    @Override
    public boolean isBean() {
        return true;
    }
    @Override
    public String getName() {
        return null;
    }
    @Override
    public String getDescription() {
        return String.valueOf("It's descriptor for external java interface: " + this.interfaceName);
    }
    @Override
    public String getClassName() {
        return this.interfaceName;
    }
    @Override
    public Set<String> getAnnotations() {
        return Collections.emptySet();
    }
    @Override
    public Set<BeanTypeDescriptor> getParents() {
        return Collections.emptySet();
    }
    @Override
    public List<AttributeDescriptor> getAttributes() {
        return Collections.emptyList();
    }
    @Override
    public AttributeDescriptor getAttribute(String _attrName) {
        return null;
    }

}

package com.twinkle.framework.core.datastruct.descriptor;

import java.util.List;
import java.util.Set;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-01 18:19<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface BeanTypeDescriptor extends TypeDescriptor {
    Set<BeanTypeDescriptor> getParents();

    List<AttributeDescriptor> getAttributes();

    AttributeDescriptor getAttribute(String _attrName);
}

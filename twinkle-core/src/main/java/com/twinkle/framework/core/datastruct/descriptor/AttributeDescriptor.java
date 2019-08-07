package com.twinkle.framework.core.datastruct.descriptor;

import java.util.Set;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-01 18:18<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface AttributeDescriptor {

    String getName();

    TypeDescriptor getType();

    BeanTypeDescriptor getOwner();

    Set<String> getAnnotations();

    boolean isRequired();

    boolean isReadOnly();

    Object getDefaultValue();
}

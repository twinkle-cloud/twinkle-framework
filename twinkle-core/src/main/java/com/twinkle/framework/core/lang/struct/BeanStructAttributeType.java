package com.twinkle.framework.core.lang.struct;

import com.twinkle.framework.core.datastruct.descriptor.BeanTypeDescriptor;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/30/19 10:13 AM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface BeanStructAttributeType extends StructAttributeType {
    /**
     * Get the bean type descriptor for this type.
     *
     * @return
     */
    BeanTypeDescriptor getTypeDescriptor();
}

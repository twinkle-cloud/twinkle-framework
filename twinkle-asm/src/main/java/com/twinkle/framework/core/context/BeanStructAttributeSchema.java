package com.twinkle.framework.core.context;

import com.twinkle.framework.core.datastruct.descriptor.TypeDescriptors;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/31/19 2:13 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface BeanStructAttributeSchema extends StructAttributeSchema {
    /**
     * Get the struct attribute's type descriptors.
     *
     * @return
     */
    TypeDescriptors getTypeDescriptors();
}

package com.twinkle.framework.struct.type;

import com.twinkle.framework.asm.descriptor.BeanTypeDescriptor;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/30/19 10:13 AM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface BeanStructType extends StructType {
    /**
     * Get the bean type descriptor for this type.
     *
     * @return
     */
    BeanTypeDescriptor getTypeDescriptor();
}

package com.twinkle.framework.struct.ref;

import com.twinkle.framework.struct.asm.descriptor.SAAttributeDescriptor;
import com.twinkle.framework.struct.type.AttributeType;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/29/19 4:57 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface AttributeRef {
    /**
     * Get the ref's name.
     *
     * @return
     */
    String getName();

    /**
     * Get the referred type.
     *
     * @return
     */
    AttributeType getType();

    /**
     * get the referred attribute's descriptor.
     *
     * @return
     */
    SAAttributeDescriptor getDescriptor();

    /**
     * Judge the ref is composite or not?
     *
     * @return
     */
    boolean isComposite();

    /**
     * Judge the referred attribute is array or not?
     *
     * @return
     */
    boolean isArray();
}

package com.twinkle.framework.core.lang.ref;

import com.twinkle.framework.core.datastruct.descriptor.SAAttributeDescriptor;
import com.twinkle.framework.core.lang.struct.StructType;

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
    StructType getType();

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
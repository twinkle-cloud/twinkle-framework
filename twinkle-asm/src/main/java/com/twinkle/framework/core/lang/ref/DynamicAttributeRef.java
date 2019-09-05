package com.twinkle.framework.core.lang.ref;

import com.twinkle.framework.core.datastruct.descriptor.SAAttributeDescriptor;
import com.twinkle.framework.core.lang.struct.StructAttribute;
import com.twinkle.framework.core.lang.struct.StructType;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/30/19 6:11 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface DynamicAttributeRef {
    /**
     * Get the attribute ref name.
     *
     * @return
     */
    String getName();

    /**
     * Get the attribute ref type.
     *
     * @return
     */
    StructType getType();

    /**
     * Get the referred attribute's descriptor.
     *
     * @return
     */
    SAAttributeDescriptor getDescriptor();

    /**
     * Get the concrete ref with the given index.
     *
     * @param _index
     * @return
     */
    AttributeRef getConcreteRef(int... _index);

    /**
     * Get concrete ref in the given attribute with given indexes.
     *
     * @param _attr
     * @param _indexes
     * @return
     */
    AttributeRef getConcreteRef(StructAttribute _attr, int... _indexes);
}

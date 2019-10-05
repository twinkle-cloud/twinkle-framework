package com.twinkle.framework.struct.ref;

import com.twinkle.framework.struct.asm.descriptor.SAAttributeDescriptor;
import com.twinkle.framework.struct.type.AttributeType;
import com.twinkle.framework.struct.type.StructAttribute;

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
    AttributeType getType();

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

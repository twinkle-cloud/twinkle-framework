package com.twinkle.framework.struct.ref;

import com.twinkle.framework.struct.type.ArrayType;
import com.twinkle.framework.struct.type.StructAttribute;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/30/19 6:00 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface ArrayAttributeRef extends AttributeRef {
    /**
     * Get array attribute name.
     *
     * @return
     */
    String getArrayAttributeName();

    /**
     * Get the array type.
     *
     * @return
     */
    ArrayType getArrayType();

    /**
     * Get the array attribute's index in the father struct attribute.
     *
     * @return
     */
    int getIndex();

    /**
     * Duplicate the attribute ref of _index ref.
     *
     * @param _index
     * @return
     */
    ArrayAttributeRef replicate(int _index);

    /**
     * To ensure the attribute's size.
     * The _attr should be an array.
     *
     * @param _attr
     */
    void ensureSize(StructAttribute _attr);
}

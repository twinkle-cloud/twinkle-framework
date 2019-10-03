package com.twinkle.framework.struct.asm.descriptor;

import com.twinkle.framework.core.type.AttributeType;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/30/19 8:51 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface SAAttributeDescriptor {
    /**
     * Struct attribute name.
     * @return
     */
    String getName();

    /**
     * Get the struct attribute type.
     *
     * @return
     */
    AttributeType getType();

    /**
     * Get the struct attribute type's name.
     *
     * @return
     */
    String getTypeName();

    /**
     * This field is optional or not?
     *
     * @return
     */
    boolean isOptional();

    /**
     * Do the equal check
     *
     * @param _obj
     * @return
     */
    @Override
    boolean equals(Object _obj);
}

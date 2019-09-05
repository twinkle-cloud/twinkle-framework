package com.twinkle.framework.core.lang.ref;

import com.twinkle.framework.core.error.AttributeNotSetException;
import com.twinkle.framework.core.lang.struct.StructAttribute;
import com.twinkle.framework.core.lang.struct.StructAttributeType;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/30/19 6:02 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface CompositeAttributeRef extends AttributeRef {
    /**
     * Get the total path for tail node.
     *
     * @return
     */
    String getTailPath();

    /**
     * Get the tail node type.
     * @return
     */
    StructAttributeType getTailType();

    /**
     * Get the tail attribute ref.
     *
     * @return
     */
    AttributeRef getTailAttributeRef();

    /**
     * Get tail Struct attribute.
     *
     * @param _attr
     * @return
     * @throws AttributeNotSetException
     */
    StructAttribute getTailStructAttribute(StructAttribute _attr) throws AttributeNotSetException;

    /**
     * Get the tail struct attribute with given attribute.
     *
     * @param _attr
     * @param _createFlag
     * @return
     */
    StructAttribute getTailStructAttribute(StructAttribute _attr, boolean _createFlag);
}

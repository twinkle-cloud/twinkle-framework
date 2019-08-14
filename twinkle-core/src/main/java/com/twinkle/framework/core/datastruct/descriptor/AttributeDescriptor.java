package com.twinkle.framework.core.datastruct.descriptor;

import java.util.Set;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-01 18:18<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface AttributeDescriptor {
    /**
     * Attribute's access.
     *
     * @return
     */
    int getAccess();
    /**
     * Attribute's name.
     *
     * @return
     */
    String getName();

    /**
     * Attribute's ASM type.
     *
     * @return
     */
    TypeDescriptor getType();

    /**
     * Attribute's owner, some bean.
     *
     * @return
     */
    BeanTypeDescriptor getOwner();

    /**
     * Attribute's annotations.
     *
     * @return
     */
    Set<String> getAnnotations();

    /**
     * Is required or not?
     *
     * @return
     */
    boolean isRequired();

    /**
     * Is final or not?
     *
     * @return
     */
    boolean isReadOnly();

    /**
     * Get default value.
     *
     * @return
     */
    Object getDefaultValue();
}

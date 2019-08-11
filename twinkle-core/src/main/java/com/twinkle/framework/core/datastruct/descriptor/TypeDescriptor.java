package com.twinkle.framework.core.datastruct.descriptor;

import java.util.Set;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-01 18:19<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface TypeDescriptor {
    /**
     * Type's Name
     *
     * @return
     */
    String getName();

    /**
     * Type's description.
     *
     * @return
     */
    String getDescription();

    /**
     * Is this type a primitive type or not?
     *
     * @return
     */
    boolean isPrimitive();

    /**
     * Is this type is a bean type or not?
     *
     * @return
     */
    boolean isBean();

    /**
     * Get annotations list of this type.
     *
     * @return
     */
    Set<String> getAnnotations();

    /**
     * Get the class of this type.
     *
     * @return
     */
    String getClassName();
}

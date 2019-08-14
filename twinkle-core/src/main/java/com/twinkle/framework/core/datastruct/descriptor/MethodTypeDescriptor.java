package com.twinkle.framework.core.datastruct.descriptor;

import java.util.List;
import java.util.Set;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-13 11:36<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface MethodTypeDescriptor {
    /**
     * Method's access.
     *
     * @return
     */
    int getAccess();

    /**
     * Method's name.
     *
     * @return
     */
    String getName();

    /**
     * Method's return type.
     *
     * @return
     */
    TypeDescriptor getReturnType();

    /**
     * Attribute's annotations.
     *
     * @return
     */
    Set<String> getAnnotations();

    /**
     * Method's parameters.
     *
     * @return
     */
    List<AttributeDescriptor> getParameterAttrs();

    /**
     * Method's local parameters.
     *
     * @return
     */
    List<AttributeDescriptor> getLocalParameterAttrs();

    /**
     * Method's exceptions.
     *
     * @return
     */
    List<TypeDescriptor> getExceptions();

    /**
     * Get the name of the dest designer's method that will be used to
     * pack the instructions of this method.
     *
     * @return
     */
    String getInstructionMethodName();
}

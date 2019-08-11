package com.twinkle.framework.core.datastruct.schema;

import java.util.List;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-10 22:04<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface MethodDef {
    /**
     * Get Method access type.
     *
     * @return
     */
    int getAccess();

    /**
     * Get method's name.
     *
     * @return
     */
    String getName();

    /**
     * Get return type.
     *
     * @return
     */
    TypeDef getReturnType();

    /**
     * Get method's Parameters list.
     *
     * @return
     */
    List<AttributeDef> getParameterAttrs();

    /**
     * Get local parameters list.
     *
     * @return
     */
    List<AttributeDef> getLocalParameterAttrs();

    /**
     * Get annotations list for this method.
     *
     * @return
     */
    List<AnnotationDef> getAnnotations();

    /**
     * Get exceptions list for this method.
     *
     * @return
     */
    List<TypeDef> getExceptions();
}

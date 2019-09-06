package com.twinkle.framework.asm.define;

import org.objectweb.asm.Type;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-31 22:57<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface TypeDef {
    /**
     * Get Type's name.
     *
     * @return
     */
    String getName();

    /**
     * Type's ASM Type.
     *
     * @return
     */
    Type getType();

    /**
     * Is primitive type or not?
     *
     * @return
     */
    boolean isPrimitive();

    /**
     * Is bean type or not?
     *
     * @return
     */
    boolean isBean();

    /**
     * Is array type or not?
     *
     * @return
     */
    boolean isArray();

    /**
     * Is generic type or not?
     *
     * @return
     */
    boolean isGeneric();

    /**
     * Is enum type or not?
     *
     * @return
     */
    boolean isEnum();
}

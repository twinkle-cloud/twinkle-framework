package com.twinkle.framework.core.datastruct.schema;

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
    String getName();

    Type getType();

    boolean isPrimitive();

    boolean isBean();

    boolean isArray();

    boolean isGeneric();

    boolean isEnum();
}

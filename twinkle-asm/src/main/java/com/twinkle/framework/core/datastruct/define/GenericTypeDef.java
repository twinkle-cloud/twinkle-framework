package com.twinkle.framework.core.datastruct.define;

import org.objectweb.asm.Type;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-31 23:04<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface GenericTypeDef extends ClassTypeDef {
    /**
     * Generic Type list.
     *
     * @return
     */
    Type[] getTypeParameters();

    String getFieldDescriptor();
    String getGetterDescriptor();
    String getSetterDescriptor();
    String getFieldSignature();
    String getGetterSignature();
    String getSetterSignature();
}

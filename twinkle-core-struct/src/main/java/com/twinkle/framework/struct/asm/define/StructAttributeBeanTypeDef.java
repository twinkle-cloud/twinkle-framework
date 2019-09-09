package com.twinkle.framework.struct.asm.define;

import com.twinkle.framework.asm.define.BeanTypeDef;
import com.twinkle.framework.struct.type.StructAttributeType;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/30/19 8:16 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface StructAttributeBeanTypeDef extends BeanTypeDef {
    /**
     * Get struct attribute type.
     *
     * @return
     */
    StructAttributeType getStructAttributeType();
}

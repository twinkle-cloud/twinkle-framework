package com.twinkle.framework.asm.define;

import com.twinkle.framework.asm.define.ArrayTypeDef;
import com.twinkle.framework.asm.define.BeanRefTypeDef;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-31 22:59<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface BeanArrayTypeDef extends ArrayTypeDef {
    BeanRefTypeDef getBeanElementType();
}

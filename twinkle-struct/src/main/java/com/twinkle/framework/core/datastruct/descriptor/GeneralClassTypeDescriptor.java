package com.twinkle.framework.core.datastruct.descriptor;

import java.util.List;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-13 14:26<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface GeneralClassTypeDescriptor extends BeanTypeDescriptor {
    /**
     * Get methods define list.
     *
     * @return
     */
    List<MethodTypeDescriptor> getMethods();
}

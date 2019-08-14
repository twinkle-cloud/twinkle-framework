package com.twinkle.framework.core.datastruct.schema;

import java.util.List;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-13 11:21<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface GeneralClassTypeDef extends BeanTypeDef {
    /**
     * Get the method definition list.
     *
     * @return
     */
    List<MethodDef> getMethods();
}

package com.twinkle.framework.core.datastruct;

import com.twinkle.framework.core.lang.struct.StructAttributeFactory;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/2/19 10:36 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface StructAttributeFactoryCenter {
    /**
     * Get Struct Attribute factory in this center.
     *
     * @return
     */
    StructAttributeFactory getStructAttributeFactory();

    /**
     * Get this center's name.
     * @return
     */
    String getName();
}

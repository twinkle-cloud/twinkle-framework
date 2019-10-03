package com.twinkle.framework.core.lang.util;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/30/19 11:32 AM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface ArrayWrapper<A> extends Cloneable {
    /**
     * Get array's length.
     *
     * @return
     */
    int length();

    /**
     * Get the wrapper array.
     * @return
     */
    A array();
}

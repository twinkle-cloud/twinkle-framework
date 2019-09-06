package com.twinkle.framework.asm.builder;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-02 21:22<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface InstanceBuilder<T> {
    /**
     * Build a new instance.
     *
     * @return
     */
    T newInstance();

    /**
     * Build a new Array.
     *
     * @param _size
     * @return
     */
    T[] newArray(int _size);
}

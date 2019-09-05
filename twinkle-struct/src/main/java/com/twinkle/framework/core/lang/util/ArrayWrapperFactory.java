package com.twinkle.framework.core.lang.util;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/30/19 7:47 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface ArrayWrapperFactory<A> {
    ArrayWrapper<A> wrap(A var1, int var2);

    ArrayWrapper<A> newArrayWrapper(int var1);

    A newArray(int var1);

    Class<A> getArrayClass();
}

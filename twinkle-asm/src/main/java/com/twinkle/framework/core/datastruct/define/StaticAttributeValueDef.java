package com.twinkle.framework.core.datastruct.define;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-16 15:11<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface StaticAttributeValueDef {
    /**
     * The Class's internal Name of the attribute value.
     *
     * @return
     */
    String getClassInternalName();

    /**
     * Static method name to build the instance of the value.
     *
     * @return
     */
    String getMethodName();

    /**
     * The method's signature of the method.
     * @return
     */
    String getMethodDescriptor();
}

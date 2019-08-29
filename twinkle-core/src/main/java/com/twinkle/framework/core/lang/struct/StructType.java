package com.twinkle.framework.core.lang.struct;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/29/19 4:56 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface StructType {

    boolean isPrimitiveType();

    boolean isArrayType();

    boolean isStructType();

    boolean isStringType();

    int getID();

    String getName();
}

package com.twinkle.framework.core.type;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/29/19 4:56 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface AttributeType {
    /**
     * Is this type a primitive type?
     *
     * @return
     */
    boolean isPrimitiveType();

    /**
     * Is this type an array type?
     *
     * @return
     */
    boolean isArrayType();

    /**
     * Is this type a struct type?
     *
     * @return
     */
    boolean isStructType();

    /**
     * Is this type a string type?
     *
     * @return
     */
    boolean isStringType();

    /**
     * is this type an object?
     * true by default.
     *
     * @return
     */
    default boolean isObject(){
        return true;
    }

    /**
     * Get the type ID.
     *
     * @return
     */
    int getID();

    /**
     * Get the type name.
     *
     * @return
     */
    String getName();
}

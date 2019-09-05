package com.twinkle.framework.core.lang;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-10 17:50<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface INumericAttribute extends Attribute {
    /**
     * Get integer value of this attribute.
     * @return
     */
    int getInt();
    /**
     * Get Long value of this attribute.
     * @return
     */
    long getLong();
    /**
     * Get Float value of this attribute.
     * @return
     */
    float getFloat();

    /**
     * Get Double value of this attribute.
     * @return
     */
    double getDouble();

    /**
     * Add the value
     *
     * @param _var1
     * @param _var2
     * @return
     */
    boolean add(INumericAttribute _var1, INumericAttribute _var2);

    boolean subtract(INumericAttribute _var1, INumericAttribute _var2);

    boolean multiply(INumericAttribute _var1, INumericAttribute _var2);

    boolean divide(INumericAttribute _var1, INumericAttribute _var2);

    boolean mod(INumericAttribute _var1, INumericAttribute _var2);

}

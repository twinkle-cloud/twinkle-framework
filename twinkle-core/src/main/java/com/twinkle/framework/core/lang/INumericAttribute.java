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
     *
     * @return
     */
    int getInt();

    /**
     * Get Long value of this attribute.
     *
     * @return
     */
    long getLong();

    /**
     * Get Float value of this attribute.
     *
     * @return
     */
    float getFloat();

    /**
     * Get Double value of this attribute.
     *
     * @return
     */
    double getDouble();

    /**
     * Add the value
     *
     * @param _attr1
     * @param _attr2
     * @return
     */
    boolean add(INumericAttribute _attr1, INumericAttribute _attr2);

    /**
     * Subtract the attr1 with attr2, and update the value with the result.
     * value = attr1 - attr2
     *
     * @param _attr1
     * @param _attr2
     * @return always to be true.
     */
    boolean subtract(INumericAttribute _attr1, INumericAttribute _attr2);

    /**
     * Multiply the attr1 with attr2, and update the value with the result.
     * value = attr1 * attr2
     *
     * @param _attr1
     * @param _attr2
     * @return always to be true.
     */
    boolean multiply(INumericAttribute _attr1, INumericAttribute _attr2);
    /**
     * Divide the attr1 with attr2, and update the value with the result.
     * value = attr1 / attr2
     *
     * @param _attr1
     * @param _attr2
     * @return always to be true.
     */
    boolean divide(INumericAttribute _attr1, INumericAttribute _attr2);
    /**
     * Mode the attr1 with attr2, and update the value with the result.
     * value = attr1 % attr2
     *
     * @param _attr1
     * @param _attr2
     * @return always to be true.
     */
    boolean mod(INumericAttribute _attr1, INumericAttribute _attr2);

}

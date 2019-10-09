package com.twinkle.framework.core.lang;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-10 17:53<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface IScalarAttribute extends INumericAttribute {
    /**
     * Do logic and (&).
     *
     * @param _attr1
     * @param _attr2
     * @return
     */
    boolean and(IScalarAttribute _attr1, IScalarAttribute _attr2);

    /**
     * Do logic or (|)
     * @param _attr1
     * @param _attr2
     * @return
     */
    boolean or(IScalarAttribute _attr1, IScalarAttribute _attr2);

    /**
     * Do logic (^)
     * @param _attr1
     * @param _attr2
     * @return
     */
    boolean xor(IScalarAttribute _attr1, IScalarAttribute _attr2);

    /**
     * Do logic (<<)
     *
     * @param _attr1
     * @param _attr2
     * @return
     */
    boolean shiftl(IScalarAttribute _attr1, IScalarAttribute _attr2);

    /**
     * Do logic (>>)
     * @param _attr1
     * @param _attr2
     * @return
     */
    boolean shiftr(IScalarAttribute _attr1, IScalarAttribute _attr2);
}

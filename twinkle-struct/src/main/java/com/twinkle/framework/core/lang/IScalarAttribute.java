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

    boolean and(IScalarAttribute _var1, IScalarAttribute _var2);

    boolean or(IScalarAttribute _var1, IScalarAttribute _var2);

    boolean xor(IScalarAttribute _var1, IScalarAttribute _var2);

    boolean shiftl(IScalarAttribute _var1, IScalarAttribute _var2);

    boolean shiftr(IScalarAttribute _var1, IScalarAttribute _var2);
}

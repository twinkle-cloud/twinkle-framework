package com.twinkle.framework.core.asm.constants;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-06-24 10:14<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface AsmConstant {

    Boolean TYPE_DISPLAY_SIMPLY = true;
    /**
     * Max number of threads to use in IO heavy tasks.
     */
    int MAX_THREADS_IO = 50;
    /**
     * Max number of threads to use in computational tasks.
     */
    int MAX_THREADS_LOGIC = 5;
    /**
     * Used to indicate if linked<i>(Overrides / parents)</i> methods should be
     * renamed when updating a method's name.
     */
    Boolean LINKED_METHOD_REPLACE = true;
}

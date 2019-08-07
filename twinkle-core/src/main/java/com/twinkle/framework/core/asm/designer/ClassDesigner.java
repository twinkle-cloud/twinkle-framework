package com.twinkle.framework.core.asm.designer;

import org.objectweb.asm.Type;

import java.io.PrintWriter;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-01 23:10<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface ClassDesigner {
    /**
     * By default the version is JDK1.8
     */
    int TARGET_JVM = 52;
    int AUTO_CALCULATE_STACK_SIZE_AND_LOCAL_VARS_NUMBER = 3;
    int AUTO_STACK_SIZE = 0;
    int AUTO_LOCAL_VARS = 0;

    String getCanonicalClassName();

    byte[] toByteArray();

    /**
     *
     * @param _checkFlag
     * @param _printWriter
     * @return
     */
    byte[] toByteArray(boolean _checkFlag, PrintWriter _printWriter);
}

package com.twinkle.framework.core.asm.designer;

import org.objectweb.asm.Opcodes;

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
    int TARGET_JVM = Opcodes.V1_8;
    int AUTO_CALCULATE_STACK_SIZE_AND_LOCAL_VARS_NUMBER = 3;
    int AUTO_STACK_SIZE = 0;
    int AUTO_LOCAL_VARS = 0;

    /**
     * Get canonical class name.
     *
     * @return
     */
    String getCanonicalClassName();

    /**
     * Generate the class and output as byte array.
     *
     * @return
     */
    byte[] toByteArray();

    /**
     * Write the class byte array to the printwriter.
     *
     * @param _checkFlag
     * @param _printWriter
     * @return
     */
    byte[] toByteArray(boolean _checkFlag, PrintWriter _printWriter);
}

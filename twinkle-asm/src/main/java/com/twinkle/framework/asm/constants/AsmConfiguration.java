package com.twinkle.framework.asm.constants;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-06-24 10:27<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class AsmConfiguration {
    /**
     * Flag for ASM output, compute maximum stack/local sizes for methods.
     */
    private static boolean computeMax = true;
    /**
     * Flag for ASM output, compute stack-frames for methods.
     * <hr>
     * Enabled by default so users do not have to worry about messing up the
     * stack and having to edit the frames.
     */
    private static boolean computeFrams = true;
    /**
     * Flag for ASM input, expand stack-frames into a standard format in method
     * code.
     */
    private static boolean expandFrames;

    /**
     * Option for toggling whether exporting jars should use reflection-lookups
     * for classes not found in the loaded input. Used for determining proper
     * parent hierarchy.
     */
    private static boolean reflectionExport = true;
    /**
     * Flag for ASM input, skip reading method code.
     */
    private static boolean skipCode;
    /**
     * Flag for ASM input, skip reading debug information <i>(variable names,
     * line numbers, etc.)</i>.
     */
    private static boolean skipDebug;
    /**
     * Flag for ASM input, skip reading stack-frames in method code.
     * <hr>
     * Enabled by default since frames are recalculated by default when Recaf
     * exports changes.
     */
    private static boolean skipFrames = true;
    /**
     * @return Flags to be used in {@code ClassWriter}s.
     */
    public static int getOutputFlags() {
        int flags = 0;
        if (computeMax) flags |= ClassWriter.COMPUTE_MAXS;
        if (computeFrams) flags |= ClassWriter.COMPUTE_FRAMES;
        return flags;
    }

    /**
     * @return {@code ClassWriter} with configuration's flags.
     */
    public static int getInputFlags() {
        int flags = 0;
        if (skipCode) flags |= ClassReader.SKIP_CODE;
        if (skipDebug) flags |= ClassReader.SKIP_DEBUG;
        if (skipFrames) flags |= ClassReader.SKIP_FRAMES;
        if (expandFrames) flags |= ClassReader.EXPAND_FRAMES;
        return flags;
    }

    /**
     * @return {@code true} if exporting can use reflection to find classes not
     *         found in the input file.
     */
    public static boolean useReflection() {
        return reflectionExport;
    }
}

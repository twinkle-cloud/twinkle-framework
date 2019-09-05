package com.twinkle.framework.core.asm.compiler;
/**
 * Wrapper for javax.tools.DiagnosticListener.
 * @author chenxj
 */
public interface CompileListener {
	void report(CompilerMessage message);
}

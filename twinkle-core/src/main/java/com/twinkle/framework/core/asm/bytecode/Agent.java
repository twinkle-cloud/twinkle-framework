package com.twinkle.framework.core.asm.bytecode;

import lombok.extern.slf4j.Slf4j;

import java.lang.instrument.Instrumentation;

/**
 * @author chenxj
 */
@Slf4j
public class Agent {
	/**
	 * Public instrumentation isntance.
	 */
	public static Instrumentation inst;

	/**
	 * Called when attached through javaagent jvm arg.
	 * 
	 * @param agentArgs
	 * @param inst
	 */
	public static void premain(String agentArgs, Instrumentation inst) {
		agent(agentArgs, inst);
	}

	/**
	 * Called when attached to a jvm externally.
	 * 
	 * @param agentArgs
	 * @param inst
	 */
	public static void agentmain(String agentArgs, Instrumentation inst) {
		agent(agentArgs, inst);
	}

	/**
	 * Populate {@link #inst} and invoke Recaf.
	 * 
	 * @param agentArgs
	 * @param inst
	 */
	private static void agent(String agentArgs, Instrumentation inst) {
		Agent.inst = inst;
		log.info("Initializing as agent");
//		Recaf.main(new String[] {});
	}

	/**
	 * @return {@code true} Agent is active. {@code false} otherwise.
	 */
	public static boolean isActive() {
		return inst != null;
	}
}

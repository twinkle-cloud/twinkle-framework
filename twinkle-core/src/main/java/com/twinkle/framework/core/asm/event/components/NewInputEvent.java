package com.twinkle.framework.core.asm.event.components;

import com.twinkle.framework.core.asm.AsmInput;
import com.twinkle.framework.core.asm.event.Bus;
import com.twinkle.framework.core.asm.event.Event;
import com.twinkle.framework.core.utils.Threads;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;

/**
 * Event for when a new asmInput is loaded.
 * 
 * @author Matt
 */
@Slf4j
public class NewInputEvent extends Event {
	private final AsmInput asmInput;

	public NewInputEvent(AsmInput asmInput) {
		this.asmInput = asmInput;
		if (asmInput.input != null) {
			log.info("Loaded asmInput from: " + asmInput.input.getName());
		} else {
			log.info("Loaded asmInput from instrumentation");
		}
	}

	public NewInputEvent(File file) throws IOException {
		this(new AsmInput(file));
	}

	public NewInputEvent(Instrumentation instrumentation) throws IOException {
		this(new AsmInput(instrumentation));
	}

	public AsmInput get() {
		return asmInput;
	}

	/**
	 * Multi-threaded invoke for event.
	 * 
	 * @param file
	 *            File to load.
	 */
	public static void call(File file) {
		Threads.runLaterFx(0, () -> {
			try {
				Bus.post(new NewInputEvent(file));
			} catch (Exception e) {
				log.error("Error encountered.", e);
			}
		});
	}

	/**
	 * Multi-threaded invoke for event.
	 * 
	 * @param inst
	 *            Instrumented runtime.
	 */
	public static void call(Instrumentation inst) {
		Threads.runLaterFx(0, () -> {
			try {
				Bus.post(new NewInputEvent(inst));
			} catch (Exception e) {
				log.error("Error encountered.", e);
			}
		});
	}
}

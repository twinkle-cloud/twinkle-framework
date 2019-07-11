package com.twinkle.framework.core.utils;

import com.twinkle.framework.core.asm.constants.AsmConstant;
import javafx.application.Platform;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author chenxj
 */
@Slf4j
public class Threads {
	public static ExecutorService pool(PoolKind kind) {
		switch (kind) {
		case IO:
			// There is more of a benefit to throwing threads at IO tasks than
			// at a computational problem. So defining caps for both allow a
			// higher cap to be set for these IO tasks.
			return Executors.newFixedThreadPool(AsmConstant.MAX_THREADS_IO);
		case LOGIC:
		default:
			return Executors.newFixedThreadPool(AsmConstant.MAX_THREADS_LOGIC);
		}
	}

	public static void waitForCompletion(ExecutorService pool) {
		try {
			pool.shutdown();
			pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			log.error("Error encountered.", e);
		}
	}

	public static void run(Runnable r) {
		runLater(0, r);
	}

	public static void runLater(int delay, Runnable r) {
		new Thread(() -> {
			try {
				if (delay > 0)
					Thread.sleep(delay);
				r.run();
			} catch (Exception e) {
				log.error("Error encountered.", e);
			}
		}).start();
	}

	public static void runFx(Runnable r) {
		Platform.runLater(r);
	}

	public static void runLaterFx(int delay, Runnable r) {
		runLater(delay, () -> Platform.runLater(r));
	}

	public enum PoolKind {
		IO, LOGIC
	}
}

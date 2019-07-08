package com.twinkle.framework.core.asm.event;

import java.lang.annotation.*;

/**
 * Annotation for methods indicating the method should receive events.
 *
 * @author chenxj
 */
@Documented
@Inherited  // @Inherited doesn't work for method, so we have to implement that manually anyway...
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Listener {
	/**
	 * Listener priority. Lower values are called first.
	 *
	 * @return Priority of event receiving.
	 */
	int priority() default 0;
}
package com.twinkle.framework.asm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Function: Will be used to identify the Bean's attributes are
 * useful or not for the Bean's HashCode. <br/>

 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-05 15:23<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Key {
    boolean value() default true;
}

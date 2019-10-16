package com.twinkle.framework.datasource.annotation;

import java.lang.annotation.*;

/**
 * Function: It can be annotate at class or method. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/12/19 6:31 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TwinkleDataSource {

    /**
     * groupName or specific database name or spring SPEL name.
     *
     * @return the database you want to switch
     */
    String value();
}
package com.twinkle.framework.core.asm.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-01 15:12<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class EnumAnnotationValueDefine {
    /**
     * The Annotation class.
     */
    @NonNull
    private Class<?> typeClass;

    /**
     * The Annotation's value.
     */
    private String value;
}

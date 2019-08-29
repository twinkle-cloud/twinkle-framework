package com.twinkle.framework.core.datastruct.descriptor;

import com.twinkle.framework.core.lang.struct.StructType;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/29/19 4:04 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface StructAttributeDescriptor {
    String getName();

    StructType getType();

    String getTypeName();

    boolean isOptional();

    boolean equals(Object var1);
}

package com.twinkle.framework.core.lang.struct;

import com.twinkle.framework.core.datastruct.descriptor.AttributeDescriptor;
import com.twinkle.framework.core.datastruct.descriptor.StructAttributeDescriptor;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/29/19 4:57 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface AttributeRef {

    String getName();

    StructType getType();

    StructAttributeDescriptor getDescriptor();

    boolean isComposite();

    boolean isArray();
}

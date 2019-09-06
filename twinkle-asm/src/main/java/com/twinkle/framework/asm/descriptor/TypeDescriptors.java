package com.twinkle.framework.asm.descriptor;

import java.util.List;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-08 11:39<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface TypeDescriptors {
    List<TypeDescriptor> getTypes();

    TypeDescriptor getType(String _typeName);
}

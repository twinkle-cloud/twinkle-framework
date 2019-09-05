package com.twinkle.framework.core.datastruct.descriptor;

import java.util.List;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-09 10:54<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface GenericTypeDescriptor extends TypeDescriptor {
    List<TypeDescriptor> getParameterTypes();
}

package com.twinkle.framework.asm.descriptor;

import java.util.Map;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-01 22:09<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface EnumTypeDescriptor extends TypeDescriptor{
    String getValueClassName();

    Map<String, Object> getEnumerationValues();

    EnumHandler getEnumHandler();
}

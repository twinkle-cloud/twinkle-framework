package com.twinkle.framework.core.datastruct.define;

import org.objectweb.asm.Type;

import java.util.List;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-31 23:03<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface EnumTypeDef extends TypeDef {
    Type getValueType();

    Class getValueClass();

    List<Object> getEnumConstraints();

    List<String> getEnumNames();
}

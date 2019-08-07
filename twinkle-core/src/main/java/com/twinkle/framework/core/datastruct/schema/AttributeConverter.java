package com.twinkle.framework.core.datastruct.schema;

import java.util.List;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-31 22:58<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface AttributeConverter {

    String normalize(String var1);

    List<AttributeDef> normalize(List<AttributeDef> var1);

    void validate(List<AttributeDef> var1);
}

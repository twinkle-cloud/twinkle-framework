package com.twinkle.framework.core.datastruct.converter;

import com.twinkle.framework.core.datastruct.define.AttributeDef;

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

    String normalize(String _attrName);

    List<AttributeDef> normalize(List<AttributeDef> _attrDefList);

    void validate(List<AttributeDef> _attrDefList);
}

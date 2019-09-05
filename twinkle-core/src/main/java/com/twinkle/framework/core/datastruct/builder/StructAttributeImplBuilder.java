package com.twinkle.framework.core.datastruct.builder;

import com.twinkle.framework.core.datastruct.Bean;
import com.twinkle.framework.core.error.AttributeNotFoundException;
import com.twinkle.framework.core.lang.ref.StructAttributeRef;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/1/19 7:30 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface StructAttributeImplBuilder<T extends Bean> extends BeanImplBuilder<T> {
    /**
     * Get the Struct Attribute ref with given attribute ref name.
     *
     * @param _attrRefName
     * @return
     * @throws AttributeNotFoundException
     */
    StructAttributeRef getAttributeReference(String _attrRefName) throws AttributeNotFoundException;
}

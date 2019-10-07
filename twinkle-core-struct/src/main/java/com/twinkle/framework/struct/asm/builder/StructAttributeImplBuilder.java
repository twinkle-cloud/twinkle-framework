package com.twinkle.framework.struct.asm.builder;

import com.twinkle.framework.asm.Bean;
import com.twinkle.framework.asm.builder.BeanImplBuilder;
import com.twinkle.framework.struct.error.AttributeNotFoundException;
import com.twinkle.framework.struct.ref.AttributeRef;

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
    AttributeRef getAttributeReference(String _attrRefName) throws AttributeNotFoundException;
}

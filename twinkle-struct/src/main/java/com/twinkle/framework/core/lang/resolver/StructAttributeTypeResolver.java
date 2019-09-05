package com.twinkle.framework.core.lang.resolver;

import com.twinkle.framework.core.lang.struct.StructAttributeType;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/31/19 10:07 AM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface StructAttributeTypeResolver {
    /**
     * Get some struct attribute with given namespace name and struct type name.
     *
     * @param _namespace
     * @param _structTypeName
     * @return
     */
    StructAttributeType getStructAttributeType(String _namespace, String _structTypeName);

    /**
     * Get the struct type with the given struct type name.
     *
     * @param _structTypeName
     * @return
     */
    StructAttributeType getStructAttributeType(String _structTypeName);
}

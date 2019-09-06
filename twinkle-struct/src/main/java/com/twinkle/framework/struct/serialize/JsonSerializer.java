package com.twinkle.framework.struct.serialize;

import com.twinkle.framework.asm.serialize.TextSerializer;
import com.twinkle.framework.struct.type.StructAttribute;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/4/19 4:15 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface JsonSerializer extends TextSerializer<StructAttribute> {
    String TYPE_PROPERTY = "TypeQualifiedName";
    String StructAttribute_PROPERTY = "StructAttribute";

    /**
     * Get Root Type.
     * @return
     */
    String getRootType();

    /**
     * Can be serialized or not?
     * @return
     */
    boolean isSerializeType();
}

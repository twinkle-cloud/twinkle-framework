package com.twinkle.framework.core.lang;

import lombok.Data;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/25/19 6:18 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Data
public class JavaAttributeInfo {
    /**
     * Map to PRIMITIVE TYE of the attribute.
     */
    private int primitiveType;
    /**
     * Name of the attribute.
     */
    private String name;

    /**
     * The class name of the attribute
     */
    private String className;
    /**
     * The class of the attribute.
     */
    private Class<?> attributeClass;
    /**
     * The description of the attribute class.
     */
    private String description;
}

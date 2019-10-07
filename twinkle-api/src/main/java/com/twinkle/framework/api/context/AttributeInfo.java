package com.twinkle.framework.api.context;

import com.twinkle.framework.core.lang.Attribute;

import java.io.Serializable;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/5/19 2:21 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface AttributeInfo extends Serializable {
    /**
     * Get the attribute's name.
     *
     * @return
     */
    String getName();

    /**
     * Get attribute's index in NormalizedAttributeType.
     *
     * @return
     */
    int getTypeIndex();

    /**
     * Get Attribute's Primitive type.
     * Refer: Attribute.TYPE
     *
     * @return
     */
    int getPrimitiveType();

    /**
     * Get Attribute's index in NormalizedContext.
     *
     * @return
     */
    int getIndex();

    /**
     * Get Class Name of this attribute.
     *
     * @return
     */
    String getClassName();

    /**
     * Get attribute's class.
     *
     * @return
     */
    Class<?> getAttributeClass();

    /**
     * Get the description for the attribute class.
     * @return
     */
    String getDescription();

    /**
     * Get the value's class of this attribute.
     * @return
     */
    Class<?> getValueClass();

    /**
     * Get the value class's description.
     *
     * @return
     */
    String getValueDescription();

    /**
     * Build a new attribute instance.
     *
     * @return
     */
    Attribute newAttributeInstance();

    /**
     * Get value's attribute Info.
     * ONLY for ListAttribute's element.
     *
     * @return
     */
    AttributeInfo getValueAttributeInfo();

    /**
     * Only for Struct Attribute.
     *
     * @return the Qualified name of the struct attribute's type.
     */
    String getValueType();
}

package com.twinkle.framework.api.context;

import com.twinkle.framework.core.lang.Attribute;

import java.io.Serializable;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/5/19 11:49 AM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface NormalizedContext extends Serializable, Cloneable {
    /**
     * Get the NormalizedAttributeType for this context.
     *
     * @return
     */
    NormalizedAttributeType getType();

    /**
     * Get all of the attributes.
     *
     * @return
     */
    Attribute[] getAttributes();

    /**
     * Get the attribute with the given attribute index in the context.
     *
     * @param _index
     * @return
     */
    Attribute getAttribute(int _index);

    /**
     * Get the attribute with given attribute name.
     *
     * @param _attrName
     * @return
     */
    Attribute getAttribute(String _attrName);

    /**
     * Update the given[_index]'s attribute with the given attribute.
     *
     * @param _attr
     * @param _index
     */
    void setAttribute(Attribute _attr, int _index);

    /**
     * Update the given[_attrname]'s attribute with the given attribute.
     *
     * @param _attr
     * @param _attrName
     */
    void setAttribute(Attribute _attr, String _attrName);

    /**
     * Update the given[_index]'s attribute with the given attribute's copy.
     *
     * @param _attr
     * @param _index
     */
    void copyAttribute(Attribute _attr, int _index);

    /**
     * Get the number of the attributes in this context.
     *
     * @return
     */
    int numAttributesSet();

    /**
     * Update the current context with given context.
     *
     * @param _context
     */
    void update(NormalizedContext _context);

    /**
     * Replace the current context with given context.
     *
     * @param _context
     */
    void set(NormalizedContext _context);

    /**
     * Output the attributes in the context as string.
     *
     * @param _printNullFlag
     * @return
     */
    String toStringWithAttrNames(boolean _printNullFlag);

    /**
     * Output the attributes in the context as string.
     *
     * @param _printNullFlag
     * @param _logAttrFlag
     * @param _logStructAttributeFlag
     * @param _inSingleLineFlag
     * @param _separator
     * @return
     */
    String toStringWithAttrNames(boolean _printNullFlag, boolean _logAttrFlag, boolean _logStructAttributeFlag, boolean _inSingleLineFlag, String _separator);

    /**
     * Output the attributes' values.
     *
     * @return
     */
    String toStringValuesOnly();

    /**
     * Judge the context is empty or not?
     *
     * @return
     */
    boolean isEmpty();

    /**
     * Clear the values of the attributes in the context.
     */
    void clearValues();

    /**
     * Clear the context.
     */
    void clear();
}

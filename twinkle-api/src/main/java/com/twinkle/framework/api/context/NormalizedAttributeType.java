package com.twinkle.framework.api.context;

import java.io.Serializable;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/5/19 11:31 AM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface NormalizedAttributeType extends Serializable {
    /**
     * Get the name of the normalized attribute type.
     *
     * @return
     */
    String getName();

    /**
     * Get the type id of the normalized attribute type.
     *
     * @return
     */
    int getTypeId();

    /**
     * Get attribute names as an array in this normalized attribute type.
     *
     * @return
     */
    String[] getAttributeNames();

    /**
     * Get the attribute indexes as an array in this normalized attribute type.
     *
     * @return
     */
    int[] getAttributeIndexes();

    /**
     * Judge the given attribute exists in this normalized attribute type or not?
     *
     * @param _attrName
     * @return
     */
    boolean isMember(String _attrName);

    /**
     * Judge the given attribute exists in this normalized attribute type or not?
     *
     * @param _index
     * @return
     */
    boolean isMember(int _index);

    /**
     * Add attribute into this normalized attribute type.
     *
     * @param _attrName
     */
    void addAttribute(String _attrName);

    /**
     * Add attribute into this normalized attribute type.
     *
     * @param _attributeInfo
     */
    void addAttribute(AttributeInfo _attributeInfo);

    /**
     * Get the attributes num in this normalized attribute type.
     *
     * @return
     */
    int getNumAttributes();

    /**
     * Get the index map.
     *
     * @return
     */
    int[] getIndexMap();

    /**
     * Get the Normalized index.
     *
     * @param _index
     * @return
     */
    int getNormalizedEventIndex(int _index);

    /**
     * Judge the normalized attribute type.
     *
     * @param _typeInfo
     * @return
     */
    boolean isInconsistentSerializedType(NormalizedAttributeType _typeInfo);
}

package com.twinkle.framework.core.lang;

import java.util.Collection;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/28/19 4:20 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface IListAttribute extends Attribute {
    /**
     * Add the given attr into the dest position.
     *
     * @param _index
     * @param _attr
     */
    void add(int _index, Attribute _attr);

    /**
     * Append the attr at the end of the list.
     *
     * @param _attr
     */
    void add(Attribute _attr);

    /**
     *
     * @param _collection
     */
    void addAll(Collection<? extends IListAttribute> _collection);

    /**
     * Get the attribute with given index.
     *
     * @param _index
     * @return
     */
    Attribute get(int _index);

    /**
     * Judge the list is empty or not?
     *
     * @return
     */
    boolean isEmpty();

    /**
     * Convert to Attribute array.
     *
     * @return
     */
    Attribute[] toArray();

    /**
     * Convert to attribute array.
     *
     * @param _attrArray
     * @return
     */
    Attribute[] toArray(Attribute[] _attrArray);

    /**
     * Update the destination Attribute with given one.
     *
     * @param _index
     * @param _attr
     */
    Attribute set(int _index, Attribute _attr);

    /**
     * Contain the given attribute or not?
     *
     * @param _attr
     * @return
     */
    boolean contains(Attribute _attr);

    /**
     * Contains the given attribute (list) or not?
     * If the Attribute is list attribute, then check all of the elements of the list.
     *
     * @param _collection
     * @return
     */
    boolean containsAll(Collection<? extends IListAttribute> _collection);

    /**
     * Contains the given attribute (list) or not?
     * If the Attribute is list attribute, then check all of the elements of the list.
     *
     * @param _attr
     * @return
     */
    boolean containsAll(Attribute _attr);

    /**
     * The list's size.
     *
     * @return
     */
    int size();
}

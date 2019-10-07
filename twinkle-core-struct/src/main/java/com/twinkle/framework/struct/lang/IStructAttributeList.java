package com.twinkle.framework.struct.lang;

import com.twinkle.framework.core.lang.IListAttribute;

import java.util.List;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/4/19 10:45 AM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface IStructAttributeList extends IListAttribute {
    /**
     * Add Struct Attribute into the given position of the list.
     *
     * @param _index
     * @param _attr
     */
    void add(int _index, StructAttribute _attr);

    /**
     * Add Struct attribute into the list.
     *
     * @param _attr
     */
    void add(StructAttribute _attr);

    /**
     * Add the structattribute list into the StructAttributeList.
     *
     * @param _collection
     */
    void addAll(List<StructAttribute> _collection);
}

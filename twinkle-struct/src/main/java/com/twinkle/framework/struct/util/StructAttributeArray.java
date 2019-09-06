package com.twinkle.framework.struct.util;

import com.twinkle.framework.core.lang.util.Array;
import com.twinkle.framework.struct.type.StructAttribute;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/30/19 5:35 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface StructAttributeArray<T> extends Array {
    /**
     * Get the array item with the given index.
     *
     * @param _index
     * @return
     * @throws ArrayIndexOutOfBoundsException
     */
    StructAttribute get(int _index) throws ArrayIndexOutOfBoundsException;

    /**
     * Update the index's item with the given attribute.
     *
     * @param _index
     * @param _attr
     * @throws ArrayIndexOutOfBoundsException
     */
    void put(int _index, StructAttribute _attr) throws ArrayIndexOutOfBoundsException;
}

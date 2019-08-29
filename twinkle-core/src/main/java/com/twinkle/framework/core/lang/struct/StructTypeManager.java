package com.twinkle.framework.core.lang.struct;

import com.twinkle.framework.core.error.TypeAlreadyExistsException;
import com.twinkle.framework.core.error.TypeNotFoundException;

import java.util.Iterator;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/29/19 10:28 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface StructTypeManager {
    int size();

    Iterator getTypeNames();

    boolean hasTypeName(String _typeName);

    Iterator getUserDefinedTypeNames();

    StructType getType(String _typeName) throws TypeNotFoundException;

    StructType getType(String _typeName, boolean _createFlag) throws TypeNotFoundException;

    void addType(String _typeName, StructType _type) throws TypeAlreadyExistsException;
}

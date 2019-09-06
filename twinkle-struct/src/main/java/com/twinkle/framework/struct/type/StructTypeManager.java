package com.twinkle.framework.struct.type;

import com.twinkle.framework.struct.error.TypeAlreadyExistsException;
import com.twinkle.framework.struct.error.TypeNotFoundException;

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
    /**
     * Get the Struct types' size in the manager
     *
     * @return
     */
    int size();

    Iterator<String> getTypeNames();

    /**
     * Check the type exists or not in the manager?
     *
     * @param _typeName
     * @return
     */
    boolean hasTypeName(String _typeName);

    /**
     * Get the user defined type names, with the iterator.
     *
     * @return
     */
    Iterator<String> getUserDefinedTypeNames();

    /**
     * Get the struct type with the given type name.
     *
     * @param _typeName
     * @return
     * @throws TypeNotFoundException
     */
    StructType getType(String _typeName) throws TypeNotFoundException;

    /**
     * Get the struct type with the given type name, and the check flag for
     * checking the alias type or not.
     *
     * @param _typeName
     * @param _checkAliasFlag
     * @return
     * @throws TypeNotFoundException
     */
    StructType getType(String _typeName, boolean _checkAliasFlag) throws TypeNotFoundException;

    /**
     * Add the struct type into the manager.
     *
     * @param _typeName
     * @param _type
     * @throws TypeAlreadyExistsException
     */
    void addType(String _typeName, StructType _type) throws TypeAlreadyExistsException;
}

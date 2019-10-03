package com.twinkle.framework.struct.context;

import com.twinkle.framework.struct.error.NamespaceAlreadyExistsException;
import com.twinkle.framework.struct.error.NamespaceNotFoundException;
import com.twinkle.framework.struct.error.StructAttributeTypeAlreadyExistsException;
import com.twinkle.framework.struct.error.StructAttributeTypeNotFoundException;
import com.twinkle.framework.struct.type.StructType;
import com.twinkle.framework.struct.type.StructTypeManager;

import java.util.Iterator;

/**
 * Function: Schema can have multi namespaces.
 *  One namespace can have multi struct types.<br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/31/19 2:09 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface StructAttributeSchema {
    /**
     * Get Struct Types' size in this schema.
     *
     * @return
     */
    int size();

    /**
     * Get the num of the namespaces in this schema.
     * @return
     */
    int getNumNamespaces();

    /**
     * Scan the the namespaces in this schema.
     * @return
     */
    Iterator<String> getNamespaces();

    /**
     * Check the given namespace existing or not in this schema?
     *
     * @param _namespace
     * @return
     */
    boolean hasNamespace(String _namespace);

    /**
     * Add namespace into this schema.
     *
     * @param _namespace
     * @throws NamespaceAlreadyExistsException
     */
    void addNamespace(String _namespace) throws NamespaceAlreadyExistsException;

    /**
     * Get the type manager is being used in this schema.
     *
     * @param _namespace
     * @return
     * @throws NamespaceNotFoundException
     */
    StructTypeManager getTypeManager(String _namespace) throws NamespaceNotFoundException;

    /**
     * Get the struct types' num of this schema.
     *
     * @param _namespace
     * @return
     * @throws NamespaceNotFoundException
     */
    int getNumTypes(String _namespace) throws NamespaceNotFoundException;

    /**
     * Scan the struct attribute type under the given namespace in this schema.
     *
     * @param _namespace
     * @return
     * @throws NamespaceNotFoundException
     */
    Iterator<StructType> getStructAttributeTypes(String _namespace) throws NamespaceNotFoundException;

    /**
     * Get some struct attribute with given namespace name and struct type name.
     *
     * @param _namespace
     * @param _structTypeName
     * @return
     * @throws NamespaceNotFoundException
     * @throws StructAttributeTypeNotFoundException
     */
    StructType getStructAttributeType(String _namespace, String _structTypeName) throws NamespaceNotFoundException, StructAttributeTypeNotFoundException;

    /**
     * Check the struct type existing or not under the given namespace?
     *
     * @param _namespace
     * @param _structTypeName
     * @return
     */
    boolean hasStructAttributeType(String _namespace, String _structTypeName);

    /**
     * Get the struct type with the given struct type name(QualifiedName).
     *
     * @param _structTypeName
     * @return
     * @throws NamespaceNotFoundException
     * @throws StructAttributeTypeNotFoundException
     */
    StructType getStructAttributeType(String _structTypeName) throws NamespaceNotFoundException, StructAttributeTypeNotFoundException;

    /**
     * Check the struct type existing or not in this schema?
     *
     * @param _structTypeName
     * @return
     */
    boolean hasStructAttributeType(String _structTypeName);

    /**
     * Build a new struct attribute type with given namespace and struct type name.
     * The struct type name will be used by Struct type resolver.
     *
     * @param _namespace
     * @param _structTypeName
     * @return
     * @throws NamespaceNotFoundException
     */
    StructType newStructAttributeType(String _namespace, String _structTypeName) throws NamespaceNotFoundException;

    /**
     * Add some packed struct attribute type.
     *
     * @param _structType
     * @throws NamespaceNotFoundException
     * @throws StructAttributeTypeAlreadyExistsException
     */
    void addStructAttributeType(StructType _structType) throws NamespaceNotFoundException, StructAttributeTypeAlreadyExistsException;
}

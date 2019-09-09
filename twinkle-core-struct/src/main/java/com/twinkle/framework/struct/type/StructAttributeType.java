package com.twinkle.framework.struct.type;

import com.twinkle.framework.struct.asm.descriptor.SAAttributeDescriptor;
import com.twinkle.framework.struct.error.*;

import java.util.Iterator;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/29/19 10:25 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface StructAttributeType extends StructType {
    int STRUCT_ID = 67108864;

    /**
     * Get the name of this struct type.
     *
     * @return
     */
    @Override
    String getName();

    /**
     * Get the published status of this struct type.
     *
     * @return
     */
    boolean isPublished();

    /**
     * Get the namespace of this struct type.
     *
     * @return
     * @throws IllegalStateException
     */
    String getNamespace() throws IllegalStateException;

    /**
     * Get the qualified name of this struct type.
     *
     * @return
     * @throws IllegalStateException
     */
    String getQualifiedName() throws IllegalStateException;

    /**
     * Get the struct type manager for this struct attribute.
     *
     * @return
     */
    StructTypeManager getTypeManager();

    /**
     * Get the attribute size of this struct attribute.
     *
     * @return
     */
    int size();

    /**
     * Scan the attributes of this struct attribute.
     *
     * @return
     */
    Iterator<SAAttributeDescriptor> getAttributes();

    /**
     * Judge the given attribte name existing in the struct attribute or not?
     *
     * @param _attrName
     * @return
     */
    boolean hasAttribute(String _attrName);

    /**
     * Get the attribute descriptor with given attribute name.
     *
     * @param _attrName
     * @return
     * @throws AttributeNotFoundException
     */
    SAAttributeDescriptor getAttribute(String _attrName) throws AttributeNotFoundException;

    /**
     * Add attribute into this struct attribute.
     *
     * @param _attrName
     * @param _attrTypeName
     * @param _optional
     * @throws AttributeAlreadyExistsException
     * @throws TypeNotFoundException
     * @throws BadAttributeNameException
     * @throws NamespaceNotFoundException
     * @throws StructAttributeTypeNotFoundException
     * @throws IllegalStateException
     */
    void addAttribute(String _attrName, String _attrTypeName, boolean _optional) throws AttributeAlreadyExistsException, TypeNotFoundException, BadAttributeNameException, NamespaceNotFoundException, StructAttributeTypeNotFoundException, IllegalStateException;
}

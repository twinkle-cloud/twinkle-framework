package com.twinkle.framework.core.lang.struct;

import com.twinkle.framework.core.datastruct.descriptor.AttributeDescriptor;
import com.twinkle.framework.core.error.*;

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

    String getName();

    boolean isPublished();

    String getNamespace() throws IllegalStateException;

    String getQualifiedName() throws IllegalStateException;

    StructTypeManager getTypeManager();

    int size();

    Iterator getAttributes();

    boolean hasAttribute(String _attrName);

    AttributeDescriptor getAttribute(String _attrName) throws AttributeNotFoundException;

    void addAttribute(String var1, String var2, boolean var3) throws AttributeAlreadyExistsException, TypeNotFoundException, BadAttributeNameException, NamespaceNotFoundException, StructAttributeTypeNotFoundException, IllegalStateException;

    String toString();

    boolean equals(Object _obj);
}

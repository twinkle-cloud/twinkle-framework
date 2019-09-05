package com.twinkle.framework.core.lang.struct;

import com.twinkle.framework.core.error.AttributeNotFoundException;
import com.twinkle.framework.core.error.AttributeTypeMismatchException;
import com.twinkle.framework.core.error.BadAttributeNameException;
import com.twinkle.framework.core.error.StructAttributeException;
import com.twinkle.framework.core.lang.ref.AttributeRef;
import com.twinkle.framework.core.lang.ref.DynamicAttributeRef;
import com.twinkle.framework.core.lang.util.ArrayAllocator;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/30/19 8:09 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface StructAttributeFactory {
    /**
     * Build new Struct Attribute with given type.
     *
     * @param _saType
     * @return
     * @throws StructAttributeException
     */
    StructAttribute newStructAttribute(StructAttributeType _saType) throws StructAttributeException;

    /**
     * Get the current array allocator.
     *
     * @return
     */
    ArrayAllocator getArrayAllocator();

    /**
     * Get the struct attribute ref with given type and Struct Attribute class name.
     *
     * @param _saType
     * @param _className
     * @return
     * @throws AttributeNotFoundException
     * @throws AttributeTypeMismatchException
     * @throws BadAttributeNameException
     */
    AttributeRef getAttributeRef(StructAttributeType _saType, String _className) throws AttributeNotFoundException, AttributeTypeMismatchException, BadAttributeNameException;

    /**
     * Get the composite attribute ref with given type and composite name.
     * the composite name will be scan in the CompositeName.
     *
     * @param _saType
     * @param _compositeName
     * @return
     * @throws AttributeNotFoundException
     * @throws AttributeTypeMismatchException
     * @throws BadAttributeNameException
     */
    AttributeRef getCompositeAttributeRef(StructAttributeType _saType, String _compositeName) throws AttributeNotFoundException, AttributeTypeMismatchException, BadAttributeNameException;

    /**
     * Get the Dynamic attribute ref with given type and composite name.
     * the composite name will be scan in the CompositeName.
     *
     * @param _saType
     * @param _compositeName
     * @return
     * @throws AttributeNotFoundException
     * @throws AttributeTypeMismatchException
     * @throws BadAttributeNameException
     */
    DynamicAttributeRef getDynamicAttributeRef(StructAttributeType _saType, String _compositeName) throws AttributeNotFoundException, AttributeTypeMismatchException, BadAttributeNameException;
}

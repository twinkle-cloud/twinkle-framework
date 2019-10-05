package com.twinkle.framework.struct.factory;

import com.twinkle.framework.struct.ref.AttributeRef;
import com.twinkle.framework.struct.ref.DynamicAttributeRef;
import com.twinkle.framework.struct.error.AttributeNotFoundException;
import com.twinkle.framework.struct.error.AttributeTypeMismatchException;
import com.twinkle.framework.struct.error.BadAttributeNameException;
import com.twinkle.framework.struct.error.StructAttributeException;
import com.twinkle.framework.struct.type.StructAttribute;
import com.twinkle.framework.struct.type.StructType;
import com.twinkle.framework.struct.util.ArrayAllocator;

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
    StructAttribute newStructAttribute(StructType _saType) throws StructAttributeException;

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
    AttributeRef getAttributeRef(StructType _saType, String _className) throws AttributeNotFoundException, AttributeTypeMismatchException, BadAttributeNameException;

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
    AttributeRef getCompositeAttributeRef(StructType _saType, String _compositeName) throws AttributeNotFoundException, AttributeTypeMismatchException, BadAttributeNameException;

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
    DynamicAttributeRef getDynamicAttributeRef(StructType _saType, String _compositeName) throws AttributeNotFoundException, AttributeTypeMismatchException, BadAttributeNameException;

    /**
     * Load the general bean class for StructAttribute Type with given StructAttributeType.
     * If some sub-attribute of this StructAttribute is StructAttributeType also, then will do load the sub-attribute as well.
     *
     * @param _saType
     * @return
     * @throws StructAttributeException
     */
    Class<?> loadGeneralBeanClass(StructType _saType)throws StructAttributeException;

    /**
     * Load the general bean class for StructAttribute Type with given type name.
     * @param _typeName
     * @return
     * @throws StructAttributeException
     */
    Class<?> loadGeneralBeanClass(String _typeName)throws StructAttributeException;
}

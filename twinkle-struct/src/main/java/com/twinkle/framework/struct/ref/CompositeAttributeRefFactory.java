package com.twinkle.framework.struct.ref;

import com.twinkle.framework.struct.context.StructAttributeManager;
import com.twinkle.framework.struct.error.AttributeNotFoundException;
import com.twinkle.framework.struct.error.AttributeTypeMismatchException;
import com.twinkle.framework.struct.error.BadAttributeNameException;
import com.twinkle.framework.struct.factory.StructAttributeFactory;
import com.twinkle.framework.struct.factory.StructAttributeFactoryImpl;
import com.twinkle.framework.struct.type.StructAttributeType;

import java.text.ParseException;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/30/19 8:06 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class CompositeAttributeRefFactory {
    public CompositeAttributeRefFactory() {
    }

    /**
     * Get composite attribute ref with given composite name.
     *
     * @param _saType
     * @param _compositeName
     * @return
     * @throws AttributeNotFoundException
     * @throws AttributeTypeMismatchException
     * @throws BadAttributeNameException
     */
    public static AttributeRef getCompositeAttributeRef(StructAttributeType _saType, String _compositeName) throws AttributeNotFoundException, AttributeTypeMismatchException, BadAttributeNameException {
        return getCompositeAttributeRef(StructAttributeManager.getStructAttributeFactory(), _saType, _compositeName);
    }

    /**
     * Get composite attribute ref with given composite name.
     *
     * @param _factory
     * @param _saType
     * @param _compositeName
     * @return
     * @throws AttributeNotFoundException
     * @throws AttributeTypeMismatchException
     * @throws BadAttributeNameException
     */
    public static AttributeRef getCompositeAttributeRef(StructAttributeFactory _factory, StructAttributeType _saType, String _compositeName) throws AttributeNotFoundException, AttributeTypeMismatchException, BadAttributeNameException {
        CompositeName tempName;
        try {
            tempName = new CompositeName(_compositeName);
        } catch (NullPointerException e) {
            throw new BadAttributeNameException("Attribute name is NULL: " + _compositeName, e);
        } catch (ParseException e) {
            throw new BadAttributeNameException("Attribute name is invalid: " + _compositeName, e);
        } catch (Exception e) {
            throw new BadAttributeNameException("Attribute name is invalid: " + _compositeName, e);
        }

        return getCompositeAttributeRef(_factory, _saType, tempName.head());
    }

    /**
     * Get the dynamic attribute ref with given composite name.
     *
     * @param _factory
     * @param _saType
     * @param _compositeName
     * @return
     * @throws AttributeNotFoundException
     * @throws AttributeTypeMismatchException
     * @throws BadAttributeNameException
     */
    public static DynamicAttributeRef getDynamicAttributeRef(StructAttributeFactory _factory, StructAttributeType _saType, String _compositeName) throws AttributeNotFoundException, AttributeTypeMismatchException, BadAttributeNameException {
        try {
            return new DynamicAttributeRefImpl(_saType, _compositeName, _factory);
        } catch (ParseException e) {
            throw new BadAttributeNameException("Attribute name is invalid: " + _compositeName, e);
        }
    }

    public static DynamicAttributeRef getDynamicAttributeRef(StructAttributeType _saType, String _compositeName) throws AttributeNotFoundException, AttributeTypeMismatchException, BadAttributeNameException {
        return getDynamicAttributeRef(StructAttributeManager.getStructAttributeFactory(), _saType, _compositeName);
    }
    /**
     * Build the composite attribute ref.
     *
     * @param _saFactory
     * @param _attrType
     * @param _compositeName
     * @return
     * @throws AttributeNotFoundException
     * @throws AttributeTypeMismatchException
     * @throws BadAttributeNameException
     */
    protected static AttributeRef getCompositeAttributeRef(StructAttributeFactory _saFactory, StructAttributeType _attrType, CompositeName _compositeName) throws AttributeNotFoundException, AttributeTypeMismatchException, BadAttributeNameException {
        CompositeName tempHeadName = _compositeName.head();
        if (tempHeadName.isTail()) {
            int tempHeadIndex = tempHeadName.index();
            String tempHNName = tempHeadName.name();
            if (tempHeadIndex < 0) {
                return ((StructAttributeFactoryImpl)_saFactory)._getAttributeRef(_attrType, tempHNName);
            } else {
                StructAttributeRef tempAttributeRef = (StructAttributeRef)((StructAttributeFactoryImpl)_saFactory)._getAttributeRef(_attrType, tempHNName);
                return new ArrayAttributeRefImpl(tempAttributeRef, tempHeadIndex, _saFactory);
            }
        } else {
            CompositeName tempName = tempHeadName.tail();
            return (tempName.index() < 0 ? new CompositeAttributeRefImpl(_attrType, tempHeadName, _saFactory) : new CompositeArrayAttributeRefImpl(_attrType, tempHeadName, _saFactory));
        }
    }
}

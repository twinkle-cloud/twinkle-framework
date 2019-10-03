package com.twinkle.framework.struct.ref;

import com.twinkle.framework.struct.context.StructAttributeSchemaManager;
import com.twinkle.framework.struct.asm.descriptor.SAAttributeDescriptor;
import com.twinkle.framework.struct.asm.descriptor.SAAttributeDescriptorImpl;
import com.twinkle.framework.core.lang.util.Array;
import com.twinkle.framework.struct.error.*;
import com.twinkle.framework.core.type.AttributeType;
import com.twinkle.framework.struct.util.MutableStructAttributeArray;
import com.twinkle.framework.struct.util.StructAttributeArray;
import com.twinkle.framework.struct.factory.StructAttributeFactory;
import com.twinkle.framework.struct.type.ArrayType;
import com.twinkle.framework.struct.type.StructAttribute;
import com.twinkle.framework.struct.type.StructType;

import java.text.ParseException;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/2/19 6:53 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class CompositeAttributeRefImpl implements StructAttributeRef, CompositeAttributeRef, Cloneable {
    private StructAttributeHierarchy head;
    protected StructAttributeHierarchy tail;
    private StructType baseType;
    protected StructAttributeRef attrRef;
    protected SAAttributeDescriptor descriptor;
    private StructAttributeFactory factory;

    public CompositeAttributeRefImpl(StructType _saType, String _compositeName) throws BadAttributeNameException, AttributeNotFoundException, ParseException {
        this(_saType, (new CompositeName(_compositeName)).head(), StructAttributeSchemaManager.getStructAttributeFactory());
    }

    protected CompositeAttributeRefImpl(StructType _saType, String _compositeName, StructAttributeFactory _factory) throws BadAttributeNameException, AttributeNotFoundException, ParseException {
        this(_saType, (new CompositeName(_compositeName)).head(), _factory);
    }

    /**
     * Build the CompositeAttributeRefImpl.
     *
     * @param _saType
     * @param _compositeName
     * @param _factory
     * @throws BadAttributeNameException
     * @throws AttributeNotFoundException
     */
    protected CompositeAttributeRefImpl(StructType _saType, CompositeName _compositeName, StructAttributeFactory _factory) throws BadAttributeNameException, AttributeNotFoundException {
        this.factory = _factory;
        try {
            this.head = new StructAttributeHierarchy(_saType, _compositeName, _factory);
        } catch (AttributeNotFoundException e) {
            throw e;
        } catch (NullPointerException | IllegalArgumentException | ClassCastException e) {
            throw new BadAttributeNameException(_compositeName.fullName(), e);
        } catch (Exception e) {
            throw new BadAttributeNameException(_compositeName.fullName(), e);
        }

        this.tail = this.head.tail();
        this.baseType = this.head.getBaseType();
        StructAttributeRef tempTailAttrRef = this.tail.getAttributeRef();
        AttributeType tempStructType = tempTailAttrRef.getType();
        int tempNameIndex = this.tail.getCompositeName().index();
        if (!tempStructType.isArrayType() && tempNameIndex >= 0) {
            throw new BadAttributeNameException(this.tail.getCompositeName().fullName());
        }
        StructType tempTailParentType = this.tail.getParentType();
        String tempCNName = this.tail.getCompositeName().name();
        SAAttributeDescriptor tempDescriptor = tempTailParentType.getAttribute(tempCNName);
        if (tempNameIndex >= 0) {
            this.descriptor = new SAAttributeDescriptorImpl(this.tail.getCompositeName().fullName(), ((ArrayType) tempStructType).getElementType(), tempDescriptor.isOptional());
            //Build an array attribute ref.
            this.attrRef = new ArrayAttributeRefImpl(tempTailAttrRef, tempNameIndex, _factory);
        } else {
            this.descriptor = new SAAttributeDescriptorImpl(this.tail.getCompositeName().fullName(), tempStructType, tempDescriptor.isOptional());
            this.attrRef = tempTailAttrRef;
        }
    }

    /**
     * Get the current struct attribute factory.
     *
     * @return
     */
    protected StructAttributeFactory getStructAttributeFactory() {
        return this.factory;
    }

    /**
     * Get the next struct attribute, if the attribute is empty and the createFlag = true,
     * then will build a new struct attribute, and put the new one as attribute ref.
     *
     * @param _attr
     * @param _hierarchy
     * @param _createFlag
     * @return
     */
    private StructAttribute getNextStructAttribute(StructAttribute _attr, StructAttributeHierarchy _hierarchy, boolean _createFlag) {
        CompositeName tempCompositeName = _hierarchy.getCompositeName();
        StructAttributeRef tempAttrRef = _hierarchy.getAttributeRef();
        int tempNameIndex = tempCompositeName.index();
        AttributeType tempStructType;
        int tempIndex;
        if (!_attr.isAttributeSet(tempAttrRef)) {
            if (!_createFlag) {
                throw new AttributeNotSetException(tempCompositeName.fullName());
            }
            AttributeType tempType = tempAttrRef.getType();
            if (tempNameIndex < 0) {
                StructAttribute tempAttr = this.factory.newStructAttribute((StructType) tempType);
                _attr.setStruct(tempAttrRef, tempAttr);
                return tempAttr;
            }
            tempStructType = ((ArrayType) tempType).getElementType();
            tempIndex = tempNameIndex + 1;
            MutableStructAttributeArray tempSAArray = this.factory.getArrayAllocator().newStructAttributeArray(tempIndex);
            tempSAArray.length(tempIndex, this.factory.newStructAttribute((StructType) tempStructType));
            _attr.setArray(tempAttrRef, tempSAArray);
            return tempSAArray.get(tempNameIndex);
        } else if (tempNameIndex < 0) {
            try {
                return _attr.getStruct(tempAttrRef);
            } catch (ClassCastException e) {
                throw new AttributeTypeMismatchException(tempCompositeName.fullName(), tempAttrRef.getType().getName(), "StructAttribute", e);
            }
        } else {
            StructAttributeArray tempSAArray;
            try {
                tempSAArray = (StructAttributeArray) _attr.getArray(tempAttrRef);
            } catch (ClassCastException e) {
                throw new AttributeTypeMismatchException(tempCompositeName.fullName(), tempAttrRef.getType().getName(), "StructAttribute[]", e);
            }

            int tempLength = tempSAArray.length();
            if (tempNameIndex < tempLength) {
                StructAttribute tempAttr = tempSAArray.get(tempNameIndex);
                if (tempAttr != null) {
                    return tempAttr;
                } else if (!_createFlag) {
                    throw new AttributeNotSetException(tempCompositeName.fullName());
                }
                AttributeType tempType = ((ArrayType) tempAttrRef.getType()).getElementType();
                StructAttribute tempNewAttr = this.factory.newStructAttribute((StructType) tempType);
                tempSAArray.put(tempNameIndex, tempNewAttr);
                return tempNewAttr;
            } else if (!_createFlag) {
                throw new AttributeNotSetException(tempCompositeName.fullName(), new ArrayIndexOutOfBoundsException(tempNameIndex));
            } else {
                tempStructType = ((ArrayType) tempAttrRef.getType()).getElementType();
                tempIndex = tempNameIndex + 1;
                tempSAArray.ensureCapacity(tempIndex);
                ((MutableStructAttributeArray) tempSAArray).length(tempIndex, this.factory.newStructAttribute((StructType) tempStructType));
                return tempSAArray.get(tempNameIndex);
            }
        }
    }

    /**
     * Retrieve the struct attribute from given _attr with the given hierarchy.
     *
     * @param _attr
     * @param _hierarchy
     * @return
     */
    private StructAttribute checkNextStructAttribute(StructAttribute _attr, StructAttributeHierarchy _hierarchy) {
        CompositeName tempCompositeName = _hierarchy.getCompositeName();
        StructAttributeRef tempAttrRef = _hierarchy.getAttributeRef();
        int tempNameIndex = tempCompositeName.index();
        if (!_attr.isAttributeSet(tempAttrRef)) {
            return null;
        } else if (tempNameIndex < 0) {
            try {
                return _attr.getStruct(tempAttrRef);
            } catch (ClassCastException e) {
                throw new AttributeTypeMismatchException(tempCompositeName.fullName(), tempAttrRef.getType().getName(), "StructAttribute", e);
            }
        }
        StructAttributeArray tempAttrArray;
        try {
            tempAttrArray = (StructAttributeArray) _attr.getArray(tempAttrRef);
        } catch (ClassCastException e) {
            throw new AttributeTypeMismatchException(tempCompositeName.fullName(), tempAttrRef.getType().getName(), "StructAttributeArray", e);
        }

        int tempLength = tempAttrArray.length();
        if (tempNameIndex < tempLength) {
            StructAttribute tempAttr = tempAttrArray.get(tempNameIndex);
            return tempAttr;
        }
        return null;
    }

    /**
     * Do check the given struct attribute.
     *
     * @param _attr
     * @return
     */
    private StructAttribute checkTailStructAttribute(StructAttribute _attr) {
        if (!this.baseType.equals(_attr.getType())) {
            throw new AttributeTypeMismatchException("", _attr.getType().getQualifiedName(), this.baseType.getQualifiedName());
        }
        StructAttributeHierarchy tempHead = this.head;
        StructAttribute tempAttr;
        for (tempAttr = _attr; !tempHead.isTail(); tempHead = tempHead.next()) {
            tempAttr = this.checkNextStructAttribute(tempAttr, tempHead);
            if (tempAttr == null) {
                return null;
            }
        }

        return tempAttr;
    }

    @Override
    public String getName() {
        return this.tail.getCompositeName().fullName();
    }

    @Override
    public AttributeType getType() {
        return this.attrRef.getType();
    }

    @Override
    public SAAttributeDescriptor getDescriptor() {
        return this.descriptor;
    }

    @Override
    public boolean isComposite() {
        return true;
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public String getTailPath() {
        return this.tail.isHead() ? "" : this.tail.previous().getCompositeName().fullName();
    }

    @Override
    public StructType getTailType() {
        return this.tail.getParentType();
    }

    @Override
    public AttributeRef getTailAttributeRef() {
        return this.attrRef;
    }

    @Override
    public StructAttribute getTailStructAttribute(StructAttribute _attr) throws AttributeNotSetException {
        return this.getTailStructAttribute(_attr, false);
    }

    @Override
    public StructAttribute getTailStructAttribute(StructAttribute _attr, boolean _createFlag) {
        if (!this.baseType.equals(_attr.getType())) {
            throw new AttributeTypeMismatchException("", _attr.getType().getQualifiedName(), this.baseType.getQualifiedName());
        } else {
            StructAttributeHierarchy tempHead = this.head;
            StructAttribute tempAttr;
            for (tempAttr = _attr; !tempHead.isTail(); tempHead = tempHead.next()) {
                tempAttr = this.getNextStructAttribute(tempAttr, tempHead, _createFlag);
                if (tempAttr == null) {
                    throw new AttributeNotSetException(tempHead.getCompositeName().fullName());
                }
            }
            return tempAttr;
        }
    }

    @Override
    public void clear(StructAttribute _attr) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        StructAttribute tempAttr = this.checkTailStructAttribute(_attr);
        if (tempAttr != null) {
            this.attrRef.clear(tempAttr);
        }
    }

    @Override
    public boolean isAttributeSet(StructAttribute _attr) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        StructAttribute tempAttr = this.checkTailStructAttribute(_attr);
        return tempAttr != null && this.attrRef.isAttributeSet(tempAttr);
    }

    @Override
    public byte getByte(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        StructAttribute tempAttr = this.getTailStructAttribute(_attr, false);
        return this.attrRef.getByte(tempAttr);
    }

    @Override
    public void setByte(StructAttribute _attr, byte _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        StructAttribute tempAttr = this.getTailStructAttribute(_attr, true);
        this.attrRef.setByte(tempAttr, _value);
    }

    @Override
    public short getShort(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        StructAttribute tempAttr = this.getTailStructAttribute(_attr, false);
        return this.attrRef.getShort(tempAttr);
    }

    @Override
    public void setShort(StructAttribute _attr, short _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        StructAttribute tempAttr = this.getTailStructAttribute(_attr, true);
        this.attrRef.setShort(tempAttr, _value);
    }

    @Override
    public int getInt(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        StructAttribute tempAttr = this.getTailStructAttribute(_attr, false);
        return this.attrRef.getInt(tempAttr);
    }

    @Override
    public void setInt(StructAttribute _attr, int _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        StructAttribute tempAttr = this.getTailStructAttribute(_attr, true);
        this.attrRef.setInt(tempAttr, _value);
    }

    @Override
    public long getLong(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        StructAttribute tempAttr = this.getTailStructAttribute(_attr, false);
        return this.attrRef.getLong(tempAttr);
    }

    @Override
    public void setLong(StructAttribute _attr, long _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        StructAttribute tempAttr = this.getTailStructAttribute(_attr, true);
        this.attrRef.setLong(tempAttr, _value);
    }

    @Override
    public char getChar(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        StructAttribute tempAttr = this.getTailStructAttribute(_attr, false);
        return this.attrRef.getChar(tempAttr);
    }

    @Override
    public void setChar(StructAttribute _attr, char _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        StructAttribute tempAttr = this.getTailStructAttribute(_attr, true);
        this.attrRef.setChar(tempAttr, _value);
    }

    @Override
    public boolean getBoolean(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        StructAttribute tempAttr = this.getTailStructAttribute(_attr, false);
        return this.attrRef.getBoolean(tempAttr);
    }

    @Override
    public void setBoolean(StructAttribute _attr, boolean _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        StructAttribute tempAttr = this.getTailStructAttribute(_attr, true);
        this.attrRef.setBoolean(tempAttr, _value);
    }

    @Override
    public float getFloat(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        StructAttribute tempAttr = this.getTailStructAttribute(_attr, false);
        return this.attrRef.getFloat(tempAttr);
    }

    @Override
    public void setFloat(StructAttribute _attr, float _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        StructAttribute tempAttr = this.getTailStructAttribute(_attr, true);
        this.attrRef.setFloat(tempAttr, _value);
    }

    @Override
    public double getDouble(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        StructAttribute tempAttr = this.getTailStructAttribute(_attr, false);
        return this.attrRef.getDouble(tempAttr);
    }

    @Override
    public void setDouble(StructAttribute _attr, double _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        StructAttribute tempAttr = this.getTailStructAttribute(_attr, true);
        this.attrRef.setDouble(tempAttr, _value);
    }

    @Override
    public String getString(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        StructAttribute tempAttr = this.getTailStructAttribute(_attr, false);
        return this.attrRef.getString(tempAttr);
    }

    @Override
    public void setString(StructAttribute _attr, String _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        StructAttribute tempAttr = this.getTailStructAttribute(_attr, true);
        this.attrRef.setString(tempAttr, _value);
    }

    @Override
    public StructAttribute getStruct(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        StructAttribute tempAttr = this.getTailStructAttribute(_attr, false);
        return this.attrRef.getStruct(tempAttr);
    }

    @Override
    public void setStruct(StructAttribute _attr, StructAttribute _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        StructAttribute tempAttr = this.getTailStructAttribute(_attr, true);
        this.attrRef.setStruct(tempAttr, _value);
    }

    @Override
    public Array getArray(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        StructAttribute tempAttr = this.getTailStructAttribute(_attr, false);
        return this.attrRef.getArray(tempAttr);
    }

    @Override
    public void setArray(StructAttribute _attr, Array _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException, ClassCastException {
        StructAttribute tempAttr = this.getTailStructAttribute(_attr, true);
        this.attrRef.setArray(tempAttr, _value);
    }

    @Override
    public int getArraySize(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        StructAttribute tempAttr = this.getTailStructAttribute(_attr, false);
        return this.attrRef.getArraySize(tempAttr);
    }

    @Override
    public void copy(StructAttribute _srcAttr, StructAttribute _destAttr) throws StructAttributeCopyException, AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        StructAttribute tempSrcAttr = this.getTailStructAttribute(_srcAttr, false);
        StructAttribute tempDestAttr = this.getTailStructAttribute(_destAttr, true);
        this.attrRef.copy(tempSrcAttr, tempDestAttr);
    }


    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public int hashCode() {
        return this.descriptor.hashCode();
    }

    @Override
    public boolean equals(Object _obj) {
        if (this == _obj) {
            return true;
        } else if (_obj != null && this.getClass() == _obj.getClass()) {
            CompositeAttributeRefImpl tempRefObj = (CompositeAttributeRefImpl) _obj;
            return this.descriptor.equals(tempRefObj.descriptor);
        } else {
            return false;
        }
    }
}

package com.twinkle.framework.core.lang.struct;

import com.twinkle.framework.core.error.*;
import com.twinkle.framework.core.lang.ref.AttributeRef;
import com.twinkle.framework.core.lang.ref.CompositeAttributeRefFactory;
import com.twinkle.framework.core.lang.ref.StructAttributeRef;
import com.twinkle.framework.core.lang.util.*;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/30/19 8:01 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public abstract class AbstractStructAttribute implements StructAttribute, Cloneable {
    private static final ArrayAllocator ARRAY_ALLOCATOR = new ArrayAllocatorImpl();

    protected AbstractStructAttribute() {
    }

    protected abstract StructAttributeRef _getAttributeRef(String _attrName);

    @Override
    public AttributeRef getAttributeRef(String _attrName) throws AttributeNotFoundException, AttributeTypeMismatchException, BadAttributeNameException {
        if (_attrName == null) {
            throw new BadAttributeNameException("Attribute name is NULL");
        } else {
            return (AttributeRef) (!this.hasAttribute(_attrName) ? CompositeAttributeRefFactory.getCompositeAttributeRef(this.getType(), _attrName) : this._getAttributeRef(_attrName));
        }
    }

    @Override
    public StructAttribute duplicate() throws StructAttributeCopyException {
        try {
            return (StructAttribute) this.clone();
        } catch (CloneNotSupportedException e) {
            throw new StructAttributeCopyException(e.getMessage(), e);
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public void clear(AttributeRef _attrRef) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        ((StructAttributeRef) _attrRef).clear(this);
    }

    @Override
    public boolean isAttributeSet(AttributeRef _attrRef) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        return ((StructAttributeRef) _attrRef).isAttributeSet(this);
    }

    @Override
    public byte getByte(AttributeRef _attrRef) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        return ((StructAttributeRef) _attrRef).getByte(this);
    }

    @Override
    public void setByte(AttributeRef _attrRef, byte _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        ((StructAttributeRef) _attrRef).setByte(this, _value);
    }

    @Override
    public short getShort(AttributeRef _attrRef) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        return ((StructAttributeRef) _attrRef).getShort(this);
    }

    @Override
    public void setShort(AttributeRef _attrRef, short _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        ((StructAttributeRef) _attrRef).setShort(this, _value);
    }

    @Override
    public int getInt(AttributeRef _attrRef) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        return ((StructAttributeRef) _attrRef).getInt(this);
    }

    @Override
    public void setInt(AttributeRef _attrRef, int _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        ((StructAttributeRef) _attrRef).setInt(this, _value);
    }

    @Override
    public long getLong(AttributeRef _attrRef) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        return ((StructAttributeRef) _attrRef).getLong(this);
    }

    @Override
    public void setLong(AttributeRef _attrRef, long _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        ((StructAttributeRef) _attrRef).setLong(this, _value);
    }

    @Override
    public char getChar(AttributeRef _attrRef) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        return ((StructAttributeRef) _attrRef).getChar(this);
    }

    @Override
    public void setChar(AttributeRef _attrRef, char _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        ((StructAttributeRef) _attrRef).setChar(this, _value);
    }

    @Override
    public boolean getBoolean(AttributeRef _attrRef) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        return ((StructAttributeRef) _attrRef).getBoolean(this);
    }

    @Override
    public void setBoolean(AttributeRef _attrRef, boolean _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        ((StructAttributeRef) _attrRef).setBoolean(this, _value);
    }

    @Override
    public float getFloat(AttributeRef _attrRef) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        return ((StructAttributeRef) _attrRef).getFloat(this);
    }

    @Override
    public void setFloat(AttributeRef _attrRef, float _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        ((StructAttributeRef) _attrRef).setFloat(this, _value);
    }

    @Override
    public double getDouble(AttributeRef _attrRef) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        return ((StructAttributeRef) _attrRef).getDouble(this);
    }

    @Override
    public void setDouble(AttributeRef _attrRef, double _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        ((StructAttributeRef) _attrRef).setDouble(this, _value);
    }

    @Override
    public String getString(AttributeRef _attrRef) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        return ((StructAttributeRef) _attrRef).getString(this);
    }

    @Override
    public void setString(AttributeRef _attrRef, String _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        ((StructAttributeRef) _attrRef).setString(this, _value);
    }

    @Override
    public StructAttribute getStruct(AttributeRef _attrRef) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        return ((StructAttributeRef) _attrRef).getStruct(this);
    }

    @Override
    public void setStruct(AttributeRef _attrRef, StructAttribute _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        ((StructAttributeRef) _attrRef).setStruct(this, _value);
    }

    @Override
    public Array getArray(AttributeRef _attrRef) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        return ((StructAttributeRef) _attrRef).getArray(this);
    }

    @Override
    public void setArray(AttributeRef _attrRef, Array _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException, ClassCastException {
        ((StructAttributeRef) _attrRef).setArray(this, _value);
    }

    @Override
    public int getArraySize(AttributeRef _attrRef) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        return ((StructAttributeRef) _attrRef).getArraySize(this);
    }

    /**
     * This method will be invoked by designed sub-StructAttribute class to wrap the array.
     * Refer to @StructAttributeClassDesigner.
     * protected MethodVisitor addObjectClassConstructorDefinition(MethodVisitor _visitor, String _className, AttributeDef _attrDef, int _parameterSize)
     *
     * @param _array
     * @return
     */
    protected static ByteArray _byteArray(byte[] _array) {
        return ARRAY_ALLOCATOR.wrap(_array.clone());
    }

    /**
     * This method will be invoked by designed sub-StructAttribute class to wrap the array.
     * Refer to @StructAttributeClassDesigner.
     * protected MethodVisitor addObjectClassConstructorDefinition(MethodVisitor _visitor, String _className, AttributeDef _attrDef, int _parameterSize)
     *
     * @param _array
     * @return
     */
    protected static ShortArray _shortArray(short[] _array) {
        return ARRAY_ALLOCATOR.wrap(_array.clone());
    }

    /**
     * This method will be invoked by designed sub-StructAttribute class to wrap the array.
     * Refer to @StructAttributeClassDesigner.
     * protected MethodVisitor addObjectClassConstructorDefinition(MethodVisitor _visitor, String _className, AttributeDef _attrDef, int _parameterSize)
     *
     * @param _array
     * @return
     */
    protected static IntegerArray _intArray(int[] _array) {
        return ARRAY_ALLOCATOR.wrap(_array.clone());
    }

    /**
     * This method will be invoked by designed sub-StructAttribute class to wrap the array.
     * Refer to @StructAttributeClassDesigner.
     * protected MethodVisitor addObjectClassConstructorDefinition(MethodVisitor _visitor, String _className, AttributeDef _attrDef, int _parameterSize)
     *
     * @param _array
     * @return
     */
    protected static LongArray _longArray(long[] _array) {
        return ARRAY_ALLOCATOR.wrap(_array.clone());
    }

    /**
     * This method will be invoked by designed sub-StructAttribute class to wrap the array.
     * Refer to @StructAttributeClassDesigner.
     * protected MethodVisitor addObjectClassConstructorDefinition(MethodVisitor _visitor, String _className, AttributeDef _attrDef, int _parameterSize)
     *
     * @param _array
     * @return
     */
    protected static CharArray _charArray(char[] _array) {
        return ARRAY_ALLOCATOR.wrap(_array.clone());
    }

    /**
     * This method will be invoked by designed sub-StructAttribute class to wrap the array.
     * Refer to @StructAttributeClassDesigner.
     * protected MethodVisitor addObjectClassConstructorDefinition(MethodVisitor _visitor, String _className, AttributeDef _attrDef, int _parameterSize)
     *
     * @param _array
     * @return
     */
    protected static BooleanArray _booleanArray(boolean[] _array) {
        return ARRAY_ALLOCATOR.wrap((boolean[]) _array.clone());
    }

    /**
     * This method will be invoked by designed sub-StructAttribute class to wrap the array.
     * Refer to @StructAttributeClassDesigner.
     * protected MethodVisitor addObjectClassConstructorDefinition(MethodVisitor _visitor, String _className, AttributeDef _attrDef, int _parameterSize)
     *
     * @param _array
     * @return
     */
    protected static FloatArray _floatArray(float[] _array) {
        return ARRAY_ALLOCATOR.wrap(_array.clone());
    }

    /**
     * This method will be invoked by designed sub-StructAttribute class to wrap the array.
     * Refer to @StructAttributeClassDesigner.
     * protected MethodVisitor addObjectClassConstructorDefinition(MethodVisitor _visitor, String _className, AttributeDef _attrDef, int _parameterSize)
     *
     * @param _array
     * @return
     */
    protected static DoubleArray _doubleArray(double[] _array) {
        return ARRAY_ALLOCATOR.wrap(_array.clone());
    }

    /**
     * This method will be invoked by designed sub-StructAttribute class to wrap the array.
     * Refer to @StructAttributeClassDesigner.
     * protected MethodVisitor addObjectClassConstructorDefinition(MethodVisitor _visitor, String _className, AttributeDef _attrDef, int _parameterSize)
     *
     * @param _array
     * @return
     */
    protected static StringArray _stringArray(String[] _array) {
        return ARRAY_ALLOCATOR.wrap(_array.clone());
    }

    /**
     * This method will be invoked by designed sub-StructAttribute class to wrap the array.
     * Refer to @StructAttributeClassDesigner.
     * protected MethodVisitor addObjectClassConstructorDefinition(MethodVisitor _visitor, String _className, AttributeDef _attrDef, int _parameterSize)
     *
     * @param _array
     * @return
     */
    protected static StructAttributeArray _structArray(StructAttribute[] _array) {
        return ARRAY_ALLOCATOR.wrap(_array.clone());
    }

}

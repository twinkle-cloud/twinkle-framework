package com.twinkle.framework.struct.ref;

import com.twinkle.framework.struct.asm.descriptor.SAAttributeDescriptor;
import com.twinkle.framework.core.lang.util.Array;
import com.twinkle.framework.struct.error.*;
import com.twinkle.framework.struct.type.*;
import com.twinkle.framework.struct.util.StructAttributeArray;
import com.twinkle.framework.struct.utils.StructTypeUtil;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/1/19 10:12 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class SAAttributeRefHandleImpl extends AbstractAttributeRef {
    private static final MethodHandles.Lookup METHOD_LOOKUP = MethodHandles.lookup();
    private final MethodHandle getterMethod;
    private final MethodHandle setterMethod;
    private final MethodHandle getterFlagMethod;
    private final MethodHandle clearMethod;

    public SAAttributeRefHandleImpl(SAAttributeDescriptor _attrDescriptor, Class _class, String _methodName, String _getFlagMethodName) {
        super(_attrDescriptor);
        String tempSetterName = setterName(_methodName);
        StructType tempAttrDesType = _attrDescriptor.getType();
        Class<?> tempReturnTypeClass;
        Method tempMethod;
        MethodType tempMethodType;
        if (tempAttrDesType.isStructType()) {
            try {
                tempMethod = _class.getMethod(_methodName);
                this.getterMethod = METHOD_LOOKUP.unreflect(tempMethod);
                tempReturnTypeClass = tempMethod.getReturnType();
            } catch (NoSuchMethodException e) {
                throw new BadAttributeNameException("Getter not found: " + _methodName, e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                tempReturnTypeClass = StructTypeUtil.getTypeClass(tempAttrDesType);
                tempMethodType = MethodType.methodType(tempReturnTypeClass);
                this.getterMethod = this.getMethodHandle(_class, _methodName, tempMethodType);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        if (tempAttrDesType.isStructType()) {
            try {
                tempMethod = _class.getMethod(tempSetterName, tempReturnTypeClass);
                this.setterMethod = METHOD_LOOKUP.unreflect(tempMethod);
            } catch (NoSuchMethodException e) {
                throw new BadAttributeNameException("Setter not found: " + tempSetterName + "(" + tempReturnTypeClass.getName() + ")", e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        } else {
            tempMethodType = MethodType.methodType(Void.TYPE, tempReturnTypeClass);
            this.setterMethod = this.getMethodHandle(_class, tempSetterName, tempMethodType);
        }

        tempMethodType = MethodType.methodType(Boolean.TYPE);
        this.getterFlagMethod = this.getMethodHandle(_class, _getFlagMethodName, tempMethodType);

        tempMethodType = MethodType.methodType(Void.TYPE, String.class);
        this.clearMethod = this.getMethodHandle(_class, "clear", tempMethodType);
    }

    /**
     * Pack the method handle.
     *
     * @param _class
     * @param _methodName
     * @param _methodType
     * @return
     */
    private MethodHandle getMethodHandle(Class _class, String _methodName, MethodType _methodType) {
        try {
            return METHOD_LOOKUP.findVirtual(_class, _methodName, _methodType);
        } catch (NoSuchMethodException e) {
            throw new BadAttributeNameException("Method [" + _methodName + "]not found.", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    private void handleException(StructAttribute _attr, String _typeName, Throwable _exception) throws AttributeNotFoundException, AttributeTypeMismatchException {
        if (_exception instanceof UnsupportedOperationException) {
            throw (UnsupportedOperationException) _exception;
        } else if (_attr.getType().hasAttribute(this.descriptor.getName())) {
            throw new AttributeTypeMismatchException(this.descriptor.getName(), this.getType().getName(), _typeName, _exception);
        } else {
            throw new AttributeNotFoundException(this.descriptor.getName(), _exception);
        }
    }

    @Override
    public boolean isComposite() {
        return false;
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public void clear(StructAttribute _attr) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        try {
            this.clearMethod.invoke(_attr, this.descriptor.getName());
        } catch (UnsupportedOperationException e) {
            throw e;
        } catch (Throwable e) {
            throw new AttributeNotFoundException(this.descriptor.getName(), e);
        }
    }

    @Override
    public boolean isAttributeSet(StructAttribute _attr) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        try {
            return (boolean) this.getterFlagMethod.invoke(_attr);
        } catch (Throwable e) {
            throw new AttributeNotFoundException(this.descriptor.getName(), e);
        }
    }

    @Override
    public byte getByte(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        boolean tempGetterFlag;
        try {
            tempGetterFlag = (boolean) this.getterFlagMethod.invoke(_attr);
        } catch (Throwable e) {
            this.handleException(_attr, PrimitiveType.BYTE.getName(), e);
            return 0;
        }

        if (!tempGetterFlag) {
            throw new AttributeNotSetException(this.descriptor.getName());
        }
        try {
            return (byte) this.getterMethod.invoke(_attr);
        } catch (Throwable e) {
            this.handleException(_attr, PrimitiveType.BYTE.getName(), e);
            return 0;
        }
    }

    @Override
    public void setByte(StructAttribute _attr, byte _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        try {
            this.setterMethod.invoke(_attr, _value);
        } catch (Throwable e) {
            this.handleException(_attr, PrimitiveType.BYTE.getName(), e);
        }
    }

    @Override
    public short getShort(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        boolean tempGetterFlag;
        try {
            tempGetterFlag = (boolean) this.getterFlagMethod.invoke(_attr);
        } catch (Throwable e) {
            this.handleException(_attr, PrimitiveType.SHORT.getName(), e);
            return 0;
        }

        if (!tempGetterFlag) {
            throw new AttributeNotSetException(this.descriptor.getName());
        }
        try {
            return (short) this.getterMethod.invoke(_attr);
        } catch (Throwable e) {
            this.handleException(_attr, PrimitiveType.SHORT.getName(), e);
            return 0;
        }
    }

    @Override
    public void setShort(StructAttribute _attr, short _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        try {
            this.setterMethod.invoke(_attr, _value);
        } catch (Throwable e) {
            this.handleException(_attr, PrimitiveType.SHORT.getName(), e);
        }
    }

    @Override
    public int getInt(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        boolean tempGetterFlag;
        try {
            tempGetterFlag = (boolean) this.getterFlagMethod.invoke(_attr);
        } catch (Throwable e) {
            this.handleException(_attr, PrimitiveType.INT.getName(), e);
            return 0;
        }

        if (!tempGetterFlag) {
            throw new AttributeNotSetException(this.descriptor.getName());
        }
        try {
            return (int) this.getterMethod.invoke(_attr);
        } catch (Throwable e) {
            this.handleException(_attr, PrimitiveType.INT.getName(), e);
            return 0;
        }
    }

    @Override
    public void setInt(StructAttribute _attr, int _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        try {
            this.setterMethod.invoke(_attr, _value);
        } catch (Throwable e) {
            this.handleException(_attr, PrimitiveType.INT.getName(), e);
        }
    }

    @Override
    public long getLong(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        boolean tempGetterFlag;
        try {
            tempGetterFlag = (boolean) this.getterFlagMethod.invoke(_attr);
        } catch (Throwable e) {
            this.handleException(_attr, PrimitiveType.LONG.getName(), e);
            return 0L;
        }

        if (!tempGetterFlag) {
            throw new AttributeNotSetException(this.descriptor.getName());
        }
        try {
            return (long) this.getterMethod.invoke(_attr);
        } catch (Throwable e) {
            this.handleException(_attr, PrimitiveType.LONG.getName(), e);
            return 0L;
        }
    }

    @Override
    public void setLong(StructAttribute _attr, long _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        try {
            this.setterMethod.invoke(_attr, _value);
        } catch (Throwable e) {
            this.handleException(_attr, PrimitiveType.LONG.getName(), e);
        }
    }

    @Override
    public char getChar(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        boolean tempGetterFlag;
        try {
            tempGetterFlag = (boolean) this.getterFlagMethod.invoke(_attr);
        } catch (Throwable e) {
            this.handleException(_attr, PrimitiveType.CHAR.getName(), e);
            return '\u0000';
        }

        if (!tempGetterFlag) {
            throw new AttributeNotSetException(this.descriptor.getName());
        }
        try {
            return (char) this.getterMethod.invoke(_attr);
        } catch (Throwable e) {
            this.handleException(_attr, PrimitiveType.CHAR.getName(), e);
            return '\u0000';
        }
    }

    @Override
    public void setChar(StructAttribute _attr, char _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        try {
            this.setterMethod.invoke(_attr, _value);
        } catch (Throwable e) {
            this.handleException(_attr, PrimitiveType.CHAR.getName(), e);
        }
    }

    @Override
    public boolean getBoolean(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        boolean tempGetterFlag;
        try {
            tempGetterFlag = (boolean) this.getterFlagMethod.invoke(_attr);
        } catch (Throwable e) {
            this.handleException(_attr, PrimitiveType.BOOLEAN.getName(), e);
            return false;
        }

        if (!tempGetterFlag) {
            throw new AttributeNotSetException(this.descriptor.getName());
        }
        try {
            return (boolean) this.getterMethod.invoke(_attr);
        } catch (Throwable e) {
            this.handleException(_attr, PrimitiveType.BOOLEAN.getName(), e);
            return false;
        }
    }

    @Override
    public void setBoolean(StructAttribute _attr, boolean _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        try {
            this.setterMethod.invoke(_attr, _value);
        } catch (Throwable e) {
            this.handleException(_attr, PrimitiveType.BOOLEAN.getName(), e);
        }
    }

    @Override
    public float getFloat(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        boolean tempGetterFlag;
        try {
            tempGetterFlag = (boolean) this.getterFlagMethod.invoke(_attr);
        } catch (Throwable e) {
            this.handleException(_attr, PrimitiveType.FLOAT.getName(), e);
            return 0.0F;
        }

        if (!tempGetterFlag) {
            throw new AttributeNotSetException(this.descriptor.getName());
        }
        try {
            return (float) this.getterMethod.invoke(_attr);
        } catch (Throwable e) {
            this.handleException(_attr, PrimitiveType.FLOAT.getName(), e);
            return 0.0F;
        }
    }

    @Override
    public void setFloat(StructAttribute _attr, float _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        try {
            this.setterMethod.invoke(_attr, _value);
        } catch (Throwable e) {
            this.handleException(_attr, PrimitiveType.FLOAT.getName(), e);
        }
    }

    @Override
    public double getDouble(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        boolean tempGetterFlag;
        try {
            tempGetterFlag = (boolean) this.getterFlagMethod.invoke(_attr);
        } catch (Throwable e) {
            this.handleException(_attr, PrimitiveType.DOUBLE.getName(), e);
            return 0.0D;
        }

        if (!tempGetterFlag) {
            throw new AttributeNotSetException(this.descriptor.getName());
        }
        try {
            return (double) this.getterMethod.invoke(_attr);
        } catch (Throwable e) {
            this.handleException(_attr, PrimitiveType.DOUBLE.getName(), e);
            return 0.0D;
        }
    }

    @Override
    public void setDouble(StructAttribute _attr, double _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        try {
            this.setterMethod.invoke(_attr, _value);
        } catch (Throwable e) {
            this.handleException(_attr, PrimitiveType.DOUBLE.getName(), e);
        }
    }

    @Override
    public String getString(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        boolean tempGetterFlag;
        try {
            tempGetterFlag = (boolean) this.getterFlagMethod.invoke(_attr);
        } catch (Throwable e) {
            this.handleException(_attr, StringType.STRING.getName(), e);
            return null;
        }

        if (!tempGetterFlag) {
            throw new AttributeNotSetException(this.descriptor.getName());
        }
        try {
            return (String) this.getterMethod.invoke(_attr);
        } catch (Throwable e) {
            this.handleException(_attr, StringType.STRING.getName(), e);
            return null;
        }
    }

    @Override
    public void setString(StructAttribute _attr, String _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        try {
            this.setterMethod.invoke(_attr, _value);
        } catch (Throwable e) {
            this.handleException(_attr, StringType.STRING.getName(), e);
        }
    }

    @Override
    public StructAttribute getStruct(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        boolean tempGetterFlag;
        try {
            tempGetterFlag = (boolean) this.getterFlagMethod.invoke(_attr);
        } catch (Throwable e) {
            this.handleException(_attr, "StructAttribute", e);
            return null;
        }

        if (!tempGetterFlag) {
            throw new AttributeNotSetException(this.descriptor.getName());
        }
        try {
            return (StructAttribute) this.getterMethod.invoke(_attr);
        } catch (Throwable e) {
            this.handleException(_attr, "StructAttribute", e);
            return null;
        }
    }

    @Override
    public void setStruct(StructAttribute _attr, StructAttribute _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        try {
            this.setterMethod.invoke(_attr, _value);
        } catch (Throwable e) {
            this.handleException(_attr, _value.getType().getName(), e);
        }
    }

    @Override
    public Array getArray(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        boolean tempGetterFlag;
        try {
            tempGetterFlag = (boolean) this.getterFlagMethod.invoke(_attr);
        } catch (Throwable e) {
            this.handleException(_attr, "Array", e);
            return null;
        }

        if (!tempGetterFlag) {
            throw new AttributeNotSetException(this.descriptor.getName());
        }
        try {
            return (Array) this.getterMethod.invoke(_attr);
        } catch (Throwable e) {
            this.handleException(_attr, "Array", e);
            return null;
        }
    }

    @Override
    public void setArray(StructAttribute _attr, Array _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException, ClassCastException {
        try {
            this.setterMethod.invoke(_attr, _value);
        } catch (Throwable e) {
            this.handleException(_attr, _value.getClass().getSimpleName(), e);
        }
    }

    @Override
    public int getArraySize(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        boolean tempGetterFlag;
        try {
            tempGetterFlag = (boolean) this.getterFlagMethod.invoke(_attr);
        } catch (Throwable e) {
            this.handleException(_attr, "Array", e);
            return 0;
        }

        if (!tempGetterFlag) {
            throw new AttributeNotSetException(this.descriptor.getName());
        }
        try {
            Array tempArray = (Array) this.getterMethod.invoke(_attr);
            return tempArray.length();
        } catch (Throwable e) {
            this.handleException(_attr, "Array", e);
            return 0;
        }
    }

    @Override
    public void copy(StructAttribute _srcAttr, StructAttribute _destAttr) throws StructAttributeCopyException, AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
        if (!this.isAttributeSet(_srcAttr)) {
            this.clear(_destAttr);
            return;
        }

        StructType tempStructType = this.getType();
        try {
            if (tempStructType.isStructType()) {
                StructAttribute tempAttr = this.getStruct(_srcAttr);
                this.setStruct(_destAttr, tempAttr.duplicate());
            } else if (tempStructType.isArrayType()) {
                Array tempSrcArray = this.getArray(_srcAttr);
                Array tempCloneSrcArray = (Array) tempSrcArray.clone();
                this.setArray(_destAttr, tempCloneSrcArray);
                StructType tempElementType = ((ArrayType) tempStructType).getElementType();
                if (tempElementType.isStructType()) {
                    StructAttributeArray tempSrcSAArray = (StructAttributeArray) tempSrcArray;
                    StructAttributeArray tempCloneSrcSAArray = (StructAttributeArray) tempCloneSrcArray;
                    int tempSrcSAArrayLength = tempSrcSAArray.length();

                    for (int i = 0; i < tempSrcSAArrayLength; ++i) {
                        if (tempSrcSAArray.get(i) != null) {
                            tempCloneSrcSAArray.put(i, tempSrcSAArray.get(i).duplicate());
                        } else {
                            tempCloneSrcSAArray.put(i, null);
                        }
                    }
                }
            } else if (tempStructType.isStringType()) {
                this.setString(_destAttr, this.getString(_srcAttr));
            } else {
                if (!tempStructType.isPrimitiveType()) {
                    throw new StructAttributeCopyException("Unsupported attribute type: " + tempStructType.getName());
                }
                switch (tempStructType.getID()) {
                    case PrimitiveType.BYTE_ID:
                        this.setByte(_destAttr, this.getByte(_srcAttr));
                        break;
                    case PrimitiveType.SHORT_ID:
                        this.setShort(_destAttr, this.getShort(_srcAttr));
                        break;
                    case PrimitiveType.INT_ID:
                        this.setInt(_destAttr, this.getInt(_srcAttr));
                        break;
                    case PrimitiveType.LONG_ID:
                        this.setLong(_destAttr, this.getLong(_srcAttr));
                        break;
                    case PrimitiveType.CHAR_ID:
                        this.setChar(_destAttr, this.getChar(_srcAttr));
                        break;
                    case PrimitiveType.BOOLEAN_ID:
                        this.setBoolean(_destAttr, this.getBoolean(_srcAttr));
                        break;
                    case 7:
                    case 8:
                    default:
                        throw new StructAttributeCopyException("Unsupported attribute type: " + tempStructType.getName());
                    case PrimitiveType.FLOAT_ID:
                        this.setFloat(_destAttr, this.getFloat(_srcAttr));
                        break;
                    case PrimitiveType.DOUBLE_ID:
                        this.setDouble(_destAttr, this.getDouble(_srcAttr));
                }
            }
        } catch (ClassCastException e) {
            throw new AttributeTypeMismatchException(this.getName(), this.getType().getName(), "", e);
        } catch (CloneNotSupportedException e) {
            throw new StructAttributeCopyException("Failed to clone attribute value for: " + this.getName(), e);
        }
    }

    private static String setterName(String _setterName) {
        if (_setterName.length() > 0 && _setterName.charAt(0) == 'g') {
            return "s" + _setterName.substring(1);
        } else {
            throw new IllegalArgumentException("Not a getter name: " + _setterName);
        }
    }
}

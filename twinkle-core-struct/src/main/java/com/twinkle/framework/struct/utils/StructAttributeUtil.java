package com.twinkle.framework.struct.utils;

import com.twinkle.framework.asm.factory.BeanFactory;
import com.twinkle.framework.core.lang.*;
import com.twinkle.framework.core.lang.util.*;
import com.twinkle.framework.struct.context.StructAttributeSchema;
import com.twinkle.framework.struct.context.StructAttributeSchemaManager;
import com.twinkle.framework.struct.error.*;
import com.twinkle.framework.struct.factory.StructAttributeFactory;
import com.twinkle.framework.struct.lang.StructAttribute;
import com.twinkle.framework.struct.ref.ArrayAttributeRef;
import com.twinkle.framework.struct.ref.AttributeRef;
import com.twinkle.framework.struct.ref.CompositeAttributeRef;
import com.twinkle.framework.struct.type.*;
import com.twinkle.framework.struct.util.ArrayAllocator;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/14/19 10:29 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class StructAttributeUtil {
    /**
     * Build a new empty StructAttribute with RootType name.
     *
     * @param _qualifiedName
     * @return
     */
    public static StructAttribute newStructAttribute(String _qualifiedName) {
        StructAttributeSchema tempStructSchema = StructAttributeSchemaManager.getStructAttributeSchema();
        StructAttributeFactory tempFactory = StructAttributeSchemaManager.getStructAttributeFactory();
        StructType tempType = tempStructSchema.getStructAttributeType(_qualifiedName);
        return tempFactory.newStructAttribute(tempType);
    }

    /**
     * Get the class with given struct type.
     *
     * @param _type
     * @return
     */
    public static Class<?> getStructAttributeClass(StructType _type) {
        String tempTypeName = _type.getQualifiedName();
        return getStructAttributeClass(tempTypeName);
    }

    /**
     * Get the class with given struct type name.
     *
     * @param _type
     * @return
     */
    public static Class<?> getStructAttributeClass(String _type) {
        StructAttributeFactory tempFactory = StructAttributeSchemaManager.getStructAttributeFactory();
        return ((BeanFactory) tempFactory).getBeanClass(_type);
    }

    /**
     * Set the given primitive attribute to Struct Attribute.
     *
     * @param _attribute
     * @param _structAttribute
     * @param _attrRef
     * @param _createFlag
     * @throws AttributeNotFoundException
     * @throws AttributeTypeMismatchException
     * @throws AttributeNotSetException
     */
    public static void setAttributeToStructAttribute(Attribute _attribute, StructAttribute _structAttribute, AttributeRef _attrRef, boolean _createFlag) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        ArrayAllocator tempAllocator = StructAttributeSchemaManager.getStructAttributeFactory().getArrayAllocator();
        AttributeType tempType = _attrRef.getType();
        int tempTypeId = tempType.getID();
        ListAttribute tempListAttribute;
        //index for loop.
        int i;
        Attribute tempAttribute;
        int tempAttrIntValue;
        switch (tempTypeId) {
            case PrimitiveType.BYTE_ID:
                if (_attribute instanceof INumericAttribute) {
                    tempAttrIntValue = ((INumericAttribute) _attribute).getInt();
                } else {
                    tempAttrIntValue = Byte.parseByte(_attribute.toString());
                }
                _structAttribute.setByte(_attrRef, (byte) tempAttrIntValue);
                break;
            case PrimitiveType.SHORT_ID:
                if (_attribute instanceof INumericAttribute) {
                    tempAttrIntValue = ((INumericAttribute) _attribute).getInt();
                } else {
                    tempAttrIntValue = Short.parseShort(_attribute.toString());
                }
                _structAttribute.setShort(_attrRef, (short) tempAttrIntValue);
                break;
            case PrimitiveType.INT_ID:
                if (_attribute instanceof INumericAttribute) {
                    tempAttrIntValue = ((INumericAttribute) _attribute).getInt();
                } else {
                    tempAttrIntValue = Integer.parseInt(_attribute.toString());
                }
                _structAttribute.setInt(_attrRef, tempAttrIntValue);
                break;
            case PrimitiveType.LONG_ID:
                long tempAttrValue;
                if (_attribute instanceof INumericAttribute) {
                    tempAttrValue = ((INumericAttribute) _attribute).getLong();
                } else {
                    tempAttrValue = Long.parseLong(_attribute.toString());
                }
                _structAttribute.setLong(_attrRef, tempAttrValue);
                break;
            case PrimitiveType.CHAR_ID:
                if (_attribute instanceof INumericAttribute) {
                    tempAttrIntValue = ((INumericAttribute) _attribute).getInt();
                } else {
                    tempAttrIntValue = _attribute.toString().charAt(0);
                }
                _structAttribute.setChar(_attrRef, (char) tempAttrIntValue);
                break;
            case PrimitiveType.BOOLEAN_ID:
                boolean tempBooleanValue;
                if (_attribute instanceof INumericAttribute) {
                    int var22 = ((INumericAttribute) _attribute).getInt();
                    tempBooleanValue = var22 != 0;
                } else {
                    tempBooleanValue = Boolean.parseBoolean(_attribute.toString());
                }

                _structAttribute.setBoolean(_attrRef, tempBooleanValue);
                break;
            case PrimitiveType.FLOAT_ID:
                float tempFloatValue;
                if (_attribute instanceof INumericAttribute) {
                    tempFloatValue = ((INumericAttribute) _attribute).getFloat();
                } else {
                    tempFloatValue = Float.parseFloat(_attribute.toString());
                }
                _structAttribute.setFloat(_attrRef, tempFloatValue);
                break;
            case PrimitiveType.DOUBLE_ID:
                double tempDoubleValue;
                if (_attribute instanceof INumericAttribute) {
                    tempDoubleValue = ((INumericAttribute) _attribute).getDouble();
                } else {
                    tempDoubleValue = Double.parseDouble(_attribute.toString());
                }
                _structAttribute.setDouble(_attrRef, tempDoubleValue);
                break;
            case ArrayType.BYTE_ARRAY_ID:
                byte[] tempByteArrayValue = ((BinaryAttribute) _attribute).getByteArray();
                if (tempByteArrayValue == null) {
                    tempByteArrayValue = new byte[0];
                }
                MutableByteArray tempByteArray = tempAllocator.newByteArray(tempByteArrayValue.length);
                tempByteArray.transfer(tempByteArrayValue, 0, 0, tempByteArrayValue.length);
                _structAttribute.setArray(_attrRef, tempByteArray);
                break;
            case ArrayType.SHORT_ARRAY_ID:
                tempListAttribute = (ListAttribute) _attribute;
                MutableShortArray tempShortArray = tempAllocator.newShortArray(tempListAttribute.size());
                for (i = 0; i < tempListAttribute.size(); i++) {
                    tempAttribute = tempListAttribute.get(i);
                    tempShortArray.add((short) ((INumericAttribute) tempAttribute).getInt());
                }
                _structAttribute.setArray(_attrRef, tempShortArray);
                break;
            case ArrayType.INT_ARRAY_ID:
                tempListAttribute = (ListAttribute) _attribute;
                MutableIntegerArray tempIntArray = tempAllocator.newIntegerArray(tempListAttribute.size());
                for (i = 0; i < tempListAttribute.size(); i++) {
                    tempAttribute = tempListAttribute.get(i);
                    tempIntArray.add(((INumericAttribute) tempAttribute).getInt());
                }
                _structAttribute.setArray(_attrRef, tempIntArray);
                break;
            case ArrayType.LONG_ARRAY_ID:
                tempListAttribute = (ListAttribute) _attribute;
                MutableLongArray tempLongArray = tempAllocator.newLongArray(tempListAttribute.size());
                for (i = 0; i < tempListAttribute.size(); i++) {
                    tempAttribute = tempListAttribute.get(i);
                    tempLongArray.add(((INumericAttribute) tempAttribute).getLong());
                }
                _structAttribute.setArray(_attrRef, tempLongArray);
                break;
            case ArrayType.CHAR_ARRAY_ID:
                char[] tempOrgCharArray = ((StringAttribute) _attribute).getValue().toCharArray();
                MutableCharArray tempCharArray = tempAllocator.newCharArray(tempOrgCharArray.length);
                tempCharArray.transfer(tempOrgCharArray, 0, 0, tempOrgCharArray.length);
                _structAttribute.setArray(_attrRef, tempCharArray);
                break;
            case ArrayType.BOOLEAN_ARRAY_ID:
                tempListAttribute = (ListAttribute) _attribute;
                MutableBooleanArray tempBooleanArray = tempAllocator.newBooleanArray(tempListAttribute.size());
                for (i = 0; i < tempListAttribute.size(); i++) {
                    tempAttribute = tempListAttribute.get(i);
                    tempBooleanArray.add(((INumericAttribute) tempAttribute).getInt() != 0);
                }
                _structAttribute.setArray(_attrRef, tempBooleanArray);
                break;
            case ArrayType.FLOAT_ARRAY_ID:
                tempListAttribute = (ListAttribute) _attribute;
                MutableFloatArray tempFloatArray = tempAllocator.newFloatArray(tempListAttribute.size());
                for (i = 0; i < tempListAttribute.size(); i++) {
                    tempAttribute = tempListAttribute.get(i);
                    tempFloatArray.add(((INumericAttribute) tempAttribute).getFloat());
                }
                _structAttribute.setArray(_attrRef, tempFloatArray);
                break;
            case ArrayType.DOUBLE_ARRAY_ID:
                tempListAttribute = (ListAttribute) _attribute;
                MutableDoubleArray tempDoubleArray = tempAllocator.newDoubleArray(tempListAttribute.size());
                for (i = 0; i < tempListAttribute.size(); i++) {
                    tempAttribute = tempListAttribute.get(i);
                    tempDoubleArray.add(((INumericAttribute) tempAttribute).getDouble());
                }
                _structAttribute.setArray(_attrRef, tempDoubleArray);
                break;
            case StringType.STRING_ID:
                String tempStr = _attribute.toString();
                _structAttribute.setString(_attrRef, tempStr);
                break;
            case ArrayType.STRING_ARRAY_ID:
                tempListAttribute = (ListAttribute) _attribute;
                MutableStringArray tempStrArray = tempAllocator.newStringArray(tempListAttribute.size());
                for (i = 0; i < tempListAttribute.size(); i++) {
                    tempAttribute = tempListAttribute.get(i);
                    tempStrArray.add(tempAttribute.toString());
                }
                _structAttribute.setArray(_attrRef, tempStrArray);
                break;
            case StructType.STRUCT_ID:
                throw new IllegalArgumentException("Can not map an struct attribute into structured attribute " + _attrRef);
            default:
                throw new IllegalArgumentException("Unknown primitive type: " + tempTypeId);
        }

    }

    /**
     * Set struct attribute value to primitive attribute.
     *
     * @param _structAttribute
     * @param _attributeRef
     * @param _attribute
     * @throws AttributeNotFoundException
     * @throws BadAttributeNameException
     * @throws AttributeTypeMismatchException
     * @throws AttributeNotSetException
     */
    public static void setStructAttributeToAttribute(StructAttribute _structAttribute, AttributeRef _attributeRef, Attribute _attribute) throws AttributeNotFoundException, BadAttributeNameException, AttributeTypeMismatchException, AttributeNotSetException {
        AttributeRef tempAttrRef = _attributeRef;
        AttributeType tempType = _attributeRef.getType();
        int tempTypeID = tempType.getID();
        ListAttribute tempListAttribute;
        int tempSrcArraySize;
        int i;
        int tempArrayLength;
        switch (tempTypeID) {
            case PrimitiveType.BYTE_ID:
                byte tempByte = _structAttribute.getByte(_attributeRef);
                if (_attribute instanceof ListAttribute) {
                    ((ListAttribute) _attribute).add(new IntegerAttribute(tempByte));
                } else if (_attribute instanceof IIntegerAttribute) {
                    ((IIntegerAttribute) _attribute).setValue(tempByte);
                } else {
                    _attribute.setValue(Byte.toString(tempByte));
                }
                break;
            case PrimitiveType.SHORT_ID:
                short tempShort = _structAttribute.getShort(_attributeRef);
                if (_attribute instanceof ListAttribute) {
                    ((ListAttribute) _attribute).add(new IntegerAttribute(tempShort));
                } else if (_attribute instanceof IIntegerAttribute) {
                    ((IIntegerAttribute) _attribute).setValue(tempShort);
                } else {
                    _attribute.setValue(Short.toString(tempShort));
                }
                break;
            case PrimitiveType.INT_ID:
                int tempInt = _structAttribute.getInt(_attributeRef);
                if (_attribute instanceof ListAttribute) {
                    ((ListAttribute) _attribute).add(new IntegerAttribute(tempInt));
                } else if (_attribute instanceof IIntegerAttribute) {
                    ((IIntegerAttribute) _attribute).setValue(tempInt);
                } else {
                    _attribute.setValue(Integer.toString(tempInt));
                }
                break;
            case PrimitiveType.LONG_ID:
                long tempLong = _structAttribute.getLong(_attributeRef);
                if (_attribute instanceof ListAttribute) {
                    ((ListAttribute) _attribute).add(new LongAttribute(tempLong));
                } else if (_attribute instanceof ILongAttribute) {
                    ((ILongAttribute) _attribute).setValue(tempLong);
                } else {
                    _attribute.setValue(Long.toString(tempLong));
                }
                break;
            case PrimitiveType.CHAR_ID:
                char tempChar = _structAttribute.getChar(_attributeRef);
                if (_attribute instanceof ListAttribute) {
                    ((ListAttribute) _attribute).add(new StringAttribute("" + tempChar));
                } else if (_attribute instanceof IIntegerAttribute) {
                    ((IIntegerAttribute) _attribute).setValue(tempChar);
                } else {
                    _attribute.setValue(Character.toString(tempChar));
                }
                break;
            case PrimitiveType.BOOLEAN_ID:
                boolean tempBoolean = _structAttribute.getBoolean(_attributeRef);
                if (_attribute instanceof ListAttribute) {
                    tempSrcArraySize = tempBoolean ? 1 : 0;
                    ((ListAttribute) _attribute).add(new IntegerAttribute(tempSrcArraySize));
                } else if (_attribute instanceof IIntegerAttribute) {
                    ((IIntegerAttribute) _attribute).setValue(tempBoolean ? 1 : 0);
                } else {
                    _attribute.setValue(Boolean.toString(tempBoolean));
                }
                break;
            case PrimitiveType.FLOAT_ID:
                float tempFloat = _structAttribute.getFloat(_attributeRef);
                if (_attribute instanceof ListAttribute) {
                    ((ListAttribute) _attribute).add(new FloatAttribute(tempFloat));
                } else if (_attribute instanceof IFloatAttribute) {
                    ((IFloatAttribute) _attribute).setValue(tempFloat);
                } else {
                    _attribute.setValue(Float.toString(tempFloat));
                }
                break;
            case PrimitiveType.DOUBLE_ID:
                double tempDouble = _structAttribute.getDouble(_attributeRef);
                if (_attribute instanceof ListAttribute) {
                    ((ListAttribute) _attribute).add(new DoubleAttribute(tempDouble));
                } else if (_attribute instanceof IDoubleAttribute) {
                    ((IDoubleAttribute) _attribute).setValue(tempDouble);
                } else {
                    _attribute.setValue(Double.toString(tempDouble));
                }
                break;
            case ArrayType.BYTE_ARRAY_ID:
                BinaryAttribute tempBinaryAttr = (BinaryAttribute) _attribute;
                ByteArray tempByteArray = (ByteArray) _structAttribute.getArray(_attributeRef);
                tempArrayLength = tempByteArray.length();
                byte[] tempDestByteArray = new byte[tempArrayLength];
                tempByteArray.copyTo(tempDestByteArray, 0, 0, tempArrayLength);
                tempBinaryAttr.setValue(tempDestByteArray);
                break;
            case ArrayType.SHORT_ARRAY_ID:
                tempListAttribute = (ListAttribute) _attribute;
                tempListAttribute.setEmptyValue();
                tempSrcArraySize = _structAttribute.getArraySize(_attributeRef);
                if (tempSrcArraySize > 0) {
                    ShortArray tempShortArray = (ShortArray) _structAttribute.getArray(_attributeRef);

                    for (i = 0; i < _structAttribute.getArraySize(tempAttrRef); i++) {
                        tempListAttribute.add(new IntegerAttribute(tempShortArray.get(i)));
                    }
                }
                break;
            case ArrayType.INT_ARRAY_ID:
                tempListAttribute = (ListAttribute) _attribute;
                tempListAttribute.setEmptyValue();
                tempSrcArraySize = _structAttribute.getArraySize(_attributeRef);
                if (tempSrcArraySize > 0) {
                    IntegerArray tempIntArray = (IntegerArray) _structAttribute.getArray(_attributeRef);
                    for (i = 0; i < _structAttribute.getArraySize(tempAttrRef); i++) {
                        tempListAttribute.add(new IntegerAttribute(tempIntArray.get(i)));
                    }
                }
                break;
            case ArrayType.LONG_ARRAY_ID:
                tempListAttribute = (ListAttribute) _attribute;
                tempListAttribute.setEmptyValue();
                tempSrcArraySize = _structAttribute.getArraySize(_attributeRef);
                if (tempSrcArraySize > 0) {
                    LongArray tempLongArray = (LongArray) _structAttribute.getArray(_attributeRef);

                    for (i = 0; i < _structAttribute.getArraySize(tempAttrRef); i++) {
                        tempListAttribute.add(new LongAttribute(tempLongArray.get(i)));
                    }
                }
                break;
            case ArrayType.CHAR_ARRAY_ID:
                StringAttribute tempStringAttr = (StringAttribute) _attribute;
                CharArray tempCharArray = (CharArray) _structAttribute.getArray(_attributeRef);
                tempArrayLength = tempCharArray.length();
                StringBuffer tempBuffer = new StringBuffer(tempArrayLength);
                for (i = 0; i < tempArrayLength; i++) {
                    tempBuffer.append(tempCharArray.get(i));
                }

                tempStringAttr.setValue(tempBuffer.toString());
                break;
            case ArrayType.BOOLEAN_ARRAY_ID:
                tempListAttribute = (ListAttribute) _attribute;
                tempListAttribute.setEmptyValue();
                tempSrcArraySize = _structAttribute.getArraySize(_attributeRef);
                if (tempSrcArraySize > 0) {
                    BooleanArray tempBooleanArray = (BooleanArray) _structAttribute.getArray(_attributeRef);
                    for (i = 0; i < tempSrcArraySize; i++) {
                        int tempValue = tempBooleanArray.get(i) ? 1 : 0;
                        tempListAttribute.add(new IntegerAttribute(tempValue));
                    }
                }
                break;
            case ArrayType.FLOAT_ARRAY_ID:
                tempListAttribute = (ListAttribute) _attribute;
                tempListAttribute.setEmptyValue();
                tempSrcArraySize = _structAttribute.getArraySize(_attributeRef);
                if (tempSrcArraySize > 0) {
                    FloatArray tempFloatArray = (FloatArray) _structAttribute.getArray(_attributeRef);
                    for (i = 0; i < _structAttribute.getArraySize(tempAttrRef); i++) {
                        tempListAttribute.add(new FloatAttribute(tempFloatArray.get(i)));
                    }
                }
                break;
            case ArrayType.DOUBLE_ARRAY_ID:
                tempListAttribute = (ListAttribute) _attribute;
                tempListAttribute.setEmptyValue();
                tempSrcArraySize = _structAttribute.getArraySize(_attributeRef);
                if (tempSrcArraySize > 0) {
                    DoubleArray tempDoubleArray = (DoubleArray) _structAttribute.getArray(_attributeRef);
                    for (i = 0; i < _structAttribute.getArraySize(tempAttrRef); i++) {
                        tempListAttribute.add(new DoubleAttribute(tempDoubleArray.get(i)));
                    }
                }
                break;
            case StringType.STRING_ID:
                String tempStr = _structAttribute.getString(_attributeRef);
                if (_attribute instanceof ListAttribute) {
                    ((ListAttribute) _attribute).add(new StringAttribute(tempStr));
                } else {
                    _attribute.setValue(tempStr);
                }
                break;
            case ArrayType.STRING_ARRAY_ID:
                tempListAttribute = (ListAttribute) _attribute;
                tempListAttribute.setEmptyValue();
                tempSrcArraySize = _structAttribute.getArraySize(_attributeRef);
                if (tempSrcArraySize > 0) {
                    StringArray tempStringArray = (StringArray) _structAttribute.getArray(_attributeRef);
                    for (i = 0; i < _structAttribute.getArraySize(tempAttrRef); i++) {
                        tempListAttribute.add(new StringAttribute(tempStringArray.get(i)));
                    }
                }
                break;
            case StructType.STRUCT_ID:
                throw new IllegalArgumentException("Can not map structured attribute " + _attributeRef + " into an primitive attribute.");
            default:
                throw new IllegalArgumentException("Unknown primitive type: " + tempTypeID);
        }
    }

    /**
     * Prepare the StructAttribute: add the attributeRef's attribute.
     *
     * @param _structAttribute
     * @param _attrRef
     * @throws BadAttributeNameException
     * @throws AttributeNotFoundException
     * @throws AttributeTypeMismatchException
     * @throws AttributeNotSetException
     */
    public static void prepareStructAttribute(StructAttribute _structAttribute, AttributeRef _attrRef) throws BadAttributeNameException, AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
        if (_attrRef.isComposite()) {
            StructAttribute tempStructAttribute = ((CompositeAttributeRef)_attrRef).getTailStructAttribute(_structAttribute, true);
            AttributeRef tempAttrRef = ((CompositeAttributeRef)_attrRef).getTailAttributeRef();
            AttributeType tempType = tempAttrRef.getType();
            if (tempType.isStructType()) {
                StructAttributeFactory var5 = StructAttributeSchemaManager.getStructAttributeFactory();
                tempStructAttribute.setStruct(tempAttrRef, var5.newStructAttribute((StructType)tempType));
            }

            if (tempAttrRef.isArray()) {
                ((ArrayAttributeRef)tempAttrRef).ensureSize(tempStructAttribute);
            }
        } else if (_attrRef.isArray()) {
            ((ArrayAttributeRef)_attrRef).ensureSize(_structAttribute);
        } else {
            AttributeType tempRefType = _attrRef.getType();
            if (tempRefType.isStructType()) {
                StructAttributeFactory tempFactory = StructAttributeSchemaManager.getStructAttributeFactory();
                _structAttribute.setStruct(_attrRef, tempFactory.newStructAttribute((StructType)tempRefType));
            }
        }
    }

    /**
     * Build an empty StructAttribute with StructType's name.
     *
     * @param _structTypeName
     * @return
     * @throws NamespaceNotFoundException
     * @throws StructAttributeTypeNotFoundException
     */
    public static StructAttribute createEmptyStructAttribute(String _structTypeName) throws NamespaceNotFoundException, StructAttributeTypeNotFoundException {
        StructType tempType = StructAttributeSchemaManager.getStructAttributeSchema().getStructAttributeType(_structTypeName);
        return StructAttributeSchemaManager.getStructAttributeFactory().newStructAttribute(tempType);
    }

    /**
     * Get StructType's attribute ref with given attribute name.
     *
     * @param _type
     * @param _attrName
     * @return
     * @throws AttributeNotFoundException
     * @throws AttributeTypeMismatchException
     * @throws BadAttributeNameException
     */
    public static AttributeRef getAttributeRef(StructType _type, String _attrName) throws AttributeNotFoundException, AttributeTypeMismatchException, BadAttributeNameException {
        return StructAttributeSchemaManager.getStructAttributeFactory().getCompositeAttributeRef(_type, _attrName);
    }
}

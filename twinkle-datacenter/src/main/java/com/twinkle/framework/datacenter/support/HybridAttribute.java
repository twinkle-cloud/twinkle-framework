package com.twinkle.framework.datacenter.support;

import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.context.AttributeInfo;
import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.context.PrimitiveAttributeSchema;
import com.twinkle.framework.core.lang.*;
import com.twinkle.framework.core.lang.util.*;
import com.twinkle.framework.api.exception.DataCenterException;
import com.twinkle.framework.datacenter.utils.JDBCUtil;
import com.twinkle.framework.struct.context.StructAttributeSchemaManager;
import com.twinkle.framework.struct.lang.StructAttrAttribute;
import com.twinkle.framework.struct.lang.StructAttribute;
import com.twinkle.framework.struct.ref.AttributeRef;
import com.twinkle.framework.struct.type.ArrayType;
import com.twinkle.framework.struct.type.PrimitiveType;
import com.twinkle.framework.struct.type.StringType;
import com.twinkle.framework.struct.type.StructType;
import com.twinkle.framework.struct.utils.StructAttributeUtil;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/16/19 10:28 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Getter
public class HybridAttribute implements Cloneable {
    private final String DELIMITER;
    private final int TOKENS_IN_LISTATTR;
    private String sourceAttributeName;
    private final int attrIndex;
    private String attributeName;
    private final StructType structType;
    private final AttributeRef attributeRef;
    private boolean arrayFlag = false;
    private boolean needItemFlag = false;

    public int getAttrIndex() {
        return this.attrIndex;
    }

    public HybridAttribute(String _attrName) throws ConfigurationException {
        this(_attrName, "");
    }

    public HybridAttribute(String _attrName, String _expression) throws ConfigurationException {
        this.DELIMITER = "|";
        this.TOKENS_IN_LISTATTR = 4;
        this.sourceAttributeName = _attrName;
        int tempIndex = this.sourceAttributeName.indexOf("[]");
        if (tempIndex > 0) {
            this.needItemFlag = true;
            this.attributeName = this.sourceAttributeName.substring(0, tempIndex);
        } else {
            this.attributeName = this.sourceAttributeName;
        }
        tempIndex = this.attributeName.indexOf(".");
        if (tempIndex > 0) {
            this.attributeName = this.attributeName.substring(0, tempIndex);
        }

        AttributeInfo tempAttrInfo = PrimitiveAttributeSchema.getInstance().getAttribute(this.attributeName);
        if (tempAttrInfo.getIndex() < 0) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_REQUIRED_ATTR_MISSED, "The Attribute[" + this.attributeName + "] in the expression [" + _expression + "] not defined in Attribute Schema.");
        }
        this.attrIndex = tempAttrInfo.getIndex();
        //The attribute is struct type.
        if (tempIndex > 0) {
            if (StringUtils.isBlank(tempAttrInfo.getValueType())) {
                throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ATTR_IS_NOT_STRUCT, "The attribute[{" + this.attributeName + "}] in the expression [" + _expression + "] is not struct attribute, so does not support sub attribute.");
            }
            String tempSubAttrName = this.sourceAttributeName.substring(tempIndex + 1);
            this.structType = StructAttributeSchemaManager.getStructAttributeSchema().getStructAttributeType(tempAttrInfo.getValueType());
            this.attributeRef = StructAttributeUtil.getAttributeRef(tempAttrInfo.getValueType(), tempSubAttrName);
            if (this.attributeRef == null) {
                throw new ConfigurationException(ExceptionCode.LOGIC_CONF_SA_TYPE_ATTR_FIELD_MISSED, "The sub attribute [{" + tempSubAttrName + "}] in the expression [" + _expression + "] is not found in the StructAttribute [{" + tempAttrInfo.getValueType() + "}].");
            }
            if (!this.attributeRef.isArray() && this.needItemFlag) {
                throw new ConfigurationException(ExceptionCode.LOGIC_CONF_SA_NAME_INVALID, "The sub attribute [{" + tempSubAttrName + "}] in the expression [" + _expression + "] is not array type, so [] not supported here.");
            }
        } else {
            this.attributeRef = null;
            this.structType = null;
            if (this.needItemFlag) {
                if (tempAttrInfo.getPrimitiveType() != Attribute.LIST_ATTRIBUTE_TYPE &&
                        tempAttrInfo.getPrimitiveType() != Attribute.LIST_STRUCT_ATTRIBUTE_TYPE) {
                    throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ATTR_TYPE_IS_UNEXPECTED, "The attribute [{" + this.attributeName + "}] in the expression [" + _expression + "] is not list type, so [] not supported here.");
                }
            }
        }
    }

    private HybridAttribute(int _index, String _attrName, StructType _structType, AttributeRef _attrRef) {
        this.DELIMITER = "|";
        this.TOKENS_IN_LISTATTR = 4;
        this.attrIndex = _index;
        this.attributeName = _attrName;
        this.structType = _structType;
        this.attributeRef = _attrRef;
    }

    /**
     * Get Array size.
     *
     * @param _context
     * @return
     */
    public int getArraySize(NormalizedContext _context) {
        Attribute tempAttr = _context.getAttribute(this.attrIndex);
        if (this.attributeRef != null) {
            StructAttribute tempStructAttribute = (StructAttribute) tempAttr.getObjectValue();
            return tempStructAttribute.getArraySize(this.attributeRef);
        }
        if (tempAttr instanceof IListAttribute) {
            return ((IListAttribute) tempAttr).size();
        }
        return 0;
    }

    public void setValue(NormalizedContext _context, String _value) {
//        if (this.attributeRef != null) {
//            StructAttributeFactory tempFactory = StructAttributeSchemaManager.getStructAttributeFactory();
//            ArrayAllocator tempAllocator = tempFactory.getArrayAllocator();
//            NMEAdapter var5 = (NMEAdapter) _context;
//            StructAttribute tempStructAttribute = var5.getNME();
//            if (tempStructAttribute == null) {
//                tempStructAttribute = tempFactory.newStructAttribute(this.structType);
//                StructAttributeUtil.prepareStructAttribute(tempStructAttribute, this.attributeRef);
//                var5.setStructAttribute(tempStructAttribute);
//            } else if (_value != null) {
//                try {
//                    if (!tempStructAttribute.isAttributeSet(this.attributeRef)) {
//                        StructAttributeUtil.prepareStructAttribute(tempStructAttribute, this.attributeRef);
//                    }
//                } catch (AttributeNotSetException e) {
//                    StructAttributeUtil.prepareStructAttribute(tempStructAttribute, this.attributeRef);
//                }
//            }
//
//            int tempTypeId = this.attributeRef.getType().getID();
//            switch (tempTypeId) {
//                case PrimitiveType.BYTE_ID:
//                    if (_value != null) {
//                        tempStructAttribute.setByte(this.attributeRef, Byte.parseByte(_value));
//                    } else {
//                        tempStructAttribute.clear(this.attributeRef);
//                    }
//                    break;
//                case PrimitiveType.SHORT_ID:
//                    if (_value != null) {
//                        tempStructAttribute.setShort(this.attributeRef, Short.parseShort(_value));
//                    } else {
//                        tempStructAttribute.clear(this.attributeRef);
//                    }
//                    break;
//                case PrimitiveType.INT_ID:
//                    if (_value != null) {
//                        tempStructAttribute.setInt(this.attributeRef, Integer.parseInt(_value));
//                    } else {
//                        tempStructAttribute.clear(this.attributeRef);
//                    }
//                    break;
//                case PrimitiveType.LONG_ID:
//                    if (_value != null) {
//                        tempStructAttribute.setLong(this.attributeRef, Long.parseLong(_value));
//                    } else {
//                        tempStructAttribute.clear(this.attributeRef);
//                    }
//                    break;
//                case PrimitiveType.CHAR_ID:
//                    if (_value != null) {
//                        tempStructAttribute.setChar(this.attributeRef, _value.charAt(0));
//                    } else {
//                        tempStructAttribute.clear(this.attributeRef);
//                    }
//                    break;
//                case PrimitiveType.BOOLEAN_ID:
//                    if (_value != null) {
//                        tempStructAttribute.setBoolean(this.attributeRef, Boolean.parseBoolean(_value));
//                    } else {
//                        tempStructAttribute.clear(this.attributeRef);
//                    }
//                    break;
//                case PrimitiveType.FLOAT_ID:
//                    if (_value != null) {
//                        tempStructAttribute.setFloat(this.attributeRef, Float.parseFloat(_value));
//                    } else {
//                        tempStructAttribute.clear(this.attributeRef);
//                    }
//                    break;
//                case PrimitiveType.DOUBLE_ID:
//                    if (_value != null) {
//                        tempStructAttribute.setDouble(this.attributeRef, Double.parseDouble(_value));
//                    } else {
//                        tempStructAttribute.clear(this.attributeRef);
//                    }
//                    break;
//                case ArrayType.BYTE_ARRAY_ID:
//                    if (_value != null) {
//                        byte[] tempByteArray;
//                        int i;
//                        MutableByteArray tempMutableArray;
//                        if (!tempStructAttribute.isAttributeSet(this.attributeRef)) {
//                            tempMutableArray = tempAllocator.newByteArray(128);
//                            tempByteArray = _value.getBytes();
//
//                            for (i = 0; i < tempByteArray.length; ++i) {
//                                tempMutableArray.add(tempByteArray[i]);
//                            }
//                        } else {
//                            tempMutableArray = (MutableByteArray) tempStructAttribute.getArray(this.attributeRef);
//                            tempByteArray = _value.getBytes();
//                            tempMutableArray.ensureCapacity(tempMutableArray.length() + tempByteArray.length);
//
//                            for (i = 0; i < tempByteArray.length; ++i) {
//                                tempMutableArray.add(tempByteArray[i]);
//                            }
//                        }
//
//                        tempStructAttribute.setArray(this.attributeRef, tempMutableArray);
//                    } else {
//                        tempStructAttribute.clear(this.attributeRef);
//                    }
//                    break;
//                case ArrayType.SHORT_ARRAY_ID:
//                    if (_value != null) {
//                        MutableShortArray tempMutableArray;
//                        if (!tempStructAttribute.isAttributeSet(this.attributeRef)) {
//                            tempMutableArray = tempAllocator.newShortArray(64);
//                            tempMutableArray.add(Short.parseShort(_value));
//                        } else {
//                            tempMutableArray = (MutableShortArray) tempStructAttribute.getArray(this.attributeRef);
//                            tempMutableArray.add(Short.parseShort(_value));
//                        }
//
//                        tempStructAttribute.setArray(this.attributeRef, tempMutableArray);
//                    } else {
//                        tempStructAttribute.clear(this.attributeRef);
//                    }
//                    break;
//                case ArrayType.INT_ARRAY_ID:
//                    if (_value != null) {
//                        MutableIntegerArray tempMutableArray;
//                        if (!tempStructAttribute.isAttributeSet(this.attributeRef)) {
//                            tempMutableArray = tempAllocator.newIntegerArray(64);
//                            tempMutableArray.add(Integer.parseInt(_value));
//                        } else {
//                            tempMutableArray = (MutableIntegerArray) tempStructAttribute.getArray(this.attributeRef);
//                            tempMutableArray.add(Integer.parseInt(_value));
//                        }
//
//                        tempStructAttribute.setArray(this.attributeRef, tempMutableArray);
//                    } else {
//                        tempStructAttribute.clear(this.attributeRef);
//                    }
//                    break;
//                case ArrayType.LONG_ARRAY_ID:
//                    if (_value != null) {
//                        MutableLongArray tempMutableArray;
//                        if (!tempStructAttribute.isAttributeSet(this.attributeRef)) {
//                            tempMutableArray = tempAllocator.newLongArray(64);
//                            tempMutableArray.add(Long.parseLong(_value));
//                        } else {
//                            tempMutableArray = (MutableLongArray) tempStructAttribute.getArray(this.attributeRef);
//                            tempMutableArray.add(Long.parseLong(_value));
//                        }
//
//                        tempStructAttribute.setArray(this.attributeRef, tempMutableArray);
//                    } else {
//                        tempStructAttribute.clear(this.attributeRef);
//                    }
//                    break;
//                case ArrayType.CHAR_ARRAY_ID:
//                    if (_value != null) {
//                        MutableCharArray tempMutableArray;
//                        if (!tempStructAttribute.isAttributeSet(this.attributeRef)) {
//                            tempMutableArray = tempAllocator.newCharArray(128);
//                            tempMutableArray.add(_value.charAt(0));
//                        } else {
//                            tempMutableArray = (MutableCharArray) tempStructAttribute.getArray(this.attributeRef);
//                            tempMutableArray.add(_value.charAt(0));
//                        }
//
//                        tempStructAttribute.setArray(this.attributeRef, tempMutableArray);
//                    } else {
//                        tempStructAttribute.clear(this.attributeRef);
//                    }
//                    break;
//                case ArrayType.BOOLEAN_ARRAY_ID:
//                    if (_value != null) {
//                        MutableBooleanArray tempMutableArray;
//                        if (!tempStructAttribute.isAttributeSet(this.attributeRef)) {
//                            tempMutableArray = tempAllocator.newBooleanArray(64);
//                            tempMutableArray.add(Boolean.parseBoolean(_value));
//                        } else {
//                            tempMutableArray = (MutableBooleanArray) tempStructAttribute.getArray(this.attributeRef);
//                            tempMutableArray.add(Boolean.parseBoolean(_value));
//                        }
//
//                        tempStructAttribute.setArray(this.attributeRef, tempMutableArray);
//                    } else {
//                        tempStructAttribute.clear(this.attributeRef);
//                    }
//                    break;
//                case ArrayType.FLOAT_ARRAY_ID:
//                    if (_value != null) {
//                        MutableFloatArray tempMutableArray;
//                        if (!tempStructAttribute.isAttributeSet(this.attributeRef)) {
//                            tempMutableArray = tempAllocator.newFloatArray(64);
//                            tempMutableArray.add(Float.parseFloat(_value));
//                        } else {
//                            tempMutableArray = (MutableFloatArray) tempStructAttribute.getArray(this.attributeRef);
//                            tempMutableArray.add(Float.parseFloat(_value));
//                        }
//
//                        tempStructAttribute.setArray(this.attributeRef, tempMutableArray);
//                    } else {
//                        tempStructAttribute.clear(this.attributeRef);
//                    }
//                    break;
//                case ArrayType.DOUBLE_ARRAY_ID:
//                    if (_value != null) {
//                        MutableDoubleArray tempMutableArray;
//                        if (!tempStructAttribute.isAttributeSet(this.attributeRef)) {
//                            tempMutableArray = tempAllocator.newDoubleArray(64);
//                            tempMutableArray.add(Double.parseDouble(_value));
//                        } else {
//                            tempMutableArray = (MutableDoubleArray) tempStructAttribute.getArray(this.attributeRef);
//                            tempMutableArray.add(Double.parseDouble(_value));
//                        }
//
//                        tempStructAttribute.setArray(this.attributeRef, tempMutableArray);
//                    } else {
//                        tempStructAttribute.clear(this.attributeRef);
//                    }
//                    break;
//                case StringType.STRING_ID:
//                    if (_value != null) {
//                        tempStructAttribute.setString(this.attributeRef, _value);
//                    } else {
//                        tempStructAttribute.clear(this.attributeRef);
//                    }
//                    break;
//                case ArrayType.STRING_ARRAY_ID:
//                    if (_value != null) {
//                        MutableStringArray tempMutableArray = null;
//                        if (!tempStructAttribute.isAttributeSet(this.attributeRef)) {
//                            tempMutableArray = tempAllocator.newStringArray(10);
//                            tempMutableArray.add(_value);
//                        } else {
//                            tempMutableArray = (MutableStringArray) tempStructAttribute.getArray(this.attributeRef);
//                            tempMutableArray.add(_value);
//                        }
//
//                        tempStructAttribute.setArray(this.attributeRef, tempMutableArray);
//                    } else {
//                        tempStructAttribute.clear(this.attributeRef);
//                    }
//                    break;
//                default:
//                    throw new IllegalArgumentException("" + this.attributeRef.getType().getName());
//            }
//        } else {
//            Attribute tempAttribute = _context.getAttribute(this.attrIndex);
//            StringTokenizer tempTokenizer;
//            int tempCount;
//            String tempToken;
//            if (tempAttribute == null) {
//                if (_value != null) {
//                    tempAttribute = PrimitiveAttributeSchema.getInstance().newAttributeInstance(this.attrIndex);
//                    _context.setAttribute(tempAttribute, this.attrIndex);
//                    if (tempAttribute != null && tempAttribute instanceof TimeAttribute) {
//                        ((TimeAttribute) tempAttribute).setTimeFormat(TimeAttribute.DEFAULT_TIME_FORMAT);
//                    }
//
//                    if (tempAttribute instanceof ListAttribute) {
//                        tempTokenizer = new StringTokenizer(_value, "|", true);
//                        tempCount = tempTokenizer.countTokens();
//                        tempToken = tempTokenizer.nextToken();
//                        if (tempToken != null && tempCount > 4) {
//                            tempAttribute.setValue(_value);
//                        } else {
//                            ((ListAttribute) tempAttribute).add(new StringAttribute(_value));
//                        }
//                    } else {
//                        tempAttribute.setValue(_value);
//                    }
//                } else {
//                    _context.setAttribute(null, this.attrIndex);
//                }
//            } else if (_value != null) {
//                if (tempAttribute instanceof ListAttribute) {
//                    tempTokenizer = new StringTokenizer(_value, "|", true);
//                    tempCount = tempTokenizer.countTokens();
//                    tempToken = tempTokenizer.nextToken();
//                    if (tempToken != null && tempCount > 4) {
//                        tempAttribute.setValue(_value);
//                    } else {
//                        ((ListAttribute) tempAttribute).add(new StringAttribute(_value));
//                    }
//                } else {
//                    if (tempAttribute instanceof TimeAttribute) {
//                        ((TimeAttribute) tempAttribute).setTimeFormat(TimeAttribute.DEFAULT_TIME_FORMAT);
//                    }
//
//                    tempAttribute.setValue(_value);
//                }
//            } else {
//                _context.setAttribute(null, this.attrIndex);
//            }
//        }
    }
    /**
    public void setObjectValue(NormalizedContext _context, Object _object) {
        if (this.attributeRef != null) {
            NMEAdapter var3 = (NMEAdapter) _context;
            StructAttribute tempStructAttribute = var3.getStructAttribute();
            if (tempStructAttribute == null) {
                tempStructAttribute = StructAttributeSchemaManager.getStructAttributeFactory().newStructAttribute(this.structType);
                StructAttributeUtil.prepareStructAttribute(tempStructAttribute, this.attributeRef);
                var3.setStructAttribute(tempStructAttribute);
            } else if (_object != null) {
                try {
                    if (!tempStructAttribute.isAttributeSet(this.attributeRef)) {
                        StructAttributeUtil.prepareStructAttribute(tempStructAttribute, this.attributeRef);
                    }
                } catch (AttributeNotSetException var6) {
                    StructAttributeUtil.prepareStructAttribute(tempStructAttribute, this.attributeRef);
                }
            }

            int tempTypeId = this.attributeRef.getType().getID();
            switch (tempTypeId) {
                case PrimitiveType.BYTE_ID:
                    if (_object != null) {
                        tempStructAttribute.setByte(this.attributeRef, (Byte) _object);
                    } else {
                        tempStructAttribute.clear(this.attributeRef);
                    }
                    break;
                case PrimitiveType.SHORT_ID:
                    if (_object != null) {
                        tempStructAttribute.setShort(this.attributeRef, (Short) _object);
                    } else {
                        tempStructAttribute.clear(this.attributeRef);
                    }
                    break;
                case PrimitiveType.INT_ID:
                    if (_object != null) {
                        tempStructAttribute.setInt(this.attributeRef, (Integer) _object);
                    } else {
                        tempStructAttribute.clear(this.attributeRef);
                    }
                    break;
                case PrimitiveType.LONG_ID:
                    if (_object != null) {
                        tempStructAttribute.setLong(this.attributeRef, (Long) _object);
                    } else {
                        tempStructAttribute.clear(this.attributeRef);
                    }
                    break;
                case PrimitiveType.CHAR_ID:
                    if (_object != null) {
                        tempStructAttribute.setChar(this.attributeRef, (Character) _object);
                    } else {
                        tempStructAttribute.clear(this.attributeRef);
                    }
                    break;
                case PrimitiveType.BOOLEAN_ID:
                    if (_object != null) {
                        tempStructAttribute.setBoolean(this.attributeRef, (Boolean) _object);
                    } else {
                        tempStructAttribute.clear(this.attributeRef);
                    }
                    break;
                case PrimitiveType.FLOAT_ID:
                    if (_object != null) {
                        tempStructAttribute.setFloat(this.attributeRef, (Float) _object);
                    } else {
                        tempStructAttribute.clear(this.attributeRef);
                    }
                    break;
                case PrimitiveType.DOUBLE_ID:
                    if (_object != null) {
                        tempStructAttribute.setDouble(this.attributeRef, (Double) _object);
                    } else {
                        tempStructAttribute.clear(this.attributeRef);
                    }
                    break;
                case StringType.STRING_ID:
                    if (_object != null) {
                        tempStructAttribute.setString(this.attributeRef, _object.toString());
                    } else {
                        tempStructAttribute.clear(this.attributeRef);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported type " + this.attributeRef.getType().getName());
            }
        } else {
            Attribute tempAttribute = _context.getAttribute(this.attrIndex);
            if (tempAttribute == null) {
                tempAttribute = PrimitiveAttributeSchema.getInstance().newAttributeInstance(this.attrIndex);
                _context.setAttribute(tempAttribute, this.attrIndex);
            }

            if (_object != null) {
                if (tempAttribute instanceof TimeAttribute) {
                    ((TimeAttribute) tempAttribute).setTimeFormat(TimeAttribute.DEFAULT_TIME_FORMAT);
                }

                tempAttribute.setValue((Attribute) _object);
            } else {
                _context.setAttribute(null, this.attrIndex);
            }
        }

    }

    public void setEmptyValue(NormalizedContext _context) {
        if (this.attributeRef != null) {
            NMEAdapter var2 = (NMEAdapter) _context;
            StructAttribute tempStructAttribute = var2.getStructAttribute();
            if (tempStructAttribute == null) {
                tempStructAttribute = StructAttributeSchemaManager.getStructAttributeFactory().newStructAttribute(this.structType);
                StructAttributeUtil.prepareStructAttribute(tempStructAttribute, this.attributeRef);
                var2.setStructAttribute(tempStructAttribute);
            }

            tempStructAttribute.clear(this.attributeRef);
        } else {
            Attribute tempAttribute = _context.getAttribute(this.attrIndex);
            if (tempAttribute == null) {
                tempAttribute = PrimitiveAttributeSchema.getInstance().newAttributeInstance(this.attrIndex);
                _context.setAttribute(tempAttribute, this.attrIndex);
            }
            tempAttribute.setEmptyValue();
        }
    }

    public Object getValue(NormalizedContext _context) {
        if (this.attributeRef != null) {
            NMEAdapter var5 = (NMEAdapter) _context;
            StructAttribute tempStructAttribute = var5.getStructAttribute();
            if (tempStructAttribute != null && tempStructAttribute.isAttributeSet(this.attributeRef)) {
                int tempTypeId = this.attributeRef.getType().getID();
                switch (tempTypeId) {
                    case PrimitiveType.BYTE_ID:
                        return "" + (short) tempStructAttribute.getByte(this.attributeRef);
                    case PrimitiveType.SHORT_ID:
                        return "" + new Short(tempStructAttribute.getShort(this.attributeRef));
                    case PrimitiveType.INT_ID:
                        return "" + new Integer(tempStructAttribute.getInt(this.attributeRef));
                    case PrimitiveType.LONG_ID:
                        return "" + new Long(tempStructAttribute.getLong(this.attributeRef));
                    case PrimitiveType.CHAR_ID:
                        return "" + new Character(tempStructAttribute.getChar(this.attributeRef));
                    case PrimitiveType.BOOLEAN_ID:
                        return "" + new Boolean(tempStructAttribute.getBoolean(this.attributeRef));
                    case PrimitiveType.FLOAT_ID:
                        return "" + new Float(tempStructAttribute.getFloat(this.attributeRef));
                    case PrimitiveType.DOUBLE_ID:
                        return "" + new Double(tempStructAttribute.getDouble(this.attributeRef));
                    case ArrayType.BYTE_ARRAY_ID:
                        return tempStructAttribute.getArray(this.attributeRef);
                    case ArrayType.SHORT_ARRAY_ID:
                        return tempStructAttribute.getArray(this.attributeRef);
                    case ArrayType.INT_ARRAY_ID:
                        return tempStructAttribute.getArray(this.attributeRef);
                    case ArrayType.LONG_ARRAY_ID:
                        return tempStructAttribute.getArray(this.attributeRef);
                    case ArrayType.CHAR_ARRAY_ID:
                        return tempStructAttribute.getArray(this.attributeRef);
                    case ArrayType.BOOLEAN_ARRAY_ID:
                        return tempStructAttribute.getArray(this.attributeRef);
                    case StringType.STRING_ID:
                        return tempStructAttribute.getString(this.attributeRef);
                    case ArrayType.STRING_ARRAY_ID:
                        return tempStructAttribute.getArray(this.attributeRef);
                    default:
                        throw new IllegalArgumentException("" + this.attributeRef.getType().getName());
                }
            }
            return null;
        }
        Attribute tempAttribute = _context.getAttribute(this.attrIndex);
        return tempAttribute == null ? null : tempAttribute.toString();
    }

    /**
     * Get the attribute's value from Context.
     *
     * @param _context
     * @param _itemIndex
     * @return
     */
    public Object getObjectValue(NormalizedContext _context, int _fieldType, int _itemIndex) {
        if (this.attributeRef != null) {
            return getObjectFromStructAttribute(_context, _fieldType, _itemIndex);
        }
        Attribute tempAttribute = _context.getAttribute(this.attrIndex);
        if (tempAttribute == null) {
            return null;
        }
        return JDBCUtil.getPrimitiveAttributeValue(tempAttribute, this.attrIndex, _fieldType);
    }

    /**
     * Get the Struct Attribute Object Value from Context.
     *
     * @param _context
     * @param _fieldType
     * @param _itemIndex
     * @return
     */
    private Object getObjectFromStructAttribute(NormalizedContext _context, int _fieldType, int _itemIndex) {
        StructAttrAttribute tempAttr = (StructAttrAttribute) _context.getAttribute(this.attrIndex);
        StructAttribute tempStructAttribute = tempAttr.getStructAttribute();
        if (tempStructAttribute == null) {
            throw new DataCenterException(ExceptionCode.RULE_APPLY_SA_IS_NULL, "The Struct Attribute[" + this.attributeName + "] has not been initialized.");
        }
        switch (_fieldType) {
            case Types.BIT:
            case Types.BOOLEAN:
                if (this.isArrayFlag() && _itemIndex >= 0) {
                    if (this.attributeRef.getType().getID() != ArrayType.BOOLEAN_ARRAY_ID) {
                        throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the StructAttribute Attribute:" + this.sourceAttributeName);
                    }
                    BooleanArray tempArray = (BooleanArray) tempStructAttribute.getArray(this.attributeRef);
                    return tempArray.get(_itemIndex);
                }
                if (this.attributeRef.getType().getID() != PrimitiveType.BOOLEAN_ID) {
                    throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the StructAttribute Attribute:" + this.sourceAttributeName);
                }
                return tempStructAttribute.getBoolean(this.attributeRef);
            case Types.TINYINT:
                if (this.isArrayFlag() && _itemIndex >= 0) {
                    if (this.attributeRef.getType().getID() != ArrayType.BYTE_ARRAY_ID) {
                        throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the StructAttribute Attribute:" + this.sourceAttributeName);
                    }
                    ByteArray tempArray = (ByteArray) tempStructAttribute.getArray(this.attributeRef);
                    return tempArray.get(_itemIndex);
                }
                if (this.attributeRef.getType().getID() != PrimitiveType.BYTE_ID) {
                    throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the StructAttribute Attribute:" + this.sourceAttributeName);
                }
                return tempStructAttribute.getByte(this.attributeRef);
            case Types.BIGINT:
                if (this.isArrayFlag() && _itemIndex >= 0) {
                    if (this.attributeRef.getType().getID() != ArrayType.LONG_ARRAY_ID) {
                        throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the StructAttribute Attribute:" + this.sourceAttributeName);
                    }
                    LongArray tempArray = (LongArray) tempStructAttribute.getArray(this.attributeRef);
                    return tempArray.get(_itemIndex);
                }
                if (this.attributeRef.getType().getID() != PrimitiveType.LONG_ID) {
                    throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the StructAttribute Attribute:" + this.sourceAttributeName);
                }
                return tempStructAttribute.getLong(this.attributeRef);
            case Types.LONGVARBINARY:
            case Types.VARBINARY:
            case Types.BINARY:
                byte[] tempByteArray = null;
                if (this.attributeRef.getType().isArrayType()) {
                    Array tempArray = tempStructAttribute.getArray(this.attributeRef);
                    if (!(tempArray instanceof MutableByteArray)) {
                        throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the StructAttribute Attribute:" + this.sourceAttributeName);
                    }
                    tempByteArray = ((MutableByteArray) tempArray).array();
                } else {
                    if (this.attributeRef.getType().getID() != StringType.STRING_ID) {
                        throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the StructAttribute Attribute:" + this.sourceAttributeName);
                    }
                    if (tempStructAttribute.getString(this.attributeRef) != null) {
                        tempByteArray = tempStructAttribute.getString(this.attributeRef).getBytes();
                    }
                }
                return tempByteArray;
            case Types.LONGVARCHAR:
            case Types.CHAR:
            case Types.VARCHAR:
                if (this.isArrayFlag() && _itemIndex >= 0) {
                    if (this.attributeRef.getType().getID() != ArrayType.STRING_ARRAY_ID) {
                        throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the StructAttribute Attribute:" + this.sourceAttributeName);
                    }
                    StringArray tempArray = (StringArray) tempStructAttribute.getArray(this.attributeRef);
                    return tempArray.get(_itemIndex);
                }
                if (this.attributeRef.getType().getID() != StringType.STRING_ID) {
                    throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the StructAttribute Attribute:" + this.sourceAttributeName);
                }

                return tempStructAttribute.getString(this.attributeRef);
            case Types.NULL:
            case 9:
            case 10:
            case 11:
            case 13:
            case 14:
            case 15:
            default:
                throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the StructAttribute Attribute:" + this.sourceAttributeName);
            case Types.NUMERIC:
            case Types.DECIMAL:
                if (this.isArrayFlag() && _itemIndex >= 0) {
                    if (this.attributeRef.getType().getID() == ArrayType.LONG_ARRAY_ID) {
                        LongArray tempArray = (LongArray) tempStructAttribute.getArray(this.attributeRef);
                        return tempArray.get(_itemIndex);
                    }
                    if (this.attributeRef.getType().getID() != ArrayType.INT_ARRAY_ID) {
                        throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the StructAttribute Attribute:" + this.sourceAttributeName);
                    }
                    IntegerArray tempArray = (IntegerArray) tempStructAttribute.getArray(this.attributeRef);
                    return (long) tempArray.get(_itemIndex);
                }

                if (this.attributeRef.getType().getID() == PrimitiveType.LONG_ID) {
                    return tempStructAttribute.getLong(this.attributeRef);
                }
                if (this.attributeRef.getType().getID() != PrimitiveType.INT_ID) {
                    throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the StructAttribute Attribute:" + this.sourceAttributeName);
                }
                return (long) tempStructAttribute.getInt(this.attributeRef);
            case Types.INTEGER:
                if (this.isArrayFlag() && _itemIndex >= 0) {
                    if (this.attributeRef.getType().getID() == ArrayType.LONG_ARRAY_ID) {
                        LongArray tempArray = (LongArray) tempStructAttribute.getArray(this.attributeRef);
                        return Long.valueOf(tempArray.get(_itemIndex)).intValue();
                    }
                    if (this.attributeRef.getType().getID() != ArrayType.INT_ARRAY_ID) {
                        throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the StructAttribute Attribute:" + this.sourceAttributeName);
                    }
                    IntegerArray tempArray = (IntegerArray) tempStructAttribute.getArray(this.attributeRef);
                    return tempArray.get(_itemIndex);
                }
                int tempValue;
                if (this.attributeRef.getType().getID() == PrimitiveType.LONG_ID && ((LongAttribute) this.attributeRef).getLong() <= 2147483647L && ((LongAttribute) this.attributeRef).getLong() >= -2147483648L) {
                    tempValue = (int) tempStructAttribute.getLong(this.attributeRef);
                } else {
                    if (this.attributeRef.getType().getID() != PrimitiveType.INT_ID) {
                        throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the StructAttribute Attribute:" + this.sourceAttributeName);
                    }
                    tempValue = tempStructAttribute.getInt(this.attributeRef);
                }
                return tempValue;
            case Types.SMALLINT:
                if (this.isArrayFlag() && _itemIndex >= 0) {
                    if (this.attributeRef.getType().getID() != ArrayType.SHORT_ARRAY_ID) {
                        throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the StructAttribute Attribute:" + this.sourceAttributeName);
                    }
                    ShortArray tempArray = (ShortArray) tempStructAttribute.getArray(this.attributeRef);
                    return tempArray.get(_itemIndex);
                }
                if (this.attributeRef.getType().getID() != PrimitiveType.SHORT_ID) {
                    throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the StructAttribute Attribute:" + this.sourceAttributeName);
                }
                return tempStructAttribute.getShort(this.attributeRef);
            case Types.FLOAT:
            case Types.REAL:
            case Types.DOUBLE:
                if (this.isArrayFlag() && _itemIndex >= 0) {
                    if (this.attributeRef.getType().getID() == ArrayType.FLOAT_ARRAY_ID) {
                        FloatArray tempArray = (FloatArray) tempStructAttribute.getArray(this.attributeRef);
                        return tempArray.get(_itemIndex);
                    }
                    if (this.attributeRef.getType().getID() != ArrayType.DOUBLE_ARRAY_ID) {
                        throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the StructAttribute Attribute:" + this.sourceAttributeName);
                    }
                    DoubleArray tempArray = (DoubleArray) tempStructAttribute.getArray(this.attributeRef);
                    return tempArray.get(_itemIndex);
                }
                if (this.attributeRef.getType().getID() == PrimitiveType.FLOAT_ID) {
                    return tempStructAttribute.getFloat(this.attributeRef);
                }
                if (this.attributeRef.getType().getID() != PrimitiveType.DOUBLE_ID) {
                    throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the StructAttribute Attribute:" + this.sourceAttributeName);
                }
                return tempStructAttribute.getDouble(this.attributeRef);
        }
    }
    /**
    public Object getValueForSQL(NormalizedContext _context) {
        if (this.attributeRef == null) {
            Attribute tempAttribute = _context.getAttribute(this.attrIndex);
            if (tempAttribute != null) {
                if (tempAttribute instanceof TimeAttribute) {
                    long tempMilliseconds = ((TimeAttribute) tempAttribute).getMilliseconds();
                    Timestamp tempTimeStamp = new Timestamp(0L);
                    tempTimeStamp.setTime(tempMilliseconds);
                    return tempTimeStamp.toString();
                }
                return tempAttribute.toString();
            }
            return null;
        }
        return this.getValue(_context);
    }**/

    public boolean isIntegerAttr(NormalizedContext _context) {
        if (this.attributeRef == null) {
            Attribute tempAttribute = _context.getAttribute(this.attrIndex);
            if (tempAttribute == null) {
                return false;
            } else {
                return tempAttribute instanceof IntegerAttribute;
            }
        }
        return this.attributeRef.getType().getID() == 3;
    }

    public boolean isStringAttr(NormalizedContext _context) {
        if (this.attributeRef == null) {
            Attribute tempAttribute = _context.getAttribute(this.attrIndex);
            if (tempAttribute == null) {
                return false;
            } else {
                return tempAttribute instanceof StringAttribute;
            }
        }
        return this.attributeRef.getType().getID() == 33554432;
    }

    public boolean isListAttribute() {
        return this.attributeRef == null ? PrimitiveAttributeSchema.getInstance().newAttributeInstance(this.attrIndex) instanceof ListAttribute : false;
    }

    public void setTimeformat(NormalizedContext _context) {
        if (this.attributeRef == null) {
            Attribute tempAttribute = _context.getAttribute(this.attrIndex);
            if (tempAttribute == null) {
                tempAttribute = PrimitiveAttributeSchema.getInstance().newAttributeInstance(this.attrIndex);
            }

            if (tempAttribute != null && tempAttribute instanceof TimeAttribute) {
                ((TimeAttribute) tempAttribute).setTimeFormat(TimeAttribute.DEFAULT_TIME_FORMAT);
            }
        }
    }

    private AttributeInfo getAttributeInfo() {
        return this.attributeRef == null ? PrimitiveAttributeSchema.getInstance().getAttribute(this.attrIndex) : null;
    }

    public AttributeRef getAttributeRef() {
        return this.attributeRef;
    }

    public String getAttributeName() {
        return this.attributeName;
    }

    public boolean isIntegerAttributeIF() {
        if (this.attributeRef == null) {
            return PrimitiveAttributeSchema.getInstance().newAttributeInstance(this.attrIndex) instanceof IIntegerAttribute;
        } else {
            int tempTypeId = this.attributeRef.getType().getID();
            return tempTypeId == 3 || tempTypeId == 4;
        }
    }

    public boolean isNumericAttribute(NormalizedContext var1) {
        if (this.attributeRef == null) {
            Attribute var3 = var1.getAttribute(this.attrIndex);
            if (var3 == null) {
                return false;
            } else {
                return var3 instanceof INumericAttribute;
            }
        } else {
            int var2 = this.attributeRef.getType().getID();
            return var2 == 3 || var2 == 4 || var2 == 9 || var2 == 10;
        }
    }

    private boolean updateSNMEAttributeFromResultSet(Object[] var1, int var2, ResultSet var3, int var4, int var5) throws
            SQLException {
        Object var6 = var3.getObject(var4);
        if (var6 == null) {
            return false;
        } else {
            boolean var8 = true;
            int var9 = this.attributeRef.getType().getID();
            switch (var5) {
                case -7:
                case -6:
                case -5:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 16:
                    switch (var9) {
                        case 1:
                            var1[var2] = "" + var3.getByte(var4);
                            return var3.wasNull() ? false : var8;
                        case 2:
                            var1[var2] = "" + var3.getShort(var4);
                            return var3.wasNull() ? false : var8;
                        case 3:
                            var1[var2] = "" + var3.getInt(var4);
                            return var3.wasNull() ? false : var8;
                        case 4:
                            var1[var2] = "" + var3.getLong(var4);
                            return var3.wasNull() ? false : var8;
                        case 9:
                            var1[var2] = "" + var3.getFloat(var4);
                            return var3.wasNull() ? false : var8;
                        case 10:
                            var1[var2] = "" + var3.getDouble(var4);
                            return var3.wasNull() ? false : var8;
                        case 16777217:
                            var1[var2] = "" + var3.getByte(var4);
                            return var3.wasNull() ? false : var8;
                        case 16777218:
                            var1[var2] = "" + var3.getShort(var4);
                            return var3.wasNull() ? false : var8;
                        case 16777219:
                            var1[var2] = "" + var3.getInt(var4);
                            return var3.wasNull() ? false : var8;
                        case 16777225:
                            var1[var2] = "" + var3.getFloat(var4);
                            return var3.wasNull() ? false : var8;
                        case 16777226:
                            var1[var2] = "" + var3.getDouble(var4);
                            return var3.wasNull() ? false : var8;
                        case 33554432:
                            var1[var2] = "" + var3.getString(var4);
                            return var3.wasNull() ? false : var8;
                        default:
                            var8 = false;
                            return var3.wasNull() ? false : var8;
                    }
                case -4:
                case -3:
                case -2:
                case 2004:
                    byte[] var13 = var3.getBytes(var4);
                    switch (var9) {
                        case 16777217:
                        case 33554432:
                            var1[var2] = "" + new String(var13);
                            return var3.wasNull() ? false : var8;
                        default:
                            var8 = false;
                            return var3.wasNull() ? false : var8;
                    }
                case -1:
                case 1:
                case 12:
                    String var7 = var3.getString(var4);
                    switch (var9) {
                        case 3:
                            var1[var2] = "" + var7;
                            return var3.wasNull() ? false : var8;
                        case 4:
                            var1[var2] = "" + Long.parseLong(var7);
                            return var3.wasNull() ? false : var8;
                        case 9:
                            var1[var2] = "" + Float.parseFloat(var7);
                            return var3.wasNull() ? false : var8;
                        case 10:
                            var1[var2] = "" + Double.parseDouble(var7);
                            return var3.wasNull() ? false : var8;
                        case 16777217:
                        case 33554432:
                            var1[var2] = "" + var7;
                            return var3.wasNull() ? false : var8;
                        case 16777219:
                            var1[var2] = "" + var7;
                            return var3.wasNull() ? false : var8;
                        case 16777220:
                            var1[var2] = "" + Long.parseLong(var7);
                            return var3.wasNull() ? false : var8;
                        case 16777225:
                            var1[var2] = "" + Float.parseFloat(var7);
                            return var3.wasNull() ? false : var8;
                        case 16777226:
                            var1[var2] = "" + Double.parseDouble(var7);
                            return var3.wasNull() ? false : var8;
                        case 50331648:
                            var1[var2] = "" + var7;
                            return var3.wasNull() ? false : var8;
                        default:
                            var8 = false;
                            return var3.wasNull() ? false : var8;
                    }
                case 91:
                case 92:
                case 93:
                    Timestamp var10 = var3.getTimestamp(var4);
                    long var11 = 0L;
                    if (var10 != null) {
                        var11 = var10.getTime();
                    }

                    switch (var9) {
                        case 3:
                            var1[var2] = "" + (int) (var11 / 1000L);
                            return var3.wasNull() ? false : var8;
                        case 4:
                            var1[var2] = "" + var11;
                            return var3.wasNull() ? false : var8;
                        case 9:
                            var1[var2] = "" + (float) var11;
                            return var3.wasNull() ? false : var8;
                        case 10:
                            var1[var2] = "" + (double) var11;
                            return var3.wasNull() ? false : var8;
                        case 33554432:
                            var1[var2] = "" + var10.toString();
                            return var3.wasNull() ? false : var8;
                        default:
                            var8 = false;
                            return var3.wasNull() ? false : var8;
                    }
                default:
                    String var14 = var3.getString(var4);
                    if (var14 != null) {
                        var1[var2] = "" + var14;
                    }
            }

            return var3.wasNull() ? false : var8;
        }
    }

    public int getColumnTypes(int _type) {
        int tempPrimitiveType;
        if (this.attributeRef == null) {
            tempPrimitiveType = PrimitiveAttributeSchema.getInstance().getAttribute(this.attrIndex).getPrimitiveType();
            if (tempPrimitiveType == 1) {
                return 4;
            }

            if (tempPrimitiveType == 3) {
                return -5;
            }

            if (tempPrimitiveType == 4) {
                return 7;
            }

            if (tempPrimitiveType == 5) {
                return 8;
            }
        } else {
            tempPrimitiveType = this.attributeRef.getType().getID();
            if (tempPrimitiveType == 3) {
                return 4;
            }

            if (tempPrimitiveType == 4) {
                return -5;
            }

            if (tempPrimitiveType == 9) {
                return 7;
            }

            if (tempPrimitiveType == 10) {
                return 8;
            }
        }

        return _type;
    }

    public boolean isListType(NormalizedContext _context) {
        return this.attributeRef == null ? _context.getAttribute(this.attrIndex) instanceof ListAttribute : this.attributeRef.getType().isArrayType();
    }

    public int getAttriButeType() {
        return this.attributeRef == null ? PrimitiveAttributeSchema.getInstance().getAttribute(this.attrIndex).getPrimitiveType() : this.attributeRef.getType().getID();
    }

    public boolean verifyType(int _type) throws ConfigurationException {
        boolean attrRefFlag = this.attributeRef == null;
        int tempPrimitiveType;
        if (attrRefFlag) {
            AttributeInfo tempAttrInfo = PrimitiveAttributeSchema.getInstance().getAttribute(this.attrIndex);
            if (tempAttrInfo == null) {
                throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ATTR_MISSED_IN_SCHEMA, "'" + this.attributeName + "' is not defined in Attribute Schema");
            }

            tempPrimitiveType = tempAttrInfo.getPrimitiveType();
        } else {
            tempPrimitiveType = this.attributeRef.getType().getID();
        }

        switch (_type) {
            case -7:
            case -6:
            case 5:
            case 16:
                if (attrRefFlag) {
                    return tempPrimitiveType == 1;
                }

                return tempPrimitiveType == 3;
            case -5:
                if (attrRefFlag) {
                    return tempPrimitiveType == 3;
                }

                return tempPrimitiveType == 4;
            case -4:
            case -3:
            case -2:
                if (attrRefFlag) {
                    return tempPrimitiveType == 6 || tempPrimitiveType == 2;
                } else {
                    return tempPrimitiveType == 16777217 || tempPrimitiveType == 33554432;
                }
            case -1:
            case 1:
            case 12:
                if (attrRefFlag) {
                    return tempPrimitiveType == 2 || tempPrimitiveType == 1 || tempPrimitiveType == 3 || tempPrimitiveType == 4 || tempPrimitiveType == 5 || tempPrimitiveType == 6;
                }

                return tempPrimitiveType == 33554432 || tempPrimitiveType == 3 || tempPrimitiveType == 4 || tempPrimitiveType == 9 || tempPrimitiveType == 10 || tempPrimitiveType == 16777217;
            case 2:
            case 3:
            case 4:
                if (attrRefFlag) {
                    return tempPrimitiveType == 1 || tempPrimitiveType == 3;
                }

                return tempPrimitiveType == 3 || tempPrimitiveType == 4;
            case 6:
            case 7:
            case 8:
                if (attrRefFlag) {
                    return tempPrimitiveType == 4 || tempPrimitiveType == 5;
                }

                return tempPrimitiveType == 9 || tempPrimitiveType == 10;
            case 91:
            case 92:
            case 93:
                if (attrRefFlag) {
                    return tempPrimitiveType == 2 || tempPrimitiveType == 1 || tempPrimitiveType == 3;
                }

                return tempPrimitiveType == 33554432 || tempPrimitiveType == 3 || tempPrimitiveType == 4;
            default:
                throw new RuntimeException("Cannot map Column :" + this.attributeName + " with SQL type : " + _type + " to the StructAttribute Attribute : " + this.attributeName);
        }
    }

    @Override
    public HybridAttribute clone() {
        return new HybridAttribute(this.attrIndex, this.attributeName, this.structType, this.attributeRef);
    }

    public Attribute newAttributeInstance(NormalizedContext _context) {
        if (this.attributeRef == null) {
            return _context.getAttribute(this.attrIndex);
        }
        int tempTypeId = this.attributeRef.getType().getID();
        switch (tempTypeId) {
            case PrimitiveType.BYTE_ID:
            case PrimitiveType.SHORT_ID:
            case PrimitiveType.CHAR_ID:
            case PrimitiveType.BOOLEAN_ID:
            case StringType.STRING_ID:
                return new StringAttribute();
            case PrimitiveType.INT_ID:
                return new IntegerAttribute();
            case PrimitiveType.LONG_ID:
                return new LongAttribute();
            case PrimitiveType.FLOAT_ID:
                return new FloatAttribute();
            case PrimitiveType.DOUBLE_ID:
                return new DoubleAttribute();
            default:
                throw new IllegalArgumentException("" + this.attributeRef.getType().getName());
        }
    }
}

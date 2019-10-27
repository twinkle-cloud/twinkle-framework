package com.twinkle.framework.datacenter.support;

import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.context.AttributeInfo;
import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.api.exception.DataCenterException;
import com.twinkle.framework.context.PrimitiveAttributeSchema;
import com.twinkle.framework.context.support.DynamicStructAttribute;
import com.twinkle.framework.core.lang.*;
import com.twinkle.framework.core.lang.util.*;
import com.twinkle.framework.datacenter.utils.JDBCUtil;
import com.twinkle.framework.struct.context.StructAttributeSchemaManager;
import com.twinkle.framework.struct.factory.StructAttributeFactory;
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
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.StringTokenizer;

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
    private DynamicStructAttribute structAttribute;
    private boolean arrayFlag = false;
    private boolean needItemFlag = false;
    /**
     * The attribute type. Refer to Attribute.TYPE...
     * and PrimitiveType.xxxx, StringType.xxxx, ArrayType.xxx.
     */
    private int attributeType = -1;

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
            StructType tempStructType = StructAttributeSchemaManager.getStructAttributeSchema().getStructAttributeType(tempAttrInfo.getValueType());
            this.structAttribute = new DynamicStructAttribute(tempSubAttrName, tempStructType, _expression);
            this.attributeType = this.structAttribute.getStructAttributeRef().getType().getID();
            if (this.structAttribute == null) {
                throw new ConfigurationException(ExceptionCode.LOGIC_CONF_SA_TYPE_ATTR_FIELD_MISSED, "The sub attribute [{" + tempSubAttrName + "}] in the expression [" + _expression + "] is not found in the StructAttribute [{" + tempAttrInfo.getValueType() + "}].");
            }
            if (!this.structAttribute.getDynamicAttributeRef().getType().isArrayType() && this.needItemFlag) {
                throw new ConfigurationException(ExceptionCode.LOGIC_CONF_SA_NAME_INVALID, "The sub attribute [{" + tempSubAttrName + "}] in the expression [" + _expression + "] is not array type, so [] not supported here.");
            }
        } else {
            this.attributeType = tempAttrInfo.getPrimitiveType();
            this.structAttribute = null;
            if (this.needItemFlag) {
                if (tempAttrInfo.getPrimitiveType() != Attribute.LIST_ATTRIBUTE_TYPE &&
                        tempAttrInfo.getPrimitiveType() != Attribute.LIST_STRUCT_ATTRIBUTE_TYPE) {
                    throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ATTR_TYPE_IS_UNEXPECTED, "The attribute [{" + this.attributeName + "}] in the expression [" + _expression + "] is not list type, so [] not supported here.");
                }
            }
        }
    }

    /**
     * For clone Attribute.
     *
     * @param _index
     * @param _attrName
     * @param _structAttr
     */
    private HybridAttribute(int _index, String _attrName, DynamicStructAttribute _structAttr) {
        this.DELIMITER = "|";
        this.TOKENS_IN_LISTATTR = 4;
        this.attrIndex = _index;
        this.attributeName = _attrName;
        this.structAttribute = _structAttr;
    }

    /**
     * Get Array size.
     *
     * @param _context
     * @return
     */
    public int getArraySize(NormalizedContext _context) {
        Attribute tempAttr = _context.getAttribute(this.attrIndex);
        if (this.structAttribute != null) {
            StructAttribute tempStructAttribute = (StructAttribute) tempAttr.getObjectValue();
            AttributeRef tempRef = this.structAttribute.resolve(_context, tempStructAttribute);
            return tempStructAttribute.getArraySize(tempRef);
        }
        if (tempAttr instanceof IListAttribute) {
            return ((IListAttribute) tempAttr).size();
        }
        return 0;
    }

    /**
     * Update the Attribute's value with SQL result.
     *
     * @param _context
     * @param _rowSet
     * @param _fieldName
     * @param _itemIndex
     * @param _defaultValue
     */
    public void setValue(NormalizedContext _context, SqlRowSet _rowSet, String _fieldName, int _sqlType, int _itemIndex, String _defaultValue) throws SQLException {
        Object tempValueObj = this.getResultSetValue(_rowSet, _fieldName, _sqlType);
        if (tempValueObj == null && _defaultValue != null) {
            tempValueObj = _defaultValue;
        }
        Attribute tempAttribute = _context.getAttribute(this.attrIndex);
        if (this.structAttribute == null) {
            if (tempValueObj == null) {
                _context.setAttribute(null, this.attrIndex);
                return;
            }
            if (tempAttribute == null) {
                tempAttribute = PrimitiveAttributeSchema.getInstance().newAttributeInstance(this.attrIndex);
                _context.setAttribute(tempAttribute, this.attrIndex);
            }
            if (tempAttribute instanceof TimeAttribute) {
                ((TimeAttribute) tempAttribute).setTimeFormat(TimeAttribute.DEFAULT_TIME_FORMAT);
                tempAttribute.setValue(tempValueObj);
            } else if (tempAttribute instanceof ListAttribute) {
                String tempStr = tempValueObj.toString();
                if (this.needItemFlag) {
                    ((ListAttribute) tempAttribute).add(new StringAttribute(tempStr));
                } else {
                    StringTokenizer tempTokenizer = new StringTokenizer(tempStr, "|", true);
                    int tempCount = tempTokenizer.countTokens();
                    String tempToken = tempTokenizer.nextToken();
                    if (tempToken != null && tempCount > 4) {
                        tempAttribute.setValue(tempStr);
                    } else {
                        ((ListAttribute) tempAttribute).add(new StringAttribute(tempStr));
                    }
                }
            } else {
                tempAttribute.setValue(tempValueObj);
            }
        } else {
            if (tempAttribute == null && tempValueObj == null) {
                return;
            } else if (tempAttribute == null) {
                tempAttribute = PrimitiveAttributeSchema.getInstance().newAttributeInstance(this.attrIndex);
                _context.setAttribute(tempAttribute, this.attrIndex);
            }
            StructAttribute tempStructAttribute = ((StructAttrAttribute) tempAttribute).getStructAttribute();
            StructAttributeFactory tempFactory = StructAttributeSchemaManager.getStructAttributeFactory();
            AttributeRef tempAttrRef = this.structAttribute.resolve(_context, tempStructAttribute);
            if (tempStructAttribute == null) {
                if (tempValueObj == null) {
                    return;
                }
                tempStructAttribute = tempFactory.newStructAttribute(this.structAttribute.getStructType());
                tempAttribute.setValue(tempStructAttribute);
            }

            if (tempValueObj == null) {
                tempStructAttribute.clear(tempAttrRef);
                return;
            }
            StructAttributeUtil.updateStructAttributeValue(tempValueObj, tempStructAttribute, tempAttrRef, _itemIndex);
            tempAttribute.setValue(tempStructAttribute);
        }
    }

    /**
     * Get the attribute's value from Context.
     *
     * @param _context
     * @param _itemIndex
     * @return
     */
    public Object getObjectValue(NormalizedContext _context, int _fieldType, int _itemIndex) {
        if (this.structAttribute != null) {
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
        AttributeRef tempAttributeRef = this.structAttribute.resolve(_context, tempStructAttribute);
        switch (_fieldType) {
            case Types.BIT:
            case Types.BOOLEAN:
                if (this.isArrayFlag() && _itemIndex >= 0) {
                    if (tempAttributeRef.getType().getID() != ArrayType.BOOLEAN_ARRAY_ID) {
                        throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the StructAttribute Attribute:" + this.sourceAttributeName);
                    }
                    BooleanArray tempArray = (BooleanArray) tempStructAttribute.getArray(tempAttributeRef);
                    return tempArray.get(_itemIndex);
                }
                if (tempAttributeRef.getType().getID() != PrimitiveType.BOOLEAN_ID) {
                    throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the StructAttribute Attribute:" + this.sourceAttributeName);
                }
                return tempStructAttribute.getBoolean(tempAttributeRef);
            case Types.TINYINT:
                if (this.isArrayFlag() && _itemIndex >= 0) {
                    if (tempAttributeRef.getType().getID() != ArrayType.BYTE_ARRAY_ID) {
                        throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the StructAttribute Attribute:" + this.sourceAttributeName);
                    }
                    ByteArray tempArray = (ByteArray) tempStructAttribute.getArray(tempAttributeRef);
                    return tempArray.get(_itemIndex);
                }
                if (tempAttributeRef.getType().getID() != PrimitiveType.BYTE_ID) {
                    throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the StructAttribute Attribute:" + this.sourceAttributeName);
                }
                return tempStructAttribute.getByte(tempAttributeRef);
            case Types.BIGINT:
                if (this.isArrayFlag() && _itemIndex >= 0) {
                    if (tempAttributeRef.getType().getID() != ArrayType.LONG_ARRAY_ID) {
                        throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the StructAttribute Attribute:" + this.sourceAttributeName);
                    }
                    LongArray tempArray = (LongArray) tempStructAttribute.getArray(tempAttributeRef);
                    return tempArray.get(_itemIndex);
                }
                if (tempAttributeRef.getType().getID() != PrimitiveType.LONG_ID) {
                    throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the StructAttribute Attribute:" + this.sourceAttributeName);
                }
                return tempStructAttribute.getLong(tempAttributeRef);
            case Types.LONGVARBINARY:
            case Types.VARBINARY:
            case Types.BINARY:
                byte[] tempByteArray = null;
                if (tempAttributeRef.getType().isArrayType()) {
                    Array tempArray = tempStructAttribute.getArray(tempAttributeRef);
                    if (!(tempArray instanceof MutableByteArray)) {
                        throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the StructAttribute Attribute:" + this.sourceAttributeName);
                    }
                    tempByteArray = ((MutableByteArray) tempArray).array();
                } else {
                    if (tempAttributeRef.getType().getID() != StringType.STRING_ID) {
                        throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the StructAttribute Attribute:" + this.sourceAttributeName);
                    }
                    if (tempStructAttribute.getString(tempAttributeRef) != null) {
                        tempByteArray = tempStructAttribute.getString(tempAttributeRef).getBytes();
                    }
                }
                return tempByteArray;
            case Types.LONGVARCHAR:
            case Types.CHAR:
            case Types.VARCHAR:
                if (this.isArrayFlag() && _itemIndex >= 0) {
                    if (tempAttributeRef.getType().getID() != ArrayType.STRING_ARRAY_ID) {
                        throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the StructAttribute Attribute:" + this.sourceAttributeName);
                    }
                    StringArray tempArray = (StringArray) tempStructAttribute.getArray(tempAttributeRef);
                    return tempArray.get(_itemIndex);
                }
                if (tempAttributeRef.getType().getID() != StringType.STRING_ID) {
                    throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the StructAttribute Attribute:" + this.sourceAttributeName);
                }

                return tempStructAttribute.getString(tempAttributeRef);
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
                    if (tempAttributeRef.getType().getID() == ArrayType.LONG_ARRAY_ID) {
                        LongArray tempArray = (LongArray) tempStructAttribute.getArray(tempAttributeRef);
                        return tempArray.get(_itemIndex);
                    }
                    if (tempAttributeRef.getType().getID() != ArrayType.INT_ARRAY_ID) {
                        throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the StructAttribute Attribute:" + this.sourceAttributeName);
                    }
                    IntegerArray tempArray = (IntegerArray) tempStructAttribute.getArray(tempAttributeRef);
                    return (long) tempArray.get(_itemIndex);
                }

                if (tempAttributeRef.getType().getID() == PrimitiveType.LONG_ID) {
                    return tempStructAttribute.getLong(tempAttributeRef);
                }
                if (tempAttributeRef.getType().getID() != PrimitiveType.INT_ID) {
                    throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the StructAttribute Attribute:" + this.sourceAttributeName);
                }
                return (long) tempStructAttribute.getInt(tempAttributeRef);
            case Types.INTEGER:
                if (this.isArrayFlag() && _itemIndex >= 0) {
                    if (tempAttributeRef.getType().getID() == ArrayType.LONG_ARRAY_ID) {
                        LongArray tempArray = (LongArray) tempStructAttribute.getArray(tempAttributeRef);
                        return Long.valueOf(tempArray.get(_itemIndex)).intValue();
                    }
                    if (tempAttributeRef.getType().getID() != ArrayType.INT_ARRAY_ID) {
                        throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the StructAttribute Attribute:" + this.sourceAttributeName);
                    }
                    IntegerArray tempArray = (IntegerArray) tempStructAttribute.getArray(tempAttributeRef);
                    return tempArray.get(_itemIndex);
                }
                int tempValue;
                if (tempAttributeRef.getType().getID() == PrimitiveType.LONG_ID && ((LongAttribute) tempAttributeRef).getLong() <= 2147483647L && ((LongAttribute) tempAttributeRef).getLong() >= -2147483648L) {
                    tempValue = (int) tempStructAttribute.getLong(tempAttributeRef);
                } else {
                    if (tempAttributeRef.getType().getID() != PrimitiveType.INT_ID) {
                        throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the StructAttribute Attribute:" + this.sourceAttributeName);
                    }
                    tempValue = tempStructAttribute.getInt(tempAttributeRef);
                }
                return tempValue;
            case Types.SMALLINT:
                if (this.isArrayFlag() && _itemIndex >= 0) {
                    if (tempAttributeRef.getType().getID() != ArrayType.SHORT_ARRAY_ID) {
                        throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the StructAttribute Attribute:" + this.sourceAttributeName);
                    }
                    ShortArray tempArray = (ShortArray) tempStructAttribute.getArray(tempAttributeRef);
                    return tempArray.get(_itemIndex);
                }
                if (tempAttributeRef.getType().getID() != PrimitiveType.SHORT_ID) {
                    throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the StructAttribute Attribute:" + this.sourceAttributeName);
                }
                return tempStructAttribute.getShort(tempAttributeRef);
            case Types.FLOAT:
            case Types.REAL:
            case Types.DOUBLE:
                if (this.isArrayFlag() && _itemIndex >= 0) {
                    if (tempAttributeRef.getType().getID() == ArrayType.FLOAT_ARRAY_ID) {
                        FloatArray tempArray = (FloatArray) tempStructAttribute.getArray(tempAttributeRef);
                        return tempArray.get(_itemIndex);
                    }
                    if (tempAttributeRef.getType().getID() != ArrayType.DOUBLE_ARRAY_ID) {
                        throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the StructAttribute Attribute:" + this.sourceAttributeName);
                    }
                    DoubleArray tempArray = (DoubleArray) tempStructAttribute.getArray(tempAttributeRef);
                    return tempArray.get(_itemIndex);
                }
                if (tempAttributeRef.getType().getID() == PrimitiveType.FLOAT_ID) {
                    return tempStructAttribute.getFloat(tempAttributeRef);
                }
                if (tempAttributeRef.getType().getID() != PrimitiveType.DOUBLE_ID) {
                    throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the StructAttribute Attribute:" + this.sourceAttributeName);
                }
                return tempStructAttribute.getDouble(tempAttributeRef);
        }
    }

    /**
     * Get the ResultSet Value.
     *
     * @param _rowSet
     * @param _fieldName
     * @param _sqlType
     * @return
     * @throws SQLException
     */
    private Object getResultSetValue(SqlRowSet _rowSet, String _fieldName, int _sqlType) throws SQLException {
        Object tempObj = _rowSet.getObject(_fieldName);
        if (tempObj == null || _rowSet.wasNull()) {
            return null;
        }
        switch (_sqlType) {
            case Types.BIT:
            case Types.TINYINT:
            case Types.BIGINT:
            case Types.NUMERIC:
            case Types.DECIMAL:
            case Types.INTEGER:
            case Types.SMALLINT:
            case Types.FLOAT:
            case Types.REAL:
            case Types.DOUBLE:
            case Types.BOOLEAN:
                if (this.structAttribute != null) {
                    switch (this.attributeType) {
                        case PrimitiveType.BYTE_ID:
                        case ArrayType.BYTE_ARRAY_ID:
                            return _rowSet.getByte(_fieldName);
                        case PrimitiveType.SHORT_ID:
                        case ArrayType.SHORT_ARRAY_ID:
                            return _rowSet.getShort(_fieldName);
                        case PrimitiveType.INT_ID:
                        case ArrayType.INT_ARRAY_ID:
                            return _rowSet.getInt(_fieldName);
                        case PrimitiveType.LONG_ID:
                        case ArrayType.LONG_ARRAY_ID:
                            return _rowSet.getLong(_fieldName);
                        case PrimitiveType.FLOAT_ID:
                        case ArrayType.FLOAT_ARRAY_ID:
                            return _rowSet.getFloat(_fieldName);
                        case PrimitiveType.DOUBLE_ID:
                        case ArrayType.DOUBLE_ARRAY_ID:
                            return _rowSet.getDouble(_fieldName);
                        case StringType.STRING_ID:
                            return _rowSet.getString(_fieldName);
                        default:
                            return tempObj;
                    }
                } else {
                    switch (this.attributeType) {
                        case Attribute.INTEGER_TYPE:
                            return _rowSet.getInt(_fieldName);
                        case Attribute.STRING_TYPE:
                        case Attribute.UNICODE_STRING_TYPE:
                            return _rowSet.getString(_fieldName);
                        case Attribute.LONG_TYPE:
                            return _rowSet.getLong(_fieldName);
                        case Attribute.FLOAT_TYPE:
                            return _rowSet.getFloat(_fieldName);
                        case Attribute.DOUBLE_TYPE:
                            return _rowSet.getDouble(_fieldName);
                        case Attribute.BYTE_ARRAY_TYPE:
                            return _rowSet.getByte(_fieldName);
                        default:
                            return tempObj;
                    }
                }
            case Types.LONGVARBINARY:
            case Types.VARBINARY:
            case Types.BINARY:
            case Types.BLOB:
                byte[] tempArray;
                if (_sqlType == Types.BLOB) {
                    Long tempLength = ((Blob) tempObj).length();
                    if (tempLength > Integer.MAX_VALUE) {
                        tempArray = ((Blob) tempObj).getBytes(0, Integer.MAX_VALUE);
                    } else {
                        tempArray = ((Blob) tempObj).getBytes(0, tempLength.intValue());
                    }
                } else {
                    tempArray = (byte[]) tempObj;
                }
                if (this.structAttribute != null) {
                    switch (this.attributeType) {
                        case ArrayType.BYTE_ARRAY_ID:
                        case StringType.STRING_ID:
                            if (tempArray == null) {
                                return null;
                            }
                            return new String(tempArray);
                        default:
                            return tempObj;
                    }
                } else {
                    switch (this.attributeType) {
                        case Attribute.BYTE_ARRAY_TYPE:
                        case Attribute.UNICODE_STRING_TYPE:
                        case Attribute.STRING_TYPE:
                            if (tempArray == null) {
                                return null;
                            }
                            return new String(tempArray);
                        default:
                            return tempObj;
                    }
                }
            case Types.LONGVARCHAR:
            case Types.CHAR:
            case Types.VARCHAR:
                String tempStr = _rowSet.getString(_fieldName);
                if(this.structAttribute != null) {
                    switch (this.attributeType) {
                        case PrimitiveType.INT_ID:
                        case ArrayType.INT_ARRAY_ID:
                            return Integer.parseInt(tempStr);
                        case PrimitiveType.LONG_ID:
                        case ArrayType.LONG_ARRAY_ID:
                            return Long.parseLong(tempStr);
                        case PrimitiveType.FLOAT_ID:
                        case ArrayType.FLOAT_ARRAY_ID:
                            return Float.parseFloat(tempStr);
                        case PrimitiveType.DOUBLE_ID:
                        case ArrayType.DOUBLE_ARRAY_ID:
                            return Double.parseDouble(tempStr);
                        case ArrayType.BYTE_ARRAY_ID:
                        case StringType.STRING_ID:
                        case ArrayType.STRING_ARRAY_ID:
                            return tempStr;
                        default:
                            return tempObj;
                    }
                } else {
                    switch (this.attributeType) {
                        case Attribute.INTEGER_TYPE:
                            return Integer.parseInt(tempStr);
                        case Attribute.LONG_TYPE:
                            return Long.parseLong(tempStr);
                        case Attribute.FLOAT_TYPE:
                            return Float.parseFloat(tempStr);
                        case Attribute.DOUBLE_TYPE:
                            return Double.parseDouble(tempStr);
                        case Attribute.BYTE_ARRAY_TYPE:
                        case Attribute.STRING_TYPE:
                        case Attribute.UNICODE_STRING_TYPE:
                            return tempStr;
                        default:
                            return tempObj;
                    }
                }
            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
                Timestamp tempTimeStamp = _rowSet.getTimestamp(_fieldName);
                long tempValue = 0L;
                if (tempTimeStamp != null) {
                    tempValue = tempTimeStamp.getTime();
                }
                if(this.structAttribute != null) {
                    switch (this.attributeType) {
                        case PrimitiveType.INT_ID:
                            return (int) (tempValue / 1000L);
                        case PrimitiveType.LONG_ID:
                            return tempValue;
                        case PrimitiveType.FLOAT_ID:
                            return (float) tempValue;
                        case PrimitiveType.DOUBLE_ID:
                            return (double) tempValue;
                        case StringType.STRING_ID:
                            return tempTimeStamp.toString();
                        default:
                            return tempObj;
                    }
                } else {
                    return tempObj;
//                    switch (this.attributeType) {
//                        case Attribute.INTEGER_TYPE:
//                            return (int) (tempValue / 1000L);
//                        case Attribute.LONG_TYPE:
//                            return tempValue;
//                        case Attribute.FLOAT_TYPE:
//                            return (float) tempValue;
//                        case Attribute.DOUBLE_TYPE:
//                            return (double) tempValue;
//                        case Attribute.STRING_TYPE:
//                        case Attribute.UNICODE_STRING_TYPE:
//                            return tempTimeStamp.toString();
//                        default:
//                            return tempObj;
//                    }
                }
            default:
                return _rowSet.getString(_fieldName);
        }
    }

    /**
     * Get mapping column type.
     *
     * @param _type
     * @return
     */
    public int getColumnTypes(int _type) {
        int tempPrimitiveType;
        if (this.structAttribute == null) {
            tempPrimitiveType = PrimitiveAttributeSchema.getInstance().getAttribute(this.attrIndex).getPrimitiveType();
            if (tempPrimitiveType == Attribute.INTEGER_TYPE) {
                return Types.INTEGER;
            }
            if (tempPrimitiveType == Attribute.LONG_TYPE) {
                return Types.BIGINT;
            }
            if (tempPrimitiveType == Attribute.FLOAT_TYPE) {
                return Types.REAL;
            }
            if (tempPrimitiveType == Attribute.DOUBLE_TYPE) {
                return Types.DOUBLE;
            }
        } else {
            tempPrimitiveType = this.structAttribute.getStructAttributeRef().getType().getID();
            if (tempPrimitiveType == PrimitiveType.INT_ID) {
                return Types.INTEGER;
            }
            if (tempPrimitiveType == PrimitiveType.LONG_ID) {
                return Types.BIGINT;
            }
            if (tempPrimitiveType == PrimitiveType.FLOAT_ID) {
                return Types.REAL;
            }
            if (tempPrimitiveType == PrimitiveType.DOUBLE_ID) {
                return Types.DOUBLE;
            }
        }

        return _type;
    }

    /**
     * Verify the SQL is mapping correct or not?
     *
     * @param _type
     * @return
     * @throws ConfigurationException
     */
    public boolean verifyType(int _type) throws ConfigurationException {
        boolean attrRefFlag = this.structAttribute == null;
        int tempPrimitiveType;
        if (attrRefFlag) {
            AttributeInfo tempAttrInfo = PrimitiveAttributeSchema.getInstance().getAttribute(this.attrIndex);
            if (tempAttrInfo == null) {
                throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ATTR_MISSED_IN_SCHEMA, "'" + this.attributeName + "' is not defined in Attribute Schema");
            }

            tempPrimitiveType = tempAttrInfo.getPrimitiveType();
        } else {
            tempPrimitiveType = this.structAttribute.getStructAttributeRef().getType().getID();
        }

        switch (_type) {
            case Types.BIT:
            case Types.TINYINT:
            case Types.SMALLINT:
            case Types.BOOLEAN:
                if (attrRefFlag) {
                    return tempPrimitiveType == Attribute.INTEGER_TYPE;
                }
                return tempPrimitiveType == PrimitiveType.INT_ID;
            case Types.BIGINT:
                if (attrRefFlag) {
                    return tempPrimitiveType == Attribute.LONG_TYPE;
                }
                return tempPrimitiveType == PrimitiveType.LONG_ID;
            case Types.LONGVARBINARY:
            case Types.VARBINARY:
            case Types.BINARY:
                if (attrRefFlag) {
                    return tempPrimitiveType == Attribute.BYTE_ARRAY_TYPE || tempPrimitiveType == Attribute.STRING_TYPE;
                } else {
                    return tempPrimitiveType == ArrayType.BYTE_ARRAY_ID || tempPrimitiveType == StringType.STRING_ID;
                }
            case Types.LONGVARCHAR:
            case Types.CHAR:
            case Types.VARCHAR:
                if (attrRefFlag) {
                    return tempPrimitiveType == Attribute.STRING_TYPE || tempPrimitiveType == Attribute.INTEGER_TYPE
                            || tempPrimitiveType == Attribute.LONG_TYPE || tempPrimitiveType == Attribute.FLOAT_TYPE
                            || tempPrimitiveType == Attribute.DOUBLE_TYPE || tempPrimitiveType == Attribute.BYTE_ARRAY_TYPE;
                }
                return tempPrimitiveType == StringType.STRING_ID || tempPrimitiveType == PrimitiveType.INT_ID
                        || tempPrimitiveType == PrimitiveType.LONG_ID || tempPrimitiveType == PrimitiveType.FLOAT_ID
                        || tempPrimitiveType == PrimitiveType.DOUBLE_ID || tempPrimitiveType == ArrayType.BYTE_ARRAY_ID;
            case Types.NUMERIC:
            case Types.DECIMAL:
            case Types.INTEGER:
                if (attrRefFlag) {
                    return tempPrimitiveType == Attribute.INTEGER_TYPE || tempPrimitiveType == Attribute.LONG_TYPE;
                }

                return tempPrimitiveType == PrimitiveType.INT_ID || tempPrimitiveType == PrimitiveType.LONG_ID;
            case Types.FLOAT:
            case Types.REAL:
            case Types.DOUBLE:
                if (attrRefFlag) {
                    return tempPrimitiveType == Attribute.FLOAT_TYPE || tempPrimitiveType == Attribute.DOUBLE_TYPE;
                }

                return tempPrimitiveType == PrimitiveType.FLOAT_ID || tempPrimitiveType == PrimitiveType.DOUBLE_ID;
            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
                if (attrRefFlag) {
                    return tempPrimitiveType == Attribute.STRING_TYPE || tempPrimitiveType == Attribute.INTEGER_TYPE
                            || tempPrimitiveType == Attribute.LONG_TYPE;
                }

                return tempPrimitiveType == StringType.STRING_ID || tempPrimitiveType == PrimitiveType.INT_ID
                        || tempPrimitiveType == PrimitiveType.LONG_ID;
            default:
                throw new RuntimeException("Cannot map Column :" + this.attributeName + " with SQL type : " + _type + " to the StructAttribute Attribute : " + this.attributeName);
        }
    }

    @Override
    public HybridAttribute clone() {
        return new HybridAttribute(this.attrIndex, this.attributeName, this.structAttribute);
    }
}

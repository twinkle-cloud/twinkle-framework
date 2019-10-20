package com.twinkle.framework.datacenter.utils;

import com.twinkle.framework.api.context.AttributeInfo;
import com.twinkle.framework.context.PrimitiveAttributeSchema;
import com.twinkle.framework.core.lang.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/18/19 6:21 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class JDBCUtil {
    public static final HashMap<String, Integer> SQL_TYPE_NAME_MAP = new HashMap();

    static {
        SQL_TYPE_NAME_MAP.put("BIT", Types.BIT);
        SQL_TYPE_NAME_MAP.put("TINYINT", Types.TINYINT);
        SQL_TYPE_NAME_MAP.put("SMALLINT", Types.SMALLINT);
        SQL_TYPE_NAME_MAP.put("INTEGER", Types.INTEGER);
        SQL_TYPE_NAME_MAP.put("BIGINT", Types.BIGINT);
        SQL_TYPE_NAME_MAP.put("FLOAT", Types.FLOAT);
        SQL_TYPE_NAME_MAP.put("REAL", Types.REAL);
        SQL_TYPE_NAME_MAP.put("DOUBLE", Types.DOUBLE);
        SQL_TYPE_NAME_MAP.put("DOUBLE PRECISION", Types.DOUBLE);
        SQL_TYPE_NAME_MAP.put("NUMERIC", Types.NUMERIC);
        SQL_TYPE_NAME_MAP.put("DECIMAL", Types.DECIMAL);
        SQL_TYPE_NAME_MAP.put("CHAR", Types.CHAR);
        SQL_TYPE_NAME_MAP.put("VARCHAR", Types.VARCHAR);
        SQL_TYPE_NAME_MAP.put("LONGVARCHAR", Types.LONGVARCHAR);
        SQL_TYPE_NAME_MAP.put("DATE", Types.DATE);
        SQL_TYPE_NAME_MAP.put("TIME", Types.TIME);
        SQL_TYPE_NAME_MAP.put("TIMESTAMP", Types.TIMESTAMP);
        SQL_TYPE_NAME_MAP.put("BINARY", Types.BINARY);
        SQL_TYPE_NAME_MAP.put("VARBINARY", Types.VARBINARY);
        SQL_TYPE_NAME_MAP.put("LONGVARBINARY", Types.LONGVARBINARY);
        SQL_TYPE_NAME_MAP.put("NULL", Types.NULL);
        SQL_TYPE_NAME_MAP.put("OTHER", Types.OTHER);
        SQL_TYPE_NAME_MAP.put("JAVA_OBJECT", Types.JAVA_OBJECT);
        SQL_TYPE_NAME_MAP.put("DISTINCT", Types.DISTINCT);
        SQL_TYPE_NAME_MAP.put("STRUCT", Types.STRUCT);
        SQL_TYPE_NAME_MAP.put("ARRAY", Types.ARRAY);
        SQL_TYPE_NAME_MAP.put("BLOB", Types.BLOB);
        SQL_TYPE_NAME_MAP.put("CLOB", Types.CLOB);
        SQL_TYPE_NAME_MAP.put("REF", Types.REF);
        SQL_TYPE_NAME_MAP.put("DATALINK", Types.DATALINK);
        SQL_TYPE_NAME_MAP.put("BOOLEAN", Types.BOOLEAN);
    }

    /**
     * Get the SQL Type with TypeName.
     * Refer to: java.sql.Types.
     *
     * @param _typeName
     * @return
     */
    public static int getSQLType(String _typeName) {
        String tempTypeName = _typeName.toUpperCase();
        Integer tempType = SQL_TYPE_NAME_MAP.get(tempTypeName);
        if (tempType == null) {
            throw new IllegalArgumentException("Type" + _typeName + " undefined SQL type.");
        }
        return tempType;
    }

    /**
     * Convert the given time.
     * The format should be #d#h#m#s.
     *
     * @param _timeStr
     * @return
     */
    public static int convertTimeToSeconds(String _timeStr) {
        String[] tempTimeKeyWords = new String[]{"d", "h", "m", "s"};
        String tempLowerTimeStr = _timeStr.toLowerCase();
        int tempResult = 0;
        boolean validFlag = false;
        int i = 0;

        for (int tempBeginIndex = 0; i < tempTimeKeyWords.length; ++i) {
            int tempKeyIndex = tempLowerTimeStr.indexOf(tempTimeKeyWords[i]);
            if (tempKeyIndex != -1) {
                validFlag = true;
                String tempStr = tempLowerTimeStr.substring(tempBeginIndex, tempKeyIndex);
                if (tempStr == "") {
                    throw new RuntimeException("Time Conversion error, '" + _timeStr + "' requires to have the format #d#h#m#s");
                }

                switch (i) {
                    case 0:
                        tempResult += Integer.parseInt(tempStr) * 24 * 3600;
                        break;
                    case 1:
                        tempResult += Integer.parseInt(tempStr) * 3600;
                        break;
                    case 2:
                        tempResult += Integer.parseInt(tempStr) * 60;
                        break;
                    case 3:
                        tempResult += Integer.parseInt(tempStr);
                }

                tempBeginIndex = tempKeyIndex + 1;
            }
        }

        if (!validFlag) {
            throw new RuntimeException("Time Conversion error, '" + _timeStr + "' requires to have the format [#d][#h][#m][#s]");
        }
        return tempResult;
    }

    /**
     * Get Primitive Attribute's SQL value.
     *
     * @param _attribute
     * @param _attributeIndex
     * @param _fieldType
     * @return
     */
    public static Object getPrimitiveAttributeValue(Attribute _attribute, int _attributeIndex, int _fieldType) {
        PrimitiveAttributeSchema tempSchema = PrimitiveAttributeSchema.getInstance();
        AttributeInfo tempAttrInfo = tempSchema.getAttribute(_attributeIndex);
        int tempAttrPrimitiveType = tempAttrInfo.getPrimitiveType();
        boolean tempBooleanResult;
        switch (_fieldType) {
            case Types.BIT:
            case Types.BOOLEAN:
                if (tempAttrPrimitiveType != Attribute.INTEGER_TYPE) {
                    throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the Attribute:" + tempSchema.getAttribute(_attributeIndex));
                }

                tempBooleanResult = (((IntegerAttribute) _attribute).getInt() & 1) != 0;
                return tempBooleanResult;
            case Types.TINYINT:
                if (tempAttrPrimitiveType != Attribute.INTEGER_TYPE) {
                    throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the Attribute:" + tempSchema.getAttribute(_attributeIndex));
                }

                byte tempValue = (byte) ((IntegerAttribute) _attribute).getInt();
                return tempValue;
            case Types.BIGINT:
                if (tempAttrPrimitiveType != Attribute.LONG_TYPE) {
                    throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the Attribute:" + tempSchema.getAttribute(_attributeIndex));
                }
                return ((LongAttribute) _attribute).getLong();
            case Types.LONGVARBINARY:
            case Types.VARBINARY:
            case Types.BINARY:
                byte[] tempArrayValue;
                if (tempAttrPrimitiveType == Attribute.BYTE_ARRAY_TYPE) {
                    tempArrayValue = ((BinaryAttribute) _attribute).getByteArray();
                } else {
                    if (tempAttrPrimitiveType != Attribute.STRING_TYPE) {
                        throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the Attribute:" + tempSchema.getAttribute(_attributeIndex));
                    }
                    tempArrayValue = _attribute.toString().getBytes();
                }

                return tempArrayValue;
            case Types.LONGVARCHAR:
            case Types.CHAR:
            case Types.VARCHAR:
                if (tempAttrPrimitiveType != Attribute.STRING_TYPE && tempAttrPrimitiveType != Attribute.INTEGER_TYPE
                        && tempAttrPrimitiveType != Attribute.LONG_TYPE && tempAttrPrimitiveType != Attribute.FLOAT_TYPE
                        && tempAttrPrimitiveType != Attribute.DOUBLE_TYPE && tempAttrPrimitiveType != Attribute.BYTE_ARRAY_TYPE) {
                    throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the Attribute:" + tempSchema.getAttribute(_attributeIndex));
                }

                return _attribute.toString();
            case Types.NUMERIC:
            case Types.DECIMAL:
                if (tempAttrPrimitiveType == Attribute.LONG_TYPE) {
                    return ((LongAttribute) _attribute).getLong();
                }
                if (tempAttrPrimitiveType != Attribute.INTEGER_TYPE) {
                    throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the Attribute:" + tempSchema.getAttribute(_attributeIndex));
                }
                return ((IntegerAttribute) _attribute).getLong();
            case Types.INTEGER:
                int tempIntValue;
                if (_attribute instanceof LongAttribute && ((LongAttribute) _attribute).getLong() <= 2147483647L && ((LongAttribute) _attribute).getLong() >= -2147483648L) {
                    tempIntValue = ((LongAttribute) _attribute).getInt();
                } else {
                    if (tempAttrPrimitiveType != Attribute.INTEGER_TYPE) {
                        throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the Attribute:" + tempSchema.getAttribute(_attributeIndex));
                    }
                    tempIntValue = ((IntegerAttribute) _attribute).getInt();
                }
                return tempIntValue;
            case Types.SMALLINT:
                if (tempAttrPrimitiveType != Attribute.INTEGER_TYPE) {
                    throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the Attribute:" + tempSchema.getAttribute(_attributeIndex));
                }
                short tempShortValue = (short) ((IntegerAttribute) _attribute).getInt();
                return tempShortValue;
            case Types.FLOAT:
            case Types.REAL:
            case Types.DOUBLE:
                if (tempAttrPrimitiveType == Attribute.FLOAT_TYPE) {
                    return ((FloatAttribute) _attribute).getFloat();
                }
                if (tempAttrPrimitiveType != Attribute.DOUBLE_TYPE) {
                    throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the Attribute:" + tempSchema.getAttribute(_attributeIndex));
                }

                return ((DoubleAttribute) _attribute).getDouble();
            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
                if (tempAttrPrimitiveType == Attribute.STRING_TYPE) {
                    return _attribute.toString();
                }
                if (tempAttrPrimitiveType != Attribute.INTEGER_TYPE && tempAttrPrimitiveType != Attribute.LONG_TYPE) {
                    throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the Attribute:" + tempSchema.getAttribute(_attributeIndex));
                }
                long tempTimeValue = 0L;
                if (_attribute instanceof TimeAttribute) {
                    tempTimeValue = ((TimeAttribute) _attribute).getMilliseconds();
                } else if (_attribute instanceof LongAttribute) {
                    tempTimeValue = ((LongAttribute) _attribute).getLong();
                } else if (_attribute instanceof IntegerAttribute) {
                    tempTimeValue = ((IntegerAttribute) _attribute).getLong() * 1000L;
                }
                if (_fieldType == Types.DATE) {
                    return new Date(tempTimeValue);
                }
                if (_fieldType == Types.TIME) {
                    return new Time(tempTimeValue);
                }
                if (_fieldType == Types.TIMESTAMP) {
                    return new Timestamp(tempTimeValue);
                }
            default:
                throw new RuntimeException("Cannot map SQL type [" + _fieldType + "] to the Attribute:" + tempSchema.getAttribute(_attributeIndex));
        }
    }

    /**
     * Get the default value for SQL field.
     *
     * @param _defaultValue
     * @param _fieldType
     * @return
     */
    public static Object getSqlObjectDefaultValue(String _defaultValue, int _fieldType) {
        if(StringUtils.isBlank(_defaultValue)) {
            return null;
        }
        switch (_fieldType) {
            case Types.BIT:
            case Types.BOOLEAN:
                if(_defaultValue.equals("true") || _defaultValue.equals("1")) {
                    return true;
                }
                if(_defaultValue.equals("false") || _defaultValue.equals("0")) {
                    return false;
                }
                return false;
            case Types.INTEGER:
            case Types.SMALLINT:
            case Types.TINYINT:
                try{
                    return Integer.parseInt(_defaultValue);
                } catch (NumberFormatException e) {
                    log.warn("The default value is invalid integer value, so dismiss it.");
                }
                return null;
            case Types.LONGVARBINARY:
            case Types.VARBINARY:
            case Types.BINARY:
                return _defaultValue.getBytes();
            case Types.LONGVARCHAR:
            case Types.CHAR:
            case Types.VARCHAR:
                return _defaultValue;
            case Types.NUMERIC:
            case Types.BIGINT:
            case Types.DECIMAL:
                try{
                    return Long.parseLong(_defaultValue);
                } catch (NumberFormatException e) {
                    log.warn("The default value is invalid long value, so dismiss it.");
                }
                return null;
            case Types.FLOAT:
            case Types.REAL:
            case Types.DOUBLE:
                try{
                    return Double.parseDouble(_defaultValue);
                } catch (NumberFormatException e) {
                    log.warn("The default value is invalid double value, so dismiss it.");
                }
                return null;
            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:

                try{
                    long tempValue = Long.parseLong(_defaultValue);
                    if (_fieldType == Types.DATE) {
                        return new Date(tempValue);
                    }
                    if (_fieldType == Types.TIME) {
                        return new Time(tempValue);
                    }
                    if (_fieldType == Types.TIMESTAMP) {
                        return new Timestamp(tempValue);
                    }
                } catch (NumberFormatException e) {
                    log.warn("The default value is invalid long value, so going to check time format.");
                    try {
                        if (_fieldType == Types.DATE) {
                            return Date.valueOf(_defaultValue);
                        }
                        if (_fieldType == Types.TIME) {
                            return Time.valueOf(_defaultValue);
                        }
                        if (_fieldType == Types.TIMESTAMP) {
                            return Timestamp.valueOf(_defaultValue);
                        }
                    } catch (IllegalArgumentException ex) {
                        log.warn("The default value is invalid data/time/timestamp value, so dismiss it.");
                    }
                }
            default:
                return _defaultValue;
        }
    }
}

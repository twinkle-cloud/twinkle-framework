package com.twinkle.framework.struct.utils;

import com.twinkle.framework.core.lang.util.Array;
import com.twinkle.framework.struct.asm.descriptor.SAAttributeDescriptor;
import com.twinkle.framework.struct.error.StructAttributeException;
import com.twinkle.framework.struct.ref.AttributeRef;
import com.twinkle.framework.struct.type.*;
import com.twinkle.framework.struct.util.StructAttributeArray;

import java.util.Iterator;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/10/19 9:43 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class StructAttributePrintFormatter {
    private static final String SYSTEM_LINE_SEPARATOR = System.getProperty("line.separator", "\n");
    private static final String DEFAULT_INDENT = "";

    public static StringBuilder append(StructAttributeArray _attrArray, StringBuilder _builder, String var2, String _separator) {
        _builder.append("[");
        int tempLength = _attrArray.length();
        String var5 = var2 + "  ";

        for(int i = 0; i < tempLength; ++i) {
            _builder.append(_separator);
            _builder.append(var5);
            if (_attrArray.get(i) != null) {
                append(_attrArray.get(i), _builder, var5, _separator);
            } else {
                _builder.append(var5);
                _builder.append("NULL");
            }

            if (i != tempLength - 1) {
                _builder.append(", ");
            }
        }

        _builder.append(_separator);
        _builder.append(var2);
        _builder.append("]");
        return _builder;
    }

    protected static StringBuilder append(StructAttribute _attr, AttributeRef _attrRef, StringBuilder _builder, String var3, String var4) {
        if (!_attr.isAttributeSet(_attrRef)) {
            return _builder;
        } else {
            AttributeType tempStructType = _attrRef.getType();
            _builder.append(var3);
            _builder.append(_attrRef.getName());
            _builder.append("  ");
            if (tempStructType.isPrimitiveType()) {
                switch(tempStructType.getID()) {
                    case PrimitiveType.BYTE_ID:
                        _builder.append(_attr.getByte(_attrRef));
                        break;
                    case PrimitiveType.SHORT_ID:
                        _builder.append(_attr.getShort(_attrRef));
                        break;
                    case PrimitiveType.INT_ID:
                        _builder.append(_attr.getInt(_attrRef));
                        break;
                    case PrimitiveType.LONG_ID:
                        _builder.append(_attr.getLong(_attrRef));
                        break;
                    case PrimitiveType.CHAR_ID:
                        _builder.append(_attr.getChar(_attrRef));
                        break;
                    case PrimitiveType.BOOLEAN_ID:
                        _builder.append(_attr.getBoolean(_attrRef));
                    case 7:
                    case 8:
                    default:
                        break;
                    case PrimitiveType.FLOAT_ID:
                        _builder.append(_attr.getFloat(_attrRef));
                        break;
                    case PrimitiveType.DOUBLE_ID:
                        _builder.append(_attr.getDouble(_attrRef));
                }
            } else if (tempStructType.isStringType()) {
                _builder.append("\"");
                _builder.append(_attr.getString(_attrRef));
                _builder.append("\"");
            } else if (tempStructType.isArrayType()) {
                Array tempArray = _attr.getArray(_attrRef);
                if (tempArray == null) {
                    _builder.append(tempArray);
                } else {
                    switch(tempStructType.getID()) {
                        case ArrayType.BYTE_ARRAY_ID:
                        case ArrayType.SHORT_ARRAY_ID:
                        case ArrayType.INT_ARRAY_ID:
                        case ArrayType.LONG_ARRAY_ID:
                        case ArrayType.CHAR_ARRAY_ID:
                        case ArrayType.BOOLEAN_ARRAY_ID:
                        case ArrayType.FLOAT_ARRAY_ID:
                        case ArrayType.DOUBLE_ARRAY_ID:
                        case ArrayType.STRING_ARRAY_ID:
                            _builder.append(tempArray.toString());
                            break;
                        default:
                            append((StructAttributeArray)tempArray, _builder, var3, var4);
                    }
                }
            } else {
                StructAttribute tempAttr = _attr.getStruct(_attrRef);
                if (tempAttr == null) {
                    _builder.append(tempAttr);
                } else {
                    append(tempAttr, _builder, var3, var4);
                }
            }

            _builder.append(var4);
            return _builder;
        }
    }

    public static StringBuilder append(StructAttribute _attr, StringBuilder _builder, String var2, String _separator) {
        StructType tempAttrType = _attr.getType();

        try {
            String var5 = var2 + "  ";
            _builder.append(tempAttrType.getQualifiedName());
            _builder.append(" {");
            _builder.append(_separator);
            Iterator<SAAttributeDescriptor> tempAttrItr = tempAttrType.getAttributes();

            while(tempAttrItr.hasNext()) {
                SAAttributeDescriptor tempDescriptor = tempAttrItr.next();
                append(_attr, _attr.getAttributeRef(tempDescriptor.getName()), _builder, var5, _separator);
            }

            _builder.append(var2);
            _builder.append("}");
        } catch (StructAttributeException var8) {
        }

        return _builder;
    }

    public static StringBuilder append(StructAttribute _attr, StringBuilder _builder) {
        return append(_attr, _builder, "", SYSTEM_LINE_SEPARATOR);
    }

    public static String toString(StructAttributeArray _attrArray, String var1, String _separator) {
        StringBuilder tempBuilder = new StringBuilder(1024);
        return append(_attrArray, tempBuilder, var1, _separator).toString();
    }

    public static String toString(StructAttribute _attr, String var1, String _separator) {
        StringBuilder tempBuilder = new StringBuilder(1024);
        return append(_attr, tempBuilder, var1, _separator).toString();
    }

    public static String toString(StructAttribute _attr) {
        StringBuilder tempBuilder = new StringBuilder(1024);
        return append(_attr, tempBuilder).toString();
    }
}

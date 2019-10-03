package com.twinkle.framework.struct.type;

import com.twinkle.framework.core.type.PrimitiveType;
import com.twinkle.framework.core.type.StringType;
import com.twinkle.framework.core.type.AttributeType;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/29/19 10:17 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public final class ArrayType implements AttributeType {
    private static final int ARRAY_MASK = 16777216;
    public static final int BYTE_ARRAY_ID = 16777217;
    public static final ArrayType BYTE_ARRAY;
    public static final int SHORT_ARRAY_ID = 16777218;
    public static final ArrayType SHORT_ARRAY;
    public static final int INT_ARRAY_ID = 16777219;
    public static final ArrayType INT_ARRAY;
    public static final int LONG_ARRAY_ID = 16777220;
    public static final ArrayType LONG_ARRAY;
    public static final int CHAR_ARRAY_ID = 16777221;
    public static final ArrayType CHAR_ARRAY;
    public static final int BOOLEAN_ARRAY_ID = 16777222;
    public static final ArrayType BOOLEAN_ARRAY;
    public static final int FLOAT_ARRAY_ID = 16777225;
    public static final ArrayType FLOAT_ARRAY;
    public static final int DOUBLE_ARRAY_ID = 16777226;
    public static final ArrayType DOUBLE_ARRAY;
    public static final int STRING_ARRAY_ID = 50331648;
    public static final ArrayType STRING_ARRAY;
    public static final int STRUCT_ARRAY_ID = 83886080;
    private int typeID = 0;
    private String name = null;
    private AttributeType elementType = null;

    static {
        BYTE_ARRAY = new ArrayType("byte[]", PrimitiveType.BYTE);
        SHORT_ARRAY = new ArrayType("short[]", PrimitiveType.SHORT);
        INT_ARRAY = new ArrayType("int[]", PrimitiveType.INT);
        LONG_ARRAY = new ArrayType("long[]", PrimitiveType.LONG);
        CHAR_ARRAY = new ArrayType("char[]", PrimitiveType.CHAR);
        BOOLEAN_ARRAY = new ArrayType("boolean[]", PrimitiveType.BOOLEAN);
        FLOAT_ARRAY = new ArrayType("float[]", PrimitiveType.FLOAT);
        DOUBLE_ARRAY = new ArrayType("double[]", PrimitiveType.DOUBLE);
        STRING_ARRAY = new ArrayType("string[]", StringType.STRING);
    }

    private ArrayType(String _name, AttributeType _type) {
        this.name = _name;
        this.elementType = _type;
        this.typeID = ARRAY_MASK | _type.getID();
    }
    @Override
    public boolean isPrimitiveType() {
        return false;
    }
    @Override
    public boolean isArrayType() {
        return true;
    }
    @Override
    public boolean isStructType() {
        return false;
    }
    @Override
    public boolean isStringType() {
        return false;
    }
    @Override
    public int getID() {
        return this.typeID;
    }
    @Override
    public String getName() {
        return this.name;
    }

    public AttributeType getElementType() {
        return this.elementType;
    }
    @Override
    public String toString() {
        return this.name;
    }

    public static ArrayType getStructAttributeTypeArray(String _name, StructType _structType) {
        return new ArrayType(_name, _structType);
    }

    @Override
    public int hashCode() {
        byte tempByte = 1;
        int tempCode = 31 * tempByte + (this.elementType == null ? 0 : this.elementType.hashCode());
        tempCode = 31 * tempCode + (this.name == null ? 0 : this.name.hashCode());
        return tempCode;
    }

    @Override
    public boolean equals(Object _obj) {
        if (this == _obj) {
            return true;
        } else if (_obj == null) {
            return false;
        } else if (this.getClass() != _obj.getClass()) {
            return false;
        } else {
            ArrayType tempType = (ArrayType) _obj;
            if (this.elementType == null) {
                if (tempType.elementType != null) {
                    return false;
                }
            } else if (!this.elementType.equals(tempType.elementType)) {
                return false;
            }

            if (this.name == null) {
                if (tempType.name != null) {
                    return false;
                }
            } else if (!this.name.equals(tempType.name)) {
                return false;
            }
            return true;
        }
    }
}

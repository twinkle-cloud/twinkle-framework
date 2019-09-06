package com.twinkle.framework.struct.type;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/29/19 10:12 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public final class PrimitiveType implements StructType {
    private int typeID = 0;
    private String name;
    public static final int BYTE_ID = 1;
    public static final PrimitiveType BYTE = new PrimitiveType("byte", BYTE_ID);
    public static final int SHORT_ID = 2;
    public static final PrimitiveType SHORT = new PrimitiveType("short", SHORT_ID);
    public static final int INT_ID = 3;
    public static final PrimitiveType INT = new PrimitiveType("int", INT_ID);
    public static final int LONG_ID = 4;
    public static final PrimitiveType LONG = new PrimitiveType("long", LONG_ID);
    public static final int CHAR_ID = 5;
    public static final PrimitiveType CHAR = new PrimitiveType("char", CHAR_ID);
    public static final int BOOLEAN_ID = 6;
    public static final PrimitiveType BOOLEAN = new PrimitiveType("boolean", BOOLEAN_ID);
    public static final int FLOAT_ID = 9;
    public static final PrimitiveType FLOAT = new PrimitiveType("float", FLOAT_ID);
    public static final int DOUBLE_ID = 10;
    public static final PrimitiveType DOUBLE = new PrimitiveType("double", DOUBLE_ID);

    private PrimitiveType(String _name, int _typeId) {
        this.name = _name;
        this.typeID = _typeId;
    }

    @Override
    public String getName() {
        return this.name;
    }
    @Override
    public int getID() {
        return this.typeID;
    }
    @Override
    public boolean isPrimitiveType() {
        return true;
    }
    @Override
    public boolean isArrayType() {
        return false;
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
    public String toString() {
        return this.name;
    }
}

package com.twinkle.framework.struct.type;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/29/19 10:09 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public final class StringType implements StructType {
    public static final int STRING_ID = 33554432;
    public static final StringType STRING = new StringType();
    private static final String STRING_NAME = "string";

    private StringType() {
    }
    @Override
    public boolean isPrimitiveType() {
        return false;
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
        return true;
    }
    @Override
    public int getID() {
        return STRING_ID;
    }
    @Override
    public String getName() {
        return STRING_NAME;
    }
    @Override
    public String toString() {
        return STRING_NAME;
    }
}

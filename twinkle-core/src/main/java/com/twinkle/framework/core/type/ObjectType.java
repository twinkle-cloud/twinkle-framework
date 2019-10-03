package com.twinkle.framework.core.type;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/3/19 11:09 AM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public final class ObjectType implements AttributeType {
    public static final int OBJECT_ID = 0;
    private static final String OBJECT_NAME = "object";
    public static final ObjectType OBJECT = new ObjectType();

    private ObjectType(){
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
        return false;
    }

    @Override
    public int getID() {
        return OBJECT_ID;
    }

    @Override
    public String getName() {
        return OBJECT_NAME;
    }
}

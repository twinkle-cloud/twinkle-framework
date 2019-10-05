package com.twinkle.framework.struct.type;

import lombok.Getter;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/31/19 2:21 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Getter
public final class AliasStructType implements AttributeType {
    /**
     * Alias name.
     */
    private String name;
    /**
     * Struct type.
     */
    private AttributeType sourceType;

    public AliasStructType(String _aliasName, AttributeType _sourceType) {
        int tempTypeId = _sourceType.getID();
        if (tempTypeId != StructType.STRUCT_ID && tempTypeId != ArrayType.STRUCT_ARRAY_ID) {
            this.name = _aliasName;
            this.sourceType = _sourceType;
        } else {
            throw new IllegalArgumentException("Can not define alias " + _aliasName + " for StructAttributeType");
        }
    }
    @Override
    public boolean equals(Object _obj) {
        if (_obj != null && _obj instanceof AliasStructType) {
            AliasStructType tempType = (AliasStructType) _obj;
            return this.name.equals(tempType.getName()) && this.sourceType.equals(tempType.getSourceType());
        } else {
            return false;
        }
    }
    @Override
    public boolean isPrimitiveType() {
        return this.sourceType.isPrimitiveType();
    }
    @Override
    public boolean isArrayType() {
        return this.sourceType.isArrayType();
    }
    @Override
    public boolean isStructType() {
        return this.sourceType.isStructType();
    }
    @Override
    public boolean isStringType() {
        return this.sourceType.isStringType();
    }
    @Override
    public int getID() {
        return this.sourceType.getID();
    }
}

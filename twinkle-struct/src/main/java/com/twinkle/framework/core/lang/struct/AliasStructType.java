package com.twinkle.framework.core.lang.struct;

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
public final class AliasStructType implements StructType {
    /**
     * Alias name.
     */
    private String name;
    /**
     * Struct type.
     */
    private StructType sourceType;

    public AliasStructType(String _aliasName, StructType _sourceType) {
        int tempTypeId = _sourceType.getID();
        if (tempTypeId != StructAttributeType.STRUCT_ID && tempTypeId != ArrayType.STRUCT_ARRAY_ID) {
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

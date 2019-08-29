package com.twinkle.framework.core.error;

import lombok.Getter;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/29/19 10:32 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Getter
public final class TypeNotFoundException extends StructAttributeException {
    private String typeName = null;

    public TypeNotFoundException(String _typeName) {
        super(_typeName + " type is not found in the TypeManager.");
        this.typeName = _typeName;
    }

    public TypeNotFoundException(String _typeName, Throwable _exception) {
        super(_typeName + " type is not found in the TypeManager.", _exception);
        this.typeName = _typeName;
    }
}

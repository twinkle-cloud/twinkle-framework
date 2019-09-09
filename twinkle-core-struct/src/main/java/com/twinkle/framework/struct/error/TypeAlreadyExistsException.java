package com.twinkle.framework.struct.error;

import lombok.Getter;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/29/19 10:34 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Getter
public final class TypeAlreadyExistsException extends StructAttributeException {
    private String typeName = null;

    public TypeAlreadyExistsException(String _typeName) {
        super(_typeName + " type already exists in the TypeManager.");
        this.typeName = _typeName;
    }

    public TypeAlreadyExistsException(String _typeName, Throwable _exception) {
        super(_typeName + " type already exists in the TypeManager.", _exception);
        this.typeName = _typeName;
    }
}

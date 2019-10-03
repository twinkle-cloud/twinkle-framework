package com.twinkle.framework.struct.error;

import com.twinkle.framework.struct.type.StructType;
import lombok.Getter;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/29/19 11:00 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Getter
public final class StructAttributeTypeAlreadyExistsException extends StructAttributeException {
    private String namespace = null;
    private String structAttributeTypeName = null;

    public StructAttributeTypeAlreadyExistsException(String _namespace, StructType _structAttrType) {
        super(_structAttrType + " type already exists in " + _namespace);
        this.namespace = _namespace;
        this.structAttributeTypeName = _structAttrType != null ? _structAttrType.getName() : null;
    }

    public StructAttributeTypeAlreadyExistsException(String _namespace, StructType _structAttrType, Throwable _exception) {
        super(_structAttrType + " type already exists in " + _namespace, _exception);
        this.namespace = _namespace;
        this.structAttributeTypeName = _structAttrType != null ? _structAttrType.getName() : null;
    }

    public StructAttributeTypeAlreadyExistsException(String _namespace, String _structAttrTypeName) {
        super(_structAttrTypeName + " type already exists in " + _namespace);
        this.namespace = _namespace;
        this.structAttributeTypeName = _structAttrTypeName;
    }

    public StructAttributeTypeAlreadyExistsException(String _namespace, String _structAttrTypeName, Throwable _exception) {
        super(_structAttrTypeName + " type already exists in " + _namespace, _exception);
        this.namespace = _namespace;
        this.structAttributeTypeName = _structAttrTypeName;
    }
}

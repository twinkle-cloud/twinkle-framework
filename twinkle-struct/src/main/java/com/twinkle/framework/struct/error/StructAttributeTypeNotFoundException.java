package com.twinkle.framework.struct.error;

import lombok.Getter;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/29/19 11:07 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Getter
public class StructAttributeTypeNotFoundException extends StructAttributeException {
    private String namespace = null;
    private String structAttributeTypeName = null;

    public StructAttributeTypeNotFoundException(String _namespace, String _structAttrTypeName) {
        super(_namespace + ":" + _structAttrTypeName + " type not found.");
        this.namespace = _namespace;
        this.structAttributeTypeName = _structAttrTypeName;
    }

    public StructAttributeTypeNotFoundException(String _namespace, String _structAttrTypeName, Throwable _exception) {
        super(_namespace + ":" + _structAttrTypeName + " type not found.", _exception);
        this.namespace = _namespace;
        this.structAttributeTypeName = _structAttrTypeName;
    }
}

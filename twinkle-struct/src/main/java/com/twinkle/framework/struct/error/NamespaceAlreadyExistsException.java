package com.twinkle.framework.struct.error;

import lombok.Getter;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/29/19 10:55 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Getter
public final class NamespaceAlreadyExistsException extends StructAttributeException {
    private String namespace = null;

    public NamespaceAlreadyExistsException(String _namespace) {
        super(_namespace + " namespace already exists.");
        this.namespace = _namespace;
    }

    public NamespaceAlreadyExistsException(String _namespace, Throwable _exception) {
        super(_namespace + " namespace already exists.", _exception);
        this.namespace = _namespace;
    }
}

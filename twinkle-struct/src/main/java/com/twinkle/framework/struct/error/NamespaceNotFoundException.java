package com.twinkle.framework.struct.error;

import lombok.Getter;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/29/19 10:57 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Getter
public final class NamespaceNotFoundException extends StructAttributeException {
    private String namespace = null;

    public NamespaceNotFoundException(String _namespace) {
        super(_namespace + " namespace not found.");
        this.namespace = _namespace;
    }

    public NamespaceNotFoundException(String _namespace, Throwable _exception) {
        super(_namespace + " namespace not found.", _exception);
        this.namespace = _namespace;
    }
}

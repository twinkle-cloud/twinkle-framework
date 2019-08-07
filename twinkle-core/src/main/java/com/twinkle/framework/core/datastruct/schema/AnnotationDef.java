package com.twinkle.framework.core.datastruct.schema;

import org.objectweb.asm.Type;

import java.util.List;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-31 22:55<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface AnnotationDef {
    AnnotationDef.Kind getKind();

    Type getType();

    List<AnnotationElementDef> getElements();

    public static enum Kind {
        CLASS,
        FIELD,
        GETTER,
        SETTER;

        private Kind() {
        }
    }
}

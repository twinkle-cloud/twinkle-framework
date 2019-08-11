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
    /**
     * Annotation Type.
     *
     * @return
     */
    AnnotationDef.Kind getKind();

    /**
     * Annotation's ASM Type.
     *
     * @return
     */
    Type getType();

    /**
     * Annotation's element's Define.
     *
     * @return
     */
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

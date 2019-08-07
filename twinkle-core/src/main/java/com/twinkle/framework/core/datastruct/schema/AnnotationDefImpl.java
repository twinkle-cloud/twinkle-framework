package com.twinkle.framework.core.datastruct.schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.objectweb.asm.Type;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-31 23:07<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Data
@AllArgsConstructor
public class AnnotationDefImpl implements AnnotationDef, Cloneable {
    private Type type;
    private Kind kind;
    private List<AnnotationElementDef> elements;
    public AnnotationDefImpl(Class<? extends Annotation> _annotationClass, List<AnnotationElementDef> _elements, Kind _kind) {
        this.type = Type.getType(_annotationClass);
        this.elements = _elements;
        this.kind = _kind;
    }

    @Override
    public boolean equals(Object _obj) {
        if (this == _obj) {
            return true;
        } else if (_obj != null && this.getClass() == _obj.getClass()) {
            AnnotationDefImpl tempAnnotationDef = (AnnotationDefImpl)_obj;
            if (!this.type.equals(tempAnnotationDef.type)) {
                return false;
            } else if (!this.elements.equals(tempAnnotationDef.elements)) {
                return false;
            } else {
                return this.kind == tempAnnotationDef.kind;
            }
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int tempHashCode = this.type.hashCode();
        // Usually multiply 31 for hashcode.
        tempHashCode = 31 * tempHashCode + this.elements.hashCode();
        tempHashCode = 31 * tempHashCode + (this.kind != null ? this.kind.hashCode() : 0);
        return tempHashCode;
    }

    @Override
    public String toString() {
        StringBuilder tempBuilder = (new StringBuilder()).append("@").append(this.type.getClassName()).append("(");
        boolean isFirst = true;

        AnnotationElementDef tempElement;
        for(Iterator tempItr = this.elements.iterator(); tempItr.hasNext(); tempBuilder.append(tempElement)) {
            tempElement = (AnnotationElementDef)tempItr.next();
            if (!isFirst) {
                tempBuilder.append(", ");
            } else {
                isFirst = false;
            }
        }

        tempBuilder.append(")");
        return tempBuilder.toString();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        AnnotationDefImpl tempAnnotationDef = (AnnotationDefImpl)super.clone();
        tempAnnotationDef.elements = new ArrayList(this.elements);
        return tempAnnotationDef;
    }
}

package com.twinkle.framework.asm.define;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.objectweb.asm.Type;

import java.lang.reflect.Array;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-01 10:09<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Data
@AllArgsConstructor
public class AnnotationElementDefImpl implements AnnotationElementDef, Cloneable {
    private static final Type STRING_TYPE = Type.getType(String.class);
    /**
     * Annotation's ASM Type.
     */
    private Type type;
    /**
     * Annotation's Element Name.
     *
     */
    private String name;
    /**
     * Element's value.
     */
    private Object value;

    @Override
    public boolean isAnnotation() {
        return this.type.getSort() == Type.OBJECT && this.value instanceof AnnotationDef;
    }

    @Override
    public boolean isArray() {
        return this.type.getSort() == Type.ARRAY;
    }

    @Override
    public boolean equals(Object _obj) {
        if (this == _obj) {
            return true;
        } else if (_obj != null && this.getClass() == _obj.getClass()) {
            AnnotationElementDefImpl tempAnnotationElementDef = (AnnotationElementDefImpl) _obj;
            if (!this.type.equals(tempAnnotationElementDef.type)) {
                return false;
            } else if (!this.name.equals(tempAnnotationElementDef.name)) {
                return false;
            } else {
                if (this.value != null) {
                    if (!this.value.equals(tempAnnotationElementDef.value)) {
                        return false;
                    }
                } else if (tempAnnotationElementDef.value != null) {
                    return false;
                }

                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int tempHashCode = this.type.hashCode();
        tempHashCode = 31 * tempHashCode + this.name.hashCode();
        tempHashCode = 31 * tempHashCode + (this.value != null ? this.value.hashCode() : 0);
        return tempHashCode;
    }

    @Override
    public String toString() {
        StringBuilder tempBuilder = (new StringBuilder()).append(this.name).append(" = ");
        if (this.isArray()) {
            tempBuilder.append("{");
            Type tempType = this.type.getElementType();
            int tempLength = Array.getLength(this.value);
            boolean isFirst = true;

            for (int i = 0; i < tempLength; i++) {
                if (!isFirst) {
                    tempBuilder.append(", ");
                } else {
                    isFirst = false;
                }

                if (STRING_TYPE.equals(tempType)) {
                    tempBuilder.append("\"").append(Array.get(this.value, i)).append("\"");
                } else {
                    tempBuilder.append(Array.get(this.value, i));
                }
            }

            tempBuilder.append("}");
        } else if (STRING_TYPE.equals(this.type)) {
            tempBuilder.append("\"").append(this.value).append("\"");
        } else {
            tempBuilder.append(this.value);
        }

        return tempBuilder.toString();
    }
}

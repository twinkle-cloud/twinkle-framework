package com.twinkle.framework.asm.define;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.objectweb.asm.Type;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-01 11:12<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Getter
@AllArgsConstructor
public abstract class AbstractTypeDefImpl implements TypeDef, Cloneable {
    /**
     * Type's Name.
     */
    private final String name;
    /**
     * Type's ASM Type.
     */
    private final Type type;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
    @Override
    public boolean equals(Object _obj) {
        return _obj == this || _obj instanceof AbstractTypeDefImpl && this.name.equals(((AbstractTypeDefImpl)_obj).getName());
    }
    @Override
    public String toString() {
        return this.name;
    }
}

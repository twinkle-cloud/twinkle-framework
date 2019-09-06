package com.twinkle.framework.asm.define;

import com.twinkle.framework.asm.define.AbstractTypeDefImpl;
import com.twinkle.framework.asm.define.ArrayTypeDef;
import com.twinkle.framework.asm.define.TypeDef;
import lombok.Getter;
import org.objectweb.asm.Type;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-01 11:11<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Getter
public abstract class AbstractArrayTypeDefImpl extends AbstractTypeDefImpl implements ArrayTypeDef, Cloneable {
    private final TypeDef elementType;

    public AbstractArrayTypeDefImpl(String _name, Type _type, TypeDef _elementTypeDefine) {
        super(_name, _type);
        this.elementType = _elementTypeDefine;
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }

    @Override
    public boolean isBean() {
        return false;
    }

    @Override
    public boolean isGeneric() {
        return false;
    }

    @Override
    public boolean isEnum() {
        return false;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Get the Array ASM Type.
     *
     * @param _type
     * @return
     */
    public static Type getArrayType(Type _type) {
        return Type.getType("[" + _type.getDescriptor());
    }

}

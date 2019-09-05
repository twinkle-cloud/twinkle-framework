package com.twinkle.framework.core.datastruct.define;

import com.twinkle.framework.core.datastruct.Bean;
import lombok.Getter;
import org.objectweb.asm.Type;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-01 22:49<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Getter
public class ClassTypeDefImpl extends AbstractTypeDefImpl implements ClassTypeDef, Cloneable {
    private final Class typeClass;

    /**
     * Initialize the Class Type Define.
     *
     * @param _name
     * @param _typeClass
     */
    public ClassTypeDefImpl(String _name, Class _typeClass) {
        super(_name, Type.getType(_typeClass));
        this.typeClass = _typeClass;
//        this.typeClass.isArray();
//        this.typeClass.isPrimitive();
        Bean.class.isAssignableFrom(this.typeClass);
    }

    @Override
    public boolean isPrimitive() {
        return this.typeClass.isPrimitive();
    }
    @Override
    public boolean isBean() {
        return false;
    }
    @Override
    public boolean isArray() {
        return this.typeClass.isArray();
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
}

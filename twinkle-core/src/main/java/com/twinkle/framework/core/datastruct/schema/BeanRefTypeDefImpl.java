package com.twinkle.framework.core.datastruct.schema;

import org.objectweb.asm.Type;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-01 22:02<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class BeanRefTypeDefImpl extends AbstractTypeDefImpl implements BeanRefTypeDef, Cloneable {
    public BeanRefTypeDefImpl(String _name, Type _type) {
        super(_name, _type);
    }
    @Override
    public boolean isPrimitive() {
        return false;
    }
    @Override
    public boolean isBean() {
        return true;
    }
    @Override
    public boolean isArray() {
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
}

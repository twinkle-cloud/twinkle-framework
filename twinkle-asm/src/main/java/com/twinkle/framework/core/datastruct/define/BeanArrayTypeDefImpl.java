package com.twinkle.framework.core.datastruct.define;

import org.objectweb.asm.Type;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-01 21:59<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class BeanArrayTypeDefImpl extends AbstractArrayTypeDefImpl implements BeanArrayTypeDef, Cloneable {
    public BeanArrayTypeDefImpl(String _name, Type _type, BeanRefTypeDef _typeDef) {
        super(_name, _type, _typeDef);
    }
    @Override
    public BeanRefTypeDef getBeanElementType() {
        return (BeanTypeDef)this.getElementType();
    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

package com.twinkle.framework.core.datastruct.define;

import org.objectweb.asm.Type;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-01 22:47<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class ClassArrayTypeDefImpl extends AbstractArrayTypeDefImpl implements ArrayTypeDef, Cloneable {
    public ClassArrayTypeDefImpl(String _name, Class _typeClass) {
        super(_name, Type.getType(_typeClass), new ClassTypeDefImpl(_typeClass.getComponentType().getName(), _typeClass.getComponentType()));
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

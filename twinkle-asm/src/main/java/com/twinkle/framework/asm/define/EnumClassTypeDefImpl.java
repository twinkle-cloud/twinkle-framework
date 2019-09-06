package com.twinkle.framework.asm.define;

import lombok.Getter;
import org.objectweb.asm.Type;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-02 20:19<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Getter
public class EnumClassTypeDefImpl extends ClassTypeDefImpl implements EnumTypeDef, Cloneable {
    private final List<Object> enumConstraints;

    public EnumClassTypeDefImpl(String _name, Class _typeClass, Collection<Object> _values) {
        super(_name, _typeClass);
        if (!_typeClass.isPrimitive() && !String.class.equals(_typeClass)) {
            throw new IllegalArgumentException("Enum value type not supported: " + _typeClass.getName());
        } else {
            this.enumConstraints = EnumTypeDefImpl.initConstraints(_typeClass, _values);
        }
    }
    @Override
    public boolean isEnum() {
        return true;
    }

    @Override
    public Type getValueType() {
        return this.getType();
    }

    @Override
    public Class getValueClass() {
        return this.getTypeClass();
    }

    @Override
    public List<String> getEnumNames() {
        return Collections.emptyList();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

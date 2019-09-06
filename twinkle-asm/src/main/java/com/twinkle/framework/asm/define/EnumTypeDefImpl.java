package com.twinkle.framework.asm.define;

import com.twinkle.framework.asm.builder.TypeDefBuilder;
import com.twinkle.framework.asm.descriptor.EnumTypeDescriptor;
import com.twinkle.framework.asm.utils.ListParser;
import lombok.Getter;
import org.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-01 22:11<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Getter
public class EnumTypeDefImpl extends AbstractTypeDefImpl implements EnumTypeDef, Cloneable {
    private EnumTypeDescriptor descriptor;
    private Type valueType;
    private Class valueClass;
    private final List<Object> enumConstraints;
    private final List<String> enumNames;

    public EnumTypeDefImpl(EnumTypeDescriptor _descriptor, ClassLoader _valueClassLoader) throws ClassNotFoundException {
        super(_descriptor.getName(), TypeDefBuilder.getObjectType(_descriptor.getClassName()));
        this.descriptor = _descriptor;
        this.valueClass = _valueClassLoader.loadClass(_descriptor.getValueClassName());
        this.valueType = Type.getType(this.valueClass);
        if (!this.valueClass.isPrimitive() && !String.class.equals(this.valueClass)) {
            throw new IllegalArgumentException("Enum value type not supported: " + this.valueClass.getName());
        } else {
            this.enumConstraints = initConstraints(this.valueClass, _descriptor.getEnumerationValues().values());
            this.enumNames = new ArrayList(_descriptor.getEnumerationValues().keySet());
        }
    }

    public EnumTypeDefImpl(EnumTypeDescriptor _descriptor, Class _valueClass) throws ClassNotFoundException {
        super(_descriptor.getName(), TypeDefBuilder.getObjectType(_descriptor.getClassName()));
        this.descriptor = _descriptor;
        this.valueClass = _valueClass;
        this.valueType = Type.getType(this.valueClass);
        if (!this.valueClass.isPrimitive() && !String.class.equals(this.valueClass)) {
            throw new IllegalArgumentException("Enum value type not supported: " + this.valueClass.getName());
        } else {
            this.enumConstraints = initConstraints(_valueClass, _descriptor.getEnumerationValues().values());
            this.enumNames = new ArrayList(_descriptor.getEnumerationValues().keySet());
        }
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
    public boolean isArray() {
        return false;
    }
    @Override
    public boolean isGeneric() {
        return false;
    }
    @Override
    public boolean isEnum() {
        return true;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Init constraints for this Enum Type Define.
     *
     * @param _valueClass
     * @param _values
     * @return
     */
    protected static List<Object> initConstraints(Class _valueClass, Collection<Object> _values) {
        Type tempValueType = Type.getType(_valueClass);
        List tempResultList = _values.stream().map(item -> {
            if (item instanceof String) {
                return initEnumeratedValue(tempValueType, _valueClass, (String)item);
            } else {
                return initEnumeratedValue(tempValueType, _valueClass, item);
            }
        }).collect(Collectors.toList());

        return tempResultList;
    }

    protected static Object initEnumeratedValue(Type _type, Class _valueClass, Object _value) {
        switch(_type.getSort()) {
            case Type.BOOLEAN:
                return (Boolean)_value ? 1 : 0;
            case Type.CHAR:
                return Integer.valueOf((Character)_value);
            case Type.BYTE:
                return ((Byte)_value).intValue();
            case Type.SHORT:
                return ((Short)_value).intValue();
            case Type.INT:
                return (Integer)_value;
            case Type.FLOAT:
                return (Float)_value;
            case Type.LONG:
                return (Long)_value;
            case Type.DOUBLE:
                return (Double)_value;
            case Type.ARRAY:
            default:
                throw new IllegalArgumentException("Data type " + _valueClass.getName() + " not supported as enumerated type");
            case Type.OBJECT:
                if (Type.getType(String.class).equals(_type)) {
                    return _value.toString();
                } else if (_valueClass.isAssignableFrom(_value.getClass())) {
                    return _value;
                } else {
                    throw new IllegalArgumentException("Cannot convert value '" + _value + "' of type " + _value.getClass().getName() + " to type " + _valueClass.getName());
                }
        }
    }

    protected static Object initEnumeratedValue(Type _type, Class _valueClass, String _value) {
        switch(_type.getSort()) {
            case Type.BOOLEAN:
                return Boolean.parseBoolean(ListParser.stripQuotes(_value)) ? 1 : 0;
            case Type.CHAR:
                return Integer.valueOf(ListParser.stripQuotes(_value).charAt(0));
            case Type.BYTE:
                return Integer.valueOf(Byte.decode(ListParser.stripQuotes(_value)));
            case Type.SHORT:
                return Short.decode(ListParser.stripQuotes(_value)).intValue();
            case Type.INT:
                return Integer.decode(ListParser.stripQuotes(_value));
            case Type.FLOAT:
                return Float.valueOf(ListParser.stripQuotes(_value));
            case Type.LONG:
                return Long.decode(ListParser.stripQuotes(_value));
            case Type.DOUBLE:
                return Double.valueOf(ListParser.stripQuotes(_value));
            case Type.ARRAY:
            default:
                throw new IllegalArgumentException("Data type " + _valueClass.getName() + " not supported as enumerated type");
            case Type.OBJECT:
                if (Type.getType(String.class).equals(_type)) {
                    return _value;
                } else {
                    throw new IllegalArgumentException("Cannot convert value '" + _value + "' of type " + _value.getClass().getName() + " to type " + _valueClass.getName());
                }
        }
    }
}

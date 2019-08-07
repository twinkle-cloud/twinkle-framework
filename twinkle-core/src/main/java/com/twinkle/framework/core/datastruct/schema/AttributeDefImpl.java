package com.twinkle.framework.core.datastruct.schema;

import com.twinkle.framework.core.datastruct.Blob;
import com.twinkle.framework.core.datastruct.codec.BinEncoding;
import com.twinkle.framework.core.datastruct.descriptor.AttributeDescriptor;
import com.twinkle.framework.core.utils.AttributeUtil;
import com.twinkle.framework.core.utils.ListParser;
import lombok.Getter;
import lombok.Setter;
import org.objectweb.asm.Type;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-01 11:20<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Getter
public class AttributeDefImpl implements AttributeDef, Cloneable{
    public static final Set<String> RESERVED_ATTRIBUTE_NAMES = new HashSet(Arrays.asList("class", "Class", "type", "Type"));
    private String name;
    private final TypeDef type;
    private List<AnnotationDef> annotations;
    private Object defaultValue;
    private final boolean required;
    private final boolean readOnly;
    private final AttributeDescriptor descriptor;
    @Setter
    private transient String getterName;
    @Setter
    private transient String setterName;
    private transient String constantName;
    private transient String fieldName;

    public AttributeDefImpl(String _name, TypeDef _type, boolean _isReadOnly, boolean _isRequired, Object _value) {
        this.setName(_name);
        if (_type == null) {
            throw new NullPointerException("type");
        } else {
            this.type = _type;
            this.readOnly = _isReadOnly;
            this.required = _isRequired;
            this.defaultValue = this.initDefaultValue(_name, _type.getType(), _value);
            this.descriptor = null;
            this.annotations = Collections.emptyList();
        }
    }

    public AttributeDefImpl(String _name, TypeDef _typeDef) {
        this(_name, _typeDef, false, false, (Object)null);
    }

    public AttributeDefImpl(AttributeDescriptor _attrDesp, TypeDef _typeDef, List<AnnotationDef> _annotationDefList) {
        this.setName(_attrDesp.getName());
        if (_typeDef == null) {
            throw new NullPointerException("type");
        } else {
            this.type = _typeDef;
            this.readOnly = _attrDesp.isReadOnly();
            this.required = _attrDesp.isRequired();
            this.defaultValue = this.initDefaultValue(_attrDesp.getName(), _typeDef.getType(), _attrDesp.getDefaultValue());
            this.descriptor = _attrDesp;
            if (_annotationDefList == null) {
                throw new NullPointerException("annotations");
            } else {
                this.annotations = _annotationDefList;
            }
        }
    }

    public AttributeDefImpl(AttributeDef _attrDef) {
        this.setName(_attrDef.getName());
        this.type = _attrDef.getType();
        this.readOnly = _attrDef.isReadOnly();
        this.required = _attrDef.isRequired();
        this.defaultValue = _attrDef.getDefaultValue();
        if (_attrDef instanceof AttributeDefImpl) {
            this.descriptor = ((AttributeDefImpl)_attrDef).getDescriptor();
        } else {
            this.descriptor = null;
        }

        this.annotations = new ArrayList(_attrDef.getAnnotations());
    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        AttributeDefImpl newObj = (AttributeDefImpl)super.clone();
        newObj.setName(this.name);
        newObj.annotations = new ArrayList(this.annotations);
        return newObj;
    }

    @Override
    public String toString() {
        return this.name;
    }
    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
    @Override
    public boolean equals(Object _obj) {
        return _obj == this || _obj instanceof AttributeDefImpl && this.name.equals(((AttributeDefImpl)_obj).getName());
    }

    protected void setName(String _name) {
        this.name = _name;
        this.getterName = AttributeUtil.getGetterName(_name);
        this.setterName = AttributeUtil.getSetterName(_name);
        this.constantName = AttributeUtil.getConstantName(_name);
        this.fieldName = AttributeUtil.getFieldName(_name);
    }

    @Override
    public boolean isConstant() {
        return this.isReadOnly() && this.getDefaultValue() != null;
    }

    protected Object initDefaultValue(String _attrName, Type _type, Object _value) {
        if (_value == null) {
            return null;
        } else if (_value instanceof String) {
            return this.initDefaultValue(_attrName, _type, (String)_value);
        } else {
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
                    return this.initDefaultArrayValue(_attrName, _type, _value);
                case Type.OBJECT:
                    if (Type.getType(String.class).equals(_type)) {
                        return ListParser.stripQuotes(_value.toString());
                    } else {
                        if (Type.getType(Blob.class).equals(_type) && _value instanceof byte[]) {
                            return (new Blob((byte[])((byte[])_value))).encode(BinEncoding.Hex, true);
                        }

                        throw new IllegalArgumentException("Data type " + _type + " is not supported for default value of attribute [" + _attrName + "]");
                    }
                default:
                    throw new IllegalArgumentException("Data type " + _type + " is not supported for default value of attribute [" + _attrName + "]");
            }
        }
    }

    protected Object initDefaultArrayValue(String _attrName, Type _arrayType, Object _value) {
        if (_value == null) {
            return null;
        } else if (_value instanceof String) {
            return this.initDefaultArrayValue(_attrName, _arrayType, (String)_value);
        } else {
            Type tempType = _arrayType.getElementType();
            if (tempType.getSort() == Type.ARRAY) {
                throw new IllegalArgumentException("Nested array type " + _arrayType + " is not supported for default value of attribute [" + _attrName + "]");
            } else if (!_value.getClass().isArray()) {
                throw new IllegalArgumentException("Default value type " + _value.getClass() + " is not an array, as expected for default value of attribute [" + _attrName + "]");
            } else {
                int tempLength = Array.getLength(_value);
                List tempValueList = new ArrayList(tempLength);

                for(int i = 0; i < tempLength; ++i) {
                    Object tempObjValue = Array.get(_value, i);
                    tempValueList.add(this.initDefaultValue(_attrName, tempType, tempObjValue));
                }

                return tempValueList.toArray();
            }
        }
    }

    protected Object initDefaultValue(String _attrName, Type _type, String _value) {
        if (_value == null) {
            return null;
        } else {
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
                    Type tempElementType = _type.getElementType();
                    if (tempElementType.getSort() == Type.BYTE && BinEncoding.which(_value, null) != null) {
                        return this.initDefaultArrayValue(_attrName, _type, (Object)(new Blob(_value)).toByteArray());
                    }

                    return this.initDefaultArrayValue(_attrName, _type, _value);
                case Type.OBJECT:
                    if (Type.getType(String.class).equals(_type)) {
                        return ListParser.stripQuotes(_value);
                    } else {
                        if (Type.getType(Blob.class).equals(_type)) {
                            String tempObjValue = ListParser.stripQuotes(_value);
                            BinEncoding tempEncoding = BinEncoding.which(tempObjValue, BinEncoding.Hex);
                            return tempEncoding.addPrefix(tempEncoding.stripPrefixOff(tempObjValue));
                        }

                        return ListParser.stripQuotes(_value);
                    }
                default:
                    throw new IllegalArgumentException("Data type " + _type + " is not supported for default value of attribute [" + _attrName + "]");
            }
        }
    }

    protected Object initDefaultArrayValue(String _attrName, Type _arrayType, String _listStr) {
        if (_listStr == null) {
            return null;
        } else {
            Type tempType = _arrayType.getElementType();
            if (tempType.getSort() == Type.ARRAY) {
                throw new IllegalArgumentException("Nested array type " + _arrayType + " is not supported for default value of attribute [" + _attrName + "]");
            } else {
                List<String> tempParsedList = ListParser.parseList(_listStr, ",");
                List tempDefaultValueList = new ArrayList(tempParsedList.size());
                Iterator tempItr = tempParsedList.iterator();

                while(tempItr.hasNext()) {
                    String tempValue = (String)tempItr.next();
                    tempDefaultValueList.add(this.initDefaultValue(_attrName, tempType, tempValue));
                }

                return tempDefaultValueList.toArray();
            }
        }
    }
}

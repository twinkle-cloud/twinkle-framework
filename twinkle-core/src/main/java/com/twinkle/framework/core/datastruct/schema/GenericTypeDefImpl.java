package com.twinkle.framework.core.datastruct.schema;

import lombok.Getter;
import org.objectweb.asm.Type;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-01 22:54<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Getter
public class GenericTypeDefImpl extends ClassTypeDefImpl implements GenericTypeDef, Cloneable {
    private final Type[] typeParameters;

    public GenericTypeDefImpl(String _name, Class _typeClass, Type[] _genericTypeArray) {
        super(_name, _typeClass);
        this.typeParameters = _genericTypeArray;
    }

    public String getFieldSignature() {
        return getGenericFieldSignature(this.getType(), this.getTypeParameters());
    }

    public String getGetterSignature() {
        return getGenericGetterSignature(this.getType(), this.getTypeParameters());
    }

    public String getSetterSignature() {
        return getGenericSetterSignature(this.getType(), this.getTypeParameters());
    }

    @Override
    public boolean isGeneric() {
        return true;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public static String getGenericDescriptor(Type _type, Type[] _genericTypeArray) {
        StringBuilder tempBuilder = new StringBuilder();
        tempBuilder.append('L');
        tempBuilder.append(_type.getInternalName());
        if (_genericTypeArray != null && _genericTypeArray.length > 0) {
            tempBuilder.append('<');
            Type[] tempTypeArray = _genericTypeArray;

            for(int i = 0; i < _genericTypeArray.length; ++i) {
                Type tempType = tempTypeArray[i];
                tempBuilder.append(tempType.getDescriptor());
            }

            tempBuilder.append('>');
        }

        tempBuilder.append(';');
        return tempBuilder.toString();
    }

    /**
     * Get Field signature with given Type and the generic Types.
     *
     * @param _type
     * @param _genericTypeArray
     * @return
     */
    public static String getGenericFieldSignature(Type _type, Type[] _genericTypeArray) {
        return getGenericDescriptor(_type, _genericTypeArray);
    }

    /**
     * Get Getter method signature with given Type and the generic Types.
     *
     * @param _type
     * @param _genericTypeArray
     * @return
     */
    public static String getGenericGetterSignature(Type _type, Type[] _genericTypeArray) {
        return "()" + getGenericDescriptor(_type, _genericTypeArray);
    }

    /**
     * Get Setter method signature with given Type and the generic Types.
     *
     * @param _type
     * @param _genericTypeArray
     * @return
     */
    public static String getGenericSetterSignature(Type _type, Type[] _genericTypeArray) {
        return "(" + getGenericDescriptor(_type, _genericTypeArray) + ")V";
    }
}

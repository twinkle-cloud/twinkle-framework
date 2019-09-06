package com.twinkle.framework.asm.data;

import com.twinkle.framework.asm.ReflectiveBean;
import com.twinkle.framework.asm.accessor.AccessorType;
import com.twinkle.framework.asm.accessor.ReflectiveBeanFieldAccessor;

import java.util.*;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-06 15:05<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class PartiallyDecodedData<E> {
    private ReflectiveBean bean;
    private Map<String, ReflectiveBeanFieldAccessor> accessors;
    private Map<String, E> rawData;

    public PartiallyDecodedData(ReflectiveBean _bean) {
        this.bean = Objects.requireNonNull(_bean);
        this.accessors = new HashMap<>();
        this.rawData = new HashMap<>();
    }

    /**
     * Get registered Fields' set.
     *
     * @return
     */
    public Set<String> registeredFields() {
        return this.accessors.keySet();
    }

    /**
     * To register the given field into the set.
     *
     * @param _fieldName
     * @param _accessorType
     */
    public void registeredField(String _fieldName, AccessorType _accessorType) {
        if (this.accessors.containsKey(_fieldName)) {
            throw new IllegalArgumentException("Field '" + _fieldName + "' already registered");
        } else {
            this.accessors.put(_fieldName, new ReflectiveBeanFieldAccessor(this.bean, _fieldName, _accessorType));
        }
    }

    /**
     * Remove some field.
     *
     * @param _fieldName
     */
    public void unregisteredField(String _fieldName) {
        this.accessors.remove(_fieldName);
        this.rawData.remove(_fieldName);
    }

    /**
     * Update some field' value.
     *
     * @param _fieldName
     * @param _value
     */
    public void setRawData(String _fieldName, E _value) {
        if (!this.accessors.containsKey(_fieldName)) {
            throw new IllegalArgumentException("Field '" + _fieldName + "' not registered");
        } else {
            this.rawData.put(_fieldName, _value);
        }
    }

    /**
     * Get some field's value.
     *
     * @param _fieldName
     * @return
     */
    public E getRawData(String _fieldName) {
        if (!this.rawData.containsKey(_fieldName)) {
            throw new IllegalArgumentException("RawData for field '" + _fieldName + "' not found");
        } else {
            return this.rawData.get(_fieldName);
        }
    }

    /**
     * Clear some field's raw data.
     *
     * @param _fieldName
     */
    public void clearRawData(String _fieldName) {
        this.rawData.remove(_fieldName);
    }

    @Override
    public String toString() {
        StringBuilder tempBuilder = new StringBuilder();
        tempBuilder.append('{');
        this.rawData.forEach((k, v) -> {
            tempBuilder.append('"').append(k).append('"').append('=').append(v);
            tempBuilder.append(',');
        });
        if(tempBuilder.length() > 0) {
            tempBuilder.deleteCharAt(tempBuilder.length());
        }
        tempBuilder.append('}');
        return tempBuilder.toString();
    }
}

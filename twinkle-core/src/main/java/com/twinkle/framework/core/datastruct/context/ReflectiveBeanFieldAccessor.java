package com.twinkle.framework.core.datastruct.context;

import com.twinkle.framework.core.datastruct.ReflectiveBean;
import lombok.Getter;

import java.util.Objects;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-05 23:00<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Getter
public class ReflectiveBeanFieldAccessor {
    protected final ReflectiveBean bean;
    protected final String fieldName;
    protected final AccessorType fieldType;

    public ReflectiveBeanFieldAccessor(ReflectiveBean _bean, String _fieldName, AccessorType _accessorType) {
        this.bean = (ReflectiveBean) Objects.requireNonNull(_bean);
        this.fieldName = _fieldName;
        this.fieldType = _accessorType;
    }

    /**
     * Update(Set) the Field's value with given _value.
     *
     * @param _value
     */
    public void set(Object _value) {
        this.fieldType.set(this.bean, this.fieldName, _value);
    }

    /**
     * Get the Field's value.
     *
     * @return
     */
    public Object get() {
        return this.fieldType.get(this.bean, this.fieldName);
    }

    /**
     * Clear the current field from current Bean.
     *
     */
    public void clear() {
        this.bean.clear(this.fieldName);
    }
    @Override
    public boolean equals(Object _obj) {
        if (this == _obj) {
            return true;
        } else if (!(_obj instanceof ReflectiveBeanFieldAccessor)) {
            return false;
        } else {
            ReflectiveBeanFieldAccessor tempAccessor = (ReflectiveBeanFieldAccessor)_obj;
            return this.bean.equals(tempAccessor.bean) && this.fieldName.equals(tempAccessor.fieldName);
        }
    }
    @Override
    public int hashCode() {
        int tempHashCode = this.bean.hashCode();
        tempHashCode = 31 * tempHashCode + this.fieldName.hashCode();
        return tempHashCode;
    }
    @Override
    public String toString() {
        return "ReflectiveBeanFieldAccessor[" + this.fieldName + "]{" + this.bean + '}';
    }
}

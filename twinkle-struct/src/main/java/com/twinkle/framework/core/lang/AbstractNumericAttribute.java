package com.twinkle.framework.core.lang;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-10 22:31<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public abstract class AbstractNumericAttribute implements INumericAttribute {
    private static final long serialVersionUID = 5959249450950713646L;

    /**
     * Add the attribute value into this value.
     * @param _attr
     */
    public abstract void add(Attribute _attr);
    /**
     * Subtract the attribute value from this value.
     * @param _attr
     */
    public abstract void subtract(Attribute _attr);
    /**
     * Set min value of the attribute value and current value.
     * @param _attr
     */
    public abstract void min(Attribute _attr);
    /**
     * Set max value of the attribute value and current value.
     * @param _attr
     */
    public abstract void max(Attribute _attr);

    @Override
    public void aggregate(int _operation, Attribute _attr) {
        switch(_operation) {
            case OPERATION_ADD:
                this.add(_attr);
                break;
            case OPERATION_SUBTRACT:
                this.subtract(_attr);
                break;
            case OPERATION_MIN:
                this.min(_attr);
                break;
            case OPERATION_MAX:
                this.max(_attr);
                break;
            case OPERATION_SET:
                this.setValue(_attr);
        }
    }

    @Override
    public int getOperationID(String _operationName) {
        if (_operationName.equals(OPERATION_NAME_ADD)) {
            return OPERATION_ADD;
        } else if (_operationName.equals(OPERATION_NAME_SUBTRACT)) {
            return OPERATION_SUBTRACT;
        } else if (_operationName.equals(OPERATION_NAME_MIN)) {
            return OPERATION_MIN;
        } else if (_operationName.equals(OPERATION_NAME_MAX)) {
            return OPERATION_MAX;
        } else {
            return _operationName.equals(OPERATION_NAME_SET) ? OPERATION_SET : -1;
        }
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new Error("Assertion failure: " + ex);
        }
    }
}

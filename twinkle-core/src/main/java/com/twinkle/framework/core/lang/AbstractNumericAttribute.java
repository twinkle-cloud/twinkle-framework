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
    public void aggregate(Operation _operation, Attribute _attr) {
        switch(_operation) {
            case ADD:
                this.add(_attr);
                break;
            case SUBTRACT:
                this.subtract(_attr);
                break;
            case MIN:
                this.min(_attr);
                break;
            case MAX:
                this.max(_attr);
                break;
            case SET:
                this.setValue(_attr);
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

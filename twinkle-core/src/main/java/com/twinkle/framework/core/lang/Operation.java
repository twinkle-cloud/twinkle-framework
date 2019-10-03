package com.twinkle.framework.core.lang;

import lombok.Getter;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/30/19 10:06 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Getter
public enum Operation {
    ADD(1, "add"), SUBTRACT(2, "subtract"),
    MIN(3, "min"), MAX(4, "max"),
    SET(5, "set"), UNKNOWN(99, "unknown");

    private int index;
    private String name;

    private Operation(int _index, String _name) {
        this.index = _index;
        this.name = _name;
    }

    /**
     * Get the Operation with given operation name.
     *
     * @param _name
     * @return
     */
    public static Operation getOperationByName(String _name) {
        for(Operation tempOpr : Operation.values()) {
            if(tempOpr.getName().equals(_name)) {
                return tempOpr;
            }
        }
        return UNKNOWN;
    }
}

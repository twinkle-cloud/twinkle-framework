package com.twinkle.framework.asm.helper;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-05 10:52<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class EqualsHelper {
    public EqualsHelper() {
    }

    /**
     * Do the equal check.
     *
     * @param _obj1
     * @param _obj2
     * @return
     */
    public static boolean equals(Object _obj1, Object _obj2) {
        if (_obj1 == _obj2) {
            return true;
        } else {
            return _obj1 != null ? _obj1.equals(_obj2) : false;
        }
    }
}

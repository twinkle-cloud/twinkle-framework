package com.twinkle.framework.core.asm.helper;

import java.lang.reflect.InvocationTargetException;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-05 10:53<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class CloneHelper {
    public CloneHelper() {
    }

    /**
     * Clone the given object
     *
     * @param _source
     * @param _className source object's class Name.
     * @return
     * @throws CloneNotSupportedException
     */
    public static Object dynamicClone(Object _source, String _className) throws CloneNotSupportedException {
        if (_source == null) {
            return null;
        } else if (_source instanceof Cloneable) {
            CloneNotSupportedException var8;
            try {
                return _source.getClass().getMethod("clone").invoke(_source);
            } catch (IllegalAccessException e) {
                var8 = new CloneNotSupportedException(_className);
                var8.initCause(e);
                throw var8;
            } catch (NoSuchMethodException e) {
                var8 = new CloneNotSupportedException(_className);
                var8.initCause(e);
                throw var8;
            } catch (InvocationTargetException e) {
                Throwable tempThrowable = e.getTargetException();
                if (tempThrowable instanceof CloneNotSupportedException) {
                    throw (CloneNotSupportedException)tempThrowable;
                } else {
                    CloneNotSupportedException tempCloneException = new CloneNotSupportedException(_className);
                    tempCloneException.initCause(tempThrowable);
                    throw tempCloneException;
                }
            }
        } else {
            throw new CloneNotSupportedException(_className);
        }
    }
}

package com.twinkle.framework.struct.utils;

import com.twinkle.framework.struct.error.BadAttributeNameException;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/6/19 4:04 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class StructAttributeNameValidator {
    /**
     * Check the Struct attribute's attribute name.
     *
     * @param _attrName
     * @throws BadAttributeNameException
     */
    public static void checkName(String _attrName) throws BadAttributeNameException {
        int tempLength = _attrName.length();

        try {
            char tempFirstChar = _attrName.charAt(0);
            if (!Character.isLetter(_attrName.charAt(0)) && tempFirstChar != '_' && tempFirstChar != '$') {
                throw new BadAttributeNameException("Name should start with a letter: [a - z, A - Z, _, $]: " + _attrName);
            } else {
                for(int i = 1; i < tempLength; ++i) {
                    tempFirstChar = _attrName.charAt(i);
                    if (!Character.isLetterOrDigit(tempFirstChar) && tempFirstChar != '_' && tempFirstChar != '$') {
                        throw new BadAttributeNameException("Name cannot contain characters other than [a -z, A - Z, 0 - 9, _, $]: " + _attrName);
                    }
                }

            }
        } catch (IndexOutOfBoundsException e) {
            throw new BadAttributeNameException("IndexOutOfBoundsException while processing struct attribute name: " + _attrName, e);
        } catch (IllegalArgumentException e) {
            throw new BadAttributeNameException("IllegalArgumentException while processing struct attribute name: " + _attrName, e);
        }
    }
}

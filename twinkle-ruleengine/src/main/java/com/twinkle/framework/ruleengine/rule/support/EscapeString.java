package com.twinkle.framework.ruleengine.rule.support;

import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.exception.ConfigurationException;

/**
 * Function: Escape String. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/7/19 1:53 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class EscapeString {
    private String charString = null;

    public EscapeString(String _str) {
        this.charString = _str;
    }

    public String getString() throws ConfigurationException {
        if (this.charString == null) {
            return null;
        }
        char[] tempCharArray = new char[this.charString.length()];
        boolean escFlag = false;
        int tempIndex = 0;
        int i;
        for (i = 0; i < this.charString.length(); i++) {
            char tempChar = this.charString.charAt(i);
            if (!escFlag) {
                if (tempChar == '\\') {
                    escFlag = true;
                } else {
                    tempCharArray[tempIndex++] = tempChar;
                }
            } else {
                if (Character.isDigit(tempChar)) {
                    try {
                        if (i + 2 >= this.charString.length()) {
                            throw new NumberFormatException("Bad octal format");
                        }

                        if (this.charString.charAt(i) != '0' || this.charString.charAt(i + 1) != 'x' && this.charString.charAt(i + 1) != 'X') {
                            tempCharArray[tempIndex++] = (char) Integer.parseInt(this.charString.substring(i, i + 3), 8);
                            i += 2;
                        } else {
                            i += 2;
                            if (i + 1 >= this.charString.length()) {
                                throw new NumberFormatException("Bad hex format");
                            }

                            tempCharArray[tempIndex++] = (char) Integer.parseInt(this.charString.substring(i, i + 2), 16);
                            ++i;
                        }
                    } catch (NumberFormatException e) {
                        throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ESCAPE_CHAR_POSITION_INVALID, "Bad character escape at position " + i + " in string (" + this.charString + "): " + e);
                    }
                } else {
                    switch (tempChar) {
                        case '\\':
                            tempCharArray[tempIndex++] = '\\';
                            break;
                        case 'b':
                            tempCharArray[tempIndex++] = '\b';
                            break;
                        case 'f':
                            tempCharArray[tempIndex++] = '\f';
                            break;
                        case 'n':
                            tempCharArray[tempIndex++] = '\n';
                            break;
                        case 'r':
                            tempCharArray[tempIndex++] = '\r';
                            break;
                        case 't':
                            tempCharArray[tempIndex++] = '\t';
                            break;
                        case 'u':
                            if (this.charString.length() - i > 4) {
                                try {
                                    tempCharArray[tempIndex++] = (char) Integer.parseInt(this.charString.substring(i + 1, i + 5), 16);
                                    i += 4;
                                    break;
                                } catch (NumberFormatException e) {
                                    throw new ConfigurationException(ExceptionCode.CHARACTER_CODE_UNICODE_INVALID, "Invalid unicode in string (" + this.charString + ")");
                                }
                            }

                            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ESCAPE_CHAR_POSITION_INVALID, "Bad character escape at position " + i + " in string (" + this.charString + ")");
                        default:
                            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ESCAPE_CHAR_POSITION_INVALID, "Bad character escape at position " + i + " in string (" + this.charString + ")");
                    }
                }
                escFlag = false;
            }
        }

        if (escFlag) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ESCAPE_CHAR_POSITION_INVALID, "Incomplete character escape at position " + i + " in string (" + this.charString + ")");
        } else {
            return new String(tempCharArray, 0, tempIndex);
        }
    }
}

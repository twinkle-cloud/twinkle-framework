package com.twinkle.framework.core.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-01 11:21<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class ListParser {
    public static final String COMMA_SEPARATOR = ",";
    public static final String SEMICOLON_SEPARATOR = ";";
    public static final String QUOTES = "\"'";
    public static final String LEFT_PARENTHESIS = "(";
    public static final String RIGHT_PARENTHESIS = ")";
    public static final String PARENTHESES = "()";
    public static final String LEFT_CURLY_BRACE = "{";
    public static final String RIGHT_CURLY_BRACE = "}";
    public static final String CURLY_BRACES = "{}";
    public static final String LEFT_ANGLE_BRACKET = "<";
    public static final String RIGHT_ANGLE_BRACKET = ">";
    public static final String ANGLE_BRACKETS = "<>";
    public static final String LEFT_SQUARE_BRACKET = "[";
    public static final String RIGHT_SQUARE_BRACKET = "]";
    public static final String SQUARE_BRACKETS = "[]";

    public ListParser() {
    }

    /**
     * Parse the given str to the String List.
     *
     * @param _str : given Str
     * @param _delimiter: delimiter
     * @return
     */
    public static List<String> parseList(String _str, String _delimiter) {
        return parseList(_str, _delimiter, false, "");
    }

    /**
     * Parse the given str to the String List.
     *
     * @param _str : given Str
     * @param _delimiter: delimiter
     * @param _keepDelimiterFlag: keep the delimiter with element or not.
     * @return
     */
    public static List<String> parseList(String _str, String _delimiter, boolean _keepDelimiterFlag) {
        return parseList(_str, _delimiter, _keepDelimiterFlag, "");
    }

    /**
     * Parse the given str to the String List.
     *
     * @param _str : given Str
     * @param _delimiter: delimiter
     * @param _stripStr: the Strip Characters which need to be removed while parsing.
     * @return
     */
    public static List<String> parseList(String _str, String _delimiter, String _stripStr) {
        return parseList(_str, _delimiter, false, _stripStr);
    }

    /**
     * Parse the given str to the String List.
     *
     * @param _str : given Str
     * @param _delimiter: delimiter
     * @param _keepDelimiterFlag: keep the delimiter with element or not.
     * @param _stripStr: the Strip Characters which need to be removed while parsing.
     * @return
     */
    public static List<String> parseList(String _str, String _delimiter, boolean _keepDelimiterFlag, String _stripStr) {
        if (_stripStr.length() % 2 != 0) {
            throw new IllegalArgumentException("List " + _stripStr + " must contain matching pair of brackets!");
        } else {
            List<String> tempResultList = new ArrayList<>();
            LinkedList tempItemList = new LinkedList();
            int tempBeginIndex = 0;
            int tempStrLength = _str.length();

            for (int i = 0; i < tempStrLength; i++) {
                char tempStrChar = _str.charAt(i);
                boolean isDelimiter = false;
                //Try to retrieve the elements.
                for (int j = 0; j < _delimiter.length(); ++j) {
                    if (tempStrChar == _delimiter.charAt(j)) {
                        if (tempItemList.isEmpty()) {
                            if (i > tempBeginIndex) {
                                tempResultList.add(_str.substring(tempBeginIndex, _keepDelimiterFlag ? i + 1 : i).trim());
                            }
                            // After add the element, then move the index.
                            tempBeginIndex = i + 1;
                        }
                        //Found the delimiter.
                        isDelimiter = true;
                        break;
                    }
                }
                //Check the character is strip character or not.
                if (!isDelimiter) {
                    if (i == tempStrLength - 1) {
                        if (i >= tempBeginIndex) {
                            tempResultList.add(_str.substring(tempBeginIndex, tempStrLength).trim());
                        }
                    } else {
                        boolean isNotStrip = true;
                        // Check the left strip character.
                        int ii;
                        for (ii = 0; ii < _stripStr.length(); ii += 2) {
                            if (tempStrChar == _stripStr.charAt(ii)) {
                                tempItemList.push(tempStrChar);
                                isNotStrip = false;
                                break;
                            }
                        }
                        // Check the right strip character.
                        if (isNotStrip) {
                            for (ii = 1; ii < _stripStr.length(); ii += 2) {
                                if (tempStrChar == _stripStr.charAt(ii)) {
                                    if (tempItemList.isEmpty() || (Character) tempItemList.pop() != _stripStr.charAt(ii - 1)) {
                                        throw new IllegalStateException(tempItemList.toString());
                                    }

                                    isNotStrip = false;
                                    break;
                                }
                            }
                        }
                        //Dismiss the QUOTES.
                        if (isNotStrip && tempStrChar == '"') {
                            if (tempItemList.isEmpty()) {
                                tempItemList.push(tempStrChar);
                            } else if ((Character) tempItemList.peek() != tempStrChar) {
                                tempItemList.push(tempStrChar);
                            } else {
                                tempItemList.pop();
                            }
                        }
                    }
                }
            }

            return tempResultList;
        }
    }

    /**
     * Strip the starter(_starter) and space from the given String (_str).
     *
     * @param _str
     * @param _starter
     * @return
     */
    public static String stripStart(String _str, String _starter) {
        int tempLength;
        if (_str != null && (tempLength = _str.length()) != 0) {
            int tempCleanStart = 0;
            if (_starter != null && _starter.length() != 0) {
                while (tempCleanStart != tempLength && (_starter.indexOf(_str.charAt(tempCleanStart)) >= 0 || Character.isWhitespace(_str.charAt(tempCleanStart)))) {
                    tempCleanStart++;
                }
            } else {
                //Remove the space.
                while (tempCleanStart != tempLength && Character.isWhitespace(_str.charAt(tempCleanStart))) {
                    tempCleanStart++;
                }
            }

            return _str.substring(tempCleanStart);
        } else {
            return _str;
        }
    }

    /**
     * Strip the ender(_ender) and space from the given String (_str).
     *
     * @param _str
     * @param _ender
     * @return
     */
    public static String stripEnd(String _str, String _ender) {
        int tempLength;
        if (_str != null && (tempLength = _str.length()) != 0) {
            if (_ender != null && _ender.length() != 0) {
                while (tempLength != 0 && (_ender.indexOf(_str.charAt(tempLength - 1)) >= 0 || Character.isWhitespace(_str.charAt(tempLength - 1)))) {
                    tempLength--;
                }
            } else {
                while (tempLength != 0 && Character.isWhitespace(_str.charAt(tempLength - 1))) {
                    tempLength--;
                }
            }

            return _str.substring(0, tempLength);
        } else {
            return _str;
        }
    }

    /**
     * Strip the starter(_starter), ender(_ender) and space from the given String (_str).
     *
     * @param _str
     * @param _starter
     * @param _ender
     * @return
     */
    public static String strip(String _str, String _starter, String _ender) {
        if (_str != null && _str.length() > 0) {
            _str = stripStart(_str, _starter);
            return stripEnd(_str, _ender);
        } else {
            return _str;
        }
    }

    /**
     * Strip the starter(_subStr), ender(_subStr) and space from the given String (_str).
     *
     * @param _str
     * @param _subStr
     * @return
     */
    public static String strip(String _str, String _subStr) {
        return strip(_str, _subStr, _subStr);
    }

    /**
     * Remove the "' from the given string.
     * both starter and ender.
     *
     * @param _str
     * @return
     */
    public static String stripQuotes(String _str) {
        return strip(_str, QUOTES);
    }

    /**
     * Remove the starter ( and ender ) from the given string.
     *
     * @param _str
     * @return
     */
    public static String stripParentheses(String _str) {
        return strip(_str, LEFT_PARENTHESIS, RIGHT_PARENTHESIS);
    }

    /**
     * Remove the starter { and ender } from the given string.
     *
     * @param _str
     * @return
     */
    public static String stripCurlyBraces(String _str) {
        return strip(_str, LEFT_CURLY_BRACE, RIGHT_CURLY_BRACE);
    }

    /**
     * Substring the given String.
     *
     * @param _str
     * @param _beginStr
     * @param _endStr
     * @return
     */
    public static String substringBetween(String _str, String _beginStr, String _endStr) {
        if (_str != null && _beginStr != null && _endStr != null) {
            int tempBeginIndex = _str.indexOf(_beginStr);
            if (tempBeginIndex < 0) {
                return _str;
            } else {
                int tempEndIndex = _str.lastIndexOf(_endStr);
                if (tempEndIndex >= 0 && tempEndIndex > tempBeginIndex) {
                    return _str.substring(tempBeginIndex + 1, tempEndIndex);
                } else {
                    throw new IllegalArgumentException(_str);
                }
            }
        } else {
            return null;
        }
    }

    /**
     * Substring the given String to get the content between ( and ).
     *
     * @param _str
     * @return
     */
    public static String substringBetweenParentheses(String _str) {
        return substringBetween(_str, LEFT_PARENTHESIS, RIGHT_PARENTHESIS);
    }

    /**
     * Substring the given String to get the content between { and } .
     *
     * @param _str
     * @return
     */
    public static String substringBetweenCurlyBraces(String _str) {
        return substringBetween(_str, LEFT_CURLY_BRACE, RIGHT_CURLY_BRACE);
    }

    /**
     * Substring the given String to get the content between < and >.
     *
     * @param _str
     * @return
     */
    public static String substringBetweenAngleBrackets(String _str) {
        return substringBetween(_str, LEFT_ANGLE_BRACKET, RIGHT_ANGLE_BRACKET);
    }

    /**
     * Substring the given String to get the content between [ and ].
     *
     * @param _str
     * @return
     */
    public static String substringBetweenSquareBrackets(String _str) {
        return substringBetween(_str, LEFT_SQUARE_BRACKET, RIGHT_SQUARE_BRACKET);
    }

}

package com.twinkle.framework.core.lang.ref;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/30/19 8:07 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class CompositeName implements Cloneable {
    public static final char DEFAULT_SEPARATOR = '.';
    public static final char[] ARRAY_BRACKETS = new char[]{'[', ']'};
    private CompositeName head;
    private CompositeName previous;
    private CompositeName next;
    private String compositeName;
    private String name;
    private String indexStr;
    private int indexPos;
    private int indexLen;
    private int index;
    private boolean hasIndex;
    private boolean indexNumeric;

    private CompositeName(char[] _chars, int _offset, int _length, CompositeName _next, char _separator, char[] _brackets, boolean _headFlag) throws ParseException {
        this.next = _next;
        this.compositeName = new String(_chars, _offset, _length);
        this.indexStr = null;
        this.index = -1;
        this.indexPos = -1;
        this.indexLen = 0;
        this.indexNumeric = true;
        this.hasIndex = false;
        char leftBracket = _brackets[0];
        char rightBracket = _brackets[1];
        int tempEndIndex = _offset + _length;
        int tempLastCharIndex = tempEndIndex - 1;
        boolean tempInvalidFlag = true;

        while (tempLastCharIndex >= _offset) {
            char tempChar = _chars[tempLastCharIndex];
            int tempRemainLength;
            int tempLength;
            if (tempChar == _separator) {
                tempRemainLength = tempLastCharIndex - _offset;
                tempLength = tempEndIndex - tempLastCharIndex - 1;
                this.name = trim(_chars, tempLastCharIndex + 1, tempLength);
                if (this.name.length() == 0) {
                    throw new ParseException("Empty name: \"" + this.compositeName + "\"", tempLastCharIndex + 1);
                }

                if (_headFlag) {
                    this.previous = new CompositeName(_chars, _offset, tempRemainLength, this, _separator, _brackets, true);
                    this.head = this.previous.head();
                } else {
                    this.previous = null;
                    this.head = this;
                }
                break;
            }

            if (tempChar != rightBracket) {
                if (tempChar == leftBracket) {
                    throw new ParseException("Unmatched bracket \"" + tempChar + "\" at: \"" + this.compositeName + "\"", tempLastCharIndex);
                }
                if (tempChar > ' ') {
                    tempInvalidFlag = false;
                }
                --tempLastCharIndex;
            } else {
                tempRemainLength = tempLastCharIndex;
                if (!tempInvalidFlag) {
                    String tempUnexpectedChar = new String(_chars, tempLastCharIndex + 1, _length - tempLastCharIndex - 1);
                    String tempCNName = this.compositeName;
                    throw new ParseException("Unexpected trailing characters: \"" + tempUnexpectedChar + "\" in \"" + tempCNName + "\"", tempLastCharIndex + 1);
                }
                tempLength = 1;
                do {
                    --tempLastCharIndex;
                    char tempInnerChar = _chars[tempLastCharIndex];
                    if (tempInnerChar == leftBracket) {
                        --tempLength;
                    } else if (tempInnerChar == rightBracket) {
                        ++tempLength;
                    }
                } while (tempLength > 0 && tempLastCharIndex > _offset);

                if (tempLength > 0) {
                    throw new ParseException("Unmatched bracket \"" + rightBracket + "\" at: \"" + this.compositeName + "\"", tempRemainLength);
                }
                tempEndIndex = tempLastCharIndex;
                this.indexPos = tempLastCharIndex + 1;
                this.indexLen = tempRemainLength - this.indexPos;
                this.indexStr = trim(_chars, this.indexPos, this.indexLen);
                if (this.indexStr.length() > 0 && this.indexStr.charAt(0) == '-') {
                    throw new ParseException("Illegal leading index character \"-\" found in \"" + this.indexStr + "\" at: \"" + this.compositeName + "\"", this.indexPos);
                }

                if (isNumeric(this.indexStr)) {
                    try {
                        this.index = Integer.parseInt(this.indexStr);
                    } catch (NumberFormatException e) {
                        ParseException tempException = new ParseException("Unable to parse index string \"" + this.indexStr + "\" in: \"" + this.compositeName + "\"", this.indexPos);
                        tempException.initCause(e);
                    }
                } else {
                    this.index = 0;
                    this.indexNumeric = false;
                }

                this.hasIndex = true;
                --tempLastCharIndex;
                tempInvalidFlag = false;
            }
        }

        if (tempLastCharIndex < _offset) {
            if (this.indexPos < 0) {
                this.name = trim(_chars, _offset, _length);
            } else {
                int tempLength = tempEndIndex - _offset;
                this.name = trim(_chars, _offset, tempLength);
            }

            if (this.name.length() == 0) {
                throw new ParseException("Empty name: \"" + this.compositeName + "\"", _offset);
            }

            this.previous = null;
            this.head = this;
        }
    }

    protected CompositeName(String _str, char _separator, char[] _brackets, boolean _headFlag) throws ParseException {
        this(_str.toCharArray(), 0, _str.length(), null, _separator, assertBrackets(_brackets), _headFlag);
    }

    public CompositeName(String _str, char _separator, char[] _brackets) throws ParseException {
        this(_str.toCharArray(), 0, _str.length(), null, _separator, assertBrackets(_brackets), true);
    }

    protected CompositeName(String _str, boolean _headFlag) throws ParseException {
        this(_str.toCharArray(), 0, _str.length(), null, '.', ARRAY_BRACKETS, _headFlag);
    }

    public CompositeName(String _str) throws ParseException {
        this(_str.toCharArray(), 0, _str.length(), null, '.', ARRAY_BRACKETS, true);
    }

    /**
     * To ensure the brackets exist in the char array.
     *
     * @param _chars
     * @return
     */
    private static char[] assertBrackets(char[] _chars) {
        Objects.requireNonNull(_chars, "Expected a pair of brackets, found NULL");
        if (_chars.length < 2) {
            throw new IllegalArgumentException("Expected a pair of brackets, found " + _chars.length);
        } else {
            return _chars;
        }
    }

    /**
     * Trim the String to get the name.
     *
     * @param _chars
     * @param _offset
     * @param _length
     * @return
     */
    private static String trim(char[] _chars, int _offset, int _length) {
        int tempEndIndex = _offset + _length;
        int tempIndex = _offset;

        int i;
        for (i = tempEndIndex; tempIndex < i && _chars[tempIndex] <= ' '; ++tempIndex) {
        }
        while (tempIndex < i && _chars[i - 1] <= ' ') {
            --i;
        }
        return i == 0 ? "" : new String(_chars, tempIndex, i - tempIndex);
    }

    /**
     * Check the first character is numeric or not?
     *
     * @param _str
     * @return
     */
    private static boolean isNumeric(String _str) {
        int tempLength = _str.length();
        if (tempLength == 0) {
            return false;
        } else {
            char tempChar = _str.charAt(0);
            return Character.isDigit(tempChar);
        }
    }

    public CompositeName head() {
        return this.head;
    }

    public boolean isHead() {
        return this.previous == null;
    }

    /**
     * Get the tail of this composite name.
     *
     * @return
     */
    public CompositeName tail() {
        CompositeName tempName;
        for (tempName = this; !tempName.isTail(); tempName = tempName.next()) {
        }

        return tempName;
    }

    /**
     * Judge this node is tail node or not?
     *
     * @return
     */
    public boolean isTail() {
        return this.next == null;
    }

    /**
     * Get the previous node.
     *
     * @return
     */
    public CompositeName previous() {
        return this.previous;
    }

    /**
     * Get the next node.
     *
     * @return
     */
    public CompositeName next() {
        return this.next;
    }

    public String fullName() {
        return this.compositeName;
    }

    public String name() {
        return this.name;
    }

    public String indexString() {
        return this.indexStr;
    }

    public int index() {
        return this.index;
    }

    public boolean isNumericIndex() {
        return this.indexNumeric;
    }

    public boolean hasIndex() {
        return this.hasIndex;
    }

    @Override
    public String toString() {
        return this.compositeName;
    }

    @Override
    public boolean equals(Object _obj) {
        if (this == _obj) {
            return true;
        } else if (_obj != null && this.getClass() == _obj.getClass()) {
            CompositeName tempName = (CompositeName) _obj;
            return this.compositeName.equals(tempName.compositeName);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.compositeName.hashCode();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Replicate the given index's composite name.
     *
     * @param _index
     * @return
     */
    public CompositeName replicate(int _index) {
        return this.replicate(Integer.toString(_index));
    }

    /**
     * Replicate the given index's composite name.
     *
     * @param _indexStr
     * @return
     */
    public CompositeName replicate(String _indexStr) {
        if (_indexStr == null) {
            throw new IllegalArgumentException("Invalid indexString: " + _indexStr);
        } else if (!this.hasIndex) {
            throw new IllegalStateException("Cannot replicate non-indexed name: " + this.compositeName);
        }
        try {
            CompositeName tempName = (CompositeName) super.clone();
            tempName.indexStr = _indexStr.trim();
            tempName.indexLen = tempName.indexStr.length();
            if (isNumeric(tempName.indexStr)) {
                tempName.indexNumeric = true;
                try {
                    tempName.index = Integer.parseInt(tempName.indexStr);
                } catch (NumberFormatException e) {
                    ParseException tempException = new ParseException("Unable to parse index string \"" + _indexStr + "\"", 0);
                    tempException.initCause(e);
                }
            } else {
                tempName.index = 0;
                tempName.indexNumeric = false;
            }

            int tempLength = tempName.indexLen - this.indexLen;
            StringBuilder tempBuilder = new StringBuilder(this.compositeName.length() + tempLength);
            tempBuilder.append(this.compositeName.substring(0, this.indexPos)).append(tempName.indexStr);
            tempBuilder.append(this.compositeName.substring(this.indexPos + this.indexLen, this.compositeName.length()));
            tempName.compositeName = tempBuilder.toString();

            CompositeName tempNameA;
            CompositeName tempNameB;
            for (tempNameA = tempName; !tempNameA.isHead(); tempNameB.previous = tempNameA) {
                tempNameB = tempNameA;
                tempNameA = tempNameA.previous;
                tempNameA = (CompositeName) tempNameA.clone();
                tempNameA.next = tempNameB;
            }

            CompositeName tempNameC;
            for (tempNameC = tempNameA; tempNameA != tempName; tempNameA = tempNameA.next) {
                tempNameA.head = tempNameC;
            }

            tempNameA = tempName;
            tempName.head = tempNameC;

            while (!tempNameA.isTail()) {
                CompositeName tempNameD = tempNameA;
                tempNameA = tempNameA.next;
                tempNameA = (CompositeName) tempNameA.clone();
                tempNameA.head = tempNameC;
                tempNameA.previous = tempNameD;
                tempNameD.next = tempNameA;
                tempNameA.compositeName = tempName.compositeName + tempNameA.compositeName.substring(this.compositeName.length());
                if (tempNameA.indexPos >= 0) {
                    tempNameA.indexPos += tempLength;
                }
            }
            return tempName;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Split the composite name into the List<CompositeName>
     *
     * @return
     */
    public List<CompositeName> splitByNonNumeric() {
        List<CompositeName> tempResultList = new ArrayList<>();
        CompositeName tempHead = this.head();

        CompositeName tempName;
        for (tempName = tempHead; tempName != null; tempName = tempName.next()) {
            if (tempName.hasIndex && !tempName.indexNumeric || tempName.isTail()) {
                tempResultList.add(tempName);
            }
        }

        CompositeName tempSubName = null;
        String tempCurrentNameStr = null;

        String tempNextNameStr;
        for (Iterator<CompositeName> tempItr = tempResultList.iterator(); tempItr.hasNext(); tempCurrentNameStr = tempNextNameStr) {
            CompositeName tempItemName = tempItr.next();
            tempName = tempItemName;
            tempNextNameStr = tempItemName.compositeName;
            if (tempSubName != null) {
                for (tempHead = tempSubName.next; tempName != tempSubName; tempName = tempName.previous) {
                    int tempLength = tempCurrentNameStr.length() + 1;
                    tempName.compositeName = tempName.compositeName.substring(tempLength);
                    if (tempName.indexPos >= 0) {
                        tempName.indexPos -= tempLength;
                    }

                    tempName.head = tempHead;
                }
                if (tempName.next != null) {
                    tempName.next.previous = null;
                    tempName.next = null;
                }
            }
            tempSubName = tempItemName;
        }
        return tempResultList;
    }
}

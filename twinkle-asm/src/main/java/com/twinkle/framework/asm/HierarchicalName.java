package com.twinkle.framework.asm;

import java.text.ParseException;
import java.util.Arrays;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-03 16:08<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class HierarchicalName implements Cloneable {
    public static final String[] DEFAULT_ARRAY_BRACKETS = new String[]{"(", ")", "[[", "]]", "[", "]"};
    public static final String[] ARRAY_LIST_BRACKETS = new String[]{"(", ")"};
    private final HierarchicalName head;
    private final HierarchicalName previous;
    private final HierarchicalName next;
    private String fullName;
    private final String name;
    private int index;
    private final int indexPos;
    private int indexLen;
    private final int kind;

    protected HierarchicalName(String _fullName, HierarchicalName _next, String[] _arrayBracketArray) throws ParseException {
        this.fullName = _fullName;
        this.next = _next;
        //Char("46")=.
        int tempLastDotIndex = _fullName.lastIndexOf(46);
        String tempDirtyName;
        if (tempLastDotIndex < 0) {
            tempDirtyName = _fullName;
            this.previous = null;
            this.head = this;
        } else {
            //If the dot exists, then sub the name to get the prefix name,
            //and try to Pack the previous Hierarchical Name.
            String tempPrefixHierarchicalName = _fullName.substring(0, tempLastDotIndex);
            tempDirtyName = _fullName.substring(tempLastDotIndex + 1);
            this.previous = new HierarchicalName(tempPrefixHierarchicalName, this, _arrayBracketArray);
            this.head = this.previous.head();
        }

        int tmepBracketArrayLen = _arrayBracketArray.length;
        if (tmepBracketArrayLen % 2 != 0) {
            throw new IllegalArgumentException("Odd count: " + Arrays.toString(_arrayBracketArray));
        } else {
            String tempName = null;
            int tempIndex = -1;
            int tempIndexPos = -1;
            int tempIndexLen = 0;
            int tempKind = -1;

            for(int i = 0; i < tmepBracketArrayLen; i += 2) {
                String tempLeftBracket = _arrayBracketArray[i];
                String tempRightBracket = _arrayBracketArray[i + 1];
                int tempLeftBracketLen = tempLeftBracket.length();
                int tempLeftBracketIndex = tempDirtyName.indexOf(tempLeftBracket);
                if (tempLeftBracketIndex >= 0) {
                    int tempRightBracketIndex = tempDirtyName.indexOf(tempRightBracket, tempLeftBracketIndex);
                    tempIndexPos = tempLeftBracketIndex + tempLeftBracketLen;
                    String tempArrayIndex = tempDirtyName.substring(tempIndexPos, tempRightBracketIndex);
                    tempIndexLen = tempArrayIndex.length();
                    if (tempLastDotIndex >= 0) {
                        tempIndexPos += tempLastDotIndex + 1;
                    }

                    String tempTrimIndex = tempArrayIndex.trim();
                    int tempRightBracketPos;
                    if (tempTrimIndex.length() > 0) {
                        try {
                            tempIndex = Integer.parseInt(tempTrimIndex);
                        } catch (NumberFormatException e) {
                            tempRightBracketPos = tempLastDotIndex + tempLeftBracketIndex + tempLeftBracketLen;
                            throw new ParseException("Bad index number at " + tempRightBracketPos + " in " + _fullName, tempRightBracketPos);
                        }
                    } else {
                        tempIndex = 0;
                    }

                    tempName = tempDirtyName.substring(0, tempLeftBracketIndex).trim();
                    tempKind = i / 2;
                    int tempRightBracketLen = tempRightBracket.length();
                    tempRightBracketPos = tempRightBracketIndex + tempRightBracketLen;
                    if (tempRightBracketPos < tempDirtyName.length()) {
                        String tempRestStr = tempDirtyName.substring(tempRightBracketPos);
                        if (tempRestStr.trim().length() > 0) {
                            throw new ParseException("Unexpected trailing characters: \"" + tempRestStr + "\" in \"" + tempDirtyName + "\"", tempRightBracketPos);
                        }
                    }
                    break;
                }
            }

            this.index = tempIndex;
            this.indexPos = tempIndexPos;
            this.indexLen = tempIndexLen;
            this.kind = tempKind;
            if (tempIndex < 0) {
                this.name = tempDirtyName.trim();
            } else {
                this.name = tempName;
            }
        }
    }

    public HierarchicalName(String _fullName) throws ParseException {
        this(_fullName, null, DEFAULT_ARRAY_BRACKETS);
    }

    public HierarchicalName(String _fullName, String[] _arrayBracketArray) throws ParseException {
        this(_fullName, null, _arrayBracketArray);
    }

    public HierarchicalName head() {
        return this.head;
    }

    public boolean isHead() {
        return this.previous == null;
    }

    public HierarchicalName tail() {
        HierarchicalName tempHName;
        for(tempHName = this; !tempHName.isTail(); tempHName = tempHName.next()) {
        }

        return tempHName;
    }

    public boolean isTail() {
        return this.next == null;
    }

    public HierarchicalName previous() {
        return this.previous;
    }

    public HierarchicalName next() {
        return this.next;
    }

    public String fullName() {
        return this.fullName;
    }

    public String name() {
        return this.name;
    }

    public int index() {
        return this.index;
    }

    public int kind() {
        return this.kind;
    }
    @Override
    public String toString() {
        return this.fullName;
    }
    @Override
    public boolean equals(Object _obj) {
        if (this == _obj) {
            return true;
        } else if (_obj != null && this.getClass() == _obj.getClass()) {
            HierarchicalName tempName = (HierarchicalName)_obj;
            return this.fullName.equals(tempName.fullName);
        } else {
            return false;
        }
    }
    @Override
    public int hashCode() {
        return this.fullName.hashCode();
    }
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public HierarchicalName replicate(int _index) {
        if (_index < 0) {
            throw new IllegalArgumentException("Invalid index: " + _index);
        } else if (this.index < 0) {
            throw new IllegalStateException("Cannot replicate non-indexed name: " + this.fullName);
        } else {
            try {
                HierarchicalName tempName = (HierarchicalName)super.clone();
                tempName.index = _index;
                String tempIndexStr = Integer.toString(_index);
                tempName.indexLen = tempIndexStr.length();
                int tempIndexLen = this.fullName.length() + tempName.indexLen - this.indexLen;
                StringBuilder tempBuilder = new StringBuilder(tempIndexLen);
                tempBuilder.append(this.fullName.substring(0, this.indexPos)).append(tempIndexStr);
                tempBuilder.append(this.fullName.substring(this.indexPos + this.indexLen, this.fullName.length()));
                tempName.fullName = tempBuilder.toString();
                return tempName;
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

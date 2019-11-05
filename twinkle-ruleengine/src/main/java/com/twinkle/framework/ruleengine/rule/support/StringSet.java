package com.twinkle.framework.ruleengine.rule.support;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     11/4/19 6:48 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class StringSet {
    private String[] stringSet;

    public StringSet() {
        this.stringSet = null;
    }

    public StringSet(String[] _strings) {
        this.setStrings(_strings);
    }

    public void setStrings(String[] _strings) {
        this.stringSet = new String[_strings.length];

        for (int i = 0; i < _strings.length; ++i) {
            this.stringSet[i] = _strings[i];
        }
    }

    /**
     * Get the set size.
     *
     * @return
     */
    public int size() {
        return this.stringSet == null ? 0 : this.stringSet.length;
    }

    public boolean compareTo(String _str) {
        if (this.stringSet == null) {
            return false;
        }
        for (int i = 0; i < this.stringSet.length; ++i) {
            if (_str.compareTo(this.stringSet[i]) == 0) {
                return true;
            }
        }

        return false;
    }

    public boolean startsWith(String _str) {
        if (this.stringSet == null) {
            return false;
        }
        for (int var2 = 0; var2 < this.stringSet.length; ++var2) {
            if (_str.startsWith(this.stringSet[var2])) {
                return true;
            }
        }
        return false;
    }

    public boolean endsWith(String _str) {
        if (this.stringSet == null) {
            return false;
        }
        for (int var2 = 0; var2 < this.stringSet.length; ++var2) {
            if (_str.endsWith(this.stringSet[var2])) {
                return true;
            }
        }
        return false;
    }
}

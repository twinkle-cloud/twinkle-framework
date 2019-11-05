package com.twinkle.framework.ruleengine.rule.support;

import java.util.Vector;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     11/4/19 5:12 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class IntegerRangeSet {
    private Vector rangeSet;

    public IntegerRangeSet() {
        this.rangeSet = new Vector();
    }

    public IntegerRangeSet(String[] _ranges) {
        this.rangeSet = new Vector(_ranges.length);
        this.addRanges(_ranges);
    }

    /**
     * Add the given range into the current range set.
     *
     * @param _range
     */
    public void addRange(IntegerRange _range) {
        this.rangeSet.addElement(_range);
    }

    /**
     * Add the given ranges into the current range set.
     *
     * @param _ranges
     */
    public void addRanges(String[] _ranges) {
        for (int i = 0; i < _ranges.length; ++i) {
            this.addRange(new IntegerRange(_ranges[i]));
        }

    }

    /**
     * Get the ranges size.
     *
     * @return
     */
    public int size() {
        return this.rangeSet.size();
    }

    /**
     * To verify the given value is in the ranges or not?
     *
     * @param _val
     * @return
     */
    public boolean testValue(int _val) {
        for (int i = 0; i < this.rangeSet.size(); ++i) {
            if (((IntegerRange) this.rangeSet.elementAt(i)).testValue(_val)) {
                return true;
            }
        }
        return false;
    }
}

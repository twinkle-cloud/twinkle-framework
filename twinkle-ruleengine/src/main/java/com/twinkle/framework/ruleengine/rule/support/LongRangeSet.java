package com.twinkle.framework.ruleengine.rule.support;

import java.util.Vector;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     11/4/19 6:46 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class LongRangeSet {
    private Vector rangeSet;

    public LongRangeSet() {
        this.rangeSet = new Vector();
    }

    public LongRangeSet(String[] _ranges) {
        this.rangeSet = new Vector(_ranges.length);
        this.addRanges(_ranges);
    }

    /**
     * Add the given range into the set.
     *
     * @param _range
     */
    public void addRange(LongRange _range) {
        this.rangeSet.addElement(_range);
    }

    /**
     * Add the given ranges into the set.
     *
     * @param _ranges
     */
    public void addRanges(String[] _ranges) {
        for(int i = 0; i < _ranges.length; ++i) {
            this.addRange(new LongRange(_ranges[i]));
        }

    }

    /**
     * Get the range size.
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
    public boolean testValue(long _val) {
        for(int i = 0; i < this.rangeSet.size(); ++i) {
            if (((LongRange)this.rangeSet.elementAt(i)).testValue(_val)) {
                return true;
            }
        }
        return false;
    }
}

package com.twinkle.framework.ruleengine.rule.support;

import java.util.Vector;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     11/4/19 4:56 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class IpAddressRangeSet {
    private Vector rangeSet;

    public IpAddressRangeSet() {
        this.rangeSet = new Vector();
    }

    public IpAddressRangeSet(String[] _ranges) {
        this.rangeSet = new Vector(_ranges.length);
        this.addRanges(_ranges);
    }

    /**
     * Add range into the list.
     *
     * @param _range
     */
    public void addRange(IpAddressRange _range) {
        this.rangeSet.addElement(_range);
    }

    /**
     * Add ranges into the list.
     *
     * @param _ranges
     */
    public void addRanges(String[] _ranges) {
        for(int i = 0; i < _ranges.length; ++i) {
            this.addRange(new IpAddressRange(_ranges[i]));
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
     * To verify the given ip address is belong to the ranges or not?
     *
     * @param _ipAddress
     * @return
     */
    public boolean testAddress(int _ipAddress) {
        for(int i = 0; i < this.rangeSet.size(); ++i) {
            if (((IpAddressRange)this.rangeSet.elementAt(i)).testValue(_ipAddress)) {
                return true;
            }
        }
        return false;
    }
}

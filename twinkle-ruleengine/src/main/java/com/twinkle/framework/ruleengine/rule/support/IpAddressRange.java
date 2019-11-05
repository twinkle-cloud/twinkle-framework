package com.twinkle.framework.ruleengine.rule.support;

import com.twinkle.framework.core.lang.IPAddrAttribute;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     11/4/19 4:57 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class IpAddressRange implements IRange {
    /**
     * The start Ip Address of this Range.
     */
    private int startAddr;
    /**
     * The end Ip address of this range.
     */
    private int endAddr;

    /**
     * Format: Ip1-Ip2
     *
     * @param _ipRange
     */
    public IpAddressRange(String _ipRange) {
        //Get the '-' index.
        int tempIndex = _ipRange.indexOf(OPERATOR_INTERVAL);
        String tempStartIp = _ipRange.substring(0, tempIndex);
        String tempEndIp = _ipRange.substring(tempIndex + 1);
        tempStartIp.trim();
        tempEndIp.trim();
        this.startAddr = IPAddrAttribute.stringToAddr(tempStartIp);
        this.endAddr = IPAddrAttribute.stringToAddr(tempEndIp);
    }

    /**
     * Build the range with the start IP and end IP.
     *
     * @param _startIp
     * @param _endIp
     */
    public IpAddressRange(int _startIp, int _endIp) {
        this.startAddr = _startIp;
        this.endAddr = _endIp;
    }

    @Override
    public String toString() {
        return IPAddrAttribute.addrToString(this.startAddr) + OPERATOR_INTERVAL + IPAddrAttribute.addrToString(this.endAddr);
    }

    @Override
    public boolean testValue(int _addr) {
        if (this.startAddr < 0) {
            return _addr >= this.startAddr && _addr <= this.endAddr;
        } else if (this.endAddr < 0) {
            if (_addr >= 0 && _addr >= this.startAddr) {
                return true;
            } else {
                return _addr < 0 && _addr <= this.endAddr;
            }
        } else {
            return _addr >= this.startAddr && _addr <= this.endAddr;
        }
    }
}

package com.twinkle.framework.ruleengine.rule.support;

import java.security.MessageDigest;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/7/19 11:07 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class DummyDigest extends MessageDigest {
    private static final byte[] EMPTY = new byte[0];

    public DummyDigest(String var1) {
        super(var1);
    }

    @Override
    protected byte[] engineDigest() {
        return EMPTY;
    }

    @Override
    protected void engineReset() {
    }

    @Override
    protected void engineUpdate(byte var1) {
    }

    @Override
    protected void engineUpdate(byte[] var1, int var2, int var3) {
    }
}

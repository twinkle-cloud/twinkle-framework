package com.twinkle.framework.ruleengine.rule.support;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/7/19 11:08 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class ConcatenateDigest extends MessageDigest {
    private final ByteArrayOutputStream bos = new ByteArrayOutputStream();

    public ConcatenateDigest() {
        super("");
    }

    @Override
    protected byte[] engineDigest() {
        byte[] var1 = this.bos.toByteArray();
        this.engineReset();
        return var1;
    }

    @Override
    protected void engineReset() {
        this.bos.reset();
    }

    @Override
    protected void engineUpdate(byte var1) {
        this.bos.write(var1);
    }

    @Override
    protected void engineUpdate(byte[] var1, int var2, int var3) {
        this.bos.write(var1, var2, var3);
    }
}

package com.twinkle.framework.ruleengine.rule.support;

import com.twinkle.framework.ruleengine.rule.operation.DigestOperation;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/7/19 11:05 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class NullOutputStream extends OutputStream {
    public static final NullOutputStream NULL_OUTPUT_STREAM = new NullOutputStream();

    private NullOutputStream() {
    }

    @Override
    public void write(byte[] var1, int var2, int var3) {
    }

    @Override
    public void write(int var1) {
    }

    @Override
    public void write(byte[] var1) throws IOException {
    }
}

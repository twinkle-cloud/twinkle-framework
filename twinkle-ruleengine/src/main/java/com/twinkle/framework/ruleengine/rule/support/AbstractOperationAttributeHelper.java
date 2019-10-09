package com.twinkle.framework.ruleengine.rule.support;

import com.twinkle.framework.core.lang.Attribute;

import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/7/19 10:47 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public abstract class AbstractOperationAttributeHelper {
    public AbstractOperationAttributeHelper() {
    }

    /**
     * Write attribute value into output stream.
     *
     * @param _srcAttr
     * @param _outputSteam
     * @throws IOException
     */
    public abstract void writeAttributeValue(Attribute _srcAttr, DataOutputStream _outputSteam) throws IOException;

    /**
     * Set the digest as the result the base64 _srcArray encoded.
     *
     * @param _destAttr
     * @param _srcArray
     */
    public void setDigest(Attribute _destAttr, byte[] _srcArray) {
        throw new IllegalArgumentException("Cannot store digest in the " + _destAttr.getClass().getName() + " attribute");
    }

    /**
     * Get the attribute's value as digest name.
     *
     * @param _attr
     * @return
     */
    public String getDigestName(Attribute _attr) {
        throw new IllegalArgumentException("Cannot extract digest name from the " + _attr.getClass().getName() + " attribute");
    }

    /**
     * Get digest with the given attribute.
     *
     * @param _attr
     * @return
     * @throws NoSuchAlgorithmException
     */
    public MessageDigest getDigest(Attribute _attr) throws NoSuchAlgorithmException {
        return MessageDigest.getInstance(this.getDigestName(_attr));
    }

    public boolean canHoldDigest() {
        return false;
    }

    public boolean canHoldDigestName() {
        return false;
    }
}

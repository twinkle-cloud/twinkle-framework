package com.twinkle.framework.ruleengine.utils;

import com.twinkle.framework.context.lang.AttributeListAttribute;
import com.twinkle.framework.core.lang.*;
import com.twinkle.framework.ruleengine.rule.support.AbstractOperationAttributeHelper;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/9/19 6:23 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class OperationUtil {
    public static final Map<Class<?>, AbstractOperationAttributeHelper> OPERATION_HELPERS = Collections.unmodifiableMap(new HashMap() {
        {
            this.put(ListAttribute.class, null);
            this.put(AttributeListAttribute.class, null);
            this.put(ObjectAttribute.class, null);
            this.put(UUIDAttribute.class, new AbstractOperationAttributeHelper() {
                @Override
                public void writeAttributeValue(Attribute _srcAttr, DataOutputStream _outputSteam) throws IOException {
                    _outputSteam.write(((UUIDAttribute) _srcAttr).getByteArray());
                }
            });
            this.put(BinaryAttribute.class, new AbstractOperationAttributeHelper() {
                @Override
                public void writeAttributeValue(Attribute _srcAttr, DataOutputStream _outputSteam) throws IOException {
                    _outputSteam.write(((BinaryAttribute) _srcAttr).getByteArray());
                }

                @Override
                public boolean canHoldDigest() {
                    return true;
                }

                @Override
                public void setDigest(Attribute _destAttr, byte[] _srcArray) {
                    ((BinaryAttribute) _destAttr).setValue(_srcArray);
                }
            });
            this.put(IPv6AddrAttribute.class, new AbstractOperationAttributeHelper() {
                @Override
                public void writeAttributeValue(Attribute _srcAttr, DataOutputStream _outputSteam) throws IOException {
                    _outputSteam.write(((IPv6AddrAttribute) _srcAttr).getByteArray());
                }
            });
            this.put(UUIDAttribute.class, new AbstractOperationAttributeHelper() {
                @Override
                public void writeAttributeValue(Attribute _srcAttr, DataOutputStream _outputSteam) throws IOException {
                    _outputSteam.write(((UUIDAttribute) _srcAttr).getByteArray());
                }
            });
            this.put(MutableStringAttribute.class, new AbstractOperationAttributeHelper() {
                @Override
                public void writeAttributeValue(Attribute _srcAttr, DataOutputStream _outputSteam) throws IOException {
                    _outputSteam.write(((MutableStringAttribute) _srcAttr).toString().getBytes("UTF-8"));
                }

                @Override
                public boolean canHoldDigest() {
                    return true;
                }

                @Override
                public void setDigest(Attribute _destAttr, byte[] _srcArray) {
                    ((MutableStringAttribute) _destAttr).setValue(Base64.getEncoder().encode(_srcArray));
                }
            });
            this.put(StringAttribute.class, new AbstractOperationAttributeHelper() {
                @Override
                public void writeAttributeValue(Attribute _srcAttr, DataOutputStream _outputSteam) throws IOException {
                    _outputSteam.write(((StringAttribute) _srcAttr).getValue().getBytes("UTF-8"));
                }

                @Override
                public boolean canHoldDigest() {
                    return true;
                }

                @Override
                public boolean canHoldDigestName() {
                    return true;
                }

                @Override
                public String getDigestName(Attribute _attr) {
                    return ((StringAttribute) _attr).getValue();
                }

                @Override
                public void setDigest(Attribute _destAttr, byte[] _srcArray) {
                    ((StringAttribute) _destAttr).setValue(Base64.getEncoder().encode(_srcArray));
                }
            });
            this.put(UnicodeStringAttribute.class, new AbstractOperationAttributeHelper() {
                @Override
                public void writeAttributeValue(Attribute _srcAttr, DataOutputStream _outputSteam) throws IOException {
                    _outputSteam.writeUTF(((UnicodeStringAttribute) _srcAttr).getValue());
                }

                @Override
                public boolean canHoldDigest() {
                    return true;
                }

                @Override
                public boolean canHoldDigestName() {
                    return true;
                }

                @Override
                public String getDigestName(Attribute _attr) {
                    return ((UnicodeStringAttribute) _attr).getValue();
                }

                @Override
                public void setDigest(Attribute _destAttr, byte[] _srcArray) {
                    ((UnicodeStringAttribute) _destAttr).setValue(Base64.getEncoder().encode(_srcArray));
                }
            });
            this.put(DoubleAttribute.class, new AbstractOperationAttributeHelper() {
                @Override
                public void writeAttributeValue(Attribute _srcAttr, DataOutputStream _outputSteam) throws IOException {
                    _outputSteam.writeDouble(((DoubleAttribute) _srcAttr).getDouble());
                }
            });
            this.put(FloatAttribute.class, new AbstractOperationAttributeHelper() {
                @Override
                public void writeAttributeValue(Attribute _srcAttr, DataOutputStream _outputSteam) throws IOException {
                    _outputSteam.writeFloat(((FloatAttribute) _srcAttr).getFloat());
                }
            });
            this.put(IntegerAttribute.class, new AbstractOperationAttributeHelper() {
                @Override
                public void writeAttributeValue(Attribute _srcAttr, DataOutputStream _outputSteam) throws IOException {
                    _outputSteam.writeInt(((IntegerAttribute) _srcAttr).getInt());
                }
            });
            this.put(IPAddrAttribute.class, new AbstractOperationAttributeHelper() {
                @Override
                public void writeAttributeValue(Attribute _srcAttr, DataOutputStream _outputSteam) throws IOException {
                    _outputSteam.writeInt(((IPAddrAttribute) _srcAttr).getInt());
                }
            });
            this.put(TimeAttribute.class, new AbstractOperationAttributeHelper() {
                @Override
                public void writeAttributeValue(Attribute _srcAttr, DataOutputStream _outputSteam) throws IOException {
                    _outputSteam.writeInt(((TimeAttribute) _srcAttr).getInt());
                }
            });
            this.put(RangeAttribute.class, new AbstractOperationAttributeHelper() {

                @Override
                public void writeAttributeValue(Attribute _srcAttr, DataOutputStream _outputSteam) throws IOException {
                    _outputSteam.write(((RangeAttribute) _srcAttr).getByteArray());
                }
            });
            this.put(LongAttribute.class, new AbstractOperationAttributeHelper() {
                @Override
                public void writeAttributeValue(Attribute _srcAttr, DataOutputStream _outputSteam) throws IOException {
                    _outputSteam.writeLong(((LongAttribute) _srcAttr).getLong());
                }
            });
        }
    });
}

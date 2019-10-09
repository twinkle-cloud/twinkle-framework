package com.twinkle.framework.core.lang;

import com.twinkle.framework.core.utils.ByteProcessor;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/8/19 3:33 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class UUIDAttribute extends BinaryAttribute {
    private static int type = 110;
    public static final short UUID_SIZE = 16;
    public static final short UUID_STRING_SIZE = 36;
    public static final byte[] NIL_UUID = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    public static final UUIDAttribute NIL_UUID_ATTR;
    private static final short UUID_VERSION = 20480;
    private static final int UUID_VARIANT = -536870912;
    private static Random UUID_SEED;
    private static final short HOSTADDR_SIZE = 4;
    private static byte[] HOST_ADDRS;
    public static final int[] IUM_UUID_FORMAT;

    static {
        NIL_UUID_ATTR = new UUIDAttribute(NIL_UUID);
        UUID_SEED = new Random();
        HOST_ADDRS = new byte[HOSTADDR_SIZE];

        try {
            InetAddress tempInetAddress = InetAddress.getLocalHost();
            HOST_ADDRS = tempInetAddress.getAddress();
        } catch (UnknownHostException e) {
            log.warn("Get Host failed. Exception: {}", e);
        }

        String tempFormat = System.getProperty("twinkle.uuid.format", "DEFAULT");
        if ("INTERNAL".equalsIgnoreCase(tempFormat)) {
            IUM_UUID_FORMAT = new int[]{3, 5, 7, 11};
        } else if ("IPDR".equalsIgnoreCase(tempFormat)) {
            IUM_UUID_FORMAT = new int[]{3, 5, 7, 9};
        } else {
            IUM_UUID_FORMAT = new int[]{4, 6, 8, 10};
        }

        long tempSeed = (System.currentTimeMillis() & -1L) << 32 | (long) ByteProcessor.readInt(HOST_ADDRS, 0);
        UUID_SEED.setSeed(tempSeed);
    }

    public UUIDAttribute() {
        byte[] tempByteArray = NIL_UUID.clone();
        super.setValue(tempByteArray);
    }

    public UUIDAttribute(byte[] _byteArray) {
        byte[] tempByteArray = NIL_UUID.clone();
        if (_byteArray != null) {
            if (_byteArray.length == UUID_SIZE) {
                tempByteArray = _byteArray;
            } else {
                log.warn("The given byteArray's length is[{}] which exceeds UUID_SIZE.", _byteArray.length);
                if (_byteArray.length > UUID_SIZE) {
                    System.arraycopy(_byteArray, 0, tempByteArray, 0, UUID_SIZE);
                } else {
                    System.arraycopy(_byteArray, 0, tempByteArray, 0, _byteArray.length);
                }
            }
        }
        super.setValue(tempByteArray);
    }

    @Override
    public final int getTypeIndex() {
        return type;
    }
    @Override
    public final void setTypeIndex(int _type) {
        type = _type;
    }

    public static int getTypeID() {
        return type;
    }

    @Override
    public void setEmptyValue() {
        byte[] tempByteArray = NIL_UUID.clone();
        super.setValue(tempByteArray);
    }

    @Override
    public void setValue(Attribute _attr) {
        if (this != _attr) {
            byte[] tempByteArray = NIL_UUID.clone();
            if (_attr != null) {
                byte[] tempAttrByteArray = ((BinaryAttribute) _attr).getByteArray();
                if (tempAttrByteArray != null) {
                    if (tempAttrByteArray.length == UUID_SIZE) {
                        System.arraycopy(tempAttrByteArray, 0, tempByteArray, 0, UUID_SIZE);
                    } else {
                        log.warn("The byteArray's length of given attribute is[{}] which exceeds UUID_SIZE.", tempAttrByteArray.length);
                        if (tempAttrByteArray.length > UUID_SIZE) {
                            System.arraycopy(tempAttrByteArray, 0, tempByteArray, 0, UUID_SIZE);
                        } else {
                            System.arraycopy(tempAttrByteArray, 0, tempByteArray, 0, tempAttrByteArray.length);
                        }
                    }
                }
            }
            super.setValue(tempByteArray);
        }
    }

    @Override
    public void setValue(byte[] _value) {
        byte[] tempByteArray = NIL_UUID.clone();
        if (_value != null) {
            if (_value.length == UUID_SIZE) {
                tempByteArray = _value;
            } else {
                log.warn("The given byteArray's length is[{}] which exceeds UUID_SIZE.", _value.length);
                if (_value.length > UUID_SIZE) {
                    System.arraycopy(_value, 0, tempByteArray, 0, UUID_SIZE);
                } else {
                    System.arraycopy(_value, 0, tempByteArray, 0, _value.length);
                }
            }
        }

        super.setValue(tempByteArray);
    }

    @Override
    public void setValue(String _value) {
        byte[] tempByteArray = NIL_UUID.clone();
        if (_value != null) {
            int tempStrSize = UUID_STRING_SIZE;
            if (_value.length() != UUID_STRING_SIZE) {
                log.warn("The give string's length [{}] is not equal with UUID_STRING_SIZE", _value.length());
                if (tempStrSize < UUID_STRING_SIZE) {
                    tempStrSize = _value.length();
                }
            }
            // MinusChar "-" .
            byte tempMinusChar = 45;
            int tempIndex = 0;
            int tempByteArrayIndex = 0;
            while (tempIndex + 2 <= tempStrSize) {
                String tempStr = _value.substring(tempIndex, tempIndex + 2);
                if (tempStr.charAt(0) == tempMinusChar) {
                    ++tempIndex;
                } else {
                    try {
                        tempByteArray[tempByteArrayIndex++] = (byte) Integer.parseInt(tempStr, 16);
                        tempIndex += 2;
                    } catch (NumberFormatException e) {
                        log.warn("Encountered error while parsing the give string. Exception: {}", e);
                        tempByteArray = NIL_UUID.clone();
                        super.setValue(tempByteArray);
                        return;
                    }
                }
            }
        }

        super.setValue(tempByteArray);
    }

    @Override
    public String toString() {
        byte[] tempByteArray = super.getByteArray();
        return toString(tempByteArray);
    }

    public static String toString(byte[] _byteArray) {
        if (_byteArray == null) {
            return null;
        }
        int tempLength = UUID_SIZE;
        if (_byteArray.length != UUID_SIZE) {
            log.warn("The length of given byte array [{}] is not equal UUID_SIZE.", _byteArray.length);
            if (_byteArray.length < UUID_SIZE) {
                tempLength = _byteArray.length;
            }
        }

        StringBuilder tempBuilder = new StringBuilder("");
        for (int i = 0; i < tempLength; i++) {
            byte tempByte = _byteArray[i];
            tempBuilder.append(Character.forDigit(tempByte >> 4 & 15, 16));
            tempBuilder.append(Character.forDigit(tempByte & 15, 16));
            if (i == IUM_UUID_FORMAT[0] || i == IUM_UUID_FORMAT[1] || i == IUM_UUID_FORMAT[2] || i == IUM_UUID_FORMAT[3]) {
                tempBuilder.append("-");
            }
        }

        return tempBuilder.toString();
    }

    /**
     * Update UUID.
     *
     * @return
     */
    public byte[] updateUUID() {
        byte[] tempByteArray = getNewUUID();
        super.setValue(tempByteArray);
        return tempByteArray;
    }

    /**
     * Update UUID.
     *
     * @param _byteArray
     * @return
     */
    public byte[] updateUUID(byte[] _byteArray) {
        _byteArray = getNewUUID(_byteArray);
        super.setValue(_byteArray);
        return _byteArray;
    }

    @Override
    public Object clone() {
        UUIDAttribute tempAttr = new UUIDAttribute();
        tempAttr.setValue(this);
        return tempAttr;
    }

    @Override
    public Object getObjectValue() {
        return super.getObjectValue();
    }

    @Override
    public String toLogString() {
        return this.toString();
    }

    /**
     * Generate new UUID.
     *
     * @return
     */
    public static byte[] getNewUUID() {
        byte[] tempByteArray = new byte[UUID_SIZE];
        return getNewUUID(tempByteArray);
    }

    /**
     * Generate new UUID.
     *
     * @param _byteArray
     * @return
     */
    public static byte[] getNewUUID(byte[] _byteArray) {
        long tempTimeStamp = System.currentTimeMillis();

        //Time-low
        int tempTimeStampValue = (int) (tempTimeStamp & -1L);
        int tempIndex = ByteProcessor.writeInt(_byteArray, 0, tempTimeStampValue);
        //Time-Mid
        short tempDateValue = (short) ((int) (tempTimeStamp >> 32 & 65535L));
        tempIndex = ByteProcessor.writeShort(_byteArray, tempIndex, tempDateValue);
        //Version &  Time-High
        short var6 = (short) ((int) (tempTimeStamp >> 48 & 4095L | UUID_VERSION));
        tempIndex = ByteProcessor.writeShort(_byteArray, tempIndex, var6);
        //Variant Seed.
        int tempSeed = UUID_SEED.nextInt() | UUID_VARIANT;
        tempIndex = ByteProcessor.writeInt(_byteArray, tempIndex, tempSeed);
        //Host Address
        for (int i = 0; i < HOSTADDR_SIZE; i++) {
            _byteArray[tempIndex++] = HOST_ADDRS[i];
        }

        return _byteArray;
    }
}

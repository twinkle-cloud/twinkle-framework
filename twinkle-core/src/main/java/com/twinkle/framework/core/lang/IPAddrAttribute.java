package com.twinkle.framework.core.lang;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/9/19 11:41 AM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class IPAddrAttribute extends IntegerAttribute implements Comparable {
    private static int type = 103;

    public IPAddrAttribute() {
        this.value = 0;
    }

    public IPAddrAttribute(int _ipValue) {
        this.value = _ipValue;
    }

    public IPAddrAttribute(String _ipValue) {
        this.setValue(_ipValue);
    }

    @Override
    public final int getPrimitiveType() {
        return Attribute.INTEGER_TYPE;
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
    public void setValue(String _value) {
        this.value = stringToAddr(_value);
    }

    public static int stringToAddr(String _str) throws NumberFormatException {
        int tempResultValue = 0;
        int tempLength = _str.length();
        int tempSegCount = 0;

        for (int i = 0; i < tempLength; ++i) {
            char tempChar = _str.charAt(i);
            int tempValue = 0;
            boolean validFlag = false;

            while (tempChar != '.') {
                if (tempChar >= '0' && tempChar <= '9') {
                    tempValue = tempValue * 10 + tempChar - 48;
                    validFlag = true;
                    ++i;
                    if (i < tempLength) {
                        tempChar = _str.charAt(i);
                        continue;
                    }
                    break;
                }
                throw new NumberFormatException("Unable to parse IP Address: '" + _str + "': Digit expected.");
            }

            if (tempValue < 0 || tempValue > 255) {
                throw new NumberFormatException("Unable to parse IP Address: '" + _str + "': Octet larger than 255.");
            }

            if (!validFlag) {
                throw new NumberFormatException("Unable to parse IP Address: '" + _str + "': Bad address format.");
            }

            tempResultValue = (tempResultValue << 8) + tempValue;
            ++tempSegCount;
        }

        if (tempSegCount != 4) {
            throw new NumberFormatException("Unable to parse IP Address: '" + _str + "': Bad address format.");
        } else {
            return tempResultValue;
        }
    }

    @Override
    public void setValue(Attribute _attr) {
        if (this != _attr) {
            if (_attr.getPrimitiveType() == Attribute.STRING_TYPE) {
                this.setValue(_attr.toString());
            } else {
                this.value = ((INumericAttribute) _attr).getInt();
            }
        }
    }

    public void setValue(byte[] _byteArray) {
        this.value = 0;
        int tempLength = _byteArray.length;

        for (int i = 0; i < tempLength; ++i) {
            byte tempByte = _byteArray[i];
            int tempValue = 0;
            boolean validFlag;
            for (validFlag = false; tempByte != 46; tempByte = _byteArray[i]) {
                tempValue = tempValue * 10 + tempByte - 48;
                validFlag = true;
                ++i;
                if (i >= tempLength) {
                    break;
                }
            }

            if (!validFlag) {
                throw new NumberFormatException("Unable to parse IP Address: '" + _byteArray + "': Bad address format.");
            }

            this.value = (this.value << 8) + tempValue;
        }

    }
    @Override
    public boolean equals(Object _obj) {
        if (_obj != null && _obj instanceof IPAddrAttribute) {
            return this.value == ((IPAddrAttribute) _obj).getValue();
        } else {
            return false;
        }
    }
    @Override
    public void aggregate(Operation _operation, Attribute _attr) {
        switch (_operation) {
            case SET:
                this.setValue(_attr);
            default:
        }
    }

    @Override
    public String toString() {
        return addrToString(this.value);
    }

    @Override
    public Object clone() {
        return new IPAddrAttribute(this.getValue());
    }

    @Override
    public int compareTo(Object _obj) throws ClassCastException {
        IPAddrAttribute tempAttr = (IPAddrAttribute) _obj;
        long tempValue = (long) this.value & 4294967295L;
        long tempObjValue = (long) tempAttr.value & 4294967295L;
        if (tempValue < tempObjValue) {
            return -1;
        } else {
            return tempValue > tempObjValue ? 1 : 0;
        }
    }

    @Override
    public Object getObjectValue() {
        return this.toString();
    }

    public static String addrToString(int _ipValue) {
        return (_ipValue >>> 24 & 255) + "." + (_ipValue >>> 16 & 255) + "." + (_ipValue >>> 8 & 255) + "." + (_ipValue >>> 0 & 255);
    }

    public static void main(String[] _args) {
        String tempStr = _args[0];

        try {
            String tempIp = "1.2.3.4";
            IPAddrAttribute tempIp1 = new IPAddrAttribute(tempStr);
            IPAddrAttribute tempIp2 = new IPAddrAttribute(tempIp);
            System.out.println("ip1.compareTo(ip2) = " + tempIp1.compareTo(tempIp2));
            System.out.println("ip2.compareTo(ip1) = " + tempIp2.compareTo(tempIp1));
            System.out.println(tempStr + "  " + Integer.toHexString(stringToAddr(tempStr)));
            StringAttribute tempStrAttr = new StringAttribute();
            tempStrAttr.setValue(new String("1.2.3.4"));

            try {
                tempIp1.setValue((Attribute) tempStrAttr);
                if (tempIp1.compareTo(tempIp2) == 0) {
                    System.out.println("Good! String attribute with ip addr setting worked");
                } else {
                    System.out.println("String attribute with ip addr setting worked but the value is " + tempIp1 + " not 1.2.3.4");
                }
            } catch (Exception e) {
                System.err.println("Error! String attribute with ip addr setting didn't work");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

package com.twinkle.framework.core.datastruct.codec;

import java.util.Arrays;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-01 11:25<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public enum BinEncoding {

    Base64("Base64", new BinEncoding.Base64Codec()),
    Hex("HEX", new BinEncoding.HexCodec()),
    Binary("BIN", new BinEncoding.BinaryCodec());

    private final String _value;
    private final String _prefix;
    private final BinCodec _codec;

    private BinEncoding(String _enumName, BinCodec _codec) {
        this._value = _enumName;
        this._prefix = _enumName + ":";
        this._codec = _codec;
    }

    public String value() {
        return this._value;
    }

    public String prefix() {
        return this._prefix;
    }

    public BinCodec codec() {
        return this._codec;
    }

    public String stripPrefixOff(String _prefix) {
        return _prefix.startsWith(this._prefix) ? _prefix.substring(this._prefix.length()) : _prefix;
    }

    public String addPrefix(String _prefix) {
        return !_prefix.startsWith(this._prefix) ? this._prefix + _prefix : _prefix;
    }

    private static String[] getValues() {
        BinEncoding[] tempEnumItemArray = values();
        String[] tempEnumNameArray = new String[tempEnumItemArray.length];

        for(int i = 0; i < tempEnumItemArray.length; ++i) {
            tempEnumNameArray[i] = tempEnumItemArray[i].value();
        }

        return tempEnumNameArray;
    }

    public static BinEncoding get(String _enumName) {
        BinEncoding[] tempEnumItemArray = values();
        for(int i = 0; i < tempEnumItemArray.length; ++i) {
            BinEncoding tempBinEncoding = tempEnumItemArray[i];
            if (tempBinEncoding._value.equals(_enumName)) {
                return tempBinEncoding;
            }
        }

        throw new IllegalArgumentException("Unknown encoding: '" + _enumName + "', one of the following values expected: " + Arrays.toString(getValues()));
    }

    public static BinEncoding which(String _enumName, BinEncoding _defaultEncoding) {
        BinEncoding[] tempEnumItemArray = values();

        for(int i = 0; i < tempEnumItemArray.length; ++i) {
            BinEncoding tempBinEncoding = tempEnumItemArray[i];
            if (_enumName.startsWith(tempBinEncoding._prefix)) {
                return tempBinEncoding;
            }
        }

        return _defaultEncoding;
    }

    private static class BinaryCodec implements BinCodec {
        private BinaryCodec() {
        }
        @Override
        public String encode(byte[] _byteArray) {
            return com.twinkle.framework.core.datastruct.codec.Binary.encode(_byteArray);
        }

        @Override
        public byte[] decode(String _str) {
            return com.twinkle.framework.core.datastruct.codec.Binary.decode(_str);
        }
    }

    private static class HexCodec implements BinCodec {
        private HexCodec() {
        }

        @Override
        public String encode(byte[] _byteArray) {
            return com.twinkle.framework.core.datastruct.codec.Hex.encode(_byteArray);
        }

        @Override
        public byte[] decode(String _str) {
            return com.twinkle.framework.core.datastruct.codec.Hex.decode(_str);
        }
    }

    private static class Base64Codec implements BinCodec {
        private Base64Codec() {
        }

        @Override
        public String encode(byte[] _byteArray) {
            return com.twinkle.framework.core.datastruct.codec.Base64.encode(_byteArray);
        }

        @Override
        public byte[] decode(String _str) {
            return com.twinkle.framework.core.datastruct.codec.Base64.decode(_str);
        }
    }
}

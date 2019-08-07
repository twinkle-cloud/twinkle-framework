package com.twinkle.framework.core.utils;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-05 15:35<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class AttributeUtil {
    protected static final String FLAG_SUFFIX = "Flag";
    protected static final String DEFAULT_SUFFIX = "Default";

    public static String toBeanNormalForm(String _attrName) {
        return _attrName.length() > 0 ? _attrName.substring(0, 1).toUpperCase() + _attrName.substring(1) : _attrName;
    }

    public static String getGetterName(String _attrName) {
        return "get" + toBeanNormalForm(_attrName);
    }

    public static String getSetterName(String _attrName) {
        return "set" + toBeanNormalForm(_attrName);
    }

    public static String getConstantName(String _attrName) {
        return _attrName.toUpperCase();
    }

    public static String getFieldName(String _attrName) {
        return _attrName;
    }

    public static String getFlagGetterName(String _attrName) {
        return getGetterName(_attrName) + "Flag";
    }

    public static String getFlagSetterName(String _attrName) {
        return getSetterName(_attrName) + "Flag";
    }

    public static String getFlagFieldName(String _attrName) {
        return AttributeUtil.getFieldName(_attrName) + FLAG_SUFFIX;
    }
}

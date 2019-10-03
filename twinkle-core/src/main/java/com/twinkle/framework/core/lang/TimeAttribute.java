package com.twinkle.framework.core.lang;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-19 17:40<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class TimeAttribute extends IntegerAttribute {
    private static int type = 108;
    private static SimpleDateFormat outDateFormat_ = new SimpleDateFormat();
    private static Date cachedDate_ = new Date();
    private static Object mutex = new Object();
    private transient TimeAttribute.TimeAttributeParser inDateFormat_;
    public static final String DEFAULT_TIME_FORMAT = new String("MM/dd/yyyy HH:mm:ss z");

    public TimeAttribute() {
        this.value = 0;
    }

    public TimeAttribute(int _value) {
        this.value = _value;
    }

    public TimeAttribute(long _milliSeconds) {
        this.value = (int)(_milliSeconds / 1000L);
    }

    public TimeAttribute(String _value) {
        this.setValue(_value);
    }

    @Override
    public final int getPrimitiveType() {
        return INTEGER_TYPE;
    }

    @Override
    public final int getType() {
        return type;
    }

    @Override
    public final void setType(int _type) {
        type = _type;
    }

    public static int getTypeID() {
        return type;
    }

    public final long getMilliseconds() {
        return (long)this.value * 1000L;
    }

    @Override
    public void setValue(int _value) {
        this.value = _value;
    }

    public final void setValue(long _milliSeconds) {
        this.value = (int)(_milliSeconds / 1000L);
    }

    @Override
    public void setValue(String _value) {
        if (this.inDateFormat_ == null) {
            this.inDateFormat_ = this.createTimeAttributeParser();
        }

        this.value = this.inDateFormat_.parseTimeString(_value);
    }

    @Override
    public void setValue(Attribute _attr) {
        if (this != _attr) {
            if (_attr.getPrimitiveType() == STRING_TYPE) {
                this.setValue(_attr.toString());
            } else {
                this.value = ((IIntegerAttribute)_attr).getInt();
            }
        }
    }

    public final void setTimeFormat(String _formatStr) {
        this.inDateFormat_ = this.createTimeAttributeParser(_formatStr, null);
    }

    public final void setTimeFormat(String _formatStr, String _timeZone) {
        this.inDateFormat_ = this.createTimeAttributeParser(_formatStr, _timeZone);
    }

    /**
     * Gets the <code>TimeZone</code> for the given ID.
     *
     * @param _timeZoneId the ID for a <code>TimeZone</code>, either an abbreviation
     * such as "PST", a full name such as "America/Los_Angeles", or a custom
     * ID such as "GMT-8:00". Note that the support of abbreviations is
     * for JDK 1.1.x compatibility only and full names should be used.
     *
     * @param _timeZoneId
     */
    public final void setTimeZone(String _timeZoneId) {
        outDateFormat_.setTimeZone(TimeZone.getTimeZone(_timeZoneId));
    }

    @Override
    public boolean equals(Object _obj) {
        if (_obj != null && _obj instanceof TimeAttribute) {
            return this.value == ((TimeAttribute)_obj).getInt();
        } else {
            return false;
        }
    }

    @Override
    public void aggregate(Operation _operation, Attribute _attr) {
        switch(_operation) {
            case MIN:
                this.min(_attr);
                break;
            case MAX:
                this.max(_attr);
                break;
            case SET:
                this.setValue(_attr);
        }
    }

    @Override
    public String toString() {
        return this.formatTime(DEFAULT_TIME_FORMAT);
    }

    @Override
    public Object clone() {
        return super.clone();
    }

    public String formatTime(String _pattern) {
        return formatTime(this.getMilliseconds(), _pattern);
    }

    public static String formatTime(long _milliSeconds) {
        return formatTime(_milliSeconds, DEFAULT_TIME_FORMAT);
    }

    public static synchronized String formatTime(long _milliSeconds, String _pattern) {
        if (_pattern.compareTo("U") == 0) {
            return Integer.toString((int)(_milliSeconds / 1000L));
        } else if (_pattern.compareTo("U.M") == 0) {
            return Integer.toString((int)(_milliSeconds / 1000L));
        } else {
            outDateFormat_.applyPattern(_pattern);
            cachedDate_.setTime(_milliSeconds);
            return outDateFormat_.format(cachedDate_);
        }
    }

    private TimeAttribute.TimeAttributeParser createTimeAttributeParser() {
        synchronized(mutex) {
            return new TimeAttribute.TimeAttributeParser();
        }
    }

    private TimeAttribute.TimeAttributeParser createTimeAttributeParser(String _formatStr, String _timeZone) {
        synchronized(mutex) {
            return new TimeAttribute.TimeAttributeParser(_formatStr, _timeZone);
        }
    }

    @Override
    public Object getObjectValue() {
        return this.value;
    }

    class TimeAttributeParser {
        private SimpleDateFormat inDateFormat;
        private String dateFormat;
        private String timeZone;
        private boolean isUTC;
        private boolean isUM;
        private static final String STRICT_STRING = "strict:";

        TimeAttributeParser() {
            this("U", (String)null);
        }

        /**
         * Initialize the time attr parser.
         *
         * @param _formatStr
         * @param _timeZone
         */
        TimeAttributeParser(String _formatStr, String _timeZone) {
            this.inDateFormat = null;
            this.timeZone = null;
            this.isUTC = true;
            this.isUM = false;
            boolean var4 = true;
            if (_formatStr != null && _formatStr.startsWith("strict:")) {
                var4 = false;
                this.dateFormat = _formatStr.substring("strict:".length());
            } else {
                this.dateFormat = _formatStr;
            }

            if (_formatStr != null && !_formatStr.equals("U") && !_formatStr.equals("")) {
                if (_formatStr.equals("U.M")) {
                    this.isUTC = false;
                    this.isUM = true;
                } else {
                    this.isUTC = false;
                    this.isUM = false;
                    this.inDateFormat = new SimpleDateFormat(this.dateFormat);
                    this.inDateFormat.setLenient(var4);
                    if (_timeZone != null) {
                        this.inDateFormat.setTimeZone(TimeZone.getTimeZone(_timeZone));
                    }
                }
            } else {
                this.isUTC = true;
                this.isUM = false;
            }

        }

        /**
         * Parse the time.
         *
         * @param _timeValue
         * @return
         * @throws NumberFormatException
         */
        int parseTimeString(String _timeValue) throws NumberFormatException {
            int tempValue;
            if (this.isUTC) {
                tempValue = Integer.parseInt(_timeValue);
            } else if (this.isUM) {
                //Must be sec.millisec.
                int tempDotIndex = _timeValue.indexOf(".", 0);
                if (tempDotIndex <= 0) {
                    throw new NumberFormatException("Timestamp not of the form sec.millisec");
                }
                tempValue = Integer.parseInt(_timeValue.substring(0, tempDotIndex));
            } else {
                try {
                    tempValue = (int)(this.inDateFormat.parse(_timeValue).getTime() / 1000L);
                } catch (ParseException e) {
                    throw new NumberFormatException("Couldn't parse the date " + _timeValue + " with format string: " + this.dateFormat);
                }
            }

            return tempValue;
        }
    }
}

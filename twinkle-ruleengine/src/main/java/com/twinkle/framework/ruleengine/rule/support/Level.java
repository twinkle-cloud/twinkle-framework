package com.twinkle.framework.ruleengine.rule.support;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/7/19 11:49 AM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class Level extends java.util.logging.Level {
    private static Map<Integer, Level> DEFAULT_LEVELS = new ConcurrentHashMap<>();
    public static final Level CRITICAL = new Level("CRITICAL", 1000);
    public static final Level ACCOUNTING = new Level("ACCOUNTING", 975);
    public static final Level ERROR = new Level("ERROR", 950);
    public static final Level WARNING = new Level("WARNING", 900);
    public static final Level INFO = new Level("INFO", 800);
    public static final Level DEBUG = new Level("DEBUG", 500);
    public static final Level DEBUG2 = new Level("DEBUG2", 400);
    public static final Level DEBUG3 = new Level("DEBUG3", 300);
    public static final Level DEBUG4 = new Level("DEBUG4", 200);

    protected Level(String _name, int _value) {
        super(_name, _value);
        DEFAULT_LEVELS.put(new Integer(_value), this);
    }

    public static java.util.logging.Level parse(String _name) {
        Iterator<Map.Entry<Integer, Level>> tempItr = DEFAULT_LEVELS.entrySet().iterator();
        Level tempLevel;
        do {
            if (!tempItr.hasNext()) {
                try {
                    Integer tempValue = new Integer(_name);
                    if (DEFAULT_LEVELS.containsKey(tempValue)) {
                        return DEFAULT_LEVELS.get(tempValue);
                    }
                } catch (NumberFormatException e) {
                }

                return java.util.logging.Level.parse(_name);
            }

            tempLevel = tempItr.next().getValue();
        } while(!_name.equalsIgnoreCase(tempLevel.toString()));

        return tempLevel;
    }
}

package com.twinkle.framework.ruleengine.utils;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-18 17:13<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class TreeMarker {
    public static final String TREE_MARKER = "tree.";
    private static final int TREE_MARKER_LENGTH = "tree.".length();

    public TreeMarker() {
    }

    public static boolean isTreeAttribute(String var0) {
        return var0.startsWith("tree.");
    }

    public static String extractAttributeName(String var0) {
        return var0.substring(TREE_MARKER_LENGTH);
    }

}

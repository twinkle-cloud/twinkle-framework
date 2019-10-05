package com.twinkle.framework.struct.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/4/19 5:29 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class TestMain {
    public static void main(String[] _args) {
        String tempStr = "TestDemo:request[][]";

        String tempStr1 = StringUtils.substringBefore(tempStr, "[]");
        String[] tempStr2 = StringUtils.splitByWholeSeparatorPreserveAllTokens(tempStr, "[]");
        System.out.println("The size is:" + tempStr2.length);
        for(int i = 0; i < tempStr2.length; i++) {
            System.out.println("The ["+i+"] is:" + tempStr2[i]);
        }
    }
}

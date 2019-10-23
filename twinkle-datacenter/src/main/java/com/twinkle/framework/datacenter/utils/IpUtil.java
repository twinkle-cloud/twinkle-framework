package com.twinkle.framework.datacenter.utils;

/**
 * Function: Copy From https://github.com/johnhuang-cn/snowflake-uid. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/16/19 10:01 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class IpUtil {
    public static String longToIpV4(long longIp) {
        int octet3 = (int) ((longIp >> 24) % 256);
        int octet2 = (int) ((longIp >> 16) % 256);
        int octet1 = (int) ((longIp >> 8) % 256);
        int octet0 = (int) ((longIp) % 256);
        return octet3 + "." + octet2 + "." + octet1 + "." + octet0;
    }

    public static long ipV4ToLong(String ip) {
        String[] octets = ip.split("\\.");
        return (Long.parseLong(octets[0]) << 24) + (Integer.parseInt(octets[1]) << 16)
                + (Integer.parseInt(octets[2]) << 8) + Integer.parseInt(octets[3]);
    }
}

package com.twinkle.framework.datasource;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/15/19 10:37 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface ExceptionCode {
    /**
     * BASE INFO ERROR.
     *
     * DATA SOURCE related.
     */
    int DATASOURCE_BASE_INITIALIZE_FAILED = 0x000101;
    /**
     * DRUID DATASOURCE initialized failed.
     */
    int DATASOURCE_DRUID_INITIALIZE_FAILED = 0x000102;
    /**
     * The required primary data source missed.
     */
    int DATASOURCE_PRIMARY_MISSING = 0x000103;
    /**
     * The data source is not initialized properly.
     */
    int DATASOURCE_NOT_INITIALIZED = 0x000104;
}

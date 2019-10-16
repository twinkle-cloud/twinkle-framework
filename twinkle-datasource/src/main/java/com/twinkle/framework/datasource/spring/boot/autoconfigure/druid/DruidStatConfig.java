package com.twinkle.framework.datasource.spring.boot.autoconfigure.druid;

import lombok.Data;

/**
 * Function: Druid Stat Monitor configuration. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/12/19 6:31 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Data
public class DruidStatConfig {
    private Long slowSqlMillis;

    private Boolean logSlowSql;

    private Boolean mergeSql;
}
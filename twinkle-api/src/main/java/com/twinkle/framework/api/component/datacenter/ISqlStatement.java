package com.twinkle.framework.api.component.datacenter;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/19/19 4:57 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface ISqlStatement extends IStatement {
    /**
     * SQL statement.
     *
     * @return
     */
    @Override
    default int getType() {
        return TYPE_SQL;
    }

    /**
     * Get the Data source name.
     *
     * @return
     */
    default String getDataSourceName(){
        return "master";
    }
}

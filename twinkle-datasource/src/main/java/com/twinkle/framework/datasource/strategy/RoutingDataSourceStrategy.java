package com.twinkle.framework.datasource.strategy;

import javax.sql.DataSource;
import java.util.List;

/**
 * Function: The interface of dynamic datasource switch strategy. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/12/19 6:31 PM<br/>
 *
 * @author chenxj
 * @see RandomRoutingDataSourceStrategy
 * @see LoadBalanceRoutingDataSourceStrategy
 * @since JDK 1.8
 */
public interface RoutingDataSourceStrategy {

    /**
     * determine a database from the given dataSources
     *
     * @param _dataSourceList given dataSources
     * @return final dataSource
     */
    DataSource determineDataSource(List<DataSource> _dataSourceList);
}

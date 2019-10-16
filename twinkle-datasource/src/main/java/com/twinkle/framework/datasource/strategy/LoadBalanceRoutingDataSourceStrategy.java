package com.twinkle.framework.datasource.strategy;

import javax.sql.DataSource;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Function: LoadBalance strategy to switch a database. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/12/19 6:31 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class LoadBalanceRoutingDataSourceStrategy implements RoutingDataSourceStrategy {

    /**
     * The count for LB algorithm.
     */
    private AtomicInteger index = new AtomicInteger(0);

    @Override
    public DataSource determineDataSource(List<DataSource> _dataSourceList) {
        return _dataSourceList.get(Math.abs(index.getAndAdd(1) % _dataSourceList.size()));
    }
}

package com.twinkle.framework.datasource.strategy;

import javax.sql.DataSource;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Function: Random strategy to switch a database. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/12/19 6:31 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class RandomRoutingDataSourceStrategy implements RoutingDataSourceStrategy {
    @Override
    public DataSource determineDataSource(List<DataSource> _dataSourceList) {
        return _dataSourceList.get(ThreadLocalRandom.current().nextInt(_dataSourceList.size()));
    }
}

package com.twinkle.framework.datasource;

import com.twinkle.framework.datasource.strategy.RoutingDataSourceStrategy;
import lombok.Data;

import javax.sql.DataSource;
import java.util.LinkedList;
import java.util.List;

/**
 * Function: Data Source Group. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/12/19 6:31 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Data
public class DynamicGroupDataSource {
    private String groupName;

    private RoutingDataSourceStrategy routingDataSourceStrategy;

    private List<DataSource> dataSources = new LinkedList<>();

    public DynamicGroupDataSource(String _groupName, RoutingDataSourceStrategy _strategy) {
        this.groupName = _groupName;
        this.routingDataSourceStrategy = _strategy;
    }

    public void addDatasource(DataSource _dataSource) {
        this.dataSources.add(_dataSource);
    }

    public void removeDatasource(DataSource _dataSource) {
        this.dataSources.remove(_dataSource);
    }

    public DataSource determineDataSource() {
        return this.routingDataSourceStrategy.determineDataSource(this.dataSources);
    }

    public int size() {
        return this.dataSources.size();
    }
}

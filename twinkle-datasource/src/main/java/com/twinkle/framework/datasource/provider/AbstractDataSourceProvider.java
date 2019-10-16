package com.twinkle.framework.datasource.provider;

import com.twinkle.framework.datasource.DynamicDataSourceCreator;
import com.twinkle.framework.datasource.spring.boot.autoconfigure.DataSourceProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/12/19 6:31 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public abstract class AbstractDataSourceProvider implements DynamicDataSourceProvider {
    @Autowired
    private DynamicDataSourceCreator dynamicDataSourceCreator;

    protected Map<String, DataSource> createDataSourceMap(
            Map<String, DataSourceProperty> _propertyMap) {
        Map<String, DataSource> tempDataSourceMap = new HashMap<>(_propertyMap.size() * 2);
        for (Map.Entry<String, DataSourceProperty> item : _propertyMap.entrySet()) {
            String tempPollName = item.getKey();
            DataSourceProperty tempProperty = item.getValue();
            tempProperty.setPollName(tempPollName);
            tempDataSourceMap.put(tempPollName, dynamicDataSourceCreator.createDataSource(tempProperty));
        }
        return tempDataSourceMap;
    }
}

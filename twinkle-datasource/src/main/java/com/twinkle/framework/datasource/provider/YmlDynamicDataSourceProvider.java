package com.twinkle.framework.datasource.provider;

import com.twinkle.framework.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.twinkle.framework.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.util.Map;

/**
 * Function: Yaml source. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/12/19 6:31 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class YmlDynamicDataSourceProvider extends AbstractDataSourceProvider implements DynamicDataSourceProvider {
    /**
     * Data Source properties.
     */
    private DynamicDataSourceProperties properties;

    public YmlDynamicDataSourceProvider(DynamicDataSourceProperties _properties) {
        this.properties = _properties;
    }

    @Override
    public Map<String, DataSource> loadDataSources() {
        Map<String, DataSourceProperty> tempDataSourcePropertyMap = this.properties.getDatasource();
        return createDataSourceMap(tempDataSourcePropertyMap);
    }
}

package com.twinkle.framework.datasource.provider;

import javax.sql.DataSource;
import java.util.Map;

/**
 * Function: DataSource providers, From Yaml or other places. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/12/19 6:31 PM<br/>
 *
 * @author chenxj
 * @see YmlDynamicDataSourceProvider
 * @see AbstractJdbcDataSourceProvider
 * @since JDK 1.8
 */
public interface DynamicDataSourceProvider {

  /**
   * Load the data sources.
   *
   * @return
   */
  Map<String, DataSource> loadDataSources();
}

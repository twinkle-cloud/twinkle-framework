package com.twinkle.framework.datacenter.executor;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.api.exception.DataCenterException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/19/19 4:30 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class DefaultSqlStatementExecutor extends AbstractConfigurableStatementExecutor {

    /**
     * Data Source name.
     */
    @Getter
    private String dataSourceName;

    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {
        super.configure(_conf);
        this.dataSourceName = _conf.getString("DataSource");
        if (StringUtils.isBlank(this.dataSourceName)) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_REQUIRED_ATTR_MISSED, "The DataSource is mandatory for SQL Statement Component.");
        }
    }
    @Override
    public void execute(NormalizedContext _context) throws DataCenterException {
        log.debug("Going to execute the SQL statement in DataSource [{}].", this.dataSourceName);
        DynamicDataSourceContextHolder.push(this.dataSourceName);
        try{
            super.execute(_context);
        } finally {
            DynamicDataSourceContextHolder.poll();
        }
    }
}

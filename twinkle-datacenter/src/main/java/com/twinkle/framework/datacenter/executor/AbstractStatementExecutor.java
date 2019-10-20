package com.twinkle.framework.datacenter.executor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.twinkle.framework.api.component.AbstractComponent;
import com.twinkle.framework.api.component.IComponentFactory;
import com.twinkle.framework.api.component.datacenter.ISqlStatement;
import com.twinkle.framework.api.component.datacenter.IStatementExecutor;
import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.api.exception.DataCenterException;
import com.twinkle.framework.api.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/19/19 4:31 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public abstract class AbstractStatementExecutor extends AbstractComponent implements IStatementExecutor {
    /**
     * The Sql statement list.
     */
    protected List<String> sqlStatementList;
    /**
     * The statement name - bean name mapping.
     */
    protected Map<String, String> statementBeanMap;

    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {
        JSONArray tempStatementItems = _conf.getJSONArray("StatementNames");
        if (CollectionUtils.isEmpty(tempStatementItems)) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_REQUIRED_ATTR_MISSED, "The Statements should not be empty for executor.");
        }
        this.sqlStatementList = new ArrayList<>(tempStatementItems.size());
        this.statementBeanMap = new HashMap<>(tempStatementItems.size());
        for (int i = 0; i < tempStatementItems.size(); i++) {
            this.sqlStatementList.add(tempStatementItems.getString(i));
        }
    }

    @Override
    public void checkStatement(List<IComponentFactory.ComponentNamePair> _definedStatementList) {
        if (CollectionUtils.isEmpty(_definedStatementList)) {
            throw new ConfigurationException(ExceptionCode.DATACENTER_NO_STATEMENT_FOUND, "No statements founds for executor.");
        }
        for (String tempItem : this.sqlStatementList) {
            for(IComponentFactory.ComponentNamePair tempPair : _definedStatementList){
                if(tempPair.getName().equals(tempItem)) {
                    String tempBeanName = DigestUtils.md5DigestAsHex(tempPair.getFullPathName().getBytes());
                    this.statementBeanMap.put(tempItem, tempBeanName);
                    log.debug("Add check passed statement [{}] -> [{}].", tempItem, tempBeanName);
                }
            }
        }
    }

    @Override
    public void execute(NormalizedContext _context, String _dataSourceName) throws DataCenterException {
        log.debug("Going to execute the SQL statement.");
        for (Map.Entry<String, String> entryItem : this.statementBeanMap.entrySet()) {
            log.debug("Going to execute the [{}] statement.", entryItem.getKey());
            ISqlStatement tempStatement = SpringUtil.getBean(entryItem.getValue(), ISqlStatement.class);
            tempStatement.execute(_context, tempStatement.getDataSourceName());
        }
    }
}

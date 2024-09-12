package com.twinkle.framework.datacenter.executor;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.twinkle.framework.api.component.AbstractConfigurableComponent;
import com.twinkle.framework.api.component.datacenter.ISqlStatement;
import com.twinkle.framework.api.component.datacenter.IStatement;
import com.twinkle.framework.api.component.datacenter.IStatementExecutor;
import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.api.exception.DataCenterException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

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
public abstract class AbstractConfigurableStatementExecutor extends AbstractConfigurableComponent implements IStatementExecutor {
    /**
     * The Sql statement list.
     */
    protected List<String> statementNameList;
    /**
     * The statement name - bean name mapping.
     */
    protected List<IStatement> statementList;

    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {
        super.configure(_conf);
        JSONArray tempStatementItems = _conf.getJSONArray("StatementNames");
        if (CollectionUtils.isEmpty(tempStatementItems)) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_REQUIRED_ATTR_MISSED, "The Statements should not be empty for executor.");
        }
        this.statementNameList = tempStatementItems.toList(String.class);
        this.statementList = new ArrayList<>(this.statementNameList.size());
    }

    @Override
    public void filterStatement(List<IStatement> _definedStatementList) {
        if (CollectionUtils.isEmpty(_definedStatementList)) {
            throw new ConfigurationException(ExceptionCode.DATACENTER_NO_STATEMENT_FOUND, "No statements founds for executor.");
        }
        for (String tempItem : this.statementNameList) {
            for (IStatement tempStatement : _definedStatementList) {
                if (tempStatement.getName().equals(tempItem)) {
                    this.statementList.add(tempStatement);
                    log.debug("Add the statement [{}] into current executor[{}].", tempItem, this.getName());
                }
            }
        }
    }

    @Override
    public void execute(NormalizedContext _context) throws DataCenterException {
        for (IStatement tempStatement : this.statementList) {
            log.debug("Going to execute the [{}] statement.", tempStatement.getName());
            if (tempStatement instanceof ISqlStatement) {
                String tempDsName = ((ISqlStatement) tempStatement).getDataSourceName();
                tempStatement.execute(_context, tempDsName);
            }
        }
    }
}

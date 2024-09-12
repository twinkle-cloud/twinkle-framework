package com.twinkle.framework.datacenter;

import com.alibaba.fastjson2.JSONObject;
import com.twinkle.framework.api.component.AbstractConfigurableComponent;
import com.twinkle.framework.api.component.IComponentFactory;
import com.twinkle.framework.api.component.datacenter.IDataCenterManager;
import com.twinkle.framework.api.component.datacenter.IStatement;
import com.twinkle.framework.api.component.datacenter.IStatementExecutor;
import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.api.exception.RuleException;
import com.twinkle.framework.configure.component.ListComponentLoader;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
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
public class DataCenterManager extends AbstractConfigurableComponent implements IDataCenterManager {
    /**
     * The statement List.
     */
    private Map<Integer, List<IComponentFactory.ComponentNamePair>> statementListMap;
    /**
     * The executor List.
     */
    private List<IStatementExecutor> executorList;

    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {
        super.configure(_conf);
        ListComponentLoader<IStatement> tempLoader = new ListComponentLoader<>();
        List<IStatement> tempList = tempLoader.loadListComponent("StatementNames", "Statements", this.getComponentName("Statements"), _conf);
        if (tempList.isEmpty()) {
            return;
        }
        ListComponentLoader<IStatementExecutor> tempExecutorLoader = new ListComponentLoader<>();
        this.executorList = tempExecutorLoader.loadListComponent("ExecutorNames", "Executors", this.getComponentName("Executors"), _conf);
        if (this.executorList.isEmpty()) {
            return;
        }
        for (IStatementExecutor tempExecutor : this.executorList) {
            tempExecutor.filterStatement(tempList);
        }
    }

    @Override
    public IStatementExecutor getStatementExecutor(String _executorName) {
        for (IStatementExecutor tempExecutor : this.executorList) {
            if (tempExecutor.getName().equals(_executorName)) {
                log.debug("Get the executor [{}] instance.", _executorName);
                return tempExecutor;
            }
        }
        throw new RuleException(ExceptionCode.DATACENTER_EXECUTOR_NOT_FOUND, "The Executor [" + _executorName + "] is not found.");
    }

    @Override
    public IStatementExecutor getStatementExecutor(int _executorIndex) {
        if (_executorIndex < 0 || _executorIndex >= this.executorList.size()) {
            throw new RuleException(ExceptionCode.DATACENTER_EXECUTOR_NOT_FOUND, "The executor index[" + _executorIndex + "] is out of bounds.");
        }
        return this.executorList.get(_executorIndex);
    }

    @Override
    public int getStatementExecutorIndexByName(String _executorName) {
        for (int i = 0; i < this.executorList.size(); i++) {
            IStatementExecutor tempExecutor = this.executorList.get(i);
            if (tempExecutor.getName().equals(_executorName)) {
                return i;
            }
        }
        throw new ConfigurationException(ExceptionCode.DATACENTER_EXECUTOR_NOT_FOUND, "The Executor [" + _executorName + "] is not found.");
    }
}

package com.twinkle.framework.datacenter;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.twinkle.framework.api.component.AbstractComponent;
import com.twinkle.framework.api.component.datacenter.IDataCenterManager;
import com.twinkle.framework.api.component.datacenter.IStatement;
import com.twinkle.framework.api.component.datacenter.IStatementExecutor;
import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.api.exception.RuleException;
import com.twinkle.framework.api.utils.SpringUtil;
import com.twinkle.framework.configure.component.ComponentFactory;
import com.twinkle.framework.api.component.IComponentFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.HashMap;
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
public class DataCenterManager extends AbstractComponent implements IDataCenterManager {
    /**
     * The statement List.
     */
    private Map<Integer, List<IComponentFactory.ComponentNamePair>> statementListMap;
    /**
     * The executor List.
     */
    private Map<String, String> executorMap;

    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {
        JSONArray tempArray = _conf.getJSONArray("StatementNames");
        JSONArray tempBeanArray = _conf.getJSONArray("Statements");
        if (tempArray.isEmpty() || tempBeanArray.isEmpty()) {
            return;
        }
        this.statementListMap = new HashMap<>(6);
        for (int i = 0; i < tempArray.size(); i++) {
            String tempItem = tempArray.getString(i);
            for (int j = 0; j < tempBeanArray.size(); j++) {
                JSONObject tempObj = tempBeanArray.getJSONObject(j);
                if (tempObj.getString("Name").equals(tempItem)) {
                    IStatement tempStatement = ComponentFactory.getInstance().loadComponent(this.getComponentName(this.getComponentName(tempItem)), tempObj);
                    List<IComponentFactory.ComponentNamePair> tempStatementList = this.statementListMap.get(tempStatement.getType());
                    if (tempStatementList == null) {
                        tempStatementList = new ArrayList<>(tempArray.size());
                    }

                    tempStatementList.add(new IComponentFactory.ComponentNamePair(tempItem, tempStatement.getFullPathName()));
                    this.statementListMap.put(tempStatement.getType(), tempStatementList);
                    break;
                }
            }
        }
        tempArray = _conf.getJSONArray("ExecutorNames");
        tempBeanArray = _conf.getJSONArray("Executors");
        if (tempArray.isEmpty() || tempBeanArray.isEmpty()) {
            return;
        }
        this.executorMap = new HashMap<>(tempArray.size());
        for (int i = 0; i < tempArray.size(); i++) {
            String tempItem = tempArray.getString(i);
            for (int j = 0; j < tempBeanArray.size(); j++) {
                JSONObject tempObj = tempBeanArray.getJSONObject(j);
                if (tempObj.getString("Name").equals(tempItem)) {
                    String tempBeanName = this.getComponentName(tempItem);
                    IStatementExecutor tempExecutor = ComponentFactory.getInstance().loadComponent(tempBeanName, tempObj);
                    for(Map.Entry<Integer, List<IComponentFactory.ComponentNamePair>> tempStateMap : this.statementListMap.entrySet()) {
                        if(CollectionUtils.isEmpty(tempStateMap.getValue())){
                            continue;
                        }
                        tempExecutor.checkStatement(tempStateMap.getValue());
                    }

                    this.executorMap.put(tempItem, DigestUtils.md5DigestAsHex(tempBeanName.getBytes()));
                    break;
                }
            }
        }
    }

    @Override
    public IStatementExecutor getStatementExecutor(String _executorName) {
        String tempBeanName = DigestUtils.md5DigestAsHex(this.getComponentName(_executorName).getBytes());
        IStatementExecutor tempExecutor = SpringUtil.getBean(tempBeanName, IStatementExecutor.class);
        if(tempExecutor == null) {
            throw new RuleException(ExceptionCode.DATACENTER_EXECUTOR_NOT_FOUND, "The Executor ["+ _executorName +"] is not found.");
        }
        log.debug("Get the executor [{}]'s bean [{}].", _executorName, tempBeanName);
        return tempExecutor;
    }

}

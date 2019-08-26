package com.twinkle.framework.ruleengine;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.twinkle.framework.api.component.AbstractComponent;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.api.component.rule.IRuleChain;
import com.twinkle.framework.api.component.rule.IRuleChainManager;
import com.twinkle.framework.configure.component.ComponentFactory;
import com.twinkle.framework.configure.component.IComponentFactory;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenxj
 */
public class RuleChainManager extends AbstractComponent implements IRuleChainManager {
    private List<String> ruleChainNameList;
    private Map<String, IRuleChain> ruleChainMap;

    public RuleChainManager(){
    }
    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {
        JSONArray tempArray = _conf.getJSONArray("RuleChainNames");
        JSONArray tempRuleChainArray = _conf.getJSONArray("RuleChains");
        if(tempArray.isEmpty() || tempRuleChainArray.isEmpty()) {
            return;
        }
        this.ruleChainNameList = new ArrayList<>(tempArray.size());
        this.ruleChainMap = new HashMap<>(tempArray.size());
        for(int i =0; i< tempArray.size(); i++) {
            String tempItem = tempArray.getString(i);
            for(int j = 0; j < tempRuleChainArray.size(); j++) {
                JSONObject tempObj = tempRuleChainArray.getJSONObject(j);
                if(tempObj.getString("Name").equals(tempItem)) {
                    StringBuilder tempBuilder = new StringBuilder(this.getFullPathName());
                    tempBuilder.append((char)92);
                    tempBuilder.append(tempItem);

                    IRuleChain tempRuleChain = ComponentFactory.getInstance().loadComponent(tempBuilder.toString(), tempObj);

                    this.ruleChainNameList.add(tempItem);
                    this.ruleChainMap.put(tempItem, tempRuleChain);
                    break;
                }
            }
        }
    }

    @Override
    public IRuleChain getRuleChain(String _ruleChainName) {
        if(MapUtils.isEmpty(this.ruleChainMap)) {
            return null;
        }
        return this.ruleChainMap.get(_ruleChainName);
    }

    @Override
    public void addRuleChain(String _ruleChainName, IRuleChain _ruleChain) {
        if(this.ruleChainMap == null) {
            this.ruleChainMap = new HashMap<>(4);
        }
        this.ruleChainMap.put(_ruleChainName, _ruleChain);
    }
}

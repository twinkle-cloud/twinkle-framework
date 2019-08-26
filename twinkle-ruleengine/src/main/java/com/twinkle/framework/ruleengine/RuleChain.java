package com.twinkle.framework.ruleengine;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.twinkle.framework.api.component.AbstractComponent;
import com.twinkle.framework.api.component.rule.IRule;
import com.twinkle.framework.api.component.rule.IRuleChain;
import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.api.exception.RuleException;
import com.twinkle.framework.configure.component.ComponentFactory;
import com.twinkle.framework.core.context.model.NormalizedContext;
import com.twinkle.framework.ruleengine.rule.LastRule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author chenxj
 */
@Slf4j
public class RuleChain extends AbstractComponent implements IRuleChain {
    private List<IRule> ruleList;
    private List<IRule> errorRuleList;
    private boolean continueAfterError = true;
    private boolean required = true;
    /**
     * The first rule in the error rule chain.
     */
    private IRule errorFirstRule;
    /**
     * First rule in the rule chain.
     */
    private IRule firstRule;
    /**
     * Last business rule in the rule chain.
     */
    private IRule tailRule;
    /**
     * The backup last rule.
     */
    private LastRule lastRule;
    /**
     * The backup error last rule.
     */
    private LastRule errorLastRule;

    public RuleChain() {
        this.lastRule = new LastRule();
        this.errorLastRule = new LastRule();
    }

    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {
        this.configureChain(_conf, "RuleNames");
        this.configureErrorChain(_conf, "ErrorRuleNames");
        this.continueAfterError = _conf.getBooleanValue("ContinueAfterError");

    }

    public void configureChain(JSONObject _conf, String _keyName) throws ConfigurationException {
        this.ruleList = this.configureChainItem(_conf, _keyName);
        if (this.required && this.ruleList.isEmpty()) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_REQUIRED_ATTR_MISSED, "The attribute " + _keyName + " in must specify at least one rule name");
        } else {
            this.addRuleToChain(this.lastRule, this.ruleList);
            this.firstRule = this.ruleList.get(0);
        }
    }

    public void configureErrorChain(JSONObject _conf, String _keyName) throws ConfigurationException {
        this.errorRuleList = this.configureChainItem(_conf, _keyName);
        if (!this.errorRuleList.isEmpty()) {
            this.errorFirstRule = this.errorRuleList.get(0);
        }

        this.addRuleToChain(this.errorLastRule, this.errorRuleList);
    }

    private List<IRule> configureChainItem(JSONObject _conf, String _keyName) throws ConfigurationException {
        JSONArray tempItems = _conf.getJSONArray(_keyName);
        JSONArray tempRuleItems = _conf.getJSONArray("Rules");
        if (CollectionUtils.isEmpty(tempItems) || CollectionUtils.isEmpty(tempRuleItems)) {
            return new ArrayList<>(1);
        }
        List<IRule> tempResultList = new ArrayList<>(tempItems.size());
        for (int i = 0; i < tempItems.size(); i++) {
            String tempItem = tempItems.getString(i);
            for(int j = 0; j<tempRuleItems.size(); j++) {
                JSONObject tempObj = tempRuleItems.getJSONObject(j);
                if(tempObj.getString("Name").equals(tempItem)) {
                    StringBuilder tempBuilder = new StringBuilder(this.getFullPathName());
                    tempBuilder.append((char) 92);
                    tempBuilder.append(tempItem);
                    IRule tempRule = ComponentFactory.getInstance().loadComponent(tempBuilder.toString(), tempObj);

                    this.addRuleToChain(tempRule, tempResultList);
                    break;
                }
            }
        }
        return tempResultList;
    }

    private void addRuleToChain(IRule _rule, List<IRule> _ruleList) {
        if (!_ruleList.isEmpty()) {
            IRule tempRule = _ruleList.get(_ruleList.size() - 1);
            tempRule.addNextRule(_rule);
        }
        _ruleList.add(_rule);
    }

    @Override
    public void addNextRule(IRule _rule) {
        if (this.firstRule == null) {
            this.addRuleToChain(this.lastRule, this.ruleList);
            this.firstRule = this.ruleList.get(0);
        }

        if (this.tailRule == null) {
            this.lastRule.addNextRule(_rule);
            if (this.continueAfterError) {
                this.errorLastRule.addNextRule(_rule);
            }
        } else {
            this.tailRule.addNextRule(_rule);
        }

        this.tailRule = _rule;
    }

    @Override
    public void applyRule(NormalizedContext _context) {
        log.debug("RuleChain.applyRule()");
        if (this.firstRule != null) {
            try {
                this.lastRule.reset();
                this.firstRule.applyRule(_context);
            } catch (RuleException e) {
                if (e.getCode() == ExceptionCode.RULE_APPLY_OUTSIDE_RULECHAIN) {
                    log.error("Outside current rule chain.");
                    throw e;
                }
                this.errorLastRule.reset();
                if (this.errorFirstRule == null) {
                    throw e;
                }
                this.errorFirstRule.applyRule(_context);
            }
        }
    }
}

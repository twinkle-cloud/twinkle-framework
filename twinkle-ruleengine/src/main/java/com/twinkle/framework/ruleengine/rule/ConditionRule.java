package com.twinkle.framework.ruleengine.rule;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.twinkle.framework.api.component.rule.IRuleChain;
import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.api.exception.RuleException;
import com.twinkle.framework.configure.component.ComponentFactory;
import com.twinkle.framework.ruleengine.RuleChain;
import com.twinkle.framework.ruleengine.rule.condition.AbstractCondition;
import com.twinkle.framework.ruleengine.rule.condition.ConditionCheck;
import lombok.extern.slf4j.Slf4j;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-18 18:48<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class ConditionRule extends AbstractRule {
    protected AbstractCondition condition = null;
    protected IRuleChain trueRuleChain;
    protected IRuleChain falseRuleChain;
    private boolean validateAttr = false;

    public ConditionRule() {
        this.trueRuleChain = new RuleChain();
        this.falseRuleChain = new RuleChain();
    }

    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {
        log.debug("ConditionRule.configure()");
        this.setCondition(_conf);
        this.validateAttr = _conf.getBooleanValue("ValidateAttributes");
        JSONArray tempRuleChain = _conf.getJSONArray("TrueRuleChain");
        if (tempRuleChain == null && _conf.getJSONObject("TrueRule") != null) {
            tempRuleChain = new JSONArray();
            tempRuleChain.add("TrueRule");
            _conf.put("TrueRuleChain", tempRuleChain);
        }

        StringBuilder tempBuilder = new StringBuilder(this.getFullPathName());
        tempBuilder.append((char) 92);
        tempBuilder.append("TrueRuleChain");
        this.trueRuleChain.setFullPathName(tempBuilder.toString());

        this.trueRuleChain.configureChain(_conf, "TrueRuleChain");
        tempRuleChain = _conf.getJSONArray("FalseRuleChain");
        if (tempRuleChain == null && _conf.getJSONObject("FalseRule") != null) {
            tempRuleChain = new JSONArray();
            tempRuleChain.add("FalseRule");
            _conf.put("FalseRuleChain", tempRuleChain);
        }

        tempBuilder = new StringBuilder(this.getFullPathName());
        tempBuilder.append((char) 92);
        tempBuilder.append("FalseRuleChain");
        this.falseRuleChain.setFullPathName(tempBuilder.toString());
        this.falseRuleChain.configureChain(_conf, "FalseRuleChain");

        if (this.trueRuleChain.size() == 0 && this.falseRuleChain.size() == 0) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_REQUIRED_ATTR_MISSED, "The ConditionRule " + this.getFullPathName() + " requires at least one of TrueRuleChain or FalseRuleChain to be defined");
        }
    }

    protected void setCondition(JSONObject _conf) throws ConfigurationException {
        JSONObject tempObj = _conf.getJSONObject("Condition");
        if (tempObj == null || tempObj.isEmpty()) {
            this.condition = new ConditionCheck();
            this.condition.configure(_conf);
        } else {
            StringBuilder tempBuilder = new StringBuilder(this.getFullPathName());
            tempBuilder.append((char) 92);
            tempBuilder.append(tempObj.getString("Name"));

            this.condition = ComponentFactory.getInstance().loadComponent(tempBuilder.toString(), tempObj);
        }
    }

    @Override
    public void applyRule(NormalizedContext _context) throws RuleException {
        log.debug("Going to apply ConditionRule.applyRule().");
        boolean tempResult = false;

        try {
            tempResult = this.condition.check(_context, this.validateAttr);
        } catch (RuntimeException e) {
            throw new RuleException(ExceptionCode.RULE_APPLY_ERROR, "Error in applying the rule " + this.getFullPathName() + " error : " + e.getCause());
        }
        if (tempResult) {
            this.trueRuleChain.applyRule(_context);
        } else {
            this.falseRuleChain.applyRule(_context);
        }
    }
}

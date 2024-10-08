package com.twinkle.framework.ruleengine.rule;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.twinkle.framework.api.component.rule.IRule;
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
public class ConditionRule extends AbstractConfigurableRule {
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
        super.configure(_conf);
        log.debug("ConditionRule.configure()");
        this.setCondition(_conf);
        this.validateAttr = _conf.getBooleanValue("ValidateAttributes");
        JSONArray tempRuleChain = _conf.getJSONArray("TrueRuleChain");
        if (tempRuleChain == null && _conf.getJSONObject("TrueRule") != null) {
            tempRuleChain = new JSONArray();
            tempRuleChain.add("TrueRule");
            _conf.put("TrueRuleChain", tempRuleChain);
        }

        this.trueRuleChain.setParentPath(this.getComponentName("TrueRuleChain"));

        this.trueRuleChain.configureChain(_conf, "TrueRuleChain");
        tempRuleChain = _conf.getJSONArray("FalseRuleChain");
        if (tempRuleChain == null && _conf.getJSONObject("FalseRule") != null) {
            tempRuleChain = new JSONArray();
            tempRuleChain.add("FalseRule");
            _conf.put("FalseRuleChain", tempRuleChain);
        }
        this.falseRuleChain.setParentPath(this.getComponentName("FalseRuleChain"));
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
            this.condition = ComponentFactory.getInstance().loadComponent(this.getFullPathName(), tempObj);
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

    @Override
    public void addNextRule(IRule _rule) {
        this.nextRule = _rule;
        this.trueRuleChain.addNextRule(_rule);
        this.falseRuleChain.addNextRule(_rule);
    }
}

package com.twinkle.framework.ruleengine.pool;

import com.alibaba.fastjson2.JSONObject;
import com.twinkle.framework.api.component.rule.IRuleChain;
import com.twinkle.framework.configure.component.ComponentFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Build the rule chain instance.
 *
 * @author chenxj
 * @date 2024/09/03
 */
@Slf4j
public class RuleChainObjectFactory implements ObjectFactory<IRuleChain> {
    private JSONObject ruleChainConfig;
    private String ruleChainPath;
    private boolean cloneRuleChainFlag;
    private AtomicBoolean tempCloneFlag = new AtomicBoolean(false);
    private IRuleChain tempRuleChain;

    public RuleChainObjectFactory(String _ruleChainPath, JSONObject _ruleChainConfig, boolean _cloneRuleChainFlag) {
        this.ruleChainPath = _ruleChainPath;
        this.ruleChainConfig = _ruleChainConfig;
        this.cloneRuleChainFlag = _cloneRuleChainFlag;
    }

    @Override
    public IRuleChain create() throws Exception {
        if (this.cloneRuleChainFlag) {
            tempRuleChain = ComponentFactory.getInstance().loadPrototypeComponent(this.ruleChainPath, ruleChainConfig);
        } else {
            if(this.tempRuleChain == null) {
                tempRuleChain = ComponentFactory.getInstance().loadComponent(this.ruleChainPath, ruleChainConfig);
            } else {
                log.debug("The rule chain[{}] hase been initialized.", this.tempRuleChain.getFullPathName());
            }
        }

        return tempRuleChain;
    }
}

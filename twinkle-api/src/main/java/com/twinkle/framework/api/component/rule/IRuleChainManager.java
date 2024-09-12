package com.twinkle.framework.api.component.rule;

import com.twinkle.framework.api.component.IComponent;
import com.twinkle.framework.api.config.Configurable;

import java.util.Collection;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-19 15:42<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface IRuleChainManager extends Configurable, IComponent {
    /**
     * Get the RuleChain names set.
     *
     * @return
     */
    Collection<String> getRuleChainNames();
    
    /**
     * Get RuleChain by Name.
     *
     * @param _ruleChainName
     * @return
     */
    IRuleChain getRuleChain(String _ruleChainName);

    /**
     * Add RuleChain into the manager.
     *
     * @param _ruleChainName
     * @param _ruleChain
     */
    void addRuleChain(String _ruleChainName, IRuleChain _ruleChain);

    /**
     * Going to release the rule chain.
     *
     * @param _name
     * @param _ruleChain
     */
    void releaseRuleChain(String _name, IRuleChain _ruleChain);
}

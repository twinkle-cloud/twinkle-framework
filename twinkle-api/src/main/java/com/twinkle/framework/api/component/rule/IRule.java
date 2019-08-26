package com.twinkle.framework.api.component.rule;

import com.twinkle.framework.api.component.IComponent;
import com.twinkle.framework.api.config.Configurable;
import com.twinkle.framework.api.exception.RuleException;
import com.twinkle.framework.core.context.model.NormalizedContext;

/**
 * IRule
 * @author chenxj
 */
public interface IRule extends Configurable, IComponent {

    /**
     * 执行rule
     */
    void applyRule(NormalizedContext _context) throws RuleException;

    /**
     * Add next rule.
     *
     * @param _rule
     */
    void addNextRule(IRule _rule);
}

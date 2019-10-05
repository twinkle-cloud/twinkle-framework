package com.twinkle.framework.api.component.rule;

import com.twinkle.framework.api.component.IComponent;
import com.twinkle.framework.api.config.Configurable;
import com.twinkle.framework.api.exception.RuleException;
import com.twinkle.framework.api.context.NormalizedContext;

/**
 * IRule
 * @author chenxj
 */
public interface IRule extends Configurable, IComponent {
    /**
     * Apply Rule.
     *
     * @param _context
     * @throws RuleException
     */
    void applyRule(NormalizedContext _context) throws RuleException;

    /**
     * Add next rule.
     *
     * @param _rule
     */
    void addNextRule(IRule _rule);
}

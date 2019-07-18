package com.twinkle.framework.api.rule;

import com.twinkle.framework.api.config.Configurable;
import com.twinkle.framework.api.exception.RuleException;
import com.twinkle.framework.core.context.model.NormalizedContext;

/**
 * IRule
 * */
public interface IRule extends Configurable {
    
    /**
     * 执行rule
     * */
    public void applyRule(NormalizedContext _context) throws RuleException;
}

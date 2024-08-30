package com.twinkle.framework.ruleengine.rule.condition;

import com.alibaba.fastjson2.JSONObject;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.api.exception.RuleException;
import com.twinkle.framework.api.component.IComponentFactory;
import com.twinkle.framework.api.context.NormalizedContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-19 11:37<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class NotCondition extends AbstractCondition {
    private ICondition condition;

    @Autowired
    protected IComponentFactory componentFactory;

    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {
        this.condition = componentFactory.loadComponent(_conf.getJSONObject("Condition"));
    }

    @Override
    public boolean check(NormalizedContext _context, boolean _validateFlag) throws RuleException {
        return this.condition.check(_context, _validateFlag);
    }
}

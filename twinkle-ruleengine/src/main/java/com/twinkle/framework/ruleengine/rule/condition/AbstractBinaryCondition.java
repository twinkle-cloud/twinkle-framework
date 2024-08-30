package com.twinkle.framework.ruleengine.rule.condition;

import com.alibaba.fastjson2.JSONObject;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.api.component.IComponentFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-19 11:24<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public abstract class AbstractBinaryCondition extends AbstractCondition{
    /**
     * Left Condition.
     */
    protected ICondition leftCondition;
    /**
     * Right Condition.
     */
    protected ICondition rightCondition;

    @Autowired
    protected IComponentFactory componentFactory;

    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {
        this.leftCondition = componentFactory.loadComponent(_conf.getJSONObject("LeftCondition"));
        this.rightCondition = componentFactory.loadComponent(_conf.getJSONObject("RightCondition"));
    }
}

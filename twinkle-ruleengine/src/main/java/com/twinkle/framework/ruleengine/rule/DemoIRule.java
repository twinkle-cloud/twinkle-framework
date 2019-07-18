package com.twinkle.framework.ruleengine.rule;

import com.alibaba.fastjson.JSONObject;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.api.rule.IRule;
import com.twinkle.framework.core.context.model.NormalizedContext;

/**
 * TODO ADD DESC <br/>
 * Date:    2019年7月14日 下午7:52:12 <br/>
 *
 * @author yukang
 * @see
 * @since JDK 1.8
 */
public class DemoIRule implements IRule {
    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {

    }

    @Override
    public void applyRule(NormalizedContext _context) {

    }

}

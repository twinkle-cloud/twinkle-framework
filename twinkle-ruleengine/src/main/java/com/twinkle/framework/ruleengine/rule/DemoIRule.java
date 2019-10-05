package com.twinkle.framework.ruleengine.rule;

import com.alibaba.fastjson.JSONObject;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.context.PrimitiveAttributeSchema;
import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.core.lang.Attribute;
import lombok.extern.slf4j.Slf4j;

/**
 * TODO ADD DESC <br/>
 * Date:    2019年7月14日 下午7:52:12 <br/>
 *
 * @author yukang
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class DemoIRule extends AbstractRule {
    private int attrIndex1 = -1;

    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {
        String tempName = _conf.getString("Attribute");
        this.attrIndex1 = this.primitiveAttributeSchema.getAttributeIndex(tempName, _conf.toJSONString());
    }

    @Override
    public void applyRule(NormalizedContext _context) {
        log.info("Going to apply Demo rule.");
        Attribute tempAttr = _context.getAttribute(this.attrIndex1);
        if(tempAttr == null) {
            tempAttr = PrimitiveAttributeSchema.getInstance().newAttributeInstance(this.attrIndex1);
        }
        tempAttr.setValue("Rule Test.");
        _context.setAttribute(tempAttr, this.attrIndex1);
        if(this.nextRule != null) {
            this.nextRule.applyRule(_context);
        }
    }
}

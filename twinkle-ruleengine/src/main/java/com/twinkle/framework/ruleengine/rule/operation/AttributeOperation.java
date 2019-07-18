package com.twinkle.framework.ruleengine.rule.operation;

import com.alibaba.fastjson.JSONObject;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.core.context.model.NormalizedContext;
import com.twinkle.framework.core.lang.Attribute;
import com.twinkle.framework.core.lang.AttributeInfo;
import com.twinkle.framework.ruleengine.rule.AbstractRule;
import lombok.extern.slf4j.Slf4j;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-18 14:09<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public abstract class AttributeOperation extends AbstractRule {

    public AttributeOperation(){
        super();
        log.info("AttributeOperation.initialized().");
    }

    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {
        String var2 = _conf.getString("Operation");
        this.loadOperation(var2);
    }

    public abstract void loadOperation(String _operation) throws ConfigurationException;

    /**
     * Update the [_index]'s attribute with the given _attr.
     *
     * @param _context
     * @param _attr
     * @param _index
     * @return
     */
    protected boolean modifyNormalizedContext(NormalizedContext _context, Attribute _attr, int _index) {
        Attribute tempDestAttr = _context.getAttribute(_index);
        if (tempDestAttr == null) {
            if (!_context.getType().isMember(_index)) {
                AttributeInfo tempNewAttr = this.contextSchema.getAttribute(_index);
                _context.getType().addAttribute(tempNewAttr);
            }
            tempDestAttr = this.contextSchema.newAttributeInstance(_index);
            tempDestAttr.setValue(_attr);
            _context.setAttribute(tempDestAttr, _index);
            return true;
        } else {
            tempDestAttr.setValue(_attr);
            return false;
        }
    }
}

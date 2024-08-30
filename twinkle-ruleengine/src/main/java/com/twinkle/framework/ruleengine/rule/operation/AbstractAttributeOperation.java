package com.twinkle.framework.ruleengine.rule.operation;

import com.alibaba.fastjson2.JSONObject;
import com.twinkle.framework.api.context.AttributeInfo;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.core.lang.Attribute;
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
public abstract class AbstractAttributeOperation extends AbstractRule {
    public AbstractAttributeOperation(){
        super();
        log.info("AttributeOperation.initialized().");
    }

    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {
        String tempOperation = _conf.getString("Operation");
        this.loadOperation(tempOperation);
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
    protected boolean modifyContextAttribute(NormalizedContext _context, Attribute _attr, int _index) {
        Attribute tempDestAttr = _context.getAttribute(_index);
        if (tempDestAttr == null) {
            if (!_context.getType().isMember(_index)) {
                AttributeInfo tempNewAttr = this.primitiveAttributeSchema.getAttribute(_index);
                _context.getType().addAttribute(tempNewAttr);
            }
            tempDestAttr = this.primitiveAttributeSchema.newAttributeInstance(_index);
            tempDestAttr.setValue(_attr);
            _context.setAttribute(tempDestAttr, _index);
            return true;
        } else {
            tempDestAttr.setValue(_attr);
            return false;
        }
    }

    /**
     * Reset the following Operation.
     *
     * @param _context
     */
    public void reset(NormalizedContext _context) {
        if (this.nextRule != null) {
            ((AbstractAttributeOperation)this.nextRule).reset(_context);
        }

    }
}

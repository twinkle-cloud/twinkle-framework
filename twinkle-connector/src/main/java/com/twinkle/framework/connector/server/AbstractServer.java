package com.twinkle.framework.connector.server;

import com.twinkle.framework.api.exception.RuleException;
import com.twinkle.framework.api.component.rule.IRuleChain;
import com.twinkle.framework.api.component.rule.IRuleChainManager;
import com.twinkle.framework.context.PrimitiveAttributeSchema;
import com.twinkle.framework.context.model.DefaultNormalizedContext;
import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.core.lang.Attribute;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-19 14:38<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public abstract class AbstractServer {
    private final ThreadLocal<DefaultNormalizedContext> ncThreadLocal = new ThreadLocal();

    @Autowired
    protected IRuleChainManager ruleChainManager;

    /**
     * Do invoke Rule Chain.
     *
     * @param _nc
     * @param _ruleChainName
     * @return
     */
    protected NormalizedContext invokeRuleChain(NormalizedContext _nc, String _ruleChainName) {
        if(StringUtils.isBlank(_ruleChainName)) {
            return _nc;
        }
        log.debug("Going to invoke RuleChain[{}].", _ruleChainName);
        IRuleChain tempRuleChain = this.ruleChainManager.getRuleChain(_ruleChainName);
        if(tempRuleChain == null) {
            log.warn("The RuleChain[{}] does not exists.", _ruleChainName);
            return _nc;
        }
        try {
            tempRuleChain.applyRule(_nc);
        } catch (RuleException e) {
            log.error("Encountered error while invoking rule chain[{}]: {}", _ruleChainName, e);
            throw new RuntimeException(e);
        }
        return _nc;
    }

    /**
     * Going to decode the parameter, and update the decoded parameter into the NormalizedContext.
     *
     * @param _context
     * @param _neIndex
     * @param _value
     */
    protected void decodeParameter(NormalizedContext _context, int _neIndex, Object _value){
        if(_neIndex < 0) {
            log.warn("The NE [{}] does not exists in PrimitiveAttributeSchema.", _neIndex);
            return;
        }
        Attribute tempAttr = _context.getAttribute(_neIndex);
        if(tempAttr == null) {
            tempAttr = PrimitiveAttributeSchema.getInstance().newAttributeInstance(_neIndex);
        }
        tempAttr.setValue(_value);
        _context.setAttribute(tempAttr, _neIndex);
    }

    /**
     * Get Return Object.
     *
     * @param _context
     * @param _neIndex
     * @param _value
     * @param <T>
     * @return
     */
    protected <T> T encodeReturnData(NormalizedContext _context, int _neIndex) {
        if(_neIndex < 0) {
            log.warn("The NE [{}] does not exists in PrimitiveAttributeSchema.", _neIndex);
            return null;
        }
        Attribute tempAttr = _context.getAttribute(_neIndex);
        if(tempAttr == null) {
            log.warn("The NE [{}] does not exists in NormalizedContext.", _neIndex);
            return null;
        }
        T tempValue = (T) tempAttr.getObjectValue();
        return tempValue;
    }

    /**
     * Get the normalized context from thread local.
     *
     * @return
     */
    protected NormalizedContext getNormalizedContext() {
        DefaultNormalizedContext tempNc = this.ncThreadLocal.get();
        if (tempNc == null) {
            log.info("HttpServer-current thread[{}]", Thread.currentThread());
            tempNc = new DefaultNormalizedContext();
            this.ncThreadLocal.set(tempNc);
        } else {
            tempNc.clear();
        }
        return tempNc;
    }
}

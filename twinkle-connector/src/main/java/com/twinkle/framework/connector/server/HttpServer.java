package com.twinkle.framework.connector.server;

import com.twinkle.framework.api.exception.RuleException;
import com.twinkle.framework.api.rule.IRuleChain;
import com.twinkle.framework.api.rule.IRuleChainManager;
import com.twinkle.framework.core.context.model.NormalizedContext;
import com.twinkle.framework.ruleengine.RuleChainManager;
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
public class HttpServer {
    private final ThreadLocal<NormalizedContext> ncThreadLocal = new ThreadLocal();

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

    private NormalizedContext getNormalizedContext() {
        NormalizedContext tempNc = this.ncThreadLocal.get();
        if (tempNc == null) {
            log.info("HttpServer-current thread[{}]", Thread.currentThread());
            tempNc = new NormalizedContext();
            this.ncThreadLocal.set(tempNc);
        } else {
            tempNc.clear();
        }
        return tempNc;
    }
}

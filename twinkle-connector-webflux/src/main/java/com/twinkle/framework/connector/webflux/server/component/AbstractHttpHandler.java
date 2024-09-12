package com.twinkle.framework.connector.webflux.server.component;

import com.twinkle.framework.api.component.rule.IRuleChain;
import com.twinkle.framework.api.component.rule.IRuleChainManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
@Slf4j
public abstract class AbstractHttpHandler implements IHttpHandler {
    @Autowired
    private IRuleChainManager ruleChainManager;

    /**
     * The Destination RuleChain
     */
    private IRuleChain ruleChain;

    protected String ruleChainName;
    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Initializing http handler ...");
        this.ruleChain = this.ruleChainManager.getRuleChain(this.ruleChainName);
    }
}

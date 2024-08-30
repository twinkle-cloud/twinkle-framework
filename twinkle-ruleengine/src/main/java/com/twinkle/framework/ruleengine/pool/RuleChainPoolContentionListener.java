package com.twinkle.framework.ruleengine.pool;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;

@Slf4j
public class RuleChainPoolContentionListener implements PoolContentionListener{
    private String ruleChain_;
    private int poolMaxSize_;
    Level contentionLogLevel_;

    public RuleChainPoolContentionListener(String var1, int var2, Level var3) {
        this.ruleChain_ = var1;
        this.poolMaxSize_ = var2;
        this.contentionLogLevel_ = var3;
    }

    public void onPoolContention() {
        if (this.contentionLogLevel_ != null && log.isEnabledForLevel(this.contentionLogLevel_)) {
            log.info( "Contention in {0} pool after reaching max size = {1}.", new Object[]{this.ruleChain_, this.poolMaxSize_});
        }
    }
}

package com.twinkle.framework.ruleengine.pool;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;

/**
 * @author chenxj
 * @date 2024/09/03
 */
@Slf4j
public class RuleChainPoolContentionListener implements PoolContentionListener{
    private String ruleChain_;
    private int poolMaxSize_;
    Level contentionLogLevel_;

    /**
     * Constructor for RuleChain Pool Contention.
     *
     * @param _ruleChain
     * @param _maxSize
     * @param _level Log Level for this RuleChain
     */
    public RuleChainPoolContentionListener(String _ruleChain, int _maxSize, Level _level) {
        this.ruleChain_ = _ruleChain;
        this.poolMaxSize_ = _maxSize;
        this.contentionLogLevel_ = _level;
    }

    public void onPoolContention() {
        if (this.contentionLogLevel_ != null && log.isEnabledForLevel(this.contentionLogLevel_)) {
            log.info( "Contention in {0} pool after reaching max size = {1}.", new Object[]{this.ruleChain_, this.poolMaxSize_});
        }
    }
}

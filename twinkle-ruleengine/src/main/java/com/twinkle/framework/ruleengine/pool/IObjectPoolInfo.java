package com.twinkle.framework.ruleengine.pool;

/**
 * Pool Info Bean
 *
 * @author chenxj
 * @date 2024/08/28
 */
public interface IObjectPoolInfo {
    /**
     * Number of get operations
     *
     * @return
     */
    long getGetOperations();

    /**
     * Number of release operations
     *
     * @return
     */
    long getReleaseOperations();

    /**
     * Number of wait operations when pool is empty
     *
     * @return
     */
    long getWaitOperations();

    /**
     * Number of create operations
     *
     * @return
     */
    long getCreateOperations();

    /**
     * Pool maximum size
     *
     * @return
     */
    int getMaxSize();

    /**
     * Pool current size
     *
     * @return
     */
    int getCurrentSize();

    /**
     * Pool has a strict size
     *
     * @return
     */
    boolean getStrictSize();
}

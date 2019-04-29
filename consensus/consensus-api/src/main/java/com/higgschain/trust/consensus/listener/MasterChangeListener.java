package com.higgschain.trust.consensus.listener;

/**
 * The interface Master change listener.
 */
public interface MasterChangeListener {

    /**
     * Before change.
     *
     * @param masterName the master name
     */
    void beforeChange(String masterName);

    /**
     * 节点 master 变更
     *
     * @param masterName 新的master名
     */
    void masterChanged(String masterName);
}

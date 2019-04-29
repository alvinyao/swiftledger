/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.consensus.core.master;

/**
 * The interface Node info service.
 *
 * @author suimi
 * @date 2018 /6/12
 */
public interface INodeInfoService {

    /**
     * Package height long.
     *
     * @return the long
     */
    Long packageHeight();

    /**
     * Block height long.
     *
     * @return the long
     */
    Long blockHeight();

    /**
     * get the max height
     *
     * @return the max height
     */
    default long getMaxHeight() {
        Long packageHeight = packageHeight();
        Long blockHeight = blockHeight();
        return Math.max(packageHeight == null ? 0 : packageHeight, blockHeight == null ? 0 : blockHeight);
    }

    /**
     * is the current node  qualified for master
     *
     * @return boolean
     */
    boolean hasMasterQualify();

    /**
     * election master
     *
     * @return boolean
     */
    boolean isElectionMaster();

    /**
     * Sets election master.
     *
     * @param electionMaster the election master
     */
    void setElectionMaster(boolean electionMaster);
}

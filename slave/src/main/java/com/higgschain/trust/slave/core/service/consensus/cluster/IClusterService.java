/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.slave.core.service.consensus.cluster;

import com.higgschain.trust.slave.model.bo.BlockHeader;

import java.util.Map;

/**
 * The interface Cluster service.
 */
public interface IClusterService {

    /**
     * get the block height of cluster
     *
     * @param size the size of height will be consensus
     * @return cluster height
     */
    Long getClusterHeight(int size);

    /**
     * get the safe block height of cluster
     *
     * @return safe height
     */
    Long getSafeHeight();

    /**
     * get the block height of cluster
     *
     * @param requestId the id of request
     * @param size      the size of height will be consensus
     * @return cluster height
     */
    Long getClusterHeight(String requestId, int size);

    /**
     * cluster validates the block header
     *
     * @param header block header
     * @return boolean
     */
    Boolean validatingHeader(BlockHeader header);

    /**
     * Get the height of all node
     *
     * @return the all cluster height
     */
    Map<String, Long> getAllClusterHeight();

    /**
     * Get the status of all node
     *
     * @return all cluster state
     */
    Map<String, String> getAllClusterState();
}

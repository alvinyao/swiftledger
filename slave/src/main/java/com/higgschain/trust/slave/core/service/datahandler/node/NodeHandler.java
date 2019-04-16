package com.higgschain.trust.slave.core.service.datahandler.node;

import com.higgschain.trust.slave.model.bo.config.ClusterNode;

/**
 * The interface Node handler.
 *
 * @author WangQuanzhou
 * @desc CA handler
 * @date 2018 /6/6 10:33
 */
public interface NodeHandler {

    /**
     * Node join.
     *
     * @param clusterNode the cluster node
     */
    void nodeJoin(ClusterNode clusterNode);

    /**
     * Node leave.
     *
     * @param clusterNode the cluster node
     */
    void nodeLeave(ClusterNode clusterNode);
}

package com.higgschain.trust.slave.core.service.datahandler.node;

import com.higgschain.trust.slave.model.bo.config.ClusterNode;

/**
 * @author WangQuanzhou
 * @desc CA handler
 * @date 2018/6/6 10:33
 */
public interface NodeHandler {

    void nodeJoin(ClusterNode clusterNode);

    void nodeLeave(ClusterNode clusterNode);
}

package com.higgschain.trust.slave.dao.mysql.config;

import com.higgschain.trust.slave.dao.po.config.ClusterNodePO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * The interface Cluster node dao.
 *
 * @author WangQuanzhou
 * @desc TODO
 * @date 2018 /6/5 14:17
 */
@Mapper public interface ClusterNodeDao {

    /**
     * Insert cluster node.
     *
     * @param clusterNodePO the cluster node po
     * @return
     * @desc insert clusterNode information into db
     */
    void insertClusterNode(ClusterNodePO clusterNodePO);

    /**
     * Update cluster node.
     *
     * @param clusterNodePO the cluster node po
     * @return
     * @desc update clusterNode
     */
    void updateClusterNode(ClusterNodePO clusterNodePO);

    /**
     * Gets cluster node.
     *
     * @param nodeName the node name
     * @return ClusterConfigPO cluster node
     * @desc get clusterNode by node name
     */
    ClusterNodePO getClusterNode(String nodeName);

    /**
     * Gets node num.
     *
     * @param
     * @return node num
     * @desc acquire node num
     */
    int getNodeNum();

    /**
     * batch insert
     *
     * @param clusterNodePOList the cluster node po list
     * @return int
     */
    int batchInsert(List<ClusterNodePO> clusterNodePOList);

    /**
     * batch update
     *
     * @param clusterNodePOList the cluster node po list
     * @return int
     */
    int batchUpdate(List<ClusterNodePO> clusterNodePOList);

    /**
     * Gets all cluster nodes.
     *
     * @return the all cluster nodes
     */
    List<ClusterNodePO> getAllClusterNodes();
}

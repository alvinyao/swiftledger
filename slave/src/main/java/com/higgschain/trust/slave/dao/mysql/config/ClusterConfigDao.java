package com.higgschain.trust.slave.dao.mysql.config;

import com.higgschain.trust.slave.dao.po.config.ClusterConfigPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * The interface Cluster config dao.
 *
 * @author WangQuanzhou
 * @desc TODO
 * @date 2018 /6/5 14:16
 */
@Mapper public interface ClusterConfigDao {

    /**
     * Insert cluster config.
     *
     * @param clusterConfigPO the cluster config po
     * @return
     * @desc insert clusterConfig into db
     */
    void insertClusterConfig(ClusterConfigPO clusterConfigPO);

    /**
     * Update cluster config.
     *
     * @param clusterConfigPO the cluster config po
     * @return
     * @desc update ClusterConfig
     */
    void updateClusterConfig(ClusterConfigPO clusterConfigPO);

    /**
     * Gets cluster config.
     *
     * @param clusterName the cluster name
     * @return ClusterConfigPO cluster config
     * @desc get ClusterConfig by cluster name
     */
    ClusterConfigPO getClusterConfig(String clusterName);

    /**
     * batch insert
     *
     * @param clusterConfigPOList the cluster config po list
     * @return int
     */
    int batchInsert(List<ClusterConfigPO> clusterConfigPOList);

    /**
     * batch update
     *
     * @param clusterConfigPOList the cluster config po list
     * @return int
     */
    int batchUpdate(List<ClusterConfigPO> clusterConfigPOList);
}

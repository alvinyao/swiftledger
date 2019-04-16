package com.higgschain.trust.slave.core.repository.config;

import com.higgschain.trust.common.dao.RocksUtils;
import com.higgschain.trust.common.utils.ThreadLocalUtils;
import com.higgschain.trust.slave.BaseTest;
import com.higgschain.trust.slave.common.config.InitConfig;
import com.higgschain.trust.slave.dao.po.config.ClusterConfigPO;
import com.higgschain.trust.slave.model.bo.config.ClusterConfig;
import org.rocksdb.Transaction;
import org.rocksdb.WriteOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * The type Cluster config repository test.
 */
public class ClusterConfigRepositoryTest extends BaseTest{

    @Autowired private ClusterConfigRepository clusterConfigRepository;
    @Autowired private InitConfig initConfig;

    /**
     * Test insert cluster config.
     *
     * @throws Exception the exception
     */
    @Test public void testInsertClusterConfig() throws Exception {

    }

    /**
     * Test get cluster config.
     *
     * @throws Exception the exception
     */
    @Test public void testGetClusterConfig() throws Exception {
       ClusterConfig clusterConfig =  clusterConfigRepository.getClusterConfig("TRUST");
       System.out.println(clusterConfig);
    }

    /**
     * Test batch insert.
     *
     * @throws Exception the exception
     */
    @Test public void testBatchInsert() throws Exception {
        List list = new LinkedList();
        for (int i = 0;i<3;i++){
            ClusterConfigPO clusterConfigPO = new ClusterConfigPO();
            clusterConfigPO.setClusterName("TRUST" + i);
            clusterConfigPO.setFaultNum(2 + i);
            clusterConfigPO.setNodeNum(5 + i);
            list.add(clusterConfigPO);
        }

        if (!initConfig.isUseMySQL()) {
            Transaction tx = RocksUtils.beginTransaction(new WriteOptions());
            ThreadLocalUtils.putRocksTx(tx);
            clusterConfigRepository.batchInsert(list);
            RocksUtils.txCommit(tx);
            ThreadLocalUtils.clearRocksTx();;
        } else {
            clusterConfigRepository.batchInsert(list);
        }
        for (int i = 0; i < 3; i++) {
            ClusterConfig clusterConfig = clusterConfigRepository.getClusterConfig("TRUST" + i);
            System.out.println(clusterConfig);
        }

    }

    /**
     * Test batch update.
     *
     * @throws Exception the exception
     */
    @Test public void testBatchUpdate() throws Exception {
        List list = new LinkedList();
        for (int i = 0;i<3;i++){
            ClusterConfigPO clusterConfigPO = new ClusterConfigPO();
            clusterConfigPO.setClusterName("TRUST" + i);
            clusterConfigPO.setFaultNum(1 + i);
            clusterConfigPO.setNodeNum(4 + i);
            list.add(clusterConfigPO);
        }

        if (!initConfig.isUseMySQL()) {
            Transaction tx = RocksUtils.beginTransaction(new WriteOptions());
            ThreadLocalUtils.putRocksTx(tx);
            clusterConfigRepository.batchUpdate(list);
            RocksUtils.txCommit(tx);
            ThreadLocalUtils.clearRocksTx();;
        } else {
            clusterConfigRepository.batchUpdate(list);
        }


        for (int i = 0; i < 6; i++) {
            ClusterConfig clusterConfig = clusterConfigRepository.getClusterConfig("TRUST" + i);
            System.out.println(clusterConfig);
        }
    }
}
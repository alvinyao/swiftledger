package com.higgschain.trust.slave.core.managment;

import com.higgschain.trust.common.dao.RocksUtils;
import com.higgschain.trust.common.utils.ThreadLocalUtils;
import com.higgschain.trust.consensus.config.NodeProperties;
import com.higgschain.trust.consensus.config.NodeState;
import com.higgschain.trust.consensus.config.NodeStateEnum;
import com.higgschain.trust.consensus.config.listener.StateChangeListener;
import com.higgschain.trust.consensus.config.listener.StateListener;
import com.higgschain.trust.network.NetworkManage;
import com.higgschain.trust.slave.api.enums.VersionEnum;
import com.higgschain.trust.slave.common.config.InitConfig;
import com.higgschain.trust.slave.core.repository.BlockRepository;
import com.higgschain.trust.slave.core.repository.config.ConfigRepository;
import com.higgschain.trust.slave.core.service.ca.CaInitService;
import com.higgschain.trust.slave.core.service.consensus.view.ClusterViewService;
import com.higgschain.trust.slave.model.bo.config.Config;
import com.higgschain.trust.slave.model.enums.UsageEnum;
import lombok.extern.slf4j.Slf4j;
import org.rocksdb.Transaction;
import org.rocksdb.WriteOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;
import java.util.List;

/**
 * The type Cluster init service.
 *
 * @author WangQuanzhou
 * @desc cluster init service
 * @date 2018 /6/28 19:53
 */
@StateListener
@Service @Slf4j public class ClusterInitService {

    @Autowired private BlockRepository blockRepository;

    @Autowired private CaInitService caInitService;

    @Autowired private ConfigRepository configRepository;

    @Autowired private NodeState nodeState;

    @Autowired private TransactionTemplate txRequired;

    @Autowired private ClusterViewService clusterViewService;

    @Autowired private InitConfig initConfig;

    @Autowired
    private NodeProperties nodeProperties;
    @Autowired
    private NetworkManage networkManage;

    /**
     * The Pub key for biz.
     */
    @Value("${higgs.trust.keys.bizPublicKey}")
    String pubKeyForBiz;

    /**
     * The Pri key for biz.
     */
    @Value("${higgs.trust.keys.bizPrivateKey}")
    String priKeyForBiz;

    /**
     * The Pub key for consensus.
     */
    @Value("${higgs.trust.keys.consensusPublicKey}")
    String pubKeyForConsensus;

    /**
     * The Pri key for consensus.
     */
    @Value("${higgs.trust.keys.consensusPrivateKey}")
    String priKeyForConsensus;

    /**
     * Init.
     *
     * @throws IOException          the io exception
     * @throws InterruptedException the interrupted exception
     */
    @StateChangeListener(value = NodeStateEnum.Initialize, before = true)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public void init() throws IOException, InterruptedException {
        if (needInit()) {
            // 1、生成公私钥,存入db
            // 2、获取其他节点的公钥,公钥写入配置文件给共识层使用
            // 3、使用所有的公钥及节点名生成创世块
            caInitService.initKeyPair();
            log.info("[ClusterInitService.init] end initKeyPair");
        }

        Config config = configRepository.getBizConfig(nodeState.getNodeName());
        nodeState.setPrivateKey(null != config ? config.getPriKey() : null);

        List<Config> configList = configRepository.getConfig(new Config(nodeState.getNodeName(), UsageEnum.CONSENSUS.getCode()));
        nodeState.setConsensusPrivateKey(null != configList ? configList.get(0).getPriKey() : null);

        //if the node is slave need to do it
        clusterViewService.initClusterViewFromDB(false);

        //start network
        networkManage.start();

        // if the node  is standby , init views form cluster
        initClusterViewFromCluster();
    }

    /**
     * initClusterViewFromCluster  and retry 20 times if not success
     */
    private void initClusterViewFromCluster() throws InterruptedException {
        if (!nodeProperties.isStandby() || !nodeProperties.isSlave()) {
           return;
        }
        boolean retry = false;
        int i = 1;
        do {
            try {
                clusterViewService.initClusterViewFromCluster();
            } catch (Throwable e) {
                log.error("initClusterViewFromCluster error", e);
                retry = true;
                Thread.sleep(1000);
            }
        } while (retry && ++i< 20);
    }

    private boolean needInit() {
        //when it is RS node and  use rocks DB，need to generateKeyPair
        if (initConfig.isUseMySQL() && !nodeProperties.isSlave() ){
            return false;
        }
        // 1、 本地没有创世块，集群也没有创世块时（即集群初始启动），需要生成公私钥，以及创世块
        // 2、 本地没有创世块，集群有创世块时（即动态单节点加入），需要进行failover得到创世块
        Long maxHeight = blockRepository.getMaxHeight();
        if (null == maxHeight) {
            log.info("[ClusterInitService.needInit] start generateKeyPair");
            generateKeyPair();
            return true;
        }
        return false;
    }

    private void generateKeyPair() {

        if (null != configRepository.getConfig(new Config(nodeState.getNodeName()))) {
            log.info("[ClusterInitService.generateKeyPair] pubKey/priKey already exist in table config");
            return;
        }
        if (initConfig.isUseMySQL()) {
            txRequired.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                    // load keyPair for cluster from json file
                    saveConfig(pubKeyForBiz, priKeyForBiz, UsageEnum.BIZ);
                    saveConfig(pubKeyForConsensus, priKeyForConsensus, UsageEnum.CONSENSUS);
                }
            });
        } else {
            Transaction tx = RocksUtils.beginTransaction(new WriteOptions());
            try {
                ThreadLocalUtils.putRocksTx(tx);

                // generate keyPair for consensus layer
                saveConfig(pubKeyForBiz, priKeyForBiz, UsageEnum.BIZ);
                saveConfig(pubKeyForConsensus, priKeyForConsensus, UsageEnum.CONSENSUS);

                RocksUtils.txCommit(tx);
            } finally {
                ThreadLocalUtils.clearRocksTx();
            }
        }
    }

    private void saveConfig(String pubKey, String priKey, UsageEnum usage) {
        Config config = new Config();
        config.setValid(true);
        config.setPubKey(pubKey);
        config.setPriKey(priKey);
        config.setVersion(VersionEnum.V1.getCode());
        config.setNodeName(nodeState.getNodeName());
        config.setUsage(usage.getCode());
        configRepository.insertConfig(config);
    }

}

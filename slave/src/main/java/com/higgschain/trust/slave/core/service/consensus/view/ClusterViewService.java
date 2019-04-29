/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.slave.core.service.consensus.view;

import com.higgschain.trust.consensus.util.CryptoUtil;
import com.higgschain.trust.consensus.view.ClusterView;
import com.higgschain.trust.consensus.view.IClusterViewManager;
import com.higgschain.trust.consensus.view.IClusterViewService;
import com.higgschain.trust.consensus.view.LastPackage;
import com.higgschain.trust.consensus.config.NodeProperties;
import com.higgschain.trust.consensus.config.NodeState;
import com.higgschain.trust.consensus.p2pvalid.api.P2pConsensusClient;
import com.higgschain.trust.consensus.p2pvalid.core.ResponseCommand;
import com.higgschain.trust.consensus.p2pvalid.core.ValidCommandWrap;
import com.higgschain.trust.consensus.p2pvalid.core.ValidConsensus;
import com.higgschain.trust.consensus.p2pvalid.core.ValidResponseWrap;
import com.higgschain.trust.slave.core.repository.BlockRepository;
import com.higgschain.trust.slave.core.repository.PackageRepository;
import com.higgschain.trust.slave.core.repository.ca.CaRepository;
import com.higgschain.trust.slave.model.bo.BlockHeader;
import com.higgschain.trust.slave.model.bo.Package;
import com.higgschain.trust.slave.model.bo.ca.Ca;
import com.higgschain.trust.slave.model.enums.UsageEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Cluster view service.
 *
 * @author suimi
 * @date 2018 /6/19
 */
@Slf4j @Service public class ClusterViewService implements IClusterViewService {

    private static final String DEFAULT_CLUSTER_INFO_ID = "cluster_info_id";

    @Autowired private ValidConsensus validConsensus;

    @Autowired private NodeProperties nodeProperties;

    @Autowired private P2pConsensusClient client;

    @Autowired private NodeState nodeState;

    @Autowired private IClusterViewManager viewManager;

    @Autowired private CaRepository caRepository;

    @Autowired private BlockRepository blockRepository;

    @Autowired private PackageRepository packageRepository;

    /**
     * Init cluster view from db.
     *
     * @param useCurrentHeight the use current height
     */
    @Override public void initClusterViewFromDB(boolean useCurrentHeight) {
        //RS node do not to initClusterView
        if (!nodeProperties.isSlave()) {
            return;
        }
        List<Ca> list = caRepository.getAllCa();
        Map<String, String> consensusNodeMap = new HashMap<>();
        list.stream().filter(ca -> ca.getUsage().equals(UsageEnum.CONSENSUS.getCode()))
            .forEach(ca -> consensusNodeMap.put(ca.getUser(), ca.getPubKey()));
        Long maxBlockHeight = blockRepository.getMaxHeight();
        Long maxPackageHeight = packageRepository.getMaxHeight();
        long height =
            Math.max(maxBlockHeight == null ? 0 : maxBlockHeight, maxPackageHeight == null ? 0 : maxPackageHeight);
        viewManager.resetViews(
            Collections.singletonList(new ClusterView(0, useCurrentHeight ? height + 1 : 2, consensusNodeMap)));

        //reset the last package time
        if (maxBlockHeight < height) {
            Package pack = packageRepository.load(height);
            viewManager.resetLastPackage(new LastPackage(pack.getHeight(), pack.getPackageTime()));
        } else {
            BlockHeader blockHeader = blockRepository.getBlockHeader(maxBlockHeight);
            viewManager.resetLastPackage(new LastPackage(blockHeader.getHeight(), blockHeader.getBlockTime()));
        }
    }

    /**
     * get the cluster info through consensus, if timeout, null will be return
     */
    @Override public void initClusterViewFromCluster() {
        log.info("init clusterInfo by cluster");
        initClusterViewFromAnyNode();
        ResponseCommand<?> responseCommand = null;
        int i = 0;
        do {
            responseCommand = validConsensus.submitSync(
                new ClusterViewCmd(DEFAULT_CLUSTER_INFO_ID + "," + System.currentTimeMillis(),
                    IClusterViewManager.CURRENT_VIEW_ID));
            if (responseCommand == null) {
                try {
                    Thread.sleep(3 * 1000);
                } catch (InterruptedException e) {
                    log.warn("init cluster info thread interrupted", e);
                }
            }
        } while (responseCommand == null && ++i < nodeProperties.getStartupRetryTime());
        if (responseCommand == null) {
            throw new RuntimeException("init clusterInfo from cluster failed");
        }
        viewManager.resetViews(Collections.singletonList((ClusterView)responseCommand.get()));
    }

    private void initClusterViewFromAnyNode() {
        log.info("init cluster info from any node");
        ValidResponseWrap<? extends ResponseCommand> response = null;
        int i = 0;
        do {
            ClusterViewCmd command = new ClusterViewCmd(DEFAULT_CLUSTER_INFO_ID + "," + System.currentTimeMillis(),
                IClusterViewManager.CURRENT_VIEW_ID);
            ValidCommandWrap commandWrap = new ValidCommandWrap();
            commandWrap.setCommandClass(command.getClass());
            commandWrap.setFromNode(nodeState.getNodeName());
            commandWrap.setSign(CryptoUtil.getProtocolCrypto()
                .sign(command.getMessageDigestHash(), nodeState.getConsensusPrivateKey()));
            commandWrap.setValidCommand(command);
            try {
                response = client.syncSendFeign(nodeState.notMeNodeNameReg(), commandWrap);
            } catch (Exception e) {
                log.error("get cluster info error", e);
            }
        } while ((response == null || !response.isSucess()) && ++i <= 10);
        if (response != null && response.isSucess()) {
            ValidClusterViewCmd viewCmd = (ValidClusterViewCmd)response.result();
            viewManager.resetViews(Collections.singletonList(viewCmd.get()));
            log.info("init cluster info from any node successful");
        } else {
            throw new RuntimeException("init clusterInfo from any node failed");
        }
    }
}

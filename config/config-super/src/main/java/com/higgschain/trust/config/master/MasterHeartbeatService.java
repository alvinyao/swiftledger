/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.config.master;

import com.higgschain.trust.config.crypto.CryptoUtil;
import com.higgschain.trust.config.master.command.MasterHeartbeatCommand;
import com.higgschain.trust.config.view.IClusterViewManager;
import com.higgschain.trust.consensus.config.NodeProperties;
import com.higgschain.trust.consensus.config.NodeState;
import com.higgschain.trust.consensus.config.NodeStateEnum;
import com.higgschain.trust.consensus.config.listener.MasterChangeListener;
import com.higgschain.trust.consensus.core.ConsensusClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

/**
 * The type Master heartbeat service.
 *
 * @author suimi
 * @date 2018 /6/4
 */
@Slf4j @Service public class MasterHeartbeatService implements MasterChangeListener {
    @Autowired private NodeProperties nodeProperties;

    @Autowired private ChangeMasterProperties properties;

    @Autowired private NodeState nodeState;

    @Autowired private ConsensusClient consensusClient;

    @Autowired private IClusterViewManager viewManager;

    private ScheduledFuture masterHeartbeatTimer;

    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread thread = new Thread(r);
        thread.setName("master heartbeat thread");
        thread.setDaemon(true);
        return thread;
    });

    /**
     * Start master heartbeat.
     */
    public void startMasterHeartbeat() {
        log.info("start master heartbeat timeout");
        resetMasterHeartbeat();
    }

    /**
     * Reset master heartbeat.
     */
    public void resetMasterHeartbeat() {
        log.debug("reset master heartbeat timeout");
        if (masterHeartbeatTimer != null) {
            masterHeartbeatTimer.cancel(true);
        }
        long delay = properties.getMasterHeartbeat();
        masterHeartbeatTimer = executor.schedule(this::sendMasterHeartbeat, delay, TimeUnit.MILLISECONDS);
    }

    private void sendMasterHeartbeat() {
        log.debug("send master heartbeat");
        if (nodeState.isMaster() && nodeState.isState(NodeStateEnum.Running)) {
            masterHeartbeat();
            resetMasterHeartbeat();
        }
    }

    private void cancelMasterHeart() {
        log.debug("cancel master heartbeat timeout");
        if (masterHeartbeatTimer != null) {
            masterHeartbeatTimer.cancel(true);
        }
    }

    /**
     * Master heartbeat.
     */
    public void masterHeartbeat() {
        MasterHeartbeatCommand command =
            new MasterHeartbeatCommand(nodeState.getCurrentTerm(), viewManager.getCurrentViewId(),
                nodeState.getNodeName());
        command
            .setSign(CryptoUtil.getProtocolCrypto().sign(command.getSignValue(), nodeState.getConsensusPrivateKey()));
        CompletableFuture future = consensusClient.submit(command);
        try {
            future.get(nodeProperties.getConsensusWaitTime(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("master heartbeat failed!", e);
        }
    }

    @Override public void beforeChange(String masterName) {

    }

    @Override public void masterChanged(String masterName) {
        if (nodeState.isMaster()) {
            startMasterHeartbeat();
        } else {
            cancelMasterHeart();
        }
    }
}

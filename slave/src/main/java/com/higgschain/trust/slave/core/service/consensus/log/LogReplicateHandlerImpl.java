package com.higgschain.trust.slave.core.service.consensus.log;

import com.higgschain.trust.consensus.util.CryptoUtil;
import com.higgschain.trust.consensus.view.IClusterViewManager;
import com.higgschain.trust.consensus.config.NodeProperties;
import com.higgschain.trust.consensus.config.NodeState;
import com.higgschain.trust.consensus.core.ConsensusClient;
import com.higgschain.trust.slave.common.enums.SlaveErrorEnum;
import com.higgschain.trust.slave.common.exception.SlaveException;
import com.higgschain.trust.slave.core.service.pack.PackageProcess;
import com.higgschain.trust.slave.core.service.pack.PackageService;
import com.higgschain.trust.slave.model.bo.consensus.PackageCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The type Log replicate handler.
 *
 * @Description: replicate the sorted package to cluster
 * @author: pengdi
 */
@Slf4j @Service public class LogReplicateHandlerImpl implements LogReplicateHandler {
    /**
     * client from the log replicate consensus layer
     */
    @Autowired ConsensusClient consensusClient;

    /**
     * The Package service.
     */
    @Autowired
    PackageService packageService;

    /**
     * The Package thread pool.
     */
    @Autowired ExecutorService packageThreadPool;

    /**
     * The Package process.
     */
    @Autowired PackageProcess packageProcess;

    /**
     * The Node state.
     */
    @Autowired NodeState nodeState;

    /**
     * The Properties.
     */
    @Autowired NodeProperties properties;

    /**
     * The View manager.
     */
    @Autowired IClusterViewManager viewManager;

    /**
     * retry time interval
     */
    private static final String[] retryInterval =
        new String[] {"50", "50", "50", "100", "100", "200", "400", "800", "1000"};

    /**
     * replicate sorted package to the cluster
     *
     * @param command
     */
    @Override public void replicatePackage(PackageCommand command) {
        // validate param
        if (command == null) {
            log.error("[LogReplicateHandler.replicatePackage]param validate failed, cause package is null ");
            throw new SlaveException(SlaveErrorEnum.SLAVE_PARAM_VALIDATE_ERROR);
        }
        command.setTerm(nodeState.getCurrentTerm());
        command.setView(viewManager.getCurrentViewId());
        String signValue = command.getSignValue();
        command.setSign(CryptoUtil.getProtocolCrypto().sign(signValue, nodeState.getConsensusPrivateKey()));
        boolean flag = false;
        /**
         * retry times
         */
        int retryTimes = 0;
        while (!flag) {
            if (!nodeState.isMaster()) {
                return;
            }
            CompletableFuture future = consensusClient.submit(command);
            try {
                future.get(properties.getConsensusWaitTime(), TimeUnit.MILLISECONDS);
                flag = true;
            } catch (Throwable e) {
                log.error("replicate log failed! pack.height={}", command.getPackageHeight(), e);
                //TODO 添加告警
                // wait for a while
                retryTimes++;
                int retryInterval = getRetryInterval(retryTimes);
                try {
                    Thread.sleep(retryInterval);
                } catch (Throwable e1) {
                    log.error("submit consensus sleep failed", e1);
                }
            }
        }
        log.info("package has been sent to consensus layer package pack.height={}",command.getPackageHeight());
    }

    /**
     * get retry interval by retryTimes
     */
    private static int getRetryInterval(int retryTimes) {
        if (retryTimes >= retryInterval.length) {
            return Integer.parseInt(retryInterval[retryInterval.length - 1]);
        } else {
            return Integer.parseInt(retryInterval[retryTimes]);
        }
    }

}

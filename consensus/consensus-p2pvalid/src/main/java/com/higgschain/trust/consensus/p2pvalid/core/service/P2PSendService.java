package com.higgschain.trust.consensus.p2pvalid.core.service;

import com.higgschain.trust.config.crypto.CryptoUtil;
import com.higgschain.trust.config.view.ClusterView;
import com.higgschain.trust.config.view.IClusterViewManager;
import com.higgschain.trust.consensus.config.NodeState;
import com.higgschain.trust.consensus.config.NodeStateEnum;
import com.higgschain.trust.consensus.p2pvalid.api.P2pConsensusClient;
import com.higgschain.trust.consensus.p2pvalid.core.ResponseCommand;
import com.higgschain.trust.consensus.p2pvalid.core.ValidCommand;
import com.higgschain.trust.consensus.p2pvalid.core.ValidCommandWrap;
import com.higgschain.trust.consensus.p2pvalid.core.ValidResponseWrap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * The type P 2 p send service.
 *
 * @author liuyu
 * @description
 * @date 2018 -08-20
 */
@Component @Slf4j public class P2PSendService {

    @Autowired private NodeState nodeState;
    /**
     * The View manager.
     */
    @Autowired protected IClusterViewManager viewManager;
    @Autowired private P2pConsensusClient p2pConsensusClient;
    @Autowired private ThreadPoolTaskExecutor p2pSendExecutor;
    /**
     * The Retry num.
     */
    @Value("${p2p.send.retryNum:3}") int retryNum;

    /**
     * submit command to p2p layer
     *
     * @param validCommand the valid command
     */
    public void submit(ValidCommand<?> validCommand) {
        if (!nodeState.isState(NodeStateEnum.Running)) {
            throw new RuntimeException(String.format("the node state is not running, please try again latter"));
        }
        ValidCommandWrap validCommandWrap = new ValidCommandWrap();
        validCommandWrap.setCommandClass(validCommand.getClass());
        validCommandWrap.setFromNode(nodeState.getNodeName());
        validCommandWrap.setSign(CryptoUtil.getProtocolCrypto()
            .sign(validCommand.getMessageDigestHash(), nodeState.getConsensusPrivateKey()));
        validCommandWrap.setValidCommand(validCommand);
        ClusterView view = viewManager.getView(validCommand.getView());
        if (view == null) {
            throw new RuntimeException(String.format("the view %d not exist", validCommand.getView()));
        }
        view.getNodeNames().forEach((toNodeName) -> {
            p2pSendExecutor.execute(() -> {
                execute(toNodeName, validCommandWrap);
            });
        });
    }

    /**
     * execute
     *
     * @param toNodeName
     * @param validCommandWrap
     */
    private void execute(String toNodeName, ValidCommandWrap validCommandWrap) {
        int i = 0;
        do {
            try {
                ValidResponseWrap<? extends ResponseCommand> sendValidResponse =
                    p2pConsensusClient.send(toNodeName, validCommandWrap);
                if (sendValidResponse.isSucess()) {
                    if (log.isDebugEnabled()) {
                        log.debug("p2p.send command to node:{} success,digestHash:{}", toNodeName,
                            validCommandWrap.getValidCommand().getMessageDigestHash());
                    }
                    break;
                } else {
                    log.error("p2p.send command to node:{} failed,response:{} ", toNodeName, sendValidResponse);
                }
            } catch (Throwable t) {
                log.error("p2p.send to node:{},command:{},error:{}", toNodeName, validCommandWrap.getValidCommand(), t);
            }
        } while (++i < retryNum);
    }
}

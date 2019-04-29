package com.higgschain.trust.consensus.p2pvalid.api;

import com.higgschain.trust.consensus.p2pvalid.core.ResponseCommand;
import com.higgschain.trust.consensus.p2pvalid.core.ValidCommandWrap;
import com.higgschain.trust.consensus.p2pvalid.core.ValidResponseWrap;
import com.higgschain.trust.consensus.view.IClusterViewManager;
import com.higgschain.trust.network.Address;
import com.higgschain.trust.network.NetworkManage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * The type Rpc p 2 p consensus client.
 *
 * @author duhongming
 * @date 2018 /9/18
 */
@ConditionalOnProperty(name = "network.rpc", havingValue = "netty", matchIfMissing = true) @Component @Slf4j
public class RpcP2pConsensusClient implements P2pConsensusClient {

    private static final String ACTION_TYPE_RECEIVE_COMMAND = "consensus/p2p/receive_command";
    private static final String ACTION_TYPE_RECEIVE_COMMAND_SYNC = "consensus/p2p/receive_command_sync";

    @Autowired private IClusterViewManager viewManager;

    @Autowired private NetworkManage networkManage;

    /**
     * Instantiates a new Rpc p 2 p consensus client.
     */
    public RpcP2pConsensusClient() {
        log.info("Use RpcP2pConsensusClient");
    }

    @Override public ValidResponseWrap<ResponseCommand> send(String nodeName, ValidCommandWrap validCommandWrap) {
        return send(nodeName, validCommandWrap, ACTION_TYPE_RECEIVE_COMMAND);
    }

    @Override public ValidResponseWrap<ResponseCommand> syncSend(String nodeName, ValidCommandWrap validCommandWrap) {
        return send(nodeName, validCommandWrap, ACTION_TYPE_RECEIVE_COMMAND_SYNC);
    }

    @Override
    public ValidResponseWrap<ResponseCommand> syncSendFeign(String nodeNameReg, ValidCommandWrap validCommandWrap) {
        Address to = getRandomPeerAddress();
        return send(to, validCommandWrap, ACTION_TYPE_RECEIVE_COMMAND_SYNC);
    }

    private ValidResponseWrap<ResponseCommand> send(String nodeName, ValidCommandWrap validCommandWrap, String action) {
        Address to = networkManage.getAddress(nodeName);
        if (to == null) {
            throw new RuntimeException(String.format("Node %s unavailable ", nodeName));
        }
        return send(to, validCommandWrap, action);
    }

    private ValidResponseWrap<ResponseCommand> send(Address to, ValidCommandWrap validCommandWrap, String action) {
        try {
            return networkManage.<ValidResponseWrap<ResponseCommand>>send(to, action, validCommandWrap).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private Address getRandomPeerAddress() {
        List<String> names = viewManager.getCurrentView().getNodeNames();
        String nodeName = networkManage.httpClient().getRandomPeer(names).getNodeName();
        return networkManage.getAddress(nodeName);
    }
}

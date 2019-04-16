package com.higgschain.trust.consensus.p2pvalid.core.storage;

import com.higgschain.trust.config.view.IClusterViewManager;
import com.higgschain.trust.consensus.config.NodeState;
import com.higgschain.trust.consensus.p2pvalid.api.P2pConsensusClient;
import com.higgschain.trust.consensus.p2pvalid.core.ResponseCommand;
import com.higgschain.trust.consensus.p2pvalid.core.ValidCommand;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The type Base send service.
 */
public abstract class BaseSendService {

    /**
     * The P 2 p consensus client.
     */
    @Autowired protected P2pConsensusClient p2pConsensusClient;

    /**
     * The View manager.
     */
    @Autowired protected IClusterViewManager viewManager;

    /**
     * The Node state.
     */
    @Autowired protected NodeState nodeState;

    /**
     * Send t.
     *
     * @param <T>          the type parameter
     * @param validCommand the valid command
     * @return the t
     */
    public abstract <T extends ResponseCommand> T send(ValidCommand<?> validCommand);

}

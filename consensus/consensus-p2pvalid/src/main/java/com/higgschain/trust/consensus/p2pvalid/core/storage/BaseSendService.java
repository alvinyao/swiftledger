package com.higgschain.trust.consensus.p2pvalid.core.storage;

import com.higgschain.trust.config.view.IClusterViewManager;
import com.higgschain.trust.consensus.config.NodeState;
import com.higgschain.trust.consensus.p2pvalid.api.P2pConsensusClient;
import com.higgschain.trust.consensus.p2pvalid.core.ResponseCommand;
import com.higgschain.trust.consensus.p2pvalid.core.ValidCommand;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseSendService {

    @Autowired protected P2pConsensusClient p2pConsensusClient;

    @Autowired protected IClusterViewManager viewManager;

    @Autowired protected NodeState nodeState;

    public abstract <T extends ResponseCommand> T send(ValidCommand<?> validCommand);

}

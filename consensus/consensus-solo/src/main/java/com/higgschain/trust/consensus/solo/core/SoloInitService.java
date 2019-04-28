package com.higgschain.trust.consensus.solo.core;

import com.higgschain.trust.config.master.INodeInfoService;
import com.higgschain.trust.config.snapshot.TermManager;
import com.higgschain.trust.config.view.IClusterViewService;
import com.higgschain.trust.consensus.config.NodeState;
import com.higgschain.trust.consensus.config.NodeStateEnum;
import com.higgschain.trust.consensus.config.listener.StateChangeListener;
import com.higgschain.trust.consensus.config.listener.StateListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author suimi
 * @date 2019/4/28
 */
@StateListener @Service public class SoloInitService {

    @Autowired INodeInfoService nodeInfoService;

    @Autowired IClusterViewService clusterViewService;

    @Autowired TermManager termManager;

    @Autowired NodeState nodeState;

    @StateChangeListener(NodeStateEnum.StartingConsensus) public void initView() {
        clusterViewService.initClusterViewFromDB(true);
        termManager.startNewTerm(0, nodeState.getNodeName(), nodeInfoService.getMaxHeight() + 1);
    }
}

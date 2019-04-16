/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.slave.core.service.consensus.cluster;

/**
 * @author suimi
 * @date 2018/6/11
 */

import com.higgschain.trust.consensus.config.NodeState;
import com.higgschain.trust.consensus.p2pvalid.annotation.P2pvalidReplicator;
import com.higgschain.trust.consensus.p2pvalid.core.ValidSyncCommit;
import com.higgschain.trust.slave.common.enums.SlaveErrorEnum;
import com.higgschain.trust.slave.common.exception.SlaveException;
import com.higgschain.trust.slave.core.repository.BlockRepository;
import com.higgschain.trust.slave.core.service.block.BlockService;
import com.higgschain.trust.slave.model.bo.BlockHeader;
import com.higgschain.trust.slave.model.bo.consensus.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Cluster command replicate.
 */
@Slf4j @P2pvalidReplicator @Component public class ClusterCommandReplicate {

    @Autowired private BlockRepository blockRepository;

    @Autowired private BlockService blockService;

    @Autowired private NodeState nodeState;

    /**
     * handle the consensus result of cluster height
     *
     * @param commit the commit
     * @return the list
     */
    public List<ValidClusterHeightCmd> handleClusterHeight(ValidSyncCommit<ClusterHeightCmd> commit) {
        ClusterHeightCmd operation = commit.operation();
        List<Long> maxHeights = blockRepository.getLimitHeight(operation.get());
        log.info("node={}, maxHeights={}", nodeState.getNodeName(), maxHeights);
        List<ValidClusterHeightCmd> cmds = new ArrayList<>();
        maxHeights.forEach(height -> cmds.add(new ValidClusterHeightCmd(operation.getRequestId(), height)));
        return cmds;
    }

    /**
     * handle the node state
     *
     * @param commit the commit
     * @return the valid cluster state cmd
     */
    public ValidClusterStateCmd handleNodeState(ValidSyncCommit<ClusterStateCmd> commit) {
        ClusterStateCmd operation = commit.operation();
        return new ValidClusterStateCmd(operation.getRequestId(), nodeState.getState());
    }

    /**
     * handle the consensus result of validating block header
     *
     * @param commit the commit
     * @return the valid block header cmd
     */
    public ValidBlockHeaderCmd handleValidHeader(ValidSyncCommit<BlockHeaderCmd> commit) {
        BlockHeaderCmd operation = commit.operation();
        BlockHeader header = operation.get();
        BlockHeader blockHeader = blockRepository.getBlockHeader(header.getHeight());
        if (blockHeader == null) {
            throw new SlaveException(SlaveErrorEnum.SLAVE_BLOCK_IS_NOT_EXIST);
        }
        Boolean result = blockService.compareBlockHeader(header, blockHeader);
        log.info("node ={}, valid header result={}", nodeState.getNodeName(), result);
        return new ValidBlockHeaderCmd(operation.getRequestId(), header, result);
    }
}

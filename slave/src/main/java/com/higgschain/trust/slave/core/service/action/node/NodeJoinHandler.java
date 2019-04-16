package com.higgschain.trust.slave.core.service.action.node;

import com.higgschain.trust.common.utils.Profiler;
import com.higgschain.trust.consensus.config.NodeState;
import com.higgschain.trust.consensus.core.ConsensusStateMachine;
import com.higgschain.trust.slave.common.enums.SlaveErrorEnum;
import com.higgschain.trust.slave.common.exception.SlaveException;
import com.higgschain.trust.slave.core.service.action.ActionHandler;
import com.higgschain.trust.slave.core.service.datahandler.node.NodeSnapshotHandler;
import com.higgschain.trust.slave.model.bo.action.Action;
import com.higgschain.trust.slave.model.bo.config.ClusterNode;
import com.higgschain.trust.slave.model.bo.context.ActionData;
import com.higgschain.trust.slave.model.bo.node.NodeAction;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The type Node join handler.
 */
@Slf4j @Component public class NodeJoinHandler implements ActionHandler {

    @Autowired private NodeSnapshotHandler nodeSnapshotHandler;
    @Autowired private ConsensusStateMachine consensusStateMachine;
    @Autowired private NodeState nodeState;

    @Override public void verifyParams(Action action) throws SlaveException {
        NodeAction bo = (NodeAction)action;
        if (StringUtils.isEmpty(bo.getNodeName())) {
            log.error("[verifyParams] nodeName is null or illegal param:{}", bo);
            throw new SlaveException(SlaveErrorEnum.SLAVE_PARAM_VALIDATE_ERROR);
        }
    }

    /**
     * the storage for the action
     *
     * @param actionData
     */
    @Override public void process(ActionData actionData) {
        NodeAction nodeAction = (NodeAction)actionData.getCurrentAction();
        log.info("[NodeJoinHandler.process] start to process node join action, user={}", nodeAction.getNodeName());

        Profiler.enter("[NodeJoinHandler.nodeJoin]");
        ClusterNode clusterNode = new ClusterNode();
        BeanUtils.copyProperties(nodeAction, clusterNode);
        nodeSnapshotHandler.nodeJoin(clusterNode);
        Profiler.release();

    }
}

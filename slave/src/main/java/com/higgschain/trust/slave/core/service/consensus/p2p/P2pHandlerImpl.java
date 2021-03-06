package com.higgschain.trust.slave.core.service.consensus.p2p;

import com.higgschain.trust.config.view.ClusterView;
import com.higgschain.trust.config.view.IClusterViewManager;
import com.higgschain.trust.consensus.p2pvalid.core.ValidConsensus;
import com.higgschain.trust.slave.common.enums.SlaveErrorEnum;
import com.higgschain.trust.slave.common.exception.SlaveException;
import com.higgschain.trust.slave.model.bo.BlockHeader;
import com.higgschain.trust.slave.model.bo.consensus.PersistCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The type P 2 p handler.
 *
 * @Description: handle p2p message sending and p2p message receiving
 * @author: pengdi
 */
@Slf4j @Service public class P2pHandlerImpl implements P2pHandler {

    @Autowired private ValidConsensus validConsensus;

    @Autowired private IClusterViewManager viewManager;

    /**
     * send validating result to p2p consensus layer
     *
     * @param header
     */
    @Override public void sendPersisting(BlockHeader header) {
        // validate param
        if (null == header) {
            log.error("[P2pReceiver.sendPersisting]param validate failed, cause block header is null ");
            throw new SlaveException(SlaveErrorEnum.SLAVE_PARAM_VALIDATE_ERROR);
        }

        ClusterView view = viewManager.getViewWithHeight(header.getHeight());
        // send header to p2p consensus
        PersistCommand persistCommand = new PersistCommand(header.getHeight(), header, view.getId());
        log.info("start send persisting command to p2p consensus layer, persistCommand : {}", persistCommand);
        validConsensus.submit(persistCommand);
        log.info("end send persisting command to p2p consensus layer");
    }

}

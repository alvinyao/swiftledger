/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.slave.core.service.consensus.p2p;

import com.higgschain.trust.consensus.config.NodeState;
import com.higgschain.trust.consensus.config.NodeStateEnum;
import com.higgschain.trust.consensus.p2pvalid.annotation.P2pvalidReplicator;
import com.higgschain.trust.consensus.p2pvalid.core.P2PValidCommit;
import com.higgschain.trust.slave.common.enums.SlaveErrorEnum;
import com.higgschain.trust.slave.common.exception.SlaveException;
import com.higgschain.trust.slave.common.util.beanvalidator.BeanValidateResult;
import com.higgschain.trust.slave.common.util.beanvalidator.BeanValidator;
import com.higgschain.trust.slave.core.service.pack.PackageService;
import com.higgschain.trust.slave.model.bo.BlockHeader;
import com.higgschain.trust.slave.model.bo.consensus.PersistCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The type Package valid replicate.
 *
 * @author suimi
 * @date 2018 /6/11
 */
@Slf4j @P2pvalidReplicator @Component public class PackageValidReplicate {

    @Autowired private PackageService packageService;

    @Autowired private NodeState nodeState;

    /**
     * majority of this cluster has persisted this command
     *
     * @param commit the commit
     */
    public void receivePersisted(P2PValidCommit<PersistCommand> commit) {
        // validate param
        BeanValidateResult result = BeanValidator.validate(commit);
        if (!result.isSuccess()) {
            log.error("[P2pReceiver.validated]param persist failed, cause: " + result.getFirstMsg());
            throw new SlaveException(SlaveErrorEnum.SLAVE_PARAM_VALIDATE_ERROR);
        }

        // get validated block p2p and package
        BlockHeader header = commit.operation().get();
        log.info("the package:{} persisted by p2p", header.getHeight());
        if (log.isDebugEnabled()) {
            log.debug("p2p persisted header:{}", header);
        }

        doReceive(commit, header);
    }

    /**
     * async start package process, maybe the right package is waiting for consensus
     *
     * @param commit
     * @param header
     */
    private void doReceive(P2PValidCommit commit, BlockHeader header) {
        if (!nodeState.isState(NodeStateEnum.Running)) {
            throw new SlaveException(SlaveErrorEnum.SLAVE_PACKAGE_REPLICATE_FAILED);
        }
        try {
            packageService.persisted(header,true);
        } catch (SlaveException e) {
            if (e.getCode() == SlaveErrorEnum.SLAVE_PACKAGE_HEADER_IS_NULL_ERROR) {
                return;
            }
            throw e;
        }

        commit.close();
    }

}

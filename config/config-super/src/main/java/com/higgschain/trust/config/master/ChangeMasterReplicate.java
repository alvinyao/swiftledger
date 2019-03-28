/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.config.master;

import com.higgschain.trust.config.crypto.CryptoUtil;
import com.higgschain.trust.config.master.command.ChangeMasterVerify;
import com.higgschain.trust.config.master.command.ChangeMasterVerifyCmd;
import com.higgschain.trust.config.master.command.ChangeMasterVerifyResponse;
import com.higgschain.trust.config.master.command.ChangeMasterVerifyResponseCmd;
import com.higgschain.trust.config.snapshot.TermManager;
import com.higgschain.trust.config.view.IClusterViewManager;
import com.higgschain.trust.consensus.config.NodeState;
import com.higgschain.trust.consensus.p2pvalid.annotation.P2pvalidReplicator;
import com.higgschain.trust.consensus.p2pvalid.core.ValidSyncCommit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author suimi
 * @date 2018/6/11
 */
@P2pvalidReplicator @Component public class ChangeMasterReplicate {

    @Autowired NodeState nodeState;

    @Autowired TermManager termManager;

    @Autowired IClusterViewManager viewManager;

    @Autowired
    INodeInfoService nodeInfoService;

    @Autowired ChangeMasterService changeMasterService;

    /**
     * handle the consensus result of validating block header
     *
     * @param commit
     */
    public ChangeMasterVerifyResponseCmd handleChangeMasterVerify(ValidSyncCommit<ChangeMasterVerifyCmd> commit) {
        ChangeMasterVerifyCmd operation = commit.operation();
        ChangeMasterVerify verify = operation.get();
        boolean changeMaster = false;
        if (!changeMasterService.getMasterHeartbeat().get() && verify.getTerm() == nodeState.getCurrentTerm() + 1
            && verify.getView() == viewManager.getCurrentViewId()) {
            Long maxHeight = nodeInfoService.packageHeight();
            maxHeight = maxHeight == null ? 0 : maxHeight;
            if (verify.getPackageHeight() >= maxHeight) {
                changeMaster = true;
            }
        }
        ChangeMasterVerifyResponse response =
            new ChangeMasterVerifyResponse(verify.getTerm(), verify.getView(), nodeState.getNodeName(),
                verify.getProposer(), verify.getPackageHeight(), changeMaster);
        String sign = CryptoUtil.getProtocolCrypto().sign(response.getSignValue(), nodeState.getConsensusPrivateKey());
        response.setSign(sign);
        return new ChangeMasterVerifyResponseCmd(operation.messageDigest(), response);
    }
}

package com.higgschain.trust.rs.core.callback;

import com.higgschain.trust.rs.core.vo.VotingRequest;
import com.higgschain.trust.slave.api.vo.RespData;
import com.higgschain.trust.slave.model.bo.BlockHeader;
import com.higgschain.trust.slave.model.bo.CoreTransaction;

/**
 * @author liuyu
 * @description
 * @date 2018-05-12
 */
public interface TxCallbackHandler {
    /**
     * on vote request
     *
     * @param votingRequest
     */
    void onVote(VotingRequest votingRequest);
    /**
     * on slave persisted phase,only current node persisted
     *
     * @param respData
     */
    void onPersisted(RespData<CoreTransaction> respData,BlockHeader blockHeader);

    /**
     * on slave end phase,cluster node persisted
     *
     * @param respData
     * @param blockHeader
     *          may be null
     */
    void onEnd(RespData<CoreTransaction> respData,BlockHeader blockHeader);

    /**
     * on fail over call back
     *
     * @param respData
     * @param blockHeader
     */
    void onFailover(RespData<CoreTransaction> respData,BlockHeader blockHeader);
}

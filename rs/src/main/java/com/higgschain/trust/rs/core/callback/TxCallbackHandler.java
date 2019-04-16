package com.higgschain.trust.rs.core.callback;

import com.higgschain.trust.rs.core.vo.VotingRequest;
import com.higgschain.trust.common.vo.RespData;
import com.higgschain.trust.slave.model.bo.BlockHeader;
import com.higgschain.trust.slave.model.bo.CoreTransaction;

/**
 * The interface Tx callback handler.
 *
 * @author liuyu
 * @description
 * @date 2018 -05-12
 */
public interface TxCallbackHandler {
    /**
     * on vote request
     *
     * @param votingRequest the voting request
     */
    void onVote(VotingRequest votingRequest);

    /**
     * on slave persisted phase,only current node persisted
     *
     * @param respData    the resp data
     * @param blockHeader the block header
     */
    void onPersisted(RespData<CoreTransaction> respData,BlockHeader blockHeader);

    /**
     * on slave end phase,cluster node persisted
     *
     * @param respData    the resp data
     * @param blockHeader may be null
     */
    void onEnd(RespData<CoreTransaction> respData,BlockHeader blockHeader);

    /**
     * on fail over call back
     *
     * @param respData    the resp data
     * @param blockHeader the block header
     */
    void onFailover(RespData<CoreTransaction> respData,BlockHeader blockHeader);
}

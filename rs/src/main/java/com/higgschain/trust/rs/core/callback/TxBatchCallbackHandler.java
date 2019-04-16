package com.higgschain.trust.rs.core.callback;

import com.higgschain.trust.rs.core.vo.RsCoreTxVO;
import com.higgschain.trust.rs.core.vo.VotingRequest;
import com.higgschain.trust.slave.model.bo.BlockHeader;

import java.util.List;

/**
 * The interface Tx batch callback handler.
 *
 * @author liuyu
 * @description
 * @date 2018 -05-12
 */
public interface TxBatchCallbackHandler {
    /**
     * on vote request
     *
     * @param votingRequest the voting request
     */
    void onVote(VotingRequest votingRequest);

    /**
     * on slave persisted phase,only current node persisted
     *
     * @param policyId    the policy id
     * @param txs         the txs
     * @param blockHeader the block header
     */
    void onPersisted(String policyId,List<RsCoreTxVO> txs,BlockHeader blockHeader);

    /**
     * on slave end phase,cluster node persisted
     *
     * @param policyId    the policy id
     * @param txs         the txs
     * @param blockHeader the block header
     */
    void onEnd(String policyId,List<RsCoreTxVO> txs,BlockHeader blockHeader);

    /**
     * on fail over call back
     *
     * @param policyId    the policy id
     * @param txs         the txs
     * @param blockHeader the block header
     */
    void onFailover(String policyId,List<RsCoreTxVO> txs,BlockHeader blockHeader);
}

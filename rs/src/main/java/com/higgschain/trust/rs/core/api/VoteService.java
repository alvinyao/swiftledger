package com.higgschain.trust.rs.core.api;

import com.higgschain.trust.rs.core.bo.CoreTxBO;
import com.higgschain.trust.rs.core.bo.VoteReceipt;
import com.higgschain.trust.rs.core.bo.VoteRequestRecord;
import com.higgschain.trust.rs.core.vo.ReceiptRequest;
import com.higgschain.trust.rs.core.vo.VotingRequest;
import com.higgschain.trust.slave.api.enums.manage.DecisionTypeEnum;
import com.higgschain.trust.slave.api.enums.manage.VotePatternEnum;
import com.higgschain.trust.common.vo.RespData;
import com.higgschain.trust.slave.model.bo.SignInfo;

import java.util.List;

/**
 * The interface Vote service.
 *
 * @author liuyu
 * @description
 * @date 2018 -06-06
 */
public interface VoteService {
    /**
     * request voting
     *
     * @param coreTxBO    the core tx bo
     * @param voters      the voters
     * @param votePattern the vote pattern
     * @return list
     */
    List<VoteReceipt> requestVoting(CoreTxBO coreTxBO, List<String> voters, VotePatternEnum votePattern);

    /**
     * accept voting request,return sign info
     *
     * @param votingRequest the voting request
     * @return vote receipt
     */
    VoteReceipt acceptVoting(VotingRequest votingRequest);

    /**
     * receipt vote for ASYNC pattern
     *
     * @param txId  the tx id
     * @param agree the agree
     */
    void receiptVote(String txId,boolean agree);

    /**
     * accept receipt request
     *
     * @param receiptRequest the receipt request
     * @return resp data
     */
    RespData<String> acceptReceipt(ReceiptRequest receiptRequest);

    /**
     * get signInfo from voteReceipts
     *
     * @param receipts the receipts
     * @param signType the sign type
     * @return sign infos
     */
    List<SignInfo> getSignInfos(List<VoteReceipt> receipts,SignInfo.SignTypeEnum signType);

    /**
     * get voters from sign info
     *
     * @param signInfos the sign infos
     * @param rsIds     the rs ids
     * @return voters
     */
    List<String> getVoters(List<SignInfo> signInfos, List<String> rsIds);

    /**
     * get decision from receipts
     *
     * @param receipts     the receipts
     * @param decisionType the decision type
     * @return decision
     */
    boolean getDecision(List<VoteReceipt> receipts, DecisionTypeEnum decisionType);

    /**
     * query all request for init result
     *
     * @param row   the row
     * @param count the count
     * @return list
     */
    List<VoteRequestRecord> queryAllInitRequest(int row, int count);
}

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
 * @author liuyu
 * @description
 * @date 2018-06-06
 */
public interface VoteService {
    /**
     * request voting
     *
     * @param coreTxBO
     * @param voters
     * @param votePattern
     * @return
     */
    List<VoteReceipt> requestVoting(CoreTxBO coreTxBO, List<String> voters, VotePatternEnum votePattern);

    /**
     * accept voting request,return sign info
     *
     * @param votingRequest
     * @return
     */
    VoteReceipt acceptVoting(VotingRequest votingRequest);

    /**
     * receipt vote for ASYNC pattern
     *
     * @param txId
     * @param agree
     */
    void receiptVote(String txId,boolean agree);

    /**
     * accept receipt request
     *
     * @param receiptRequest
     * @return
     */
    RespData<String> acceptReceipt(ReceiptRequest receiptRequest);
    /**
     * get signInfo from voteReceipts
     *
     * @param receipts
     * @param signType
     * @return
     */
    List<SignInfo> getSignInfos(List<VoteReceipt> receipts,SignInfo.SignTypeEnum signType);

    /**
     * get voters from sign info
     *
     * @param signInfos
     * @param rsIds
     * @return
     */
    List<String> getVoters(List<SignInfo> signInfos, List<String> rsIds);

    /**
     * get decision from receipts
     *
     * @param receipts
     * @param decisionType
     * @return
     */
    boolean getDecision(List<VoteReceipt> receipts, DecisionTypeEnum decisionType);
    /**
     * query all request for init result
     *
     * @param row
     * @param count
     * @return
     */
    List<VoteRequestRecord> queryAllInitRequest(int row, int count);
}

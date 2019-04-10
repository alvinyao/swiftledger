package com.higgschain.trust.rs.core.service;

import com.higgschain.trust.IntegrateBaseTest;
import com.higgschain.trust.rs.core.api.VoteService;
import com.higgschain.trust.rs.core.api.enums.VoteResultEnum;
import com.higgschain.trust.rs.core.bo.CoreTxBO;
import com.higgschain.trust.rs.core.bo.VoteReceipt;
import com.higgschain.trust.rs.core.repository.CoreTxRepository;
import com.higgschain.trust.rs.core.vo.ReceiptRequest;
import com.higgschain.trust.rs.core.vo.VotingRequest;
import com.higgschain.trust.slave.api.enums.manage.VotePatternEnum;
import com.higgschain.trust.common.vo.RespData;
import com.higgschain.trust.slave.core.repository.PolicyRepository;
import com.higgschain.trust.slave.model.bo.CoreTransaction;
import com.higgschain.trust.slave.model.bo.manage.Policy;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import java.util.List;

/**
 * @author liuyu
 * @description
 * @date 2018-06-08
 */
public class VoteServiceTest extends IntegrateBaseTest {
    @Autowired VoteService voteService;
    @Autowired CoreTxRepository coreTxRepository;
    @Autowired PolicyRepository policyRepository;

    @Test
    public void testRequestVoting() {
        CoreTxBO coreTxBO = coreTxRepository.convertTxBO(coreTxRepository.queryByTxId("tx_id_001",false));
        Policy policy = policyRepository.getPolicyById(coreTxBO.getPolicyId());
        List<String> voters = policy.getRsIds();
        VotePatternEnum votePattern = VotePatternEnum.SYNC;
//        VotePatternEnum votePattern = VotePatternEnum.ASYNC;
        List<VoteReceipt> receipts = voteService.requestVoting(coreTxBO, voters, votePattern);
        System.out.println(receipts);
    }

    @Test
    public void testAcceptVoting() {
        CoreTxBO coreTxBO = coreTxRepository.convertTxBO(coreTxRepository.queryByTxId("tx_id_001",false));
        CoreTransaction coreTxVo = coreTxRepository.convertTxVO(coreTxBO);
        VotingRequest votingRequest =
            new VotingRequest("TRUST-TEST1", coreTxVo, VotePatternEnum.SYNC.getCode());
        VoteReceipt voteReceipt = voteService.acceptVoting(votingRequest);
        System.out.println(voteReceipt);
    }

    @Test
    public void testReceiptVote() {
        String txId = "tx_id_001";
        boolean agree = true;
        voteService.receiptVote(txId,agree);
    }

    @Test
    public void testAcceptReceipt() {
        ReceiptRequest receiptRequest = new ReceiptRequest();
        receiptRequest.setTxId("tx_id_001");
        receiptRequest.setVoter("TRUST-TEST1");
        receiptRequest.setSign("sign");
        receiptRequest.setVoteResult(VoteResultEnum.AGREE.getCode());
        RespData<String> respData = voteService.acceptReceipt(receiptRequest);
        System.out.println(respData);
    }
}

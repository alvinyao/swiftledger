/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.rs.core.controller;

import com.higgschain.trust.rs.core.api.VoteService;
import com.higgschain.trust.rs.core.bo.VoteReceipt;
import com.higgschain.trust.rs.core.vo.ReceiptRequest;
import com.higgschain.trust.rs.core.vo.VotingRequest;
import com.higgschain.trust.common.vo.RespData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Voting controller.
 *
 * @author liuyu
 * @date 2018 /5/12
 */
@RestController
@Slf4j
public class VotingController {
    @Autowired
    private VoteService voteService;

    /**
     * request voting
     *
     * @param votingRequest the voting request
     * @return vote receipt
     */
    @RequestMapping(value = "/voting")
    VoteReceipt acceptVoting(@RequestBody VotingRequest votingRequest) {
        return voteService.acceptVoting(votingRequest);
    }

    /**
     * request receipting
     *
     * @param receiptRequest the receipt request
     * @return resp data
     */
    @RequestMapping(value = "/receipting")
    RespData<String> receiptVote(@RequestBody ReceiptRequest receiptRequest) {
        return voteService.acceptReceipt(receiptRequest);
    }

    /**
     * receipt vote for test
     *
     * @param txId  the tx id
     * @param agree the agree
     * @return
     */
    @RequestMapping(value = "/receiptVote")
    void receiptVote(String txId, boolean agree) {
        voteService.receiptVote(txId, agree);
    }
}

/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.rs.core.integration;

import com.higgschain.trust.rs.core.bo.VoteReceipt;
import com.higgschain.trust.rs.core.vo.ReceiptRequest;
import com.higgschain.trust.rs.core.vo.VotingRequest;
import com.higgschain.trust.common.vo.RespData;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * The interface Service provider client.
 */
//@FeignClient("${higgs.trust.prefix}")
public interface ServiceProviderClient {
    /**
     * voting transaction
     *
     * @param nodeName      the node name
     * @param votingRequest the voting request
     * @return vote receipt
     */
    @RequestMapping(value = "/voting", method = RequestMethod.POST)
    VoteReceipt voting(String nodeName, @RequestBody VotingRequest votingRequest);

    /**
     * receipt vote
     *
     * @param nodeName       the node name
     * @param receiptRequest the receipt request
     * @return resp data
     */
    @RequestMapping(value = "/receipting", method = RequestMethod.POST)
    RespData<String> receipting(String nodeName, @RequestBody ReceiptRequest receiptRequest);

}

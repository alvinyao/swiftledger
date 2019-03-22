package com.higgs.trust.rs.core.controller;

import com.higgs.trust.rs.core.api.MultiSignService;
import com.higgs.trust.rs.core.api.VoteService;
import com.higgs.trust.rs.core.bo.VoteReceipt;
import com.higgs.trust.rs.core.vo.MultiSignRuleVO;
import com.higgs.trust.rs.core.vo.ReceiptRequest;
import com.higgs.trust.rs.core.vo.VotingRequest;
import com.higgs.trust.slave.api.vo.RespData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuyu
 * @date 2019/3/21
 */

@RequestMapping(path = "/multiSign")
@RestController
@Slf4j
public class MultiSignController {
    @Autowired
    private MultiSignService multiSignService;

    /**
     * create address
     *
     * @param rule
     * @return
     */
    @RequestMapping(value = "/create")
    RespData<String> create(@RequestBody MultiSignRuleVO rule) {
        return multiSignService.createAddress(rule);
    }
}

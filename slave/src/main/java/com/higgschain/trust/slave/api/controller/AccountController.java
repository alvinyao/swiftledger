/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.slave.api.controller;

import com.higgschain.trust.slave.api.AccountInfoService;
import com.higgschain.trust.slave.api.vo.AccountInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The type Account controller.
 *
 * @author suimi
 * @date 2018 /4/24
 */
@RestController @Slf4j @RequestMapping("/account") public class AccountController {

    @Autowired private AccountInfoService accountInfoService;

    /**
     * batch query the account info
     *
     * @param accountNos the account nos
     * @return list
     */
    @RequestMapping(value = "/batchQuery") List<AccountInfoVO> queryByAccountNos(
        @RequestBody List<String> accountNos) {
        return accountInfoService.queryByAccountNos(accountNos);
    }
}

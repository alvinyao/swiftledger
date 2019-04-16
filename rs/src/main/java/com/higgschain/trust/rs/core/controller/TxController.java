package com.higgschain.trust.rs.core.controller;

import com.higgschain.trust.rs.core.api.CoreTransactionService;
import com.higgschain.trust.common.vo.RespData;
import com.higgschain.trust.slave.model.bo.CoreTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Tx controller.
 *
 * @author liuyu
 * @description
 * @date 2018 -06-22
 */
@RestController
public class TxController {
    @Autowired
    private CoreTransactionService coreTransactionService;

    /**
     * Submit tx resp data.
     *
     * @param coreTx the core tx
     * @return the resp data
     */
    @RequestMapping(value = "/submitTx")
    RespData submitTx(@RequestBody CoreTransaction coreTx) {
        coreTransactionService.submitTx(coreTx);
        return coreTransactionService.syncWait(coreTx.getTxId(), true);
    }
}

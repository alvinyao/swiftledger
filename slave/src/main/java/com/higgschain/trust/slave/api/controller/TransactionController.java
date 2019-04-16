package com.higgschain.trust.slave.api.controller;

import com.higgschain.trust.evmcontract.core.TransactionResultInfo;
import com.higgschain.trust.slave.api.BlockChainService;
import com.higgschain.trust.common.vo.RespData;
import com.higgschain.trust.slave.api.vo.TransactionVO;
import com.higgschain.trust.slave.core.Blockchain;
import com.higgschain.trust.slave.model.bo.SignedTransaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * transaction tx
 *
 * @author lingchao
 * @create 2018年04月18日14 :29
 */
@RequestMapping(value = "/transaction")
@RestController
@Slf4j
public class TransactionController {
    @Autowired
    private BlockChainService blockChainService;
    @Autowired
    private Blockchain blockchain;

    /**
     * Submit transactions resp data.
     *
     * @param transactions the transactions
     * @return the resp data
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public RespData submitTransactions(@RequestBody List<SignedTransaction> transactions) {
        if (log.isDebugEnabled()) {
            log.debug("submit transactions receive parameter :{}", transactions);
        }
        return blockChainService.submitTransactions(transactions);
    }

    /**
     * Submit transaction resp data.
     *
     * @param transaction the transaction
     * @return the resp data
     */
    @RequestMapping(value = "/post", method = RequestMethod.POST)
    @ResponseBody
    public RespData submitTransaction(@RequestBody SignedTransaction transaction) {

        if (log.isDebugEnabled()) {
            log.debug("submit transaction receive parameter :{}", transaction);
        }

        List<SignedTransaction> signedTransactionList = new ArrayList<>();
        signedTransactionList.add(transaction);
        return blockChainService.submitTransactions(signedTransactionList);
    }

    /**
     * Master receive resp data.
     *
     * @param transactions the transactions
     * @return the resp data
     */
    @RequestMapping(value = "/master/submit", method = RequestMethod.POST)
    @ResponseBody
    public RespData<List<TransactionVO>> masterReceive(@RequestBody List<SignedTransaction> transactions) {
        if (log.isDebugEnabled()) {
            log.debug("master receive transactions, parameter :{}", transactions);
        }
        return blockChainService.submitToMaster(transactions);
    }

    /**
     * Query result map.
     *
     * @param txId the tx id
     * @return the map
     */
    @GetMapping("result/{txId}")
    public Map<String, Object> queryResult(@PathVariable("txId") String txId) {
        TransactionResultInfo resultInfo =  blockchain.getTransactionResultInfo(txId);
        return resultInfo.toMap();
    }
}

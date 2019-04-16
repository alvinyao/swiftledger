package com.higgschain.trust.slave.core.service.transaction;

import com.higgschain.trust.slave.model.bo.TransactionReceipt;
import com.higgschain.trust.slave.model.bo.context.TransactionData;

import java.util.Map;

/**
 * The interface Transaction executor.
 *
 * @Description:
 * @author: pengdi
 */
public interface TransactionExecutor {

    /**
     * persist all transactions,return validate results and persistedDatas
     *
     * @param transactionData the transaction data
     * @param rsPubKeyMap     the rs pub key map
     * @return transaction receipt
     */
    TransactionReceipt process(TransactionData transactionData, Map<String, String> rsPubKeyMap);
}

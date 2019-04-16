package com.higgschain.trust.slave.core.service.pending;

import com.higgschain.trust.slave.api.vo.TransactionVO;
import com.higgschain.trust.slave.model.bo.SignedTransaction;

import java.util.List;

/**
 * The interface Pending state.
 *
 * @Description: hold all the processing transaction
 * @author: pengdi
 */
public interface PendingState {
    /**
     * Add new transactions into pending transaction pool
     *
     * @param transactions the transactions
     * @return list
     */
    List<TransactionVO> addPendingTransactions(List<SignedTransaction> transactions);

    /**
     * Get a specified number(count) of transactions
     *
     * @param count the count
     * @return object [ ]
     */
    Object[] getPendingTransactions(int count);

    /**
     * Add pending txs to queue first.
     *
     * @param signedTransactions the signed transactions
     */
    void addPendingTxsToQueueFirst(List<SignedTransaction> signedTransactions);
}

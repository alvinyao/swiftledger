package com.higgschain.trust.slave.api;

import com.higgschain.trust.slave.model.bo.BlockHeader;
import com.higgschain.trust.slave.model.bo.SignedTransaction;
import com.higgschain.trust.slave.model.bo.TransactionReceipt;

import java.util.List;
import java.util.Map;

/**
 * @author liuyu
 * @description
 * @date 2018-07-27
 */
public interface SlaveBatchCallbackHandler {
    /**
     * on tx persisted
     *
     * @param txs
     * @param txReceipts
     * @param blockHeader
     */
    void onPersisted(List<SignedTransaction> txs, Map<String, TransactionReceipt> txReceipts,BlockHeader blockHeader);

    /**
     * when the cluster persisted of tx
     *
     * @param txs
     * @param txReceipts
     * @param blockHeader
     */
    void onClusterPersisted(List<SignedTransaction> txs, Map<String, TransactionReceipt> txReceipts,BlockHeader blockHeader);

    /**
     * on failover
     *
     * @param txs
     * @param txReceipts
     * @param blockHeader
     */
    void onFailover(List<SignedTransaction> txs, Map<String, TransactionReceipt> txReceipts,BlockHeader blockHeader);
}

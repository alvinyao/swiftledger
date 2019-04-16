package com.higgschain.trust.slave.dao.rocks.transaction;

import com.higgschain.trust.common.dao.RocksBaseDao;
import com.higgschain.trust.common.utils.ThreadLocalUtils;
import com.higgschain.trust.slave.common.enums.SlaveErrorEnum;
import com.higgschain.trust.slave.common.exception.SlaveException;
import com.higgschain.trust.slave.dao.po.transaction.TransactionReceiptPO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.rocksdb.Transaction;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The type Transaction rocks dao.
 *
 * @author tangfashuang
 */
@Service
@Slf4j
public class TransactionRocksDao extends RocksBaseDao<TransactionReceiptPO> {
    @Override protected String getColumnFamilyName() {
        return "transaction";
    }

    /**
     * Query tx receipts list.
     *
     * @param txIds the tx ids
     * @return the list
     */
    public List<TransactionReceiptPO> queryTxReceipts(List<String> txIds) {

        if (CollectionUtils.isEmpty(txIds)) {
            log.error("[TransactionRocksDao.queryTxReceipts] txIds is empty");
            return null;
        }

        Map<String, TransactionReceiptPO> resultMap = multiGet(txIds);
        if (MapUtils.isEmpty(resultMap)) {
            return null;
        }

        List<TransactionReceiptPO> pos = new ArrayList<>(resultMap.size());
        for (String key : resultMap.keySet()) {
            pos.add(resultMap.get(key));
        }

        return pos;
    }

    /**
     * Query tx ids list.
     *
     * @param txIds the tx ids
     * @return the list
     */
    public List<String> queryTxIds(List<String> txIds) {

        if (CollectionUtils.isEmpty(txIds)) {
            log.error("[TransactionRocksDao.queryTxIds] txIds is empty");
            return null;
        }

        return multiGetKeys(txIds);
    }

    /**
     * Batch insert.
     *
     * @param receiptPOS the receipt pos
     */
    public void batchInsert(List<TransactionReceiptPO> receiptPOS) {
        if (CollectionUtils.isEmpty(receiptPOS)) {
            log.info("[TransactionRocksDao.batchInsert] receiptPOS is empty");
            return;
        }

        Transaction tx = ThreadLocalUtils.getRocksTx();
        if (null == tx) {
            log.error("[TransactionRocksDao.batchInsert] transaction is null");
            throw new SlaveException(SlaveErrorEnum.SLAVE_ROCKS_TRANSACTION_IS_NULL);
        }
        for (TransactionReceiptPO po : receiptPOS) {
            txPut(tx, po.getTxId(), po);
        }
    }
}

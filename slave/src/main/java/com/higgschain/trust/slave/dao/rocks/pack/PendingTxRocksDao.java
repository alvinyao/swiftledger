package com.higgschain.trust.slave.dao.rocks.pack;

import com.higgschain.trust.common.dao.RocksBaseDao;
import com.higgschain.trust.common.utils.ThreadLocalUtils;
import com.higgschain.trust.slave.common.enums.SlaveErrorEnum;
import com.higgschain.trust.slave.common.exception.SlaveException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.rocksdb.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Pending tx rocks dao.
 *
 * @author tangfashuang
 * @desc key : txId, value: height
 */
@Slf4j @Service public class PendingTxRocksDao extends RocksBaseDao<Long> {
    @Override protected String getColumnFamilyName() {
        return "pendingTransaction";
    }

    /**
     * Gets tx ids.
     *
     * @param txIds the tx ids
     * @return the tx ids
     */
    public List<String> getTxIds(List<String> txIds) {
        if (CollectionUtils.isEmpty(txIds)) {
            log.error("[PendingTxRocksDao.getTxIds] txIds is null");
            return null;
        }
        return multiGetKeys(txIds);
    }

    /**
     * Batch insert.
     *
     * @param txIds      the tx ids
     * @param packHeight the pack height
     */
    public void batchInsert(List<String> txIds, Long packHeight) {
        if (CollectionUtils.isEmpty(txIds) || packHeight == null || packHeight < 1) {
            log.error("[PendingTxRocksDao.batchInsert] txIds is empty or packHeight is null");
            return;
        }
        Transaction tx = ThreadLocalUtils.getRocksTx();
        if (null == tx) {
            log.error("[PendingTxRocksDao.batchInsert] transaction is null");
            throw new SlaveException(SlaveErrorEnum.SLAVE_ROCKS_TRANSACTION_IS_NULL);
        }

        for (String txId : txIds) {
            txPut(tx, txId, packHeight);
        }
    }

    /**
     * Batch delete.
     *
     * @param txId the tx id
     */
    public void batchDelete(String txId) {
        Transaction tx = ThreadLocalUtils.getRocksTx();
        if (null == tx) {
            log.error("[PackStatusRocksDao.batchDelete] transaction is null");
            throw new SlaveException(SlaveErrorEnum.SLAVE_ROCKS_TRANSACTION_IS_NULL);
        }

        txDelete(tx, txId);
    }

}

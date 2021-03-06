package com.higgschain.trust.rs.core.dao.rocks;

import com.higgschain.trust.common.dao.RocksBaseDao;
import com.higgschain.trust.common.utils.ThreadLocalUtils;
import com.higgschain.trust.rs.common.enums.RsCoreErrorEnum;
import com.higgschain.trust.rs.common.exception.RsCoreException;
import com.higgschain.trust.rs.core.dao.po.CoreTransactionPO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.rocksdb.Transaction;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * The type Core tx rocks dao.
 *
 * @author tangfashuang
 */
@Service
@Slf4j
public class CoreTxRocksDao extends RocksBaseDao<CoreTransactionPO>{
    @Override protected String getColumnFamilyName() {
        return "coreTransaction";
    }

    /**
     * db transaction
     *
     * @param po the po
     */
    public void saveWithTransaction(CoreTransactionPO po) {
        Transaction tx = ThreadLocalUtils.getRocksTx();
        if (null == tx) {
            log.error("[CoreTxRocksDao.saveWithTransaction] transaction is null");
            throw new RsCoreException(RsCoreErrorEnum.RS_CORE_ROCKS_TRANSACTION_IS_NULL);
        }

        String key = po.getTxId();
        if (keyMayExist(key) && null != get(key)) {
            log.error("[CoreTxRocksDao.saveWithTransaction] core transaction is already exist, key={}", key);
            throw new RsCoreException(RsCoreErrorEnum.RS_CORE_ROCKS_KEY_ALREADY_EXIST);
        }
        po.setCreateTime(new Date());
        txPut(tx, key, po);
    }

    /**
     * Query by tx ids list.
     *
     * @param txIdList the tx id list
     * @return the list
     */
    public List<CoreTransactionPO> queryByTxIds(List<String> txIdList) {
        if (CollectionUtils.isEmpty(txIdList)) {
            return null;
        }

        Map<String, CoreTransactionPO> resultMap = multiGet(txIdList);
        if (MapUtils.isEmpty(resultMap)) {
            return null;
        }

        List<CoreTransactionPO> pos = new ArrayList<>(resultMap.size());
        for (String key : resultMap.keySet()) {
            pos.add(resultMap.get(key));
        }
        return pos;
    }

    /**
     * Update sign datas.
     *
     * @param txId     the tx id
     * @param signJSON the sign json
     */
    public void updateSignDatas(String txId, String signJSON) {
        Transaction tx = ThreadLocalUtils.getRocksTx();
        if (null == tx) {
            log.error("[CoreTxRocksDao.updateSignDatas] transaction is null");
            throw new RsCoreException(RsCoreErrorEnum.RS_CORE_ROCKS_TRANSACTION_IS_NULL);
        }

        CoreTransactionPO po = get(txId);
        if (null == po) {
            log.error("[CoreTxRocksDao.updateSignDatas] core transaction is not exist, txId={}", txId);
            throw new RsCoreException(RsCoreErrorEnum.RS_CORE_ROCKS_KEY_IS_NOT_EXIST);
        }
        po.setUpdateTime(new Date());
        po.setSignDatas(signJSON);
        txPut(tx, txId, po);
    }

    /**
     * Save execute result and height.
     *
     * @param txId        the tx id
     * @param result      the result
     * @param respCode    the resp code
     * @param respMsg     the resp msg
     * @param blockHeight the block height
     */
    public void saveExecuteResultAndHeight(String txId, String result, String respCode, String respMsg,
        Long blockHeight) {
        Transaction tx = ThreadLocalUtils.getRocksTx();
        if (null == tx) {
            log.error("[CoreTxRocksDao.saveExecuteResultAndHeight] transaction is null");
            throw new RsCoreException(RsCoreErrorEnum.RS_CORE_ROCKS_TRANSACTION_IS_NULL);
        }

        CoreTransactionPO po = get(txId);
        if (null == po) {
            log.error("[CoreTxRocksDao.saveExecuteResultAndHeight] core transaction is not exist, txId={}", txId);
            throw new RsCoreException(RsCoreErrorEnum.RS_CORE_ROCKS_KEY_IS_NOT_EXIST);
        }
        po.setUpdateTime(new Date());
        po.setExecuteResult(result);
        po.setErrorCode(respCode);
        po.setErrorMsg(respMsg);
        po.setBlockHeight(blockHeight);
        txPut(tx, txId, po);
    }

    /**
     * Update with transaction.
     *
     * @param po the po
     */
    public void updateWithTransaction(CoreTransactionPO po) {
        Transaction tx = ThreadLocalUtils.getRocksTx();
        if (null == tx) {
            log.error("[CoreTxRocksDao.updateWithTransaction] transaction is null");
            throw new RsCoreException(RsCoreErrorEnum.RS_CORE_ROCKS_TRANSACTION_IS_NULL);
        }
        txPut(tx, po.getTxId(), po);
    }

    /**
     * Batch insert.
     *
     * @param coreTransactionPOList the core transaction po list
     */
    public void batchInsert(List<CoreTransactionPO> coreTransactionPOList) {
        if (CollectionUtils.isEmpty(coreTransactionPOList)) {
            return;
        }

        Transaction tx = ThreadLocalUtils.getRocksTx();
        if (null == tx) {
            log.error("[CoreTxRocksDao.batchInsert] transaction is null");
            throw new RsCoreException(RsCoreErrorEnum.RS_CORE_ROCKS_TRANSACTION_IS_NULL);
        }

        for (CoreTransactionPO po : coreTransactionPOList) {
            po.setCreateTime(new Date());
            txPut(tx, po.getTxId(), po);
        }
    }

    /**
     * Failover batch insert.
     *
     * @param coreTransactionPOList the core transaction po list
     */
    public void failoverBatchInsert(List<CoreTransactionPO> coreTransactionPOList) {
        if (CollectionUtils.isEmpty(coreTransactionPOList)) {
            return;
        }

        Transaction tx = ThreadLocalUtils.getRocksTx();
        if (null == tx) {
            log.error("[CoreTxRocksDao.failoverBatchInsert] transaction is null");
            throw new RsCoreException(RsCoreErrorEnum.RS_CORE_ROCKS_TRANSACTION_IS_NULL);
        }

        for (CoreTransactionPO po : coreTransactionPOList) {
            String txId = po.getTxId();
            CoreTransactionPO oldPo = get(txId);
            if (null == oldPo) {
                txPut(tx, txId, po);
            } else {
                oldPo.setUpdateTime(new Date());
                oldPo.setBlockHeight(po.getBlockHeight());
                oldPo.setErrorCode(po.getErrorCode());
                oldPo.setErrorMsg(po.getErrorMsg());
                oldPo.setExecuteResult(po.getExecuteResult());
                txPut(tx, txId, oldPo);
            }
        }
    }
}

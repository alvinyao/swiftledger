package com.higgschain.trust.slave.dao.rocks.utxo;

import com.higgschain.trust.common.dao.RocksBaseDao;
import com.higgschain.trust.common.utils.ThreadLocalUtils;
import com.higgschain.trust.slave.common.enums.SlaveErrorEnum;
import com.higgschain.trust.slave.common.exception.SlaveException;
import com.higgschain.trust.slave.dao.po.utxo.TxOutPO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.rocksdb.Transaction;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * The type Tx out rocks dao.
 *
 * @author tangfashuang
 */
@Service
@Slf4j
public class TxOutRocksDao extends RocksBaseDao<TxOutPO>{
    @Override protected String getColumnFamilyName() {
        return "txOut";
    }

    /**
     * Batch insert int.
     *
     * @param txOutPOList the tx out po list
     * @return the int
     */
    public int batchInsert(List<TxOutPO> txOutPOList) {
        if (CollectionUtils.isEmpty(txOutPOList)) {
            return 0;
        }

        Transaction tx = ThreadLocalUtils.getRocksTx();
        if (null == tx) {
            log.error("[TxOutRocksDao.batchInsert] transaction is null");
            throw new SlaveException(SlaveErrorEnum.SLAVE_ROCKS_TRANSACTION_IS_NULL);
        }

        for (TxOutPO po : txOutPOList) {
            String key = po.getTxId() + "_" + po.getIndex() + "_" + po.getActionIndex();
            if (null == po.getCreateTime()) {
                po.setCreateTime(new Date());
            } else {
                po.setUpdateTime(new Date());
            }
            txPut(tx, key, po);
        }
        return txOutPOList.size();
    }
}

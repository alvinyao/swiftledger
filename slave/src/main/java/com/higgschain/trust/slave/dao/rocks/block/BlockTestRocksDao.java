package com.higgschain.trust.slave.dao.rocks.block;

import com.higgschain.trust.common.dao.RocksBaseDao;
import com.higgschain.trust.common.utils.ThreadLocalUtils;
import com.higgschain.trust.slave.common.enums.SlaveErrorEnum;
import com.higgschain.trust.slave.common.exception.SlaveException;
import com.higgschain.trust.slave.dao.po.block.BlockPO;
import lombok.extern.slf4j.Slf4j;
import org.rocksdb.Transaction;
import org.springframework.stereotype.Service;

/**
 * The type Block test rocks dao.
 *
 * @author tangfashuang
 */
@Service
@Slf4j
public class BlockTestRocksDao extends RocksBaseDao<BlockPO> {
    @Override protected String getColumnFamilyName() {
        return "blockTest";
    }

    /**
     * Save.
     *
     * @param po the po
     */
    public void save(BlockPO po) {
        Transaction tx = ThreadLocalUtils.getRocksTx();
        if (null == tx) {
            log.error("[BlockTestRocksDao.save] transaction is null");
            throw new SlaveException(SlaveErrorEnum.SLAVE_ROCKS_TRANSACTION_IS_NULL);
        }

        String height = String.valueOf(po.getHeight());
        if (keyMayExist(height) && null != get(height)) {
            throw new SlaveException(SlaveErrorEnum.SLAVE_ROCKS_KEY_ALREADY_EXIST);
        }
        po.setSignedTxs(null);

        txPut(tx, height, po);
    }
}

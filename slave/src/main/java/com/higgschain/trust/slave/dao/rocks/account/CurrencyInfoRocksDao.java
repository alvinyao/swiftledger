package com.higgschain.trust.slave.dao.rocks.account;

import com.higgschain.trust.common.dao.RocksBaseDao;
import com.higgschain.trust.common.utils.ThreadLocalUtils;
import com.higgschain.trust.slave.common.enums.SlaveErrorEnum;
import com.higgschain.trust.slave.common.exception.SlaveException;
import com.higgschain.trust.slave.dao.po.account.CurrencyInfoPO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.rocksdb.Transaction;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * The type Currency info rocks dao.
 *
 * @author tangfashuang
 */
@Service
@Slf4j
public class CurrencyInfoRocksDao extends RocksBaseDao<CurrencyInfoPO> {

    @Override protected String getColumnFamilyName() {
        return "currencyInfo";
    }

    /**
     * Batch insert.
     *
     * @param currencyInfos the currency infos
     */
    public void batchInsert(List<CurrencyInfoPO> currencyInfos) {
        if (CollectionUtils.isEmpty(currencyInfos)) {
            return;
        }

        Transaction tx = ThreadLocalUtils.getRocksTx();
        if (null == tx) {
            log.error("[AccountInfoRocksDao.batchInsert] transaction is null");
            throw new SlaveException(SlaveErrorEnum.SLAVE_ROCKS_TRANSACTION_IS_NULL);
        }

        for (CurrencyInfoPO currencyInfo : currencyInfos) {
            currencyInfo.setCreateTime(new Date());
            txPut(tx, currencyInfo.getCurrency(), currencyInfo);
        }
    }
}

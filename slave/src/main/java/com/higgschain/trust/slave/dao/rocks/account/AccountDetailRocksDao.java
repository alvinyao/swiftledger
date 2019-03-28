package com.higgschain.trust.slave.dao.rocks.account;

import com.higgschain.trust.common.constant.Constant;
import com.higgschain.trust.common.dao.RocksBaseDao;
import com.higgschain.trust.common.utils.ThreadLocalUtils;
import com.higgschain.trust.slave.common.enums.SlaveErrorEnum;
import com.higgschain.trust.slave.common.exception.SlaveException;
import com.higgschain.trust.slave.dao.po.account.AccountDetailPO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.rocksdb.Transaction;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 *
 * @author tangfashuang
 */
@Service
@Slf4j
public class AccountDetailRocksDao extends RocksBaseDao<AccountDetailPO> {

    @Override protected String getColumnFamilyName() {
        return "accountDetail";
    }

    public void batchInsert(List<AccountDetailPO> pos) {
        if (CollectionUtils.isEmpty(pos)) {
            return;
        }

        Transaction tx = ThreadLocalUtils.getRocksTx();
        if (null == tx) {
            log.error("[AccountDetailRocksDao.batchInsert] transaction is null");
            throw new SlaveException(SlaveErrorEnum.SLAVE_ROCKS_TRANSACTION_IS_NULL);
        }
        for (AccountDetailPO po : pos) {
            String key = po.getBizFlowNo() + Constant.SPLIT_SLASH + po.getAccountNo();
            po.setCreateTime(new Date());
            txPut(tx, key, po);
        }
    }
}

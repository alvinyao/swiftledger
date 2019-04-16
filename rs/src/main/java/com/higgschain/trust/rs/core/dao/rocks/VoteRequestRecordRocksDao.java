package com.higgschain.trust.rs.core.dao.rocks;

import com.higgschain.trust.common.dao.RocksBaseDao;
import com.higgschain.trust.common.utils.ThreadLocalUtils;
import com.higgschain.trust.rs.common.enums.RsCoreErrorEnum;
import com.higgschain.trust.rs.common.exception.RsCoreException;
import com.higgschain.trust.rs.core.dao.po.VoteRequestRecordPO;
import lombok.extern.slf4j.Slf4j;
import org.rocksdb.Transaction;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * The type Vote request record rocks dao.
 *
 * @author tangfashuang
 * @desc key : txId, value: voteRequestRecordPO
 */
@Service
@Slf4j
public class VoteRequestRecordRocksDao extends RocksBaseDao<VoteRequestRecordPO> {
    @Override protected String getColumnFamilyName() {
        return "voteRequestRecord";
    }

    /**
     * Save with transaction.
     *
     * @param voteRequestRecordPO the vote request record po
     */
    public void saveWithTransaction(VoteRequestRecordPO voteRequestRecordPO) {
        Transaction tx = ThreadLocalUtils.getRocksTx();
        if (null == tx) {
            log.error("[VoteRequestRecordRocksDao.saveWithTransaction] transaction is null");
            throw new RsCoreException(RsCoreErrorEnum.RS_CORE_ROCKS_TRANSACTION_IS_NULL);
        }

        String key = voteRequestRecordPO.getTxId();
        if (keyMayExist(key) && null != get(key)) {
            log.error("[VoteRequestRecordRocksDao.save] vote request record is exist, txId={}", key);
            throw new RsCoreException(RsCoreErrorEnum.RS_CORE_ROCKS_KEY_ALREADY_EXIST);
        }

        voteRequestRecordPO.setCreateTime(new Date());
        txPut(tx, key, voteRequestRecordPO);
    }

    /**
     * Sets vote result.
     *
     * @param txId the tx id
     * @param sign the sign
     * @param code the code
     */
    public void setVoteResult(String txId, String sign, String code) {
        Transaction tx = ThreadLocalUtils.getRocksTx();
        if (null == tx) {
            log.error("[VoteRequestRecordRocksDao.setVoteResult] transaction is null");
            throw new RsCoreException(RsCoreErrorEnum.RS_CORE_ROCKS_TRANSACTION_IS_NULL);
        }

        VoteRequestRecordPO po = get(txId);
        if (null == po) {
            log.error("[VoteRequestRecordRocksDao.setVoteResult] vote request record is null, txId={}", txId);
            throw new RsCoreException(RsCoreErrorEnum.RS_CORE_ROCKS_KEY_IS_NOT_EXIST);
        }

        po.setUpdateTime(new Date());
        po.setVoteResult(code);
        po.setSign(sign);

        txPut(tx, txId, po);
    }
}

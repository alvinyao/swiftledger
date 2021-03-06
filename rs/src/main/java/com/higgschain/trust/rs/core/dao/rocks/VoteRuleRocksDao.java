package com.higgschain.trust.rs.core.dao.rocks;

import com.higgschain.trust.common.dao.RocksBaseDao;
import com.higgschain.trust.common.utils.ThreadLocalUtils;
import com.higgschain.trust.rs.common.enums.RsCoreErrorEnum;
import com.higgschain.trust.rs.common.exception.RsCoreException;
import com.higgschain.trust.rs.core.dao.po.VoteRulePO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.rocksdb.Transaction;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * The type Vote rule rocks dao.
 *
 * @author tangfashuang
 * @desc key : policyId, value: voteRulePO
 */
@Service
@Slf4j
public class VoteRuleRocksDao extends RocksBaseDao<VoteRulePO>{

    @Override protected String getColumnFamilyName() {
        return "voteRule";
    }

    /**
     * Save with transaction.
     *
     * @param voteRulePO the vote rule po
     */
    public void saveWithTransaction(VoteRulePO voteRulePO) {
        Transaction tx = ThreadLocalUtils.getRocksTx();
        if (null == tx) {
            log.error("[VoteRuleRocksDao.saveWithTransaction] transaction is null");
            throw new RsCoreException(RsCoreErrorEnum.RS_CORE_ROCKS_TRANSACTION_IS_NULL);
        }

        String policyId = voteRulePO.getPolicyId();
        if (keyMayExist(policyId) && null != get(policyId)) {
            log.error("[VoteRuleRocksDao.saveWithTransaction] vote rule  is already exist, policyId={}", policyId);
            throw new RsCoreException(RsCoreErrorEnum.RS_CORE_ROCKS_KEY_ALREADY_EXIST);
        }
        voteRulePO.setCreateTime(new Date());
        txPut(tx, policyId, voteRulePO);
    }

    /**
     * Batch insert.
     *
     * @param voteRulePOs the vote rule p os
     */
    public void batchInsert(List<VoteRulePO> voteRulePOs) {
        if (CollectionUtils.isEmpty(voteRulePOs)) {
            return;
        }

        Transaction tx = ThreadLocalUtils.getRocksTx();
        if (null == tx) {
            log.error("[VoteRuleRocksDao.batchInsert] transaction is null");
            throw new RsCoreException(RsCoreErrorEnum.RS_CORE_ROCKS_TRANSACTION_IS_NULL);
        }

        for (VoteRulePO po : voteRulePOs) {
            po.setCreateTime(new Date());
            txPut(tx, po.getPolicyId(), po);
        }
    }
}

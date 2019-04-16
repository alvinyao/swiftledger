package com.higgschain.trust.slave.dao.rocks.config;

import com.higgschain.trust.common.dao.RocksBaseDao;
import com.higgschain.trust.common.utils.ThreadLocalUtils;
import com.higgschain.trust.slave.common.enums.SlaveErrorEnum;
import com.higgschain.trust.slave.common.exception.SlaveException;
import com.higgschain.trust.slave.dao.po.config.ClusterConfigPO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.rocksdb.Transaction;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * The type Cluster config rocks dao.
 *
 * @author tangfashuang
 */
@Service
@Slf4j
public class ClusterConfigRocksDao extends RocksBaseDao<ClusterConfigPO>{
    @Override protected String getColumnFamilyName() {
        return "clusterConfig";
    }

    /**
     * Save with transaction.
     *
     * @param clusterConfigPO the cluster config po
     */
    public void saveWithTransaction(ClusterConfigPO clusterConfigPO) {
        Transaction tx = ThreadLocalUtils.getRocksTx();
        if (null == tx) {
            log.error("[ClusterConfigRocksDao.saveWithTransaction] transaction is null");
            throw new SlaveException(SlaveErrorEnum.SLAVE_ROCKS_TRANSACTION_IS_NULL);
        }
        String key = clusterConfigPO.getClusterName();
        if (keyMayExist(key) && null != get(key)) {
            log.error("[ClusterConfigRocksDao.save] cluster config is exist, clusterName={}", key);
            throw new SlaveException(SlaveErrorEnum.SLAVE_ROCKS_KEY_ALREADY_EXIST);
        }

        clusterConfigPO.setCreateTime(new Date());
        txPut(tx, key, clusterConfigPO);
    }

    /**
     * Batch insert int.
     *
     * @param clusterConfigPOList the cluster config po list
     * @return the int
     */
    public int batchInsert(List<ClusterConfigPO> clusterConfigPOList) {
        if (CollectionUtils.isEmpty(clusterConfigPOList)) {
            return 0;
        }

        Transaction tx = ThreadLocalUtils.getRocksTx();
        if (null == tx) {
            log.error("[ClusterConfigRocksDao.batchInsert] transaction is null");
            throw new SlaveException(SlaveErrorEnum.SLAVE_ROCKS_TRANSACTION_IS_NULL);
        }

        for (ClusterConfigPO po : clusterConfigPOList) {
            if (null == po.getCreateTime()) {
                po.setCreateTime(new Date());
            } else {
                po.setUpdateTime(new Date());
            }
            txPut(tx, po.getClusterName(), po);
        }
        return clusterConfigPOList.size();
    }
}

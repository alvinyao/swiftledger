package com.higgschain.trust.slave.core.service.snapshot.agent;

import com.higgschain.trust.slave.api.enums.SnapshotBizKeyEnum;
import com.higgschain.trust.slave.common.enums.SlaveErrorEnum;
import com.higgschain.trust.slave.common.exception.SlaveException;
import com.higgschain.trust.slave.core.repository.DataIdentityRepository;
import com.higgschain.trust.slave.core.service.snapshot.CacheLoader;
import com.higgschain.trust.slave.core.service.snapshot.SnapshotService;
import com.higgschain.trust.slave.model.bo.BaseBO;
import com.higgschain.trust.slave.model.bo.DataIdentity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Data identity snapshot agent.
 *
 * @author liuyu
 * @description an agent for data identity snapshot
 * @date 2018 -04-09
 */
@Slf4j
@Component
public class DataIdentitySnapshotAgent implements CacheLoader {
    /**
     * The Snapshot.
     */
    @Autowired
    SnapshotService snapshot;
    /**
     * The Data identity repository.
     */
    @Autowired
    DataIdentityRepository dataIdentityRepository;

    private <T> T get(Object key) {
        return (T) snapshot.get(SnapshotBizKeyEnum.DATA_IDENTITY, key);
    }

    /**
     * insert  object into the snapshot
     *
     * @param key
     * @param value
     */
    private void insert(Object key, Object value) {
        snapshot.insert(SnapshotBizKeyEnum.DATA_IDENTITY, key, value);
    }

    /**
     * query data identity
     *
     * @param identity the identity
     * @return data identity
     */
    public DataIdentity getDataIdentity(String identity) {
        return get(new DataIdentityCacheKey(identity));
    }

    /**
     * save data identity
     *
     * @param dataIdentity the data identity
     */
    public void saveDataIdentity(DataIdentity dataIdentity) {
        insert(new DataIdentityCacheKey(dataIdentity.getIdentity()), dataIdentity);
    }

    /**
     * when cache is not exists,load from db
     */
    @Override
    public Object query(Object object) {
        if (!(object instanceof DataIdentityCacheKey)) {
            log.error("object {} is not the type of DataIdentityCacheKey error", object);
            throw new SlaveException(SlaveErrorEnum.SLAVE_SNAPSHOT_DATA_TYPE_ERROR_EXCEPTION);
        }
        DataIdentityCacheKey key = (DataIdentityCacheKey) object;
        return dataIdentityRepository.queryDataIdentity(key.getIdentity());

    }

    /**
     * the method to batchInsert data into db
     *
     * @param insertList
     * @return
     */
    @Override
    public boolean batchInsert(List<Pair<Object, Object>> insertList) {
        if (CollectionUtils.isEmpty(insertList)) {
            return true;
        }

        //get bach insert data
        List<DataIdentity> dataIdentityList = new ArrayList<>();
        for (Pair<Object, Object> pair : insertList) {
            if (!(pair.getLeft() instanceof DataIdentityCacheKey)) {
                log.error("insert key is not the type of TxOutCacheKey error");
                throw new SlaveException(SlaveErrorEnum.SLAVE_SNAPSHOT_DATA_TYPE_ERROR_EXCEPTION);
            }
            dataIdentityList.add((DataIdentity) pair.getRight());
        }

        return dataIdentityRepository.batchInsert(dataIdentityList);
    }

    /**
     * the method to batchUpdate data into db
     *
     * @param updateList
     * @return
     */
    @Override
    public boolean batchUpdate(List<Pair<Object, Object>> updateList) {
        return true;
    }

    /**
     * the cache key of data identity
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataIdentityCacheKey extends BaseBO {
        private String identity;
    }
}

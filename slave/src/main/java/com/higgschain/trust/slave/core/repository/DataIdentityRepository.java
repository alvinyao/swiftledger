package com.higgschain.trust.slave.core.repository;

import com.higgschain.trust.common.utils.BeanConvertor;
import com.higgschain.trust.slave.common.config.InitConfig;
import com.higgschain.trust.slave.common.enums.SlaveErrorEnum;
import com.higgschain.trust.slave.common.exception.SlaveException;
import com.higgschain.trust.slave.dao.mysql.dataIdentity.DataIdentityDao;
import com.higgschain.trust.slave.dao.mysql.dataIdentity.DataIdentityJDBCDao;
import com.higgschain.trust.slave.dao.po.DataIdentityPO;
import com.higgschain.trust.slave.dao.rocks.dataidentity.DataIdentityRocksDao;
import com.higgschain.trust.slave.model.bo.DataIdentity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The type Data identity repository.
 *
 * @author liuyu
 * @description
 * @date 2018 -04-10
 */
@Repository
@Slf4j
public class DataIdentityRepository {
    @Autowired
    private DataIdentityDao dataIdentityDao;

    @Autowired
    private DataIdentityRocksDao dataIdentityRocksDao;

    @Autowired
    private DataIdentityJDBCDao dataIdentityJDBCDao;

    @Autowired
    private InitConfig initConfig;

    /**
     * query identity data by identity
     *
     * @param identity the identity
     * @return data identity
     */
    public DataIdentity queryDataIdentity(String identity) {
        DataIdentityPO identityPO;
        if (initConfig.isUseMySQL()) {
            identityPO = dataIdentityDao.queryByIdentity(identity);
        } else {
            identityPO = dataIdentityRocksDao.get(identity);
        }
        return BeanConvertor.convertBean(identityPO, DataIdentity.class);
    }

    /**
     * batch insert data identity
     *
     * @param dataIdentityList the data identity list
     * @return the boolean
     */
    public boolean batchInsert(List<DataIdentity> dataIdentityList) {
        if(CollectionUtils.isEmpty(dataIdentityList)){
            log.info("dataIdentityList is empty");
            return true;
        }
        List<DataIdentityPO> dataIdentityPOList = BeanConvertor.convertList(dataIdentityList,DataIdentityPO.class);
        try {
            if (initConfig.isUseMySQL()) {
                return dataIdentityPOList.size() == dataIdentityJDBCDao.batchInsert(dataIdentityPOList);
            }
            return dataIdentityList.size() == dataIdentityRocksDao.batchInsert(dataIdentityPOList);
        } catch (DuplicateKeyException e) {
            log.error("bachInsert dataIdentity fail, because there is DuplicateKeyException for dataIdentityPOList:", dataIdentityPOList);
            throw new SlaveException(SlaveErrorEnum.SLAVE_IDEMPOTENT, e);
        }
    }

    /**
     * check there the identity is existed in the slave
     *
     * @param identity the identity
     * @return boolean
     */
    public boolean isExist(String identity) {
        DataIdentity dataIdentity = queryDataIdentity(identity);
        return dataIdentity != null;
    }
}

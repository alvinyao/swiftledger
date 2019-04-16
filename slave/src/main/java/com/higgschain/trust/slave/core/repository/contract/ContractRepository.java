package com.higgschain.trust.slave.core.repository.contract;

import com.higgschain.trust.common.utils.BeanConvertor;
import com.higgschain.trust.slave.api.vo.ContractVO;
import com.higgschain.trust.slave.common.config.InitConfig;
import com.higgschain.trust.slave.dao.mysql.contract.ContractDao;
import com.higgschain.trust.slave.dao.po.contract.ContractPO;
import com.higgschain.trust.slave.dao.rocks.contract.ContractRocksDao;
import com.higgschain.trust.slave.model.bo.contract.Contract;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Contract Repository
 *
 * @author duhongming
 * @date 2018 -04-12
 */
@Repository
@Slf4j
public class ContractRepository {

    @Autowired private ContractDao contractDao;
    @Autowired private ContractRocksDao contractRocksDao;
    @Autowired private InitConfig initConfig;

    /**
     * Batch insert boolean.
     *
     * @param list the list
     * @return the boolean
     */
    public boolean batchInsert(List<ContractPO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return true;
        }

        int result;
        if (initConfig.isUseMySQL()) {
            result = contractDao.batchInsert(list);
        } else {
            result = contractRocksDao.batchInsert(list);
        }
        return result == list.size();
    }

    /**
     * Query count long.
     *
     * @param height the height
     * @param txId   the tx id
     * @return the long
     */
    public Long queryCount(Long height, String txId) {
        Long rowCount = contractDao.getQueryCount(height, txId);
        return rowCount;
    }

    /**
     * Query list.
     *
     * @param height    the height
     * @param txId      the tx id
     * @param pageIndex the page index
     * @param pageSize  the page size
     * @return the list
     */
    public List<Contract> query(Long height, String txId, int pageIndex, int pageSize) {
        List<ContractPO> list = contractDao.query(height, txId, (pageIndex - 1) * pageSize, pageSize);
        return BeanConvertor.convertList(list, Contract.class);
    }

    /**
     * Query by address contract.
     *
     * @param address the address
     * @return the contract
     */
    public Contract queryByAddress(String address) {

        ContractPO po;
        if (initConfig.isUseMySQL()) {
            po = contractDao.queryByAddress(address);
        } else {
            po = contractRocksDao.get(address);
        }
        return BeanConvertor.convertBean(po, Contract.class);
    }

    /**
     * Is existed address boolean.
     *
     * @param address the address
     * @return the boolean
     */
    public boolean isExistedAddress(String address){
        if (StringUtils.isBlank(address)) {
            return false;
        }
        Contract contract = queryByAddress(address);
        if (null == contract) {
            return false;
        }
        return true;
    }

    /**
     * query by txId and action index
     *
     * @param txId        the tx id
     * @param actionIndex the action index
     * @return contract vo
     */
    public ContractVO queryByTxId(String txId, int actionIndex){
        ContractPO po = contractDao.queryByTxId(txId,actionIndex);
        return BeanConvertor.convertBean(po, ContractVO.class);
    }
}
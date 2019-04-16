package com.higgschain.trust.slave.core.repository.contract;

import com.alibaba.fastjson.JSON;
import com.higgschain.trust.contract.JsonHelper;
import com.higgschain.trust.slave.common.config.InitConfig;
import com.higgschain.trust.slave.dao.mysql.contract.ContractStateDao;
import com.higgschain.trust.slave.dao.po.contract.ContractStatePO;
import com.higgschain.trust.slave.dao.rocks.contract.ContractStateRocksDao;
import com.higgschain.trust.slave.model.bo.contract.ContractState;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Contract state repository.
 */
@Repository
@Slf4j
public class ContractStateRepository {

    /**
     * The Contract state dao.
     */
    @Autowired
    ContractStateDao contractStateDao;
    /**
     * The Contract state rocks dao.
     */
    @Autowired
    ContractStateRocksDao contractStateRocksDao;
    /**
     * The Init config.
     */
    @Autowired InitConfig initConfig;

    /**
     * Batch insert boolean.
     *
     * @param list the list
     * @return the boolean
     */
    public boolean batchInsert(Collection<ContractStatePO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return true;
        }

        int result;
        if (initConfig.isUseMySQL()) {
            result = contractStateDao.batchInsert(list);
        } else {
            result = contractStateRocksDao.batchInsert(list);
        }
        return result == list.size();
    }

    /**
     * Batch update boolean.
     *
     * @param list the list
     * @return the boolean
     */
    public boolean batchUpdate(Collection<ContractStatePO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return true;
        }
        int result;
        if (initConfig.isUseMySQL()) {
            result = contractStateDao.batchUpdate(list);
        } else {
            result = contractStateRocksDao.batchInsert(list);
        }
        return result == list.size();
    }

    /**
     * Get map.
     *
     * @param address the address
     * @return the map
     */
    public Map<String, Object> get(String address) {
        ContractStatePO po;
        if (initConfig.isUseMySQL()) {
            po = contractStateDao.queryByAddress(address);
        } else {
            po = contractStateRocksDao.get(address);
        }

        if (null == po) {
            return null;
        }

        Map<String, Object> state = JSON.parseObject(po.getState());
        Map<String, Object> newState = new HashMap<>(state.size());
        state.forEach((key, value) -> newState.put(key, value));
        return newState;
    }

    /**
     * Gets state.
     *
     * @param address the address
     * @return the state
     */
    public ContractState getState(String address) {
        ContractStatePO po;
        if (initConfig.isUseMySQL()) {
            po = contractStateDao.queryByAddress(address);
        } else {
            po = contractStateRocksDao.get(address);
        }

        if (null == po) {
            return null;
        }

        ContractState contractState = new ContractState();
        contractState.setAddress(po.getAddress());
        if(StringUtils.isNotEmpty(po.getState())){
            String stateJSON = po.getState();
            if(stateJSON.startsWith("[")){
                contractState.setState(JSON.parseArray(po.getState()));
            }else{
                contractState.setState(JSON.parseObject(po.getState()));
            }
        }
        contractState.setKeyDesc(po.getKeyDesc());
        return contractState;
    }

    /**
     * Put.
     *
     * @param address the address
     * @param state   the state
     */
    public void put(String address,Object state) {
        ContractStatePO po = new ContractStatePO();
        po.setAddress(address);
        po.setState(JsonHelper.serialize(state));
        if (initConfig.isUseMySQL()) {
            contractStateDao.save(po);
        } else {
            contractStateRocksDao.save(po);
        }
    }
}

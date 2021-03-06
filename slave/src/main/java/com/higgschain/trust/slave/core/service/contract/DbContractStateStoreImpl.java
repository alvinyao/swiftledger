package com.higgschain.trust.slave.core.service.contract;

import com.higgschain.trust.contract.ContractStateStore;
import com.higgschain.trust.slave.core.repository.contract.ContractStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The type Db contract state store.
 *
 * @author duhongming
 * @date 2018 /05/07
 */
@Service public class DbContractStateStoreImpl implements ContractStateStore {

    @Autowired private ContractStateRepository contractStateRepository;

    @Override
    public void put(String key, Object state) {
        contractStateRepository.put(key,state);
    }

    @Override
    public Object get(String key) {
        return contractStateRepository.get(key);
    }

    @Override
    public void remove(String key) {
    }
}

package com.higgschain.trust.slave.core.service.contract;

import com.higgschain.trust.contract.ExecuteContextData;

public interface UTXOSmartContract {
    boolean isExist(String address);
    boolean execute(String address, ExecuteContextData data);
}
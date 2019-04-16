package com.higgschain.trust.slave.core.service.contract;

import com.higgschain.trust.contract.ExecuteContextData;

/**
 * The interface Utxo smart contract.
 */
public interface UTXOSmartContract {
    /**
     * Is exist boolean.
     *
     * @param address the address
     * @return the boolean
     */
    boolean isExist(String address);

    /**
     * Execute boolean.
     *
     * @param address the address
     * @param data    the data
     * @return the boolean
     */
    boolean execute(String address, ExecuteContextData data);
}
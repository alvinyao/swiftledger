package com.higgschain.trust.slave.core.service.contract;

import com.higgschain.trust.contract.impl.AbstractExecuteContextData;
import com.higgschain.trust.slave.model.bo.action.UTXOAction;

import java.util.Map;

/**
 * the context data container of utxo
 *
 * @author duhongming
 * @date 2018 -04-23
 */
public class UTXOExecuteContextData extends AbstractExecuteContextData {

    private final static String ACTION_KEY = "Action";

    /**
     * Instantiates a new Utxo execute context data.
     */
    public UTXOExecuteContextData() {
        super();
    }

    /**
     * Instantiates a new Utxo execute context data.
     *
     * @param data the data
     */
    public UTXOExecuteContextData(Map<String, Object> data) {
        super(data);
    }

    /**
     * Gets action.
     *
     * @return the action
     */
    public UTXOAction getAction() {
        return (UTXOAction) get(ACTION_KEY);
    }

    /**
     * Sets action.
     *
     * @param action the action
     * @return the action
     */
    public UTXOExecuteContextData setAction(UTXOAction action) {
        this.put(ACTION_KEY, action);
        return this;
    }
}

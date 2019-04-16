package com.higgschain.trust.slave.core.service.contract;

import com.higgschain.trust.contract.impl.AbstractExecuteContextData;
import com.higgschain.trust.slave.model.bo.context.ActionData;

import java.util.Map;

/**
 * Standard Execute Context Data
 *
 * @author duhongming
 * @date 2018 -04-23
 */
public class StandardExecuteContextData extends AbstractExecuteContextData {

    private static final String ACTION_KEY = "TX_ACTION_DATA_KEY";

    /**
     * Instantiates a new Standard execute context data.
     */
    public StandardExecuteContextData() {
        super();
    }

    /**
     * Instantiates a new Standard execute context data.
     *
     * @param data the data
     */
    public StandardExecuteContextData(Map<String, Object> data) {
        super(data);
    }

    /**
     * Gets action.
     *
     * @return the action
     */
    public ActionData getAction() {
        return (ActionData) get(ACTION_KEY);
    }

    /**
     * Sets action.
     *
     * @param action the action
     * @return the action
     */
    public StandardExecuteContextData setAction(ActionData action) {
        this.put(ACTION_KEY, action);
        return this;
    }
}

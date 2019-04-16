package com.higgschain.trust.slave.model.bo.context;

import com.higgschain.trust.slave.model.bo.action.Action;

/**
 * The interface Transaction data.
 *
 * @Description:
 * @author: pengdi
 */
public interface TransactionData extends CommonData {

    /**
     * set the current action in this transaction processing
     *
     * @param action the action
     * @return
     */
    void setCurrentAction(Action action);

    /**
     * transfer to action data
     * use parse not get for JSON
     *
     * @return action data
     */
    ActionData parseActionData();
}

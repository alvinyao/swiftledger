package com.higgschain.trust.slave.core.service.action;

import com.higgschain.trust.slave.common.exception.SlaveException;
import com.higgschain.trust.slave.model.bo.action.Action;
import com.higgschain.trust.slave.model.bo.context.ActionData;

/**
 * The interface Action handler.
 *
 * @Description:
 * @author: pengdi
 */
public interface ActionHandler {
    /**
     * params verify
     *
     * @param action the action
     * @return
     * @throws SlaveException the slave exception
     */
    void verifyParams(Action action)throws SlaveException;

    /**
     * the storage for the action
     *
     * @param actionData the action data
     */
    void process(ActionData actionData);

}

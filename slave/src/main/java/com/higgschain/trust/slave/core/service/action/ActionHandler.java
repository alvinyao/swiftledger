package com.higgschain.trust.slave.core.service.action;

import com.higgschain.trust.slave.common.exception.SlaveException;
import com.higgschain.trust.slave.model.bo.action.Action;
import com.higgschain.trust.slave.model.bo.context.ActionData;

/**
 * @Description:
 * @author: pengdi
 **/
public interface ActionHandler {
    /**
     * params verify
     *
     * @return
     */
    void verifyParams(Action action)throws SlaveException;
    /**
     * the storage for the action
     *
     * @param actionData
     */
    void process(ActionData actionData);

}

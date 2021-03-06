package com.higgschain.trust.slave.core.service.action.dataidentity;

import com.higgschain.trust.slave.common.enums.SlaveErrorEnum;
import com.higgschain.trust.slave.common.exception.SlaveException;
import com.higgschain.trust.slave.core.service.action.ActionHandler;
import com.higgschain.trust.slave.model.bo.action.Action;
import com.higgschain.trust.slave.model.bo.action.DataIdentityAction;
import com.higgschain.trust.slave.model.bo.context.ActionData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * dataidentity action handler
 *
 * @author lingchao
 * @create 2018年03月30日17 :58
 */
@Slf4j
@Component
public class DataIdentityActionHandler implements ActionHandler {
    @Autowired
    private DataIdentityService dataIdentityService;

    @Override public void verifyParams(Action action) throws SlaveException {
        DataIdentityAction bo = (DataIdentityAction) action;
        if(StringUtils.isEmpty(bo.getIdentity()) || bo.getIdentity().length() > 64){
            log.error("[verifyParams] identity is null or illegal param:{}",bo);
            throw new SlaveException(SlaveErrorEnum.SLAVE_PARAM_VALIDATE_ERROR);
        }
        if(StringUtils.isEmpty(bo.getDataOwner()) || bo.getDataOwner().length() > 24){
            log.error("[verifyParams] dataOwner is null or illegal param:{}",bo);
            throw new SlaveException(SlaveErrorEnum.SLAVE_PARAM_VALIDATE_ERROR);
        }
        if(StringUtils.isEmpty(bo.getChainOwner()) || bo.getChainOwner().length() > 24){
            log.error("[verifyParams] chainOwner is null or illegal param:{}",bo);
            throw new SlaveException(SlaveErrorEnum.SLAVE_PARAM_VALIDATE_ERROR);
        }
    }

    @Override
    public void process(ActionData actionData) {
        log.debug("[ DataIdentityAction.process] is starting");
        dataIdentityService.process(actionData);
        log.debug("[ DataIdentityAction.process] is end");
    }



}

package com.higgschain.trust.slave.core.service.action.ca;

import com.higgschain.trust.slave.api.enums.ActionTypeEnum;
import com.higgschain.trust.slave.common.enums.SlaveErrorEnum;
import com.higgschain.trust.slave.common.exception.SlaveException;
import com.higgschain.trust.slave.core.service.datahandler.ca.CaSnapshotHandler;
import com.higgschain.trust.slave.dao.po.ca.CaPO;
import com.higgschain.trust.slave.model.bo.ca.CaAction;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The type Ca helper.
 */
@Slf4j @Component public class CaHelper {
    /**
     * The Ca snapshot handler.
     */
    @Autowired
    CaSnapshotHandler caSnapshotHandler;

    /**
     * Validate boolean.
     *
     * @param caAction the ca action
     * @param type     the type
     * @return the boolean
     */
    public boolean validate(CaAction caAction, ActionTypeEnum type) {
        // convert action and validate it
        log.info("[CaHelper.process] is start,params:{}", caAction);

        //validate idempotent
        CaPO caPO = caSnapshotHandler.getCa(caAction.getUser(),caAction.getUsage());
        if (type == ActionTypeEnum.CA_AUTH) {
            if (null != caPO && StringUtils.equals(caPO.getPubKey(), caAction.getPubKey()) && caPO.isValid()) {
                return false;
            } else {
                return true;
            }
        }

        if (type == ActionTypeEnum.CA_UPDATE) {
            if (null != caPO && !StringUtils.equals(caPO.getPubKey(), caAction.getPubKey())) {
                return true;
            } else {
                return false;
            }
        }

        if (type == ActionTypeEnum.CA_CANCEL) {
            if (null != caPO && StringUtils.equals(caPO.getPubKey(), caAction.getPubKey())) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * param verify
     *
     * @param caAction the ca action
     * @throws SlaveException the slave exception
     */
    public void verifyParams(CaAction caAction) throws SlaveException {
        if(StringUtils.isEmpty(caAction.getUser())){
            log.error("[verifyParams] user is null or illegal param:{}",caAction);
            throw new SlaveException(SlaveErrorEnum.SLAVE_PARAM_VALIDATE_ERROR);
        }
        if(StringUtils.isEmpty(caAction.getPubKey())){
            log.error("[verifyParams] pubKey is null or illegal param:{}",caAction);
            throw new SlaveException(SlaveErrorEnum.SLAVE_PARAM_VALIDATE_ERROR);
        }
        if(StringUtils.isEmpty(caAction.getUsage())){
            log.error("[verifyParams] usage is null or illegal param:{}",caAction);
            throw new SlaveException(SlaveErrorEnum.SLAVE_PARAM_VALIDATE_ERROR);
        }
    }
}

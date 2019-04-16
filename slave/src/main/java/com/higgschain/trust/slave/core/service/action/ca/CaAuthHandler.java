package com.higgschain.trust.slave.core.service.action.ca;

import com.higgschain.trust.common.utils.Profiler;
import com.higgschain.trust.slave.api.enums.ActionTypeEnum;
import com.higgschain.trust.slave.common.enums.SlaveErrorEnum;
import com.higgschain.trust.slave.common.exception.SlaveException;
import com.higgschain.trust.slave.core.service.action.ActionHandler;
import com.higgschain.trust.slave.core.service.datahandler.ca.CaSnapshotHandler;
import com.higgschain.trust.slave.model.bo.action.Action;
import com.higgschain.trust.slave.model.bo.ca.Ca;
import com.higgschain.trust.slave.model.bo.ca.CaAction;
import com.higgschain.trust.slave.model.bo.context.ActionData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The type Ca auth handler.
 *
 * @author WangQuanzhou
 * @desc auth ca handler
 * @date 2018 /6/6 10:25
 */
@Slf4j @Component public class CaAuthHandler implements ActionHandler {

    /**
     * The Ca snapshot handler.
     */
    @Autowired
    CaSnapshotHandler caSnapshotHandler;
    /**
     * The Ca helper.
     */
    @Autowired CaHelper caHelper;

    @Override public void verifyParams(Action action) throws SlaveException {
        CaAction caAction = (CaAction)action;
        caHelper.verifyParams(caAction);
    }

    @Override public void process(ActionData actionData) {
        CaAction caAction = (CaAction)actionData.getCurrentAction();

        log.info("[CaAuthHandler.process] start to process ca auth action, user={}, pubKey={}, usage={}",
            caAction.getUser(), caAction.getPubKey(), caAction.getUsage());

        if (!caHelper.validate(caAction, ActionTypeEnum.CA_AUTH)) {
            log.error("[CaAuthHandler.process] actionData validate error, user={}, pubKey={}, usage={}",
                caAction.getUser(), caAction.getPubKey(), caAction.getUsage());
            throw new SlaveException(SlaveErrorEnum.SLAVE_CA_VALIDATE_ERROR,
                "[CaAuthHandler.process] actionData validate error");
        }

        Profiler.enter("[CaAuthHandler.authCa]");
        Ca ca = new Ca();
        BeanUtils.copyProperties(caAction, ca);
        caSnapshotHandler.authCa(ca);
        Profiler.release();

    }
}

package com.higgschain.trust.slave.core.service.action.manage;

import com.higgschain.trust.slave.api.enums.manage.InitPolicyEnum;
import com.higgschain.trust.slave.common.enums.SlaveErrorEnum;
import com.higgschain.trust.slave.common.exception.SlaveException;
import com.higgschain.trust.slave.core.repository.ca.CaRepository;
import com.higgschain.trust.slave.core.service.action.ActionHandler;
import com.higgschain.trust.slave.core.service.datahandler.manage.RsSnapshotHandler;
import com.higgschain.trust.slave.model.bo.CoreTransaction;
import com.higgschain.trust.slave.model.bo.action.Action;
import com.higgschain.trust.slave.model.bo.ca.Ca;
import com.higgschain.trust.slave.model.bo.context.ActionData;
import com.higgschain.trust.slave.model.bo.manage.RegisterRS;
import com.higgschain.trust.slave.model.bo.manage.RsNode;
import com.higgschain.trust.slave.model.enums.biz.RsNodeStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The type Register rs handler.
 *
 * @author tangfashuang
 * @date 2018 /03/28
 * @desc register RS handler
 */
@Slf4j @Component public class RegisterRsHandler implements ActionHandler {

    @Autowired private RsSnapshotHandler rsSnapshotHandler;

    @Autowired private CaRepository caRepository;

    @Override public void verifyParams(Action action) throws SlaveException {
        RegisterRS bo = (RegisterRS)action;
        if(StringUtils.isEmpty(bo.getRsId()) || bo.getRsId().length() > 32){
            log.error("[verifyParams] rsId is null or illegal param:{}",bo);
            throw new SlaveException(SlaveErrorEnum.SLAVE_PARAM_VALIDATE_ERROR);
        }
        if(StringUtils.isEmpty(bo.getDesc()) || bo.getDesc().length() > 128){
            log.error("[verifyParams] desc is null or illegal param:{}",bo);
            throw new SlaveException(SlaveErrorEnum.SLAVE_PARAM_VALIDATE_ERROR);
        }
    }

    @Override public void process(ActionData actionData) {
        RegisterRS bo = (RegisterRS)actionData.getCurrentAction();
        log.info("[RegisterRSHandler.process] start, actionData: {} ", bo);

        if (null == bo) {
            log.error("[RegisterRSHandler.process] convert to RegisterRS failed");
            throw new SlaveException(SlaveErrorEnum.SLAVE_PARAM_VALIDATE_ERROR);
        }

        //check rsId and sender
        CoreTransaction coreTx = actionData.getCurrentTransaction().getCoreTx();
        String rsId = bo.getRsId();
        if (!StringUtils.equals(rsId, coreTx.getSender())) {
            log.error("[RegisterRSHandler.process] register rsId:{} is not equals transaction sender: {}", rsId,
                coreTx.getSender());
            throw new SlaveException(SlaveErrorEnum.SLAVE_PARAM_VALIDATE_ERROR);
        }

        //check policy id
        InitPolicyEnum initPolicyEnum = InitPolicyEnum.getInitPolicyEnumByPolicyId(coreTx.getPolicyId());
        if (!InitPolicyEnum.REGISTER_RS.equals(initPolicyEnum)) {
            log.error("[RegisterRSHandler.process] policy id is not for register rs");
            throw new SlaveException(SlaveErrorEnum.SLAVE_PARAM_VALIDATE_ERROR);
        }

        Ca ca = caRepository.getCaForBiz(rsId);
        if (null == ca || !ca.isValid()) {
            log.error("[RegisterRSHandler.process] ca not register or is not valid which rsId={}", rsId);
            throw new SlaveException(SlaveErrorEnum.SLAVE_PARAM_VALIDATE_ERROR);
        }

        RsNode rsNode = rsSnapshotHandler.getRsNode(rsId);
        if (rsNode != null) {
            if (rsNode.getStatus() == RsNodeStatusEnum.COMMON) {
                log.warn("rsNode already exists. rsId={}", rsId);
                throw new SlaveException(SlaveErrorEnum.SLAVE_RS_EXISTS_ERROR);
            } else {
                rsSnapshotHandler.updateRsNode(rsId, RsNodeStatusEnum.COMMON);
            }
        } else {
            rsSnapshotHandler.registerRsNode(bo);
        }

        log.info("[RegisterRSHandler.process] finish");
    }

}

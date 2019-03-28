package com.higgschain.trust.slave.core.service.pending;

import com.higgschain.trust.slave.api.enums.VersionEnum;
import com.higgschain.trust.slave.common.enums.SlaveErrorEnum;
import com.higgschain.trust.slave.common.exception.SlaveException;
import com.higgschain.trust.slave.core.service.action.ActionHandler;
import com.higgschain.trust.slave.core.service.version.TransactionProcessor;
import com.higgschain.trust.slave.core.service.version.TxProcessorHolder;
import com.higgschain.trust.slave.model.bo.CoreTransaction;
import com.higgschain.trust.slave.model.bo.SignedTransaction;
import com.higgschain.trust.slave.model.bo.action.Action;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author liuyu
 * @description
 * @date 2018-08-27
 */
@Component public class TransactionValidator {
    @Autowired
    TxProcessorHolder processorHolder;

    /**
     * verify SignedTransaction
     *
     * @param tx
     * @return
     */
    public void verify(SignedTransaction tx) throws SlaveException {
        if (tx == null) {
            throw new SlaveException(SlaveErrorEnum.SLAVE_PARAM_VALIDATE_ERROR);
        }
        if (CollectionUtils.isEmpty(tx.getSignatureList())) {
            throw new SlaveException(SlaveErrorEnum.SLAVE_PARAM_VALIDATE_ERROR);
        }
        verify(tx.getCoreTx());
    }
    /**
     * verify CoreTransaction
     *
     * @param coreTx
     * @return
     */
    public void verify(CoreTransaction coreTx) throws SlaveException {
        if (coreTx == null) {
            throw new SlaveException(SlaveErrorEnum.SLAVE_PARAM_VALIDATE_ERROR);
        }
        if (StringUtils.isEmpty(coreTx.getTxId())) {
            throw new SlaveException(SlaveErrorEnum.SLAVE_PARAM_VALIDATE_ERROR);
        }
        if (StringUtils.isEmpty(coreTx.getPolicyId())) {
            throw new SlaveException(SlaveErrorEnum.SLAVE_PARAM_VALIDATE_ERROR);
        }
        if (coreTx.getSendTime() == null) {
            throw new SlaveException(SlaveErrorEnum.SLAVE_PARAM_VALIDATE_ERROR);
        }
        if (StringUtils.isEmpty(coreTx.getSender())) {
            throw new SlaveException(SlaveErrorEnum.SLAVE_PARAM_VALIDATE_ERROR);
        }
        if (StringUtils.isEmpty(coreTx.getVersion())) {
            throw new SlaveException(SlaveErrorEnum.SLAVE_PARAM_VALIDATE_ERROR);
        }
        if (!CollectionUtils.isEmpty(coreTx.getActionList())) {
            TransactionProcessor processor =
                processorHolder.getProcessor(VersionEnum.getBizTypeEnumBycode(coreTx.getVersion()));
            for (Action action : coreTx.getActionList()) {
                if (action == null || action.getType() == null || action.getIndex() == null) {
                    throw new SlaveException(SlaveErrorEnum.SLAVE_PARAM_VALIDATE_ERROR);
                }
                ActionHandler actionHandler = processor.getHandlerByType(action.getType());
                actionHandler.verifyParams(action);
            }
        }
    }
}

package com.higgschain.trust.slave.core.service.action.utxo;

import com.higgschain.trust.slave.common.exception.SlaveException;
import com.higgschain.trust.slave.core.service.action.ActionHandler;
import com.higgschain.trust.slave.model.bo.action.Action;
import com.higgschain.trust.slave.model.bo.context.ActionData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * UTXO action Handler
 *
 * @author lingchao
 * @create 2018年03月27日16 :53
 */
@Slf4j
@Component
public class UTXOActionHandler implements ActionHandler {

    @Autowired
    private UTXOActionService utxoActionService;

    @Override
    public void verifyParams(Action action) throws SlaveException {
        log.info("[UTXOAction.verifyParams] is starting!");
        utxoActionService.verifyUTXOAction(action);
        log.info("[UTXOAction.verifyParams] is success!");
    }

    /**
     * action process
     *
     * @param actionData
     */
    @Override
    public void process(ActionData actionData) {
        log.info("[UTXOAction.process] is starting!");
        utxoActionService.process(actionData);
        log.info("[UTXOAction.process] is success!");
    }

}

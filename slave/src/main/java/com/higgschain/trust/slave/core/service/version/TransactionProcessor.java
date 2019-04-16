package com.higgschain.trust.slave.core.service.version;

import com.higgschain.trust.slave.api.enums.ActionTypeEnum;
import com.higgschain.trust.slave.core.service.action.ActionHandler;
import com.higgschain.trust.slave.model.bo.context.TransactionData;

/**
 * The interface Transaction processor.
 *
 * @author WangQuanzhou
 * @desc transaction processor
 * @date 2018 /3/28 18:00
 */
public interface TransactionProcessor {
    /**
     * process
     *
     * @param transactionData the transaction data
     */
    void process(TransactionData transactionData);

    /**
     * get action handler
     *
     * @param typeEnum the type enum
     * @return handler by type
     */
    ActionHandler getHandlerByType(ActionTypeEnum typeEnum);
}

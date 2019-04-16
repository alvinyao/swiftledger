package com.higgschain.trust.slave.common.listener;

import com.higgschain.trust.evmcontract.core.TransactionResultInfo;
import com.higgschain.trust.slave.model.bo.BlockHeader;

/**
 * The interface Trust listener.
 *
 * @author duhongming
 * @date 2018 /12/17
 */
public interface TrustListener {

    /**
     * On block.
     *
     * @param header the header
     */
    void onBlock(BlockHeader header);

    /**
     * On transaction executed.
     *
     * @param resultInfo the result info
     */
    void onTransactionExecuted(TransactionResultInfo resultInfo);
}

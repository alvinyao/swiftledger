package com.higgschain.trust.slave.common.listener;

import com.higgschain.trust.evmcontract.core.TransactionResultInfo;
import com.higgschain.trust.slave.model.bo.BlockHeader;

/**
 * @author duhongming
 * @date 2018/12/17
 */
public class TrustListenerAdapter implements TrustListener {

    @Override
    public void onBlock(BlockHeader header) {

    }

    @Override
    public void onTransactionExecuted(TransactionResultInfo resultInfo) {

    }
}

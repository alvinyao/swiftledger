package com.higgschain.trust.slave.common.listener;

import com.higgschain.trust.evmcontract.core.TransactionResultInfo;
import com.higgschain.trust.slave.model.bo.BlockHeader;

/**
 * @author duhongming
 * @date 2018/12/17
 */
public interface TrustListener {

    void onBlock(BlockHeader header);

    void onTransactionExecuted(TransactionResultInfo resultInfo);
}

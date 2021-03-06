package com.higgschain.trust.rs.core.api;

import com.higgschain.trust.rs.common.exception.RsCoreException;
import com.higgschain.trust.rs.core.vo.RsCoreTxVO;
import com.higgschain.trust.common.vo.RespData;
import com.higgschain.trust.slave.model.bo.CoreTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The type Rs core facade.
 *
 * @author liuyu
 * @description
 * @date 2018 -08-28
 */
@Component public class RsCoreFacade {
    /**
     * The Core transaction service.
     */
    @Autowired CoreTransactionService coreTransactionService;

    /**
     * 处理交易，异步接口，需要同步结果时，需配合syncWait接口
     *
     * @param coreTx 要处理的交易对象
     * @return
     * @throws RsCoreException the rs core exception
     * @RsCoreException 可能会抛出异常 ，常见幂等异常：RsCoreErrorEnum.RS_CORE_IDEMPOTENT
     */
    public void processTx(CoreTransaction coreTx) throws RsCoreException {
        coreTransactionService.submitTx(coreTx);
    }

    /**
     * 异步处理交易
     *
     * @param txId           交易id
     * @param waitForCluster 是否等待集群共识                       共识分为：单机和集群共识，集群共识会稍慢于单机共识                       需根据业务特点选择用哪种
     * @return resp data
     */
    public RespData syncWait(String txId, boolean waitForCluster) {
        return coreTransactionService.syncWait(txId, waitForCluster);
    }

    /**
     * 根据txId查询交易信息及处理结果
     *
     * @param txId the tx id
     * @return rs core tx vo
     */
    public RsCoreTxVO queryTxById(String txId) {
        return coreTransactionService.queryCoreTx(txId);
    }
}

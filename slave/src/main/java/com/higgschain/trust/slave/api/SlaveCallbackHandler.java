package com.higgschain.trust.slave.api;

import com.higgschain.trust.common.vo.RespData;
import com.higgschain.trust.slave.model.bo.BlockHeader;
import com.higgschain.trust.slave.model.bo.CoreTransaction;
import com.higgschain.trust.slave.model.bo.SignInfo;

import java.util.List;

/**
 * The interface Slave callback handler.
 *
 * @author liuyu
 * @description
 * @date 2018 -05-13
 */
public interface SlaveCallbackHandler {
    /**
     * on tx persisted
     *
     * @param respData    the resp data
     * @param signInfos   the sign infos
     * @param blockHeader the block header
     */
    void onPersisted(RespData<CoreTransaction> respData, List<SignInfo> signInfos, BlockHeader blockHeader);

    /**
     * when the cluster persisted of tx
     *
     * @param respData    the resp data
     * @param signInfos   the sign infos
     * @param blockHeader the block header
     */
    void onClusterPersisted(RespData<CoreTransaction> respData, List<SignInfo> signInfos, BlockHeader blockHeader);

    /**
     * on failover
     *
     * @param respData    the resp data
     * @param signInfos   the sign infos
     * @param blockHeader the block header
     */
    void onFailover(RespData<CoreTransaction> respData, List<SignInfo> signInfos,BlockHeader blockHeader);
}

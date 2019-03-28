package com.higgschain.trust.slave.api;

import com.higgschain.trust.slave.api.vo.RespData;
import com.higgschain.trust.slave.model.bo.BlockHeader;
import com.higgschain.trust.slave.model.bo.CoreTransaction;
import com.higgschain.trust.slave.model.bo.SignInfo;

import java.util.List;

/**
 * @author liuyu
 * @description
 * @date 2018-05-13
 */
public interface SlaveCallbackHandler {
    /**
     * on tx persisted
     *
     * @param respData
     * @param signInfos
     * @param blockHeader
     */
    void onPersisted(RespData<CoreTransaction> respData, List<SignInfo> signInfos, BlockHeader blockHeader);

    /**
     * when the cluster persisted of tx
     *
     * @param respData
     * @param signInfos
     * @param blockHeader
     */
    void onClusterPersisted(RespData<CoreTransaction> respData, List<SignInfo> signInfos, BlockHeader blockHeader);

    /**
     * on failover
     *
     * @param respData
     * @param signInfos
     * @param blockHeader
     */
    void onFailover(RespData<CoreTransaction> respData, List<SignInfo> signInfos,BlockHeader blockHeader);
}

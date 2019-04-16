package com.higgschain.trust.rs.core.api;

import com.higgschain.trust.slave.model.bo.CoreTransaction;
import com.higgschain.trust.slave.model.bo.SignInfo;

/**
 * The interface Sign service.
 *
 * @author liuyu
 * @description
 * @date 2018 -05-12
 */
public interface SignService {
    /**
     * sign transaction
     *
     * @param coreTx the core tx
     * @return sign info
     */
    SignInfo signTx(CoreTransaction coreTx);

    /**
     * sign anything by sign type BIZ or CONSENSUS
     *
     * @param signValue    the sign value
     * @param signTypeEnum the sign type enum
     * @return string
     */
    String sign(String signValue,SignInfo.SignTypeEnum signTypeEnum);
}

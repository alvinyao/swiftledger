package com.higgschain.trust.slave.core.service.datahandler.manage;

import com.higgschain.trust.slave.model.bo.manage.RegisterRS;
import com.higgschain.trust.slave.model.bo.manage.RsNode;
import com.higgschain.trust.slave.model.enums.biz.RsNodeStatusEnum;

/**
 * The interface Rs handler.
 *
 * @author tangfashuang
 * @date 2018 /04/17 19:23
 * @desc rs handler interface
 */
public interface RsHandler {
    /**
     * get RsNode
     *
     * @param rsId the rs id
     * @return rs node
     */
    RsNode getRsNode(String rsId);

    /**
     * register RsNode
     *
     * @param registerRS the register rs
     */
    void registerRsNode(RegisterRS registerRS);

    /**
     * update rs node status
     *
     * @param rsId             the rs id
     * @param rsNodeStatusEnum the rs node status enum
     */
    void updateRsNode(String rsId, RsNodeStatusEnum rsNodeStatusEnum);
}

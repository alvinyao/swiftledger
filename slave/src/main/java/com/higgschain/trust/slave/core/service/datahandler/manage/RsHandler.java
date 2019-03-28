package com.higgschain.trust.slave.core.service.datahandler.manage;

import com.higgschain.trust.slave.model.bo.manage.RegisterRS;
import com.higgschain.trust.slave.model.bo.manage.RsNode;
import com.higgschain.trust.slave.model.enums.biz.RsNodeStatusEnum;

/**
 * @author tangfashuang
 * @date 2018/04/17 19:23
 * @desc rs handler interface
 */
public interface RsHandler {
    /**
     * get RsNode
     * @param rsId
     * @return
     */
    RsNode getRsNode(String rsId);

    /**
     * register RsNode
     * @param registerRS
     */
    void registerRsNode(RegisterRS registerRS);

    /**
     * update rs node status
     * @param rsId
     * @param rsNodeStatusEnum
     */
    void updateRsNode(String rsId, RsNodeStatusEnum rsNodeStatusEnum);
}

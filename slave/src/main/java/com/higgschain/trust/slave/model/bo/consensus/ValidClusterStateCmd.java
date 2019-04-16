/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.slave.model.bo.consensus;

import com.higgschain.trust.common.constant.Constant;
import com.higgschain.trust.consensus.config.NodeStateEnum;
import com.higgschain.trust.consensus.p2pvalid.core.IdValidCommand;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The type Valid cluster state cmd.
 *
 * @author liuyu
 * @date 2018 /10/12
 */
@Getter @Setter @NoArgsConstructor public class ValidClusterStateCmd extends IdValidCommand<NodeStateEnum> {

    private static final long serialVersionUID = -7652400642865085127L;

    /**
     * Instantiates a new Valid cluster state cmd.
     *
     * @param id    the id
     * @param state the state
     */
    public ValidClusterStateCmd(String id, NodeStateEnum state) {
        super(id, state);
    }

    @Override public String messageDigest() {
        return getRequestId() + Constant.SPLIT_SLASH + get();
    }
}

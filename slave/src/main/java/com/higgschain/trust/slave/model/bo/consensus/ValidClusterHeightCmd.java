/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.slave.model.bo.consensus;

import com.higgschain.trust.common.constant.Constant;
import com.higgschain.trust.consensus.p2pvalid.core.IdValidCommand;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The type Valid cluster height cmd.
 *
 * @author suimi
 * @date 2018 /4/17
 */
@Getter @Setter @NoArgsConstructor public class ValidClusterHeightCmd extends IdValidCommand<Long> {

    private static final long serialVersionUID = -7652400642865085127L;

    /**
     * Instantiates a new Valid cluster height cmd.
     *
     * @param id     the id
     * @param height the height
     */
    public ValidClusterHeightCmd(String id, Long height) {
        super(id, height);
    }

    @Override public String messageDigest() {
        return getRequestId() + Constant.SPLIT_SLASH + get();
    }
}

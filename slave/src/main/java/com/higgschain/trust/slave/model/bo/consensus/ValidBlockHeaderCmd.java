/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.slave.model.bo.consensus;

import com.higgschain.trust.common.constant.Constant;
import com.higgschain.trust.consensus.p2pvalid.core.IdValidCommand;
import com.higgschain.trust.slave.model.bo.BlockHeader;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The type Valid block header cmd.
 *
 * @author suimi
 * @date 2018 /4/17
 */
@Getter @Setter @NoArgsConstructor public class ValidBlockHeaderCmd extends IdValidCommand<Boolean> {

    private static final long serialVersionUID = 1644770444682750035L;

    private BlockHeader header;

    /**
     * Instantiates a new Valid block header cmd.
     *
     * @param requestId the request id
     * @param header    the header
     * @param valid     the valid
     */
    public ValidBlockHeaderCmd(String requestId, BlockHeader header, Boolean valid) {
        super(requestId, valid);
        this.header = header;
    }

    @Override public String messageDigest() {
        return getRequestId() + Constant.SPLIT_SLASH + this.get();
    }
}

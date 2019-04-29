/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.consensus.core.master.command;

import com.higgschain.trust.consensus.p2pvalid.core.IdValidCommand;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The type Change master verify response cmd.
 *
 * @author suimi
 * @date 2018 /6/5
 */
@Getter @Setter @NoArgsConstructor public class ChangeMasterVerifyResponseCmd
    extends IdValidCommand<ChangeMasterVerifyResponse> {
    private static final long serialVersionUID = 7506595686406239636L;

    /**
     * Instantiates a new Change master verify response cmd.
     *
     * @param requestId                  the request id
     * @param changeMasterVerifyResponse the change master verify response
     */
    public ChangeMasterVerifyResponseCmd(String requestId, ChangeMasterVerifyResponse changeMasterVerifyResponse) {
        super(requestId, changeMasterVerifyResponse);
    }

    @Override public String messageDigest() {
        return getRequestId() + get().isChangeMaster();
    }
}

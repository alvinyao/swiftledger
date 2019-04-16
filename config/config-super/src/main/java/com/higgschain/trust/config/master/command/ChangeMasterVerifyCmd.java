/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.config.master.command;

import com.higgschain.trust.consensus.p2pvalid.core.ValidCommand;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The type Change master verify cmd.
 *
 * @author suimi
 * @date 2018 /6/5
 */
@Getter @Setter @NoArgsConstructor public class ChangeMasterVerifyCmd extends ValidCommand<ChangeMasterVerify> {
    private static final long serialVersionUID = 961625577325944353L;

    private static final String CHANGE_MASTER_VERIFY = "change_master_verify";

    private String requestId;

    /**
     * Instantiates a new Change master verify cmd.
     *
     * @param value the value
     */
    public ChangeMasterVerifyCmd(ChangeMasterVerify value) {
        super(value, value.getView());
        this.requestId = String.join("_", CHANGE_MASTER_VERIFY, "" + value.getTerm(), "" + value.getView(),
            "" + System.currentTimeMillis());
    }

    @Override public String messageDigest() {
        return requestId;
    }
}

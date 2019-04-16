/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.config.master.command;

import lombok.*;

import java.io.Serializable;

/**
 * The type Change master verify response.
 *
 * @author suimi
 * @date 2018 /6/5
 */
@Data @NoArgsConstructor @RequiredArgsConstructor @ToString(exclude = {"sign"}) public class ChangeMasterVerifyResponse
    implements Serializable {

    private static final long serialVersionUID = -6091022260408731431L;

    /**
     * the term number
     */
    @NonNull private long term;

    /**
     * the cluster view number
     */
    @NonNull private long view;

    /**
     * the node name of voter
     */
    @NonNull private String voter;

    /**
     * the node name of proposer
     */
    @NonNull private String proposer;

    /**
     * package height
     */
    @NonNull private long packageHeight;

    /**
     * if change the master
     */
    @NonNull private boolean changeMaster;

    /**
     * signature
     */
    private String sign;

    /**
     * Gets sign value.
     *
     * @return the sign value
     */
    public String getSignValue() {
        return String.join(",", "" + term, "" + view, voter, proposer, "" + packageHeight, "" + changeMaster);
    }
}

/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.slave.model.bo.consensus;

import com.higgschain.trust.consensus.p2pvalid.core.ValidCommand;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The type Cluster height cmd.
 *
 * @author suimi
 * @date 2018 /4/17
 */
@NoArgsConstructor @Getter @Setter public class ClusterHeightCmd extends ValidCommand<Integer> {

    private static final long serialVersionUID = -2067709119627092336L;

    private String requestId;

    /**
     * Instantiates a new Cluster height cmd.
     *
     * @param requestId the request id
     * @param value     the value
     * @param view      the view
     */
    public ClusterHeightCmd(String requestId, Integer value, long view) {
        super(value, view);
        this.requestId = requestId;
    }

    @Override public String messageDigest() {
        return requestId;
    }
}

/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.slave.core.service.consensus.view;

import com.higgschain.trust.consensus.p2pvalid.core.ValidCommand;
import lombok.NoArgsConstructor;

/**
 * The type Cluster view cmd.
 *
 * @author suimi
 * @date 2018 /6/19
 */
@NoArgsConstructor public class ClusterViewCmd extends ValidCommand<String> {

    private static final long serialVersionUID = -7729848938347712491L;

    /**
     * Instantiates a new Cluster view cmd.
     *
     * @param requestId the request id
     * @param view      the view
     */
    public ClusterViewCmd(String requestId, long view) {
        super(requestId, view);
    }

    @Override public String messageDigest() {
        return get();
    }
}

/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.slave.core.service.consensus.view;

import com.higgschain.trust.consensus.view.ClusterView;
import com.higgschain.trust.consensus.p2pvalid.core.IdValidCommand;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Comparator;
import java.util.Map;

/**
 * The type Valid cluster view cmd.
 *
 * @author suimi
 * @date 2018 /6/19
 */
@NoArgsConstructor @Getter @Setter public class ValidClusterViewCmd extends IdValidCommand<ClusterView> {

    private static final long serialVersionUID = -3243023833695624710L;

    /**
     * Instantiates a new Valid cluster view cmd.
     *
     * @param requestId   the request id
     * @param clusterView the cluster view
     */
    public ValidClusterViewCmd(String requestId, ClusterView clusterView) {
        super(requestId, clusterView);
    }

    @Override public String messageDigest() {
        StringBuilder sb = new StringBuilder();
        get().getNodes().entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey))
            .forEach(entry -> sb.append(entry.getKey()).append(entry.getValue()));
        return String.join(",", getRequestId(), "" + get().getFaultNum(), sb.toString());
    }
}

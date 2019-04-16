package com.higgschain.trust.slave.core.service.consensus.log;

import com.higgschain.trust.slave.model.bo.consensus.PackageCommand;

/**
 * The interface Log replicate handler.
 *
 * @Description: log replicate handler, mostly deal with sorted package
 * @author: pengdi
 */
public interface LogReplicateHandler {

    /**
     * replicate sorted package to the cluster
     *
     * @param command the command
     */
    void replicatePackage(PackageCommand command);
}

/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.slave.core.service.consensus.log;

import com.higgschain.trust.slave.model.bo.Package;

/**
 * The interface Package listener.
 *
 * @author suimi
 * @date 2018 /6/13
 */
public interface PackageListener {

    /**
     * Received.
     *
     * @param pack the pack
     */
    void received(Package pack);
}

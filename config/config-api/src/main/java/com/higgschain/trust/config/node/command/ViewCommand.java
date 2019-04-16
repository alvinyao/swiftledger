/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.config.node.command;

import com.higgschain.trust.config.view.ClusterOptTx;

/**
 * The interface View command.
 *
 * @author suimi
 * @date 2018 /9/4
 */
public interface ViewCommand {

    /**
     * get the view id
     *
     * @return the view
     */
    long getView();

    /**
     * get the package height
     *
     * @return the package height
     */
    long getPackageHeight();

    /**
     * get the package time
     *
     * @return the package time
     */
    long getPackageTime();

    /**
     * get the cluster operation transaction {@link ClusterOptTx}
     *
     * @return the cluster opt tx
     */
    ClusterOptTx getClusterOptTx();

}

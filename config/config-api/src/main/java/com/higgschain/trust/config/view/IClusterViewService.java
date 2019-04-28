package com.higgschain.trust.config.view;

/**
 * @author suimi
 * @date 2019/4/28
 */
public interface IClusterViewService {

    /**
     * init the cluster view from db
     *
     * @param useCurrentHeight if use current height
     */
    void initClusterViewFromDB(boolean useCurrentHeight);

    /**
     * init the cluster view from cluster
     */
    void initClusterViewFromCluster();
}

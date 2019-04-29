/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.consensus.view;

import java.util.List;

/**
 * The interface Cluster view manager.
 */
public interface IClusterViewManager {

    /**
     * The constant CURRENT_VIEW_ID.
     */
    long CURRENT_VIEW_ID = -1;

    /**
     * reset the cluster views
     *
     * @param views the views
     */
    void resetViews(List<ClusterView> views);

    /**
     * get the cluster views
     *
     * @return the views
     */
    List<ClusterView> getViews();

    /**
     * get current view, if null will be return start view
     *
     * @return the current view
     */
    ClusterView getCurrentView();

    /**
     * get current view id
     *
     * @return the current view id
     */
    default long getCurrentViewId() {
        return getCurrentView().getId();
    }

    /**
     * get the {@link ClusterView} at height
     *
     * @param height the height
     * @return the view with height
     */
    ClusterView getViewWithHeight(long height);

    /**
     * get the cluster view by view id, if viewId is {@link IClusterViewManager#CURRENT_VIEW_ID}, return the currentView.
     *
     * @param viewId the view id
     * @return the view
     */
    ClusterView getView(long viewId);

    /**
     * change cluster view
     *
     * @param command the command
     */
    void changeView(ViewCommand command);

    /**
     * reset the end height of current view
     *
     * @param height the height
     */
    void resetEndHeight(long height);

    /**
     * get the last package
     *
     * @return the last package
     */
    LastPackage getLastPackage();

    /**
     * reset the last package
     *
     * @param lastPackage the last package
     */
    void resetLastPackage(LastPackage lastPackage);
}

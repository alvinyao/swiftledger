/**
 * Copyright (c) 2007-2013 Alysson Bessani, Eduardo Alchieri, Paulo Sousa, and the authors indicated in the @author tags
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package bftsmart.reconfiguration;

import bftsmart.reconfiguration.util.TOMConfiguration;
import bftsmart.reconfiguration.views.DefaultViewStorage;
import bftsmart.reconfiguration.views.View;
import bftsmart.reconfiguration.views.ViewStorage;

import java.net.SocketAddress;

/**
 * The type View controller.
 *
 * @author eduardo
 */
public class ViewController {

    /**
     * The Last view.
     */
    protected View lastView = null;
    /**
     * The Current view.
     */
    protected View currentView = null;
    private TOMConfiguration staticConf;
    private ViewStorage viewStore;

    /**
     * Instantiates a new View controller.
     *
     * @param procId the proc id
     */
    public ViewController(int procId) {
        this.staticConf = new TOMConfiguration(procId);
    }

    /**
     * Instantiates a new View controller.
     *
     * @param procId     the proc id
     * @param configHome the config home
     */
    public ViewController(int procId, String configHome) {
        this.staticConf = new TOMConfiguration(procId, configHome);
    }

    /**
     * Gets view store.
     *
     * @return the view store
     */
    public final ViewStorage getViewStore() {
        if (this.viewStore == null) {
            String className = staticConf.getViewStoreClass();
            try {
                this.viewStore = (ViewStorage)Class.forName(className).newInstance();
            } catch (Exception e) {
                this.viewStore = new DefaultViewStorage();
            }

        }
        return this.viewStore;
    }

    /**
     * Gets current view.
     *
     * @return the current view
     */
    public View getCurrentView() {
        if (this.currentView == null) {
            this.currentView = getViewStore().readView();
        }
        return this.currentView;
    }

    /**
     * Gets last view.
     *
     * @return the last view
     */
    public View getLastView() {
        return this.lastView;
    }

    /**
     * Gets remote address.
     *
     * @param id the id
     * @return the remote address
     */
    public SocketAddress getRemoteAddress(int id) {
        return getCurrentView().getAddress(id);
    }

    /**
     * Reconfigure to.
     *
     * @param newView the new view
     */
    public void reconfigureTo(View newView) {
        this.lastView = this.currentView;
        this.currentView = newView;
    }

    /**
     * Gets static conf.
     *
     * @return the static conf
     */
    public TOMConfiguration getStaticConf() {
        return staticConf;
    }

    /**
     * Is current view member boolean.
     *
     * @param id the id
     * @return the boolean
     */
    public boolean isCurrentViewMember(int id) {
        return getCurrentView().isMember(id);
    }

    /**
     * Gets current view id.
     *
     * @return the current view id
     */
    public int getCurrentViewId() {
        return getCurrentView().getId();
    }

    /**
     * Gets current view f.
     *
     * @return the current view f
     */
    public int getCurrentViewF() {
        return getCurrentView().getF();
    }

    /**
     * Gets current view n.
     *
     * @return the current view n
     */
    public int getCurrentViewN() {
        return getCurrentView().getN();
    }

    /**
     * Gets current view pos.
     *
     * @param id the id
     * @return the current view pos
     */
    public int getCurrentViewPos(int id) {
        return getCurrentView().getPos(id);
    }

    /**
     * Get current view processes int [ ].
     *
     * @return the int [ ]
     */
    public int[] getCurrentViewProcesses() {
        return getCurrentView().getProcesses();
    }
}
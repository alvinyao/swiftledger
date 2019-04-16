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
package bftsmart.statemanagement;

import bftsmart.tom.core.DeliveryThread;
import bftsmart.tom.core.TOMLayer;

/**
 * TODO: Don't know if this class will be used. For now, leave it here
 * <p>
 * Check if the changes for supporting dynamicity are correct
 *
 * @author Joao Sousa
 */
public interface StateManager {

    /**
     * Request app state.
     *
     * @param cid the cid
     */
    public void requestAppState(int cid);

    /**
     * Analyze state.
     *
     * @param cid the cid
     */
    public void analyzeState(int cid);

    /**
     * State timeout.
     */
    public void stateTimeout();

    /**
     * Init.
     *
     * @param tomLayer the tom layer
     * @param dt       the dt
     */
    public void init(TOMLayer tomLayer, DeliveryThread dt);

    /**
     * Sm request deliver.
     *
     * @param msg   the msg
     * @param isBFT the is bft
     */
    public void SMRequestDeliver(SMMessage msg, boolean isBFT);

    /**
     * Sm reply deliver.
     *
     * @param msg   the msg
     * @param isBFT the is bft
     */
    public void SMReplyDeliver(SMMessage msg, boolean isBFT);

    /**
     * Ask current consensus id.
     */
    public void askCurrentConsensusId();

    /**
     * Current consensus id asked.
     *
     * @param sender the sender
     */
    public void currentConsensusIdAsked(int sender);

    /**
     * Current consensus id received.
     *
     * @param msg the msg
     */
    public void currentConsensusIdReceived(SMMessage msg);

    /**
     * Sets last cid.
     *
     * @param lastCID the last cid
     */
    public void setLastCID(int lastCID);

    /**
     * Init last cid.
     *
     * @param lastCID the last cid
     */
    public void initLastCID(int lastCID);

    /**
     * Gets last cid.
     *
     * @return the last cid
     */
    public int getLastCID();

    /**
     * Is retrieving state boolean.
     *
     * @return the boolean
     */
    public boolean isRetrievingState();
}

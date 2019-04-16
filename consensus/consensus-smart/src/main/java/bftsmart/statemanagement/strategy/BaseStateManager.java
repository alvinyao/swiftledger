/**
 * Copyright (c) 2007-2013 Alysson Bessani, Eduardo Alchieri, Paulo Sousa, and
 * the authors indicated in the @author tags
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package bftsmart.statemanagement.strategy;

import bftsmart.consensus.messages.ConsensusMessage;
import bftsmart.reconfiguration.ServerViewController;
import bftsmart.reconfiguration.views.View;
import bftsmart.statemanagement.ApplicationState;
import bftsmart.statemanagement.SMMessage;
import bftsmart.statemanagement.StateManager;
import bftsmart.tom.core.DeliveryThread;
import bftsmart.tom.core.TOMLayer;
import bftsmart.tom.leaderchange.CertifiedDecision;
import bftsmart.tom.leaderchange.LCManager;
import bftsmart.tom.util.Logger;
import bftsmart.tom.util.TOMUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**
 * The type Base state manager.
 *
 * @author Marcel Santos
 */
@Slf4j public abstract class BaseStateManager implements StateManager {

    /**
     * The Tom layer.
     */
    protected TOMLayer tomLayer;
    /**
     * The Sv controller.
     */
    protected ServerViewController SVController;
    /**
     * The Dt.
     */
    protected DeliveryThread dt;

    /**
     * The Sender states.
     */
    protected HashMap<Integer, ApplicationState> senderStates = null;
    /**
     * The Sender views.
     */
    protected HashMap<Integer, View> senderViews = null;
    /**
     * The Sender regencies.
     */
    protected HashMap<Integer, Integer> senderRegencies = null;
    /**
     * The Sender leaders.
     */
    protected HashMap<Integer, Integer> senderLeaders = null;
    /**
     * The Sender proofs.
     */
    protected HashMap<Integer, CertifiedDecision> senderProofs = null;

    /**
     * The App state only.
     */
    protected boolean appStateOnly;
    /**
     * The Waiting cid.
     */
    protected int waitingCID = -1;
    /**
     * The Last cid.
     */
    protected int lastCID;
    /**
     * The State.
     */
    protected ApplicationState state;

    /**
     * The Is initializing.
     */
    protected boolean isInitializing = true;
    private HashMap<Integer, Integer> senderCIDs = null;

    /**
     * Instantiates a new Base state manager.
     */
    public BaseStateManager() {
        senderStates = new HashMap<>();
        senderViews = new HashMap<>();
        senderRegencies = new HashMap<>();
        senderLeaders = new HashMap<>();
        senderProofs = new HashMap<>();
    }

    /**
     * Gets replies.
     *
     * @return the replies
     */
    protected int getReplies() {
        return senderStates.size();
    }

    /**
     * Enough replies boolean.
     *
     * @return the boolean
     */
    protected boolean enoughReplies() {
        return senderStates.size() > SVController.getCurrentViewF();
    }

    /**
     * Enough regencies boolean.
     *
     * @param regency the regency
     * @return the boolean
     */
    protected boolean enoughRegencies(int regency) {
        return senderRegencies.size() > SVController.getQuorum();
    }

    /**
     * Enough leaders boolean.
     *
     * @param leader the leader
     * @return the boolean
     */
    protected boolean enoughLeaders(int leader) {
        return senderLeaders.size() > SVController.getQuorum();
    }

    /**
     * Enough views boolean.
     *
     * @param view the view
     * @return the boolean
     */
    protected boolean enoughViews(View view) {
        Collection<View> views = senderViews.values();
        int counter = 0;
        for (View v : views) {
            if (view.equals(v)) {
                counter++;
            }
        }
        boolean result = counter > SVController.getQuorum();
        return result;
    }
    
    // check if the consensus messages are consistent without checking the mac/signatures
    // if it is consistent, it returns the respective consensus ID; otherwise, returns -1
    private int proofIsConsistent(Set<ConsensusMessage> proof) {
        
        int id = -1;
        byte[] value = null;
        
        for (ConsensusMessage cm : proof) {
            
            if (id == -1) id = cm.getNumber();
            if (value == null) value = cm.getValue();
            
            if (id != cm.getNumber() || !Arrays.equals(value, cm.getValue())) {
                return -1; // they are not consistent, so the proof is invalid
            }
                    
        }
        
        // if the values are still these, this means the proof is empty, thus is invalid
        if (id == -1 || value == null) return -1;
        
        return id;
    }

    /**
     * Enough proofs boolean.
     *
     * @param cid the cid
     * @param lc  the lc
     * @return the boolean
     */
    protected boolean enoughProofs(int cid, LCManager lc) {
        
        int counter = 0;
        for (CertifiedDecision cDec : senderProofs.values()) {
                                    
            if (cDec != null && cid == proofIsConsistent(cDec.getConsMessages()) && lc.hasValidProof(cDec)) {
                counter++;
            }
            
        }
        boolean result = counter > SVController.getQuorum();
        return result;
    }

    /**
     * Clear the collections and state hold by this object. Calls clear() in the
     * States, Leaders, regencies and Views collections. Sets the state to
     * null;
     */
    protected void reset() {
        senderStates.clear();
        senderLeaders.clear();
        senderRegencies.clear();
        senderViews.clear();
        senderProofs.clear();
        state = null;
    }

    /**
     * Received states collection.
     *
     * @return the collection
     */
    public Collection<ApplicationState> receivedStates() {
        return senderStates.values();
    }

    @Override
    public void setLastCID(int cid) {
        lastCID = cid;
    }

    @Override
    public int getLastCID() {
        return lastCID;
    }

    @Override
    public void requestAppState(int cid) {
        lastCID = cid + 1;
        waitingCID = cid;
        System.out.println("waitingcid is now " + cid);
        appStateOnly = true;
        requestState();
    }

    @Override
    public void analyzeState(int cid) {
        Logger.println("(TOMLayer.analyzeState) The state transfer protocol is enabled");
        if (waitingCID == -1) {
            Logger.println("(TOMLayer.analyzeState) I'm not waiting for any state, so I will keep record of this message");
            if (tomLayer.execManager.isDecidable(cid)) {
                System.out.println("BaseStateManager.analyzeState: I have now more than " + SVController.getCurrentViewF() + " messages for CID " + cid + " which are beyond CID " + lastCID);
                lastCID = cid;
                waitingCID = cid - 1;
                System.out.println("analyzeState " + waitingCID);
                requestState();
            }
        }
    }

    @Override
    public abstract void init(TOMLayer tomLayer, DeliveryThread dt);

    @Override
    public boolean isRetrievingState() {
        if (isInitializing) {
            return true;
        }
        return waitingCID > -1;
    }

    @Override
    public void askCurrentConsensusId() {
        int me = SVController.getStaticConf().getProcessId();
        int[] target = SVController.getCurrentViewAcceptors();

        SMMessage currentCID = new StandardSMMessage(me, -1, TOMUtil.SM_ASK_INITIAL, 0, null, null, 0, 0);
        tomLayer.getCommunication().send(target, currentCID);

        target = SVController.getCurrentViewOtherAcceptors();

        while (isInitializing) {
            tomLayer.getCommunication().send(target, currentCID);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void currentConsensusIdAsked(int sender) {
        int me = SVController.getStaticConf().getProcessId();
        int lastConsensusId = tomLayer.getLastExec();
        SMMessage currentCIDReply = new StandardSMMessage(me, lastConsensusId, TOMUtil.SM_REPLY_INITIAL, 0, null, null, 0, 0);
        tomLayer.getCommunication().send(new int[]{sender}, currentCIDReply);
    }

    @Override
    public synchronized void currentConsensusIdReceived(SMMessage smsg) {
        if (!isInitializing || waitingCID > -1) {            
            return;
        }
        if (senderCIDs == null) {
            senderCIDs = new HashMap<>();
        }
        senderCIDs.put(smsg.getSender(), smsg.getCID());
        log.info("senderCIDs.size={},SVController.getQuorum={}", senderCIDs.size(), SVController.getQuorum());
        if (senderCIDs.size() >= SVController.getQuorum()) {

            HashMap<Integer, Integer> cids = new HashMap<>();
            for (int id : senderCIDs.keySet()) {
                                
                int value = senderCIDs.get(id);
                
                Integer count = cids.get(value);
                if (count == null) {
                    cids.put(value, 0);
                } else {
                    cids.put(value, count + 1);
                }
            }
            for (int key : cids.keySet()) {
                if (cids.get(key) >= SVController.getQuorum()) {
                    if (key == lastCID) {
                        Logger.println("-- Replica state is up to date, lastCID=" + lastCID);
                        dt.deliverLock();
                        isInitializing = false;
                        tomLayer.setLastExec(key);
                        dt.canDeliver();
                        dt.deliverUnlock();
                        break;
                    } else {
                        //ask for state
                        Logger.println("-- Requesting state from other replicas");
                        lastCID = key + 1;
                        if (waitingCID == -1) {
                            waitingCID = key;
                            requestState();
                        }
                    }
                }
            }
        }
    }

    /**
     * Request state.
     */
    protected abstract void requestState();

    @Override
    public abstract void stateTimeout();

    @Override
    public abstract void SMRequestDeliver(SMMessage msg, boolean isBFT);

    @Override
    public abstract void SMReplyDeliver(SMMessage msg, boolean isBFT);

    @Override public void initLastCID(int lastCID) {
        this.lastCID = lastCID;
        tomLayer.setLastExec(lastCID);
    }
}

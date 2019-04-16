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
package bftsmart.tom;

import bftsmart.communication.client.CommunicationSystemClientSide;
import bftsmart.communication.client.CommunicationSystemClientSideFactory;
import bftsmart.communication.client.ReplyReceiver;
import bftsmart.reconfiguration.ClientViewController;
import bftsmart.tom.core.messages.TOMMessage;
import bftsmart.tom.core.messages.TOMMessageType;

import java.io.Closeable;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class is used to multicast messages to replicas and receive replies.
 */
public abstract class TOMSender implements ReplyReceiver, Closeable, AutoCloseable {

    private int me; // process id

    private ClientViewController viewController;

    private int session = 0; // session id
    private int sequence = 0; // sequence number
    private int unorderedMessageSequence = 0; // sequence number for readonly messages
    private CommunicationSystemClientSide cs; // Client side comunication system
    private Lock lock = new ReentrantLock(); // lock to manage concurrent access to this object by other threads
    private boolean useSignatures = false;
    private AtomicInteger opCounter = new AtomicInteger(0);

    /**
     * Creates a new instance of TOMulticastSender
     * <p>
     * TODO: This may really be empty?
     */
    public TOMSender() {
    }

    public void close() {
        cs.close();
    }

    /**
     * Gets communication system.
     *
     * @return the communication system
     */
    public CommunicationSystemClientSide getCommunicationSystem() {
        return this.cs;
    }

    /**
     * Gets view manager.
     *
     * @return the view manager
     */
    //******* EDUARDO BEGIN **************//
    public ClientViewController getViewManager() {
        return this.viewController;
    }

    /**
     * This method initializes the object
     * TODO: Ask if this method cannot be protected (compiles, but....)
     *
     * @param processId ID of the process
     */
    public void init(int processId) {
        this.viewController = new ClientViewController(processId);
        startsCS(processId);
    }

    /**
     * Init.
     *
     * @param processId  the process id
     * @param configHome the config home
     */
    public void init(int processId, String configHome) {
        this.viewController = new ClientViewController(processId, configHome);
        startsCS(processId);
    }

    private void startsCS(int clientId) {
        this.cs = CommunicationSystemClientSideFactory.getCommunicationSystemClientSide(clientId, this.viewController);
        this.cs.setReplyReceiver(this); // This object itself shall be a reply receiver
        this.me = this.viewController.getStaticConf().getProcessId();
        this.useSignatures = this.viewController.getStaticConf().getUseSignatures() == 1 ? true : false;
        this.session = new Random().nextInt();
    }
    //******* EDUARDO END **************//

    /**
     * Gets process id.
     *
     * @return the process id
     */
    public int getProcessId() {
        return me;
    }

    /**
     * Generate request id int.
     *
     * @param type the type
     * @return the int
     */
    public int generateRequestId(TOMMessageType type) {
        lock.lock();
        int id;
        if (type == TOMMessageType.ORDERED_REQUEST)
            id = sequence++;
        else
            id = unorderedMessageSequence++;
        lock.unlock();

        return id;
    }

    /**
     * Generate operation id int.
     *
     * @return the int
     */
    public int generateOperationId() {
        return opCounter.getAndIncrement();
    }

    /**
     * To multicast.
     *
     * @param sm the sm
     */
    public void TOMulticast(TOMMessage sm) {
        cs.send(useSignatures, this.viewController.getCurrentViewProcesses(), sm);
    }

    /**
     * To multicast.
     *
     * @param m           the m
     * @param reqId       the req id
     * @param operationId the operation id
     * @param reqType     the req type
     */
    public void TOMulticast(byte[] m, int reqId, int operationId, TOMMessageType reqType) {
        cs.send(useSignatures, viewController.getCurrentViewProcesses(),
            new TOMMessage(me, session, reqId, operationId, m, viewController.getCurrentViewId(), reqType));
    }

    /**
     * Send message to targets.
     *
     * @param m           the m
     * @param reqId       the req id
     * @param operationId the operation id
     * @param targets     the targets
     * @param type        the type
     */
    public void sendMessageToTargets(byte[] m, int reqId, int operationId, int[] targets, TOMMessageType type) {
        if (this.getViewManager().getStaticConf().isTheTTP()) {
            type = TOMMessageType.ASK_STATUS;
        }
        cs.send(useSignatures, targets,
            new TOMMessage(me, session, reqId, operationId, m, viewController.getCurrentViewId(), type));
    }

    /**
     * Gets session.
     *
     * @return the session
     */
    public int getSession() {
        return session;
    }
}

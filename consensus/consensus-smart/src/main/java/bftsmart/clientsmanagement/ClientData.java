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
package bftsmart.clientsmanagement;

import bftsmart.tom.core.messages.TOMMessage;
import bftsmart.tom.util.Logger;
import bftsmart.tom.util.TOMUtil;

import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The type Client data.
 */
public class ClientData {

    /**
     * The Client lock.
     */
    ReentrantLock clientLock = new ReentrantLock();

    private int clientId;
    //private PublicKey publicKey = null;

    private int session = -1;

    private int lastMessageReceived = -1;
    private long lastMessageReceivedTime = 0;

    private int lastMessageExecuted = -1;

    private RequestList pendingRequests = new RequestList();
    //anb: new code to deal with client requests that arrive after their execution
    private RequestList orderedRequests = new RequestList(5);

    private Signature signatureVerificator = null;

    /**
     * Class constructor. Just store the clientId and creates a signature
     * verificator for a given client public key.
     *
     * @param clientId  client unique id
     * @param publicKey client public key
     */
    public ClientData(int clientId, PublicKey publicKey) {
        this.clientId = clientId;
        if (publicKey != null) {
            try {
                signatureVerificator = Signature.getInstance("SHA1withRSA");
                signatureVerificator.initVerify(publicKey);
                Logger.println("Signature verifier initialized for client " + clientId);
            } catch (Exception ex) {
                Logger.printError(ex.getMessage(), ex);
            }
        }
    }

    /**
     * Gets client id.
     *
     * @return the client id
     */
    public int getClientId() {
        return clientId;
    }

    /**
     * Gets session.
     *
     * @return the session
     */
    public int getSession() {
        return session;
    }

    /**
     * Sets session.
     *
     * @param session the session
     */
    public void setSession(int session) {
        this.session = session;
    }

    /**
     * Gets pending requests.
     *
     * @return the pending requests
     */
    public RequestList getPendingRequests() {
        return pendingRequests;
    }

    /**
     * Gets ordered requests.
     *
     * @return the ordered requests
     */
    public RequestList getOrderedRequests() {
        return orderedRequests;
    }

    /**
     * Sets last message executed.
     *
     * @param lastMessageExecuted the last message executed
     */
    public void setLastMessageExecuted(int lastMessageExecuted) {
        this.lastMessageExecuted = lastMessageExecuted;
    }

    /**
     * Gets last message executed.
     *
     * @return the last message executed
     */
    public int getLastMessageExecuted() {
        return lastMessageExecuted;
    }

    /**
     * Sets last message received.
     *
     * @param lastMessageReceived the last message received
     */
    public void setLastMessageReceived(int lastMessageReceived) {
        this.lastMessageReceived = lastMessageReceived;
    }

    /**
     * Gets last message received.
     *
     * @return the last message received
     */
    public int getLastMessageReceived() {
        return lastMessageReceived;
    }

    /**
     * Sets last message received time.
     *
     * @param lastMessageReceivedTime the last message received time
     */
    public void setLastMessageReceivedTime(long lastMessageReceivedTime) {
        this.lastMessageReceivedTime = lastMessageReceivedTime;
    }

    /**
     * Gets last message received time.
     *
     * @return the last message received time
     */
    public long getLastMessageReceivedTime() {
        return lastMessageReceivedTime;
    }

    /**
     * Verify signature boolean.
     *
     * @param message   the message
     * @param signature the signature
     * @return the boolean
     */
    public boolean verifySignature(byte[] message, byte[] signature) {
        if (signatureVerificator != null) {
            try {
                return TOMUtil.verifySignature(signatureVerificator, message, signature);
            } catch (SignatureException ex) {
                Logger.printError("Error in processing client " + clientId + " signature: ", ex);
            }
        }
        return false;
    }

    /**
     * Remove ordered request boolean.
     *
     * @param request the request
     * @return the boolean
     */
    public boolean removeOrderedRequest(TOMMessage request) {
        if (pendingRequests.remove(request)) {
            //anb: new code to deal with client requests that arrive after their execution
            orderedRequests.addLast(request);
            return true;
        }
        return false;
    }

    /**
     * Remove request boolean.
     *
     * @param request the request
     * @return the boolean
     */
    public boolean removeRequest(TOMMessage request) {
        lastMessageExecuted = request.getSequence();
        boolean result = pendingRequests.remove(request);
        //anb: new code to deal with client requests that arrive after their execution
        orderedRequests.addLast(request);

        for (Iterator<TOMMessage> it = pendingRequests.iterator(); it.hasNext(); ) {
            TOMMessage msg = it.next();
            if (msg.getSequence() < request.getSequence()) {
                it.remove();
            }
        }

        return result;
    }

    /**
     * Gets reply.
     *
     * @param reqSequence the req sequence
     * @return the reply
     */
    public TOMMessage getReply(int reqSequence) {
        TOMMessage request = orderedRequests.getBySequence(reqSequence);
        if (request != null) {
            return request.reply;
        } else {
            return null;
        }
    }

}

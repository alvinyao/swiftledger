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

import bftsmart.consensus.messages.ConsensusMessage;
import bftsmart.tom.core.messages.TOMMessage;
import bftsmart.tom.core.messages.TOMMessageType;

import java.io.Serializable;
import java.util.Random;
import java.util.Set;

/**
 * This class represents the whole context of a request ordered in the system.
 * It stores all informations regarding the message sent by the client, as well as
 * the consensus instance in which it was ordered.
 *
 * @author alysson
 */
public class MessageContext implements Serializable {

    private static final long serialVersionUID = -3757195646384786213L;

    // Client info
    private final int sender;
    private final int viewID;
    private final TOMMessageType type;
    private final int session;
    private final int sequence;
    private final int operationId;
    private final int replyServer;
    private final byte[] signature;

    // Consensus info
    private final long timestamp;
    private final int regency;
    private final int leader;
    private final int consensusId;
    private final int numOfNonces;
    private final long seed;
    private final Set<ConsensusMessage> proof;

    private final TOMMessage firstInBatch; //to be replaced by a statistics class
    private boolean lastInBatch; // indicates that the command is the last in the batch. Used for logging
    private final boolean noOp;

    /**
     * The Read only.
     */
    public boolean readOnly = false;

    private byte[] nonces;

    /**
     * Constructor
     *
     * @param sender       the sender
     * @param viewID       the view id
     * @param type         the type
     * @param session      the session
     * @param sequence     the sequence
     * @param operationId  the operation id
     * @param replyServer  the reply server
     * @param signature    the signature
     * @param timestamp    the timestamp
     * @param numOfNonces  the num of nonces
     * @param seed         the seed
     * @param regency      the regency
     * @param leader       the leader
     * @param consensusId  the consensus id
     * @param proof        the proof
     * @param firstInBatch the first in batch
     * @param noOp         the no op
     */
    public MessageContext(int sender, int viewID, TOMMessageType type, int session, int sequence, int operationId,
        int replyServer, byte[] signature, long timestamp, int numOfNonces, long seed, int regency, int leader,
        int consensusId, Set<ConsensusMessage> proof, TOMMessage firstInBatch, boolean noOp) {

        this.nonces = null;

        this.sender = sender;
        this.viewID = viewID;
        this.type = type;
        this.session = session;
        this.sequence = sequence;
        this.operationId = operationId;
        this.replyServer = replyServer;
        this.signature = signature;

        this.timestamp = timestamp;
        this.regency = regency;
        this.leader = leader;
        this.consensusId = consensusId;
        this.numOfNonces = numOfNonces;
        this.seed = seed;

        this.proof = proof;
        this.firstInBatch = firstInBatch;
        this.noOp = noOp;
    }

    /**
     * Gets serial version uid.
     *
     * @return the serial version uid
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * Gets view id.
     *
     * @return the view id
     */
    public int getViewID() {
        return viewID;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public TOMMessageType getType() {
        return type;
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
     * Gets sequence.
     *
     * @return the sequence
     */
    public int getSequence() {
        return sequence;
    }

    /**
     * Gets operation id.
     *
     * @return the operation id
     */
    public int getOperationId() {
        return operationId;
    }

    /**
     * Gets reply server.
     *
     * @return the reply server
     */
    public int getReplyServer() {
        return replyServer;
    }

    /**
     * Get signature byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getSignature() {
        return signature;
    }

    /**
     * Returns the sender of the message
     *
     * @return The sender of the message
     */
    public int getSender() {
        return sender;
    }

    /**
     * Gets timestamp.
     *
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Get nonces byte [ ].
     *
     * @return the nonces
     */
    public byte[] getNonces() {

        if (nonces == null) { //obtain the nonces to be delivered to the application          

            nonces = new byte[numOfNonces];
            if (nonces.length > 0) {
                Random rnd = new Random(seed);
                rnd.nextBytes(nonces);
            }

        }

        return nonces;
    }

    /**
     * Gets num of nonces.
     *
     * @return the num of nonces
     */
    public int getNumOfNonces() {
        return numOfNonces;
    }

    /**
     * Gets seed.
     *
     * @return the seed
     */
    public long getSeed() {
        return seed;
    }

    /**
     * Gets consensus id.
     *
     * @return the consensusId
     */
    public int getConsensusId() {
        return consensusId;
    }

    /**
     * Gets leader.
     *
     * @return the leader with which the batch was decided
     */
    public int getLeader() {
        return leader;
    }

    /**
     * Gets proof.
     *
     * @return the proof for the consensus
     */
    public Set<ConsensusMessage> getProof() {
        return proof;
    }

    /**
     * Gets regency.
     *
     * @return the regency
     */
    public int getRegency() {
        return regency;
    }

    /**
     * Gets first in batch.
     *
     * @return the first message in the ordered batch
     */
    public TOMMessage getFirstInBatch() {
        return firstInBatch;
    }

    /**
     * Sets last in batch.
     */
    public void setLastInBatch() {
        lastInBatch = true;
    }

    /**
     * Is last in batch boolean.
     *
     * @return the boolean
     */
    public boolean isLastInBatch() {
        return lastInBatch;
    }

    /**
     * Is no op boolean.
     *
     * @return the boolean
     */
    public boolean isNoOp() {
        return noOp;
    }

    /**
     * Generates a TOMMessage for its associated requests using the new info that it now supports since the previous commit.
     * It is assumed that the byte array passed to this method is the serialized request associated to the original TOMMessage.
     *
     * @param content Serialized request associated to the original TOMMessage.
     * @return A TOMMessage object that is equal to the original object issued by the client
     */
    public TOMMessage recreateTOMMessage(byte[] content) {

        TOMMessage ret = new TOMMessage(sender, session, sequence, operationId, content, viewID, type);
        ret.setReplyServer(replyServer);
        ret.serializedMessageSignature = signature;
        ret.serializedMessage = TOMMessage.messageToBytes(ret);

        return ret;
    }

}
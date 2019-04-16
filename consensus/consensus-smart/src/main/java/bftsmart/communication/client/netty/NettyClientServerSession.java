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
package bftsmart.communication.client.netty;

import io.netty.channel.Channel;

import javax.crypto.Mac;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The type Netty client server session.
 *
 * @author Paulo Sousa
 */
public class NettyClientServerSession {
    private Channel channel;
    private Mac macSend;
    private Mac macReceive;
    private int replicaId;
    private Lock lock;
    private int lastMsgReceived;

    /**
     * Instantiates a new Netty client server session.
     *
     * @param channel    the channel
     * @param macSend    the mac send
     * @param macReceive the mac receive
     * @param replicaId  the replica id
     */
    public NettyClientServerSession(Channel channel, Mac macSend, Mac macReceive, int replicaId) {
        this.channel = channel;
        this.macSend = macSend;
        this.macReceive = macReceive;
        this.replicaId = replicaId;
        this.lock = new ReentrantLock();
        this.lastMsgReceived = -1;
    }

    /**
     * Gets mac receive.
     *
     * @return the mac receive
     */
    public Mac getMacReceive() {
        return macReceive;
    }

    /**
     * Gets mac send.
     *
     * @return the mac send
     */
    public Mac getMacSend() {
        return macSend;
    }

    /**
     * Gets channel.
     *
     * @return the channel
     */
    public Channel getChannel() {
        return channel;
    }

    /**
     * Gets replica id.
     *
     * @return the replica id
     */
    public int getReplicaId() {
        return replicaId;
    }

    /**
     * Gets lock.
     *
     * @return the lock
     */
    public Lock getLock() {
        return lock;
    }

    /**
     * Gets last msg received.
     *
     * @return the last msg received
     */
    public int getLastMsgReceived() {
        return lastMsgReceived;
    }

    /**
     * Sets last msg received.
     *
     * @param lastMsgReceived_ the last msg received
     */
    public void setLastMsgReceived(int lastMsgReceived_) {
        this.lastMsgReceived = lastMsgReceived_;
    }

}

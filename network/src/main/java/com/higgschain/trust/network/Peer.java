package com.higgschain.trust.network;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The type Peer.
 *
 * @author duhongming
 * @date 2018 /8/22
 */
public class Peer implements Serializable {

    private long nonce;
    private Address address;
    private String publicKey = "";
    private String nodeName = "";
    private int httpPort;
    private boolean backup;
    private transient boolean connected = false;
    private transient AtomicInteger connectTimes = new AtomicInteger(0);

    /**
     * Instantiates a new Peer.
     *
     * @param address the address
     */
    public Peer(Address address) {
        this.address = address;
    }

    /**
     * Instantiates a new Peer.
     *
     * @param address   the address
     * @param publicKey the public key
     */
    public Peer(Address address, String publicKey) {
        this(address);
        this.publicKey = publicKey;
    }

    /**
     * Instantiates a new Peer.
     *
     * @param address   the address
     * @param nodeName  the node name
     * @param publicKey the public key
     */
    public Peer(Address address, String nodeName, String publicKey) {
        this(address, publicKey);
        this.nodeName = nodeName;
    }

    /**
     * Gets nonce.
     *
     * @return the nonce
     */
    public long getNonce() {
        return nonce;
    }

    /**
     * Sets nonce.
     *
     * @param nonce the nonce
     */
    public void setNonce(long nonce) {
        this.nonce = nonce;
    }

    /**
     * Gets address.
     *
     * @return the address
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Sets address.
     *
     * @param address the address
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * Gets node name.
     *
     * @return the node name
     */
    public String getNodeName() {
        return nodeName;
    }

    /**
     * Sets node name.
     *
     * @param nodeName the node name
     */
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    /**
     * Gets public key.
     *
     * @return the public key
     */
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * Sets public key.
     *
     * @param publicKey the public key
     */
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * Gets http port.
     *
     * @return the http port
     */
    public int getHttpPort() {
        return httpPort;
    }

    /**
     * Sets http port.
     *
     * @param httpPort the http port
     */
    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }

    /**
     * Is backup boolean.
     *
     * @return the boolean
     */
    public boolean isBackup() {
        return backup;
    }

    /**
     * Sets backup.
     *
     * @param backup the backup
     */
    public void setBackup(boolean backup) {
        this.backup = backup;
    }

    /**
     * Is connected boolean.
     *
     * @return the boolean
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Sets connected.
     *
     * @param connected if connected
     * @return connect failed times
     */
    public synchronized int setConnected(boolean connected) {
        this.connected = connected;
        if (connected) {
            connectTimes.lazySet(0);
            return 0;
        } else {
            return connectTimes.incrementAndGet();
        }
    }

    /**
     * Update.
     *
     * @param newPeer the new peer
     */
    public void update(Peer newPeer) {
        nonce = newPeer.nonce;
        publicKey = newPeer.publicKey;
        nodeName = newPeer.nodeName;
        httpPort = newPeer.httpPort;
        backup = newPeer.backup;
        connectTimes.lazySet(0);
    }

    @Override public int hashCode() {
        return Objects.hash(address, publicKey, nodeName);
    }

    @Override public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        Peer that = (Peer)obj;
        return nodeName.equals(nodeName) && publicKey.equals(that.publicKey) && this.address.equals(that.address)
            && this.backup == that.backup;
    }

    @Override public String toString() {
        String pubKey = (publicKey != null && publicKey.length() > 24) ?
            String.format("%s...%s", publicKey.substring(0, 12), publicKey.substring(this.publicKey.length() - 12)) :
            publicKey;
        return String
            .format("%s, name=%s, nonce=%s publicKey=%s, connected=%s", address.toString(), nodeName, nonce, pubKey,
                connected);
    }
}

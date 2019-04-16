package com.higgschain.trust.network;

import java.io.Serializable;
import java.util.Objects;

/**
 * The type Peer.
 *
 * @author duhongming
 * @date 2018 /8/22
 */
public class Peer implements Serializable {

    /**
     * The constant MAX_TRY_CONNECT_TIMES.
     */
    public static final int MAX_TRY_CONNECT_TIMES = 10;

    private long nonce;
    private Address address;
    private String publicKey = "";
    private String nodeName = "";
    private int httpPort;
    private boolean isSlave;
    private transient boolean connected = false;

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
     * Is slave boolean.
     *
     * @return the boolean
     */
    public boolean isSlave() {
        return isSlave;
    }

    /**
     * Sets slave.
     *
     * @param slave the slave
     */
    public void setSlave(boolean slave) {
        isSlave = slave;
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
     * @param connected the connected
     */
    public void setConnected(boolean connected) {
        this.connected = connected;
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
        isSlave = newPeer.isSlave;
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, publicKey, nodeName);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        Peer that = (Peer) obj;
        return nodeName.equals(nodeName) && publicKey.equals(that.publicKey) && this.address.equals(that.address) && this.isSlave == that.isSlave;
    }

    @Override
    public String toString() {
        String pubKey = (publicKey != null && publicKey.length() > 24)
                ? String.format("%s...%s", publicKey.substring(0, 12), publicKey.substring(this.publicKey.length() - 12))
                : publicKey;
        return String.format("%s, name=%s, nonce=%s publicKey=%s, connected=%s",
                address.toString(), nodeName, nonce, pubKey, connected);
    }
}

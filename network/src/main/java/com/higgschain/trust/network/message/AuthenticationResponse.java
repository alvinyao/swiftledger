package com.higgschain.trust.network.message;

import com.higgschain.trust.network.Peer;

import java.io.Serializable;
import java.util.List;

/**
 * The type Authentication response.
 *
 * @author duhongming
 * @date 2018 /8/31
 */
public class AuthenticationResponse implements Serializable {
    private String message;
    private List<Peer> peers;
    private Peer peer;

    /**
     * Instantiates a new Authentication response.
     *
     * @param message the message
     */
    public AuthenticationResponse(String message) {
        this.message = message;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets message.
     *
     * @param message the message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets peers.
     *
     * @return the peers
     */
    public List<Peer> getPeers() {
        return peers;
    }

    /**
     * Sets peers.
     *
     * @param peers the peers
     */
    public void setPeers(List<Peer> peers) {
        this.peers = peers;
    }

    /**
     * Gets peer.
     *
     * @return the peer
     */
    public Peer getPeer() {
        return peer;
    }

    /**
     * Sets peer.
     *
     * @param peer the peer
     */
    public void setPeer(Peer peer) {
        this.peer = peer;
    }
}

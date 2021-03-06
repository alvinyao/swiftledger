package com.higgschain.trust.network.message;

import com.higgschain.trust.network.Peer;

import java.io.Serializable;
import java.util.Set;

/**
 * The type Discovery peers response.
 *
 * @author duhongming
 * @date 2018 /8/30
 */
public class DiscoveryPeersResponse implements Serializable {
    private Set<Peer> peers;

    /**
     * Instantiates a new Discovery peers response.
     *
     * @param peers the peers
     */
    public DiscoveryPeersResponse(Set<Peer> peers) {
        this.peers = peers;
    }

    /**
     * Gets peers.
     *
     * @return the peers
     */
    public Set<Peer> getPeers() {
        return peers;
    }

    /**
     * Sets peers.
     *
     * @param peers the peers
     */
    public void setPeers(Set<Peer> peers) {
        this.peers = peers;
    }
}

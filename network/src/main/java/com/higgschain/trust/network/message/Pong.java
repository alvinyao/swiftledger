package com.higgschain.trust.network.message;

import java.util.List;

/**
 * The type Pong.
 *
 * @author duhongming
 * @date 2018 /8/21
 */
public class Pong {

    private List<String> peers;

    /**
     * Gets peers.
     *
     * @return the peers
     */
    public List<String> getPeers() {
        return peers;
    }

    /**
     * Sets peers.
     *
     * @param peers the peers
     */
    public void setPeers(List<String> peers) {
        this.peers = peers;
    }
}

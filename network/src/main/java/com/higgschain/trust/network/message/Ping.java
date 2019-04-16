package com.higgschain.trust.network.message;

import java.util.List;

/**
 * The type Ping.
 *
 * @author duhongming
 * @date 2018 /8/21
 */
public class Ping {

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

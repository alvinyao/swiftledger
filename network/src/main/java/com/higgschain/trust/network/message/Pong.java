package com.higgschain.trust.network.message;

import java.util.List;

/**
 * @author duhongming
 * @date 2018/8/21
 */
public class Pong {

    private List<String> peers;

    public List<String> getPeers() {
        return peers;
    }

    public void setPeers(List<String> peers) {
        this.peers = peers;
    }
}
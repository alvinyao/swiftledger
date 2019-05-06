package com.higgschain.trust.network;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The type Peers.
 *
 * @author duhongming
 * @date 2018 /9/6
 */
public final class Peers {

    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * The constant MAX_TRY_CONNECT_TIMES.
     */
    public static final int MAX_TRY_CONNECT_TIMES = 200;

    /**
     * The Map.
     */
    final Map<Address, Peer> map = Maps.newConcurrentMap();
    /**
     * The Local address.
     */
    Address localAddress;
    /**
     * The Local peer.
     */
    Peer localPeer;

    /**
     * Init.
     *
     * @param localAddress the local address
     * @param seeds        the seeds
     * @param config       the config
     */
    public void init(final Address localAddress, final List<Peer> seeds, final NetworkConfig config) {
        this.localAddress = localAddress;
        localPeer = config.localPeer();
        map.put(localAddress, localPeer);
        seeds.forEach(peer -> {
            map.putIfAbsent(peer.getAddress(), peer);
        });
    }

    /**
     * Gets by address.
     *
     * @param address the address
     * @return the by address
     */
    public Peer getByAddress(Address address) {
        return map.get(address);
    }

    /**
     * Gets peers.
     *
     * @return the peers
     */
    public Set<Peer> getPeers() {
        return Sets.newConcurrentHashSet(map.values());
    }

    /**
     * get connected peers
     *
     * @return the active peers
     */
    public Set<Peer> getActivePeers() {
        return Sets.newConcurrentHashSet(map.values().stream().filter(Peer::isConnected).collect(Collectors.toSet()));
    }

    /**
     * Gets peer.
     *
     * @param nodeName the node name
     * @return the peer
     */
    public Peer getPeer(String nodeName) {
        for (Peer peer : map.values()) {
            if (nodeName.equals(peer.getNodeName()) && !peer.isBackup()) {
                return peer;
            }
        }
        return null;
    }

    /**
     * Gets backup peer.
     *
     * @param nodeName the node name
     * @return the backup peer
     */
    public Peer getBackupPeer(String nodeName) {
        for (Peer peer : map.values()) {
            if (nodeName.equals(peer.getNodeName()) && peer.isBackup()) {
                return peer;
            }
        }
        return null;
    }

    /**
     * Gets address.
     *
     * @param nodeName the node name
     * @return the address
     */
    public Address getAddress(String nodeName) {
        Peer peer = getPeer(nodeName);
        return peer == null ? null : peer.getAddress();
    }

    /**
     * Get peer.
     *
     * @param address the address
     * @return the peer
     */
    public Peer get(Address address) {
        return map.get(address);
    }

    /**
     * Put peer.
     *
     * @param peer the peer
     * @return the peer
     */
    public Peer put(Peer peer) {
        return map.put(peer.getAddress(), peer);
    }

    /**
     * Put if absent peer.
     *
     * @param peer the peer
     * @return the peer
     */
    public Peer putIfAbsent(Peer peer) {
        return map.putIfAbsent(peer.getAddress(), peer);
    }

    /**
     * remove peer
     *
     * @param peer peer
     * @return remove peer
     */
    public Peer remove(Peer peer) {
        return map.remove(peer.getAddress());
    }

    /**
     * Update peer connected.
     *
     * @param address   the address
     * @param connected the connected
     */
    public void updatePeerConnected(Address address, boolean connected) {
        Peer peer = map.get(address);
        if (peer != null) {
            int times = peer.setConnected(connected);
            if (times >= MAX_TRY_CONNECT_TIMES) {
                Peer removed = map.remove(address);
                log.warn("removed unconnected peer:{}", removed);
            }
        } else {
            log.warn("{} not fond", address);
        }
    }
}

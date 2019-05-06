package com.higgschain.trust.network;

import com.higgschain.trust.network.message.DiscoveryPeersRequest;
import com.higgschain.trust.network.message.DiscoveryPeersResponse;
import com.higgschain.trust.network.utils.Hessian;
import com.higgschain.trust.network.utils.Threads;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.*;

/**
 * The type Discovery peers service.
 *
 * @author duhongming
 * @date 2018 /9/12
 */
public class DiscoveryPeersService {

    private static final String DISCOVERY_NODES_ACTION = "discoveryNodes";

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final NetworkManage networkManage;
    private final Peers peers;
    private final ExecutorService executorService;
    private final Address localAddress;
    private final ScheduledExecutorService scheduledExecutor;

    private ScheduledFuture<?> timeoutFuture;

    /**
     * Instantiates a new Discovery peers service.
     *
     * @param networkManage   the network manage
     * @param peers           the peers
     * @param executorService the executor service
     */
    public DiscoveryPeersService(final NetworkManage networkManage, final Peers peers,
        final ExecutorService executorService) {
        this.networkManage = networkManage;
        this.peers = peers;
        this.executorService = executorService;
        this.localAddress = networkManage.localPeer().getAddress();
        this.scheduledExecutor =
            Executors.newSingleThreadScheduledExecutor(Threads.namedThreads("Network-DiscoveryPeersService-%d", log));

        networkManage.messagingService.registerHandler(DISCOVERY_NODES_ACTION, this::discoveryPeersHandler);
    }

    /**
     * Start.
     */
    public void start() {
        timeoutFuture = scheduledExecutor.scheduleAtFixedRate(() -> {
            Set<Peer> allPeers = peers.getPeers();
            DiscoveryPeersRequest request = new DiscoveryPeersRequest(peers.getActivePeers());
            allPeers.forEach(peer -> {
                if (peer.getAddress().equals(localAddress)) {
                    return;
                }
                discoveryPeers(peer, request);
            });
        }, 0, 3, TimeUnit.SECONDS);
    }

    /**
     * Shutdown.
     */
    public void shutdown() {
        timeoutFuture.cancel(false);
        scheduledExecutor.shutdown();
    }

    /**
     * Add peer.
     *
     * @param peer the peer
     */
    protected void addPeer(Peer peer) {
        if (peer.getAddress().equals(localAddress)) {
            return;
        }

        if (peer.isBackup()) {
            return;
        }

        Peer oldPeer = peers.putIfAbsent(peer);

        if (oldPeer == null || oldPeer.getNonce() < peer.getNonce()) {
            log.debug("Discovered a new peer {}", peer);
            if (oldPeer != null) {
                oldPeer.update(peer);
            }
            scheduledExecutor.execute(() -> discoveryPeers(peer, new DiscoveryPeersRequest(peers.getActivePeers())));
        }
    }

    private CompletableFuture<byte[]> discoveryPeersHandler(Address address, byte[] requestData) {
        DiscoveryPeersRequest request = Hessian.parse(requestData);
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        executorService.execute(() -> {
            log.trace("Received discovery peers message from {}", address);
            DiscoveryPeersResponse response = new DiscoveryPeersResponse(peers.getActivePeers());
            future.complete(Hessian.serialize(response));
            request.getPeers().forEach(this::addPeer);
        });
        return future;
    }

    private void discoveryPeers(Peer to, DiscoveryPeersRequest request) {
        this.networkManage.<DiscoveryPeersResponse>send(to.getAddress(), DISCOVERY_NODES_ACTION, request)
            .whenComplete((response, error) -> {
                if (error != null) {
                    peers.updatePeerConnected(to.getAddress(), false);
                    log.warn("Peer {} not available, {}", to, error.getMessage());
                    return;
                }
                log.debug("Send discovery peers message to {}", to.getAddress());
                peers.updatePeerConnected(to.getAddress(), true);
                response.getPeers().forEach(this::addPeer);
            });
    }
}

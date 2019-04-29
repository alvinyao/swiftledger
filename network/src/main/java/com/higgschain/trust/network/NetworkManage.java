package com.higgschain.trust.network;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.higgschain.trust.network.utils.Hessian;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The type Network manage.
 *
 * @author duhongming
 * @date 2018 /8/30
 */
public class NetworkManage {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final AtomicBoolean started = new AtomicBoolean(false);

    /**
     * The constant instance.
     */
    protected static NetworkManage instance;
    private static TrafficReporter trafficReporter = TrafficReporter.Default;

    private final Peers peers = new Peers();
    private final NetworkConfig config;
    private final Address localAddress;
    /**
     * The Messaging service.
     */
    protected final MessagingService messagingService;
    private final DiscoveryPeersService discoveryPeersService;
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private List<NetworkListener> listeners;
    private HttpClient httpClient;
    private RpcClient rpcClient;

    /**
     * Instantiates a new Network manage.
     *
     * @param config the config
     */
    public NetworkManage(final NetworkConfig config) {
        if (config.isSingleton()) {
            instance = this;
        }

        this.config = config;
        localAddress = config.localPeer().getAddress();
        List<Peer> seeds = Lists.newArrayList();
        config.seeds().forEach(s -> seeds.add(new Peer(s)));
        this.peers.init(localAddress, seeds, config);

        listeners = new ArrayList<>();

        messagingService = new MessagingService(this);
        discoveryPeersService = new DiscoveryPeersService(this, this.peers, executor);
        this.httpClient = new HttpClient(this);
        this.rpcClient = new RpcClient(this);
        this.initDefaultListener();
    }

    /**
     * Get the instance of NetworkManage if config singleton is true
     *
     * @return instance
     */
    public static NetworkManage getInstance() {
        return instance;
    }

    /**
     * Install traffic reporter.
     *
     * @param trafficReporter the traffic reporter
     */
    public static void installTrafficReporter(TrafficReporter trafficReporter) {
        NetworkManage.trafficReporter = trafficReporter;
    }

    /**
     * Gets traffic reporter.
     *
     * @return the traffic reporter
     */
    public static TrafficReporter getTrafficReporter() {
        return trafficReporter;
    }

    private void initDefaultListener() {
        listeners.add(((event, message) -> {
            if (event == NetworkListener.Event.LEAVE) {
                Address address = (Address)message;
                Peer peer = peers.getByAddress(address);

                if (peer != null) {
                    peer.setConnected(false);
                }
                log.info("Peer {} disconnected", message);
            }
        }));
    }

    /**
     * Gets peers.
     *
     * @return the peers
     */
    public Set<Peer> getPeers() {
        return peers.getPeers();
    }

    /**
     * Gets address.
     *
     * @param nodeName the node name
     * @return the address
     */
    public Address getAddress(String nodeName) {
        return peers.getAddress(nodeName);
    }

    /**
     * Http client http client.
     *
     * @return the http client
     */
    public HttpClient httpClient() {
        return httpClient;
    }

    /**
     * Rpc client rpc client.
     *
     * @return the rpc client
     */
    public RpcClient rpcClient() {
        return rpcClient;
    }

    /**
     * Gets peer by name.
     *
     * @param nodeName the node name
     * @return the peer by name
     */
    public Peer getPeerByName(String nodeName) {
        return peers.getPeer(nodeName);
    }

    /**
     * Gets backup peer by name.
     *
     * @param nodeName the node name
     * @return the backup peer by name
     */
    public Peer getBackupPeerByName(String nodeName) {
        return peers.getBackupPeer(nodeName);
    }

    /**
     * Add listener.
     *
     * @param listener the listener
     */
    public void addListener(NetworkListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    /**
     * Notify listeners.
     *
     * @param event   the event
     * @param message the message
     */
    protected void notifyListeners(NetworkListener.Event event, Object message) {
        synchronized (listeners) {
            if (listeners.size() == 0) {
                return;
            }
            for (NetworkListener listener : listeners) {
                messagingService.clientGroup.execute(() -> listener.handle(event, message));
            }
        }
    }

    /**
     * Start.
     */
    public void start() {

        if (started.get()) {
            log.warn("Network already running ...");
            return;
        }
        started.set(true);

        messagingService.start().whenComplete((messagingService, error) -> {
            if (error != null) {
                log.error("Start net server failed, {}", error.getMessage());
                return;
            }

            this.discoveryPeersService.start();
            log.info("Network started on {}", localAddress);
        });
    }

    /**
     * Shutdown.
     */
    public void shutdown() {
        if (!started.get()) {
            log.warn("Network can't shutdown, it's not started ...");
            return;
        }

        messagingService.stop();
        this.discoveryPeersService.shutdown();
        started.set(false);
    }

    /**
     * Config network config.
     *
     * @return the network config
     */
    public NetworkConfig config() {
        return this.config;
    }

    /**
     * Add peer.
     *
     * @param peer the peer
     */
    protected void addPeer(Peer peer) {
        this.discoveryPeersService.addPeer(peer);
    }

    /**
     * Local peer peer.
     *
     * @return the peer
     */
    public Peer localPeer() {
        return peers.localPeer;
    }

    /**
     * Send completable future.
     *
     * @param <T>     the type parameter
     * @param to      the to
     * @param action  the action
     * @param request the request
     * @return the completable future
     */
    public <T> CompletableFuture<T> send(Address to, String action, Object request) {
        CompletableFuture<T> future = new CompletableFuture<>();
        messagingService.sendAndReceive(to, action, Hessian.serialize(request)).whenComplete((data, error) -> {
            if (error != null) {
                future.completeExceptionally(error);
                return;
            }
            T response = Hessian.parse(data);
            future.complete(response);
        });
        return future;
    }

    /**
     * Register handler.
     *
     * @param <T>      the type parameter
     * @param type     the type
     * @param handler  the handler
     * @param executor the executor
     */
    public <T> void registerHandler(String type, Consumer<T> handler, Executor executor) {
        messagingService.registerHandler(type, (address, payload) -> {
            handler.accept(Hessian.parse(payload));
        }, executor);
    }

    /**
     * Register handler.
     *
     * @param <T>      the type parameter
     * @param <R>      the type parameter
     * @param type     the type
     * @param handler  the handler
     * @param executor the executor
     */
    public <T, R> void registerHandler(String type, Function<T, R> handler, Executor executor) {
        messagingService.registerHandler(type, (address, payload) -> {
            R ret = handler.apply((T)Hessian.parse(payload));
            return Hessian.serialize(ret);
        }, executor);
    }

    /**
     * Register handler.
     *
     * @param <T>     the type parameter
     * @param <R>     the type parameter
     * @param type    the type
     * @param handler the handler
     */
    public <T, R> void registerHandler(String type, Function<T, R> handler) {
        messagingService.registerHandler(type, (address, payload) -> {
            R ret = handler.apply((T)Hessian.parse(payload));
            return Hessian.serialize(ret);
        }, executor);
    }

    /**
     * Register completable future handler.
     *
     * @param <T>     the type parameter
     * @param <R>     the type parameter
     * @param type    the type
     * @param handler the handler
     */
    public <T, R> void registerCompletableFutureHandler(String type, Function<T, CompletableFuture<R>> handler) {
        messagingService.registerHandler(type, (address, payload) -> {
            CompletableFuture<R> resultFuture = handler.apply(Hessian.parse(payload));
            CompletableFuture<byte[]> future = new CompletableFuture<>();
            resultFuture.whenComplete((result, error) -> {
                if (error != null) {
                    future.completeExceptionally(error);
                } else {
                    future.complete(Hessian.serialize(result));
                }
            });
            return future;
        });
    }
}

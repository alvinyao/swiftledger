package com.higgschain.trust.network;

import com.higgschain.trust.network.message.handler.MessageHandlerRegistry;

import java.util.HashSet;

/**
 * The type Network config.
 *
 * @author duhongming
 * @date 2018 /8/30
 */
public class NetworkConfig {
    private String host;
    private int port;
    private int httpPort;
    private String privateKey;
    private String publicKey;
    private String nodeName;
    private HashSet<Address> seeds;
    private Peer localPeer;
    private boolean isBackupNode;
    private boolean singleton;
    private String signature;
    private MessageHandlerRegistry handlerRegistry;
    private int timeout;
    private int clientThreadNum;

    private Authentication authentication;

    private NetworkConfig() {
        seeds = new HashSet();
    }

    /**
     * Host string.
     *
     * @return the string
     */
    public String host() {
        return host;
    }

    /**
     * Port int.
     *
     * @return the int
     */
    public int port() {
        return port;
    }

    /**
     * Http port int.
     *
     * @return the int
     */
    public int httpPort() {
        return httpPort;
    }

    /**
     * Private key string.
     *
     * @return the string
     */
    public String privateKey() {
        return this.privateKey;
    }

    /**
     * Public key string.
     *
     * @return the string
     */
    public String publicKey() {
        return this.publicKey;
    }

    /**
     * Node name string.
     *
     * @return the string
     */
    public String nodeName() {
        return this.nodeName;
    }

    /**
     * Seeds hash set.
     *
     * @return the hash set
     */
    public HashSet<Address> seeds() {
        return seeds;
    }

    /**
     * Local peer peer.
     *
     * @return the peer
     */
    public Peer localPeer() {
        return localPeer;
    }

    /**
     * Is backup node boolean.
     *
     * @return the boolean
     */
    public boolean isBackupNode() {
        return isBackupNode;
    }

    /**
     * Sets backup node.
     *
     * @param backupNode the backup node
     */
    public void setBackupNode(boolean backupNode) {
        isBackupNode = backupNode;
    }

    /**
     * Authentication authentication.
     *
     * @return the authentication
     */
    public Authentication authentication() {
        return authentication;
    }

    /**
     * Is singleton boolean.
     *
     * @return the boolean
     */
    public boolean isSingleton() {
        return singleton;
    }

    /**
     * Signature string.
     *
     * @return the string
     */
    public String signature() {
        return this.signature;
    }

    /**
     * Handler registry message handler registry.
     *
     * @return the message handler registry
     */
    public MessageHandlerRegistry handlerRegistry() {
        return this.handlerRegistry;
    }

    /**
     * Timeout int.
     *
     * @return the int
     */
    public int timeout() {
        return this.timeout;
    }

    /**
     * The num of Netty client EventLoopGroup's threads
     *
     * @return int
     */
    public int clientThreadNum() {
        return this.clientThreadNum;
    }

    /**
     * The type Network config builder.
     */
    public static class NetworkConfigBuilder {
        private final NetworkConfig config;

        /**
         * Instantiates a new Network config builder.
         */
        public NetworkConfigBuilder() {
            config = new NetworkConfig();
        }

        /**
         * Build network config.
         *
         * @return the network config
         */
        public NetworkConfig build() {
            long nonce = System.currentTimeMillis();
            Address localAddress = new Address(config.host, config.port);
            config.localPeer = new Peer(localAddress, config.nodeName(), config.publicKey());
            config.localPeer.setConnected(true);
            config.localPeer.setNonce(nonce);
            config.localPeer.setHttpPort(config.httpPort);
            config.localPeer.setSlave(config.isBackupNode());

            config.signature = config.authentication.sign(config.localPeer, config.privateKey);

            config.handlerRegistry = new MessageHandlerRegistry();
            return config;
        }

        /**
         * Host network config builder.
         *
         * @param host the host
         * @return the network config builder
         */
        public NetworkConfigBuilder host(String host) {
            config.host = host;
            return this;
        }

        /**
         * Port network config builder.
         *
         * @param port the port
         * @return the network config builder
         */
        public NetworkConfigBuilder port(int port) {
            config.port = port;
            return this;
        }

        /**
         * Http port network config builder.
         *
         * @param httpPort the http port
         * @return the network config builder
         */
        public NetworkConfigBuilder httpPort(int httpPort) {
            config.httpPort= httpPort;
            return this;
        }

        /**
         * Private key network config builder.
         *
         * @param privateKey the private key
         * @return the network config builder
         */
        public NetworkConfigBuilder privateKey(String privateKey) {
            config.privateKey = privateKey;
            return this;
        }

        /**
         * Node name network config builder.
         *
         * @param nodeName the node name
         * @return the network config builder
         */
        public NetworkConfigBuilder nodeName(String nodeName) {
            config.nodeName = nodeName;
            return this;
        }

        /**
         * Backup node network config builder.
         *
         * @param isBackupNode the is backup node
         * @return the network config builder
         */
        public NetworkConfigBuilder backupNode(boolean isBackupNode) {
            config.isBackupNode = isBackupNode;
            return this;
        }

        /**
         * Public key network config builder.
         *
         * @param publicKey the public key
         * @return the network config builder
         */
        public NetworkConfigBuilder publicKey(String publicKey) {
            config.publicKey = publicKey;
            return this;
        }

        /**
         * Seed network config builder.
         *
         * @param seedPeers the seed peers
         * @return the network config builder
         */
        public NetworkConfigBuilder seed(Address... seedPeers) {
            if (seedPeers != null && seedPeers.length > 0) {
                for(Address address : seedPeers) {
                    config.seeds.add(address);
                }
            }
            return this;
        }

        /**
         * Seed network config builder.
         *
         * @param host the host
         * @param port the port
         * @return the network config builder
         */
        public NetworkConfigBuilder seed(String host, int port) {
            config.seeds.add(new Address(host, port));
            return this;
        }

        /**
         * Seed network config builder.
         *
         * @param ports the ports
         * @return the network config builder
         */
        public NetworkConfigBuilder seed(int... ports) {
            if (ports.length > 0) {
                for(int port : ports) {
                    config.seeds.add(Address.from(port));
                }
            }
            return this;
        }

        /**
         * Authentication network config builder.
         *
         * @param authentication the authentication
         * @return the network config builder
         */
        public NetworkConfigBuilder authentication(Authentication authentication) {
            config.authentication = authentication;
            return this;
        }

        /**
         * Singleton network config builder.
         *
         * @return the network config builder
         */
        public NetworkConfigBuilder singleton() {
            config.singleton = true;
            return this;
        }

        /**
         * Timeout network config builder.
         *
         * @param timeout the timeout
         * @return the network config builder
         */
        public NetworkConfigBuilder timeout(int timeout) {
            config.timeout = timeout;
            return this;
        }

        /**
         * Client thread num network config builder.
         *
         * @param num the num
         * @return the network config builder
         */
        public NetworkConfigBuilder clientThreadNum(int num) {
            config.clientThreadNum = num;
            return this;
        }
    }

    /**
     * Builder network config builder.
     *
     * @return the network config builder
     */
    public static NetworkConfigBuilder builder() {
        return new NetworkConfigBuilder();
    }
}

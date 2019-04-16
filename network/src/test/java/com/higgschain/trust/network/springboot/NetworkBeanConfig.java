package com.higgschain.trust.network.springboot;

import com.higgschain.trust.network.Address;
import com.higgschain.trust.network.AuthenticationImp;
import com.higgschain.trust.network.NetworkConfig;
import com.higgschain.trust.network.NetworkManage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The type Network bean config.
 *
 * @author duhongming
 * @date 2018 /9/11
 */
@Configuration
public class NetworkBeanConfig {
    /**
     * Instantiates a new Network bean config.
     */
    public NetworkBeanConfig() {
        System.out.println("NetworkBeanConfig ..." + host);
    }

    /**
     * The Host.
     */
    @Value("${network.host}")
    public String host;

    /**
     * The Port.
     */
    @Value("${network.port}")
    public int port;

    /**
     * The Peers.
     */
    @Value("${network.peers}")
    public String[] peers;

    /**
     * The Http port.
     */
    @Value("${server.port}")
    public int httpPort;

    /**
     * Gets network manage.
     *
     * @return the network manage
     */
    @Bean
    public NetworkManage getNetworkManage() {
        if (peers == null || peers.length == 0) {
            throw new IllegalArgumentException("Network peers is empty");
        }
        Address[] seeds = new Address[peers.length];
        for (int i = 0; i < peers.length; i++) {
            String[] host_port = peers[i].split(":");
            seeds[i] = new Address(host_port[0].trim(), Integer.parseInt(host_port[1].trim()));
        }

        NetworkConfig networkConfig = NetworkConfig.builder()
                .host("127.0.0.1")
                .port(port)
                .httpPort(httpPort)
                .nodeName("test")
                .publicKey("kddkdkk")
                .seed(seeds)
                .authentication(new AuthenticationImp())
                .singleton()
                .build();
        NetworkManage networkManage = new NetworkManage(networkConfig);
//        networkManage.start();
        return networkManage;
    }
}

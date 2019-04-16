package com.higgschain.trust.slave.core.network;

import com.higgschain.trust.config.crypto.CryptoUtil;
import com.higgschain.trust.consensus.config.NodeProperties;
import com.higgschain.trust.consensus.config.NodeState;
import com.higgschain.trust.consensus.config.NodeStateEnum;
import com.higgschain.trust.consensus.config.listener.StateChangeListener;
import com.higgschain.trust.consensus.config.listener.StateListener;
import com.higgschain.trust.network.Address;
import com.higgschain.trust.network.NetworkConfig;
import com.higgschain.trust.network.NetworkManage;
import com.higgschain.trust.slave.core.repository.config.ConfigRepository;
import com.higgschain.trust.slave.model.bo.config.Config;
import com.higgschain.trust.slave.model.enums.UsageEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * The type Network configuration.
 *
 * @author duhongming
 * @date 2018 /9/12
 */
@Configuration
@Slf4j
@StateListener
public class NetworkConfiguration {
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
     * The Timeout.
     */
    @Value("${network.timeout:0}")
    public int timeout;

    /**
     * The Client thread num.
     */
    @Value("${network.clientThreadNum:0}")
    public int clientThreadNum;

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
     * The Pub key for consensus.
     */
    @Value("${higgs.trust.keys.consensusPublicKey}")
    String pubKeyForConsensus;

    /**
     * The Pri key for consensus.
     */
    @Value("${higgs.trust.keys.consensusPrivateKey}")
    String priKeyForConsensus;

    @Autowired
    private NodeState nodeState;

    @Autowired
    private NetworkAuthentication authentication;

    @Autowired
    private CryptoUtil cryptoUtil;

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private NodeProperties nodeProperties;

    /**
     * Handle state change.
     */
    @StateChangeListener({
            NodeStateEnum.Starting,
            NodeStateEnum.Initialize,
            NodeStateEnum.Running,
            NodeStateEnum.Offline,
            NodeStateEnum.ArtificialSync,
            NodeStateEnum.AutoSync,
            NodeStateEnum.SelfChecking,
            NodeStateEnum.StartingConsensus,
            NodeStateEnum.Standby
    })
    public void handleStateChange() {
        if (nodeState.getState() == NodeStateEnum.Offline) {
            log.info("Start to shutdown network because of node state changed to Offline");
//            NetworkManage.getInstance().shutdown();
        } else {
            NetworkManage.getInstance().start();
        }
    }

    /**
     * Gets network manage.
     *
     * @return the network manage
     */
    @Bean
    public NetworkManage getNetworkManage() {
        log.info("Init NetworkManage bean ...");
        if (peers == null || peers.length == 0) {
            throw new IllegalArgumentException("Network peers is empty");
        }
        Address[] seeds = new Address[peers.length];
        for (int i = 0; i < peers.length; i++) {
            String[] host_port = peers[i].split(":");
            seeds[i] = new Address(host_port[0].trim(), Integer.parseInt(host_port[1].trim()));
        }

        List<Config> configList = configRepository.getConfig(new Config(nodeState.getNodeName(), UsageEnum.CONSENSUS.getCode()));
        String privateKey = configList == null ? priKeyForConsensus : configList.get(0).getPriKey();
        String publicKey = configList == null ? pubKeyForConsensus : configList.get(0).getPubKey();

        NetworkConfig networkConfig = NetworkConfig.builder()
                .host(host)
                .port(port)
                .httpPort(httpPort)
                .nodeName(nodeState.getNodeName())
                .privateKey(privateKey)
                .publicKey(publicKey)
                .seed(seeds)
                .authentication(authentication)
                .timeout(timeout)
                .clientThreadNum(clientThreadNum)
                .singleton()
                .backupNode(nodeProperties.isStandby() || !nodeProperties.isSlave())
                .build();
        NetworkManage networkManage = new NetworkManage(networkConfig);
        return networkManage;
    }
}
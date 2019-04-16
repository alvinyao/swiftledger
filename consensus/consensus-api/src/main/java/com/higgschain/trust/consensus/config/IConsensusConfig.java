package com.higgschain.trust.consensus.config;

/**
 * The interface Consensus config.
 *
 * @author: zhouyafeng
 * @create: 2018 /06/15 16:42
 * @description:
 */
public interface IConsensusConfig {

    /**
     * get public key create the given nodeName
     *
     * @param nodeName the node name
     * @return string
     */
    String pubKey(String nodeName);

    /**
     * get the self private key
     *
     * @return string
     */
    String privateKey();
}
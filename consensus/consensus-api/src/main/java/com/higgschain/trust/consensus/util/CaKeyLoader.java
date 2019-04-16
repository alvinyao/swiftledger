package com.higgschain.trust.consensus.util;

/**
 * The interface Ca key loader.
 *
 * @author: zhouyafeng
 * @create: 2018 /06/15 15:51
 * @description:
 */
public interface CaKeyLoader {

    /**
     * Load public key string.
     *
     * @param nodeName the node name
     * @return the string
     * @throws Exception the exception
     */
    String loadPublicKey(String nodeName) throws Exception;

    /**
     * Load private key string.
     *
     * @return the string
     * @throws Exception the exception
     */
    String loadPrivateKey() throws Exception;

}
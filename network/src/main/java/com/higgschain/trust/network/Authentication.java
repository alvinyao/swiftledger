package com.higgschain.trust.network;

/**
 * The interface Authentication.
 *
 * @author duhongming
 * @date 2018 /9/7
 */
public interface Authentication {

    /**
     * validate
     *
     * @param peer      the peer
     * @param signature the signature
     * @return boolean
     */
    boolean validate(Peer peer, String signature);

    /**
     * sign
     *
     * @param localPeer  the local peer
     * @param privateKey the private key
     * @return string
     */
    String sign(Peer localPeer, String privateKey);
}

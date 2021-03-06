package com.higgschain.trust.network;

import com.higgschain.trust.network.Authentication;
import com.higgschain.trust.network.Peer;

/**
 * The type Authentication imp.
 *
 * @author duhongming
 * @date 2018 /9/7
 */
public class AuthenticationImp implements Authentication {


    @Override
    public boolean validate(Peer peer, String signature) {
        return peer.getNodeName().equals(signature);
    }

    @Override
    public String sign(Peer localPeer, String privateKey) {
        return localPeer.getNodeName();
    }
}

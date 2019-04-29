package com.higgschain.trust.slave.core.network;

import com.higgschain.trust.consensus.util.CryptoUtil;
import com.higgschain.trust.network.Authentication;
import com.higgschain.trust.network.Peer;
import org.springframework.stereotype.Component;

/**
 * The type Network authentication.
 *
 * @author duhongming
 * @date 2018 /9/13
 */
@Component
public class NetworkAuthentication implements Authentication {

    @Override
    public boolean validate(Peer peer, String signature) {
        return CryptoUtil.getProtocolCrypto().verify(getSignContent(peer), signature, peer.getPublicKey());
    }

    @Override
    public String sign(Peer localPeer, String privateKey) {
        return CryptoUtil.getProtocolCrypto().sign(getSignContent(localPeer), privateKey);
    }

    private String getSignContent(Peer peer) {
        String signContent = String.format("%s%s%s", peer.getNodeName(), peer.getPublicKey(), peer.getNonce());
        return signContent;
    }
}
package com.higgschain.trust.common.crypto;

import lombok.Getter;
import lombok.Setter;

/**
 * The type Key pair.
 *
 * @author WangQuanzhou
 * @desc KeyPair Base64 Encoded
 * @date 2018 /8/10 15:44
 */
@Getter @Setter public class KeyPair {
    private String pubKey;
    private String priKey;

    /**
     * Instantiates a new Key pair.
     *
     * @param pubKey the pub key
     * @param priKey the pri key
     */
    public KeyPair(String pubKey, String priKey) {
        this.pubKey = pubKey;
        this.priKey = priKey;
    }
}

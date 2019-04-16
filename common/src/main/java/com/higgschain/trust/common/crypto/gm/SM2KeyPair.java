package com.higgschain.trust.common.crypto.gm;

import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

/**
 * SM2密钥对Bean
 *
 * @author Potato
 */
public class SM2KeyPair {

	private final ECPoint publicKey;
	private final BigInteger privateKey;

    /**
     * Instantiates a new Sm 2 key pair.
     *
     * @param publicKey  the public key
     * @param privateKey the private key
     */
    SM2KeyPair(ECPoint publicKey, BigInteger privateKey) {
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}

    /**
     * Gets public key.
     *
     * @return the public key
     */
    public ECPoint getPublicKey() {
		return publicKey;
	}

    /**
     * Gets private key.
     *
     * @return the private key
     */
    public BigInteger getPrivateKey() {
		return privateKey;
	}

}
